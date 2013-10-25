package it.chalmers.tendu;

import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.event.Listener;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.SessionResult;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.screen.GameOverScreen;
import it.chalmers.tendu.screen.InterimScreen;
import it.chalmers.tendu.screen.MainMenuScreen;
import it.chalmers.tendu.screen.MiniGameScreenFactory;
import it.chalmers.tendu.screen.Screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * ENTRY CLASS of Tendu.
 * 
 */
public class Tendu implements ApplicationListener, Listener {
	public static final String TAG = "Tendu";

	/**
	 * The {@link Screen}-object to render.
	 */
	private Screen screen;

	/**
	 * "Accumulated" numeric used to lock frame rate.
	 */
	private float accum = 0;

	/**
	 * User's physical input.
	 */
	private InputController input;

	/**
	 * "Camera" object for dynamic rendering of varying display resolutions and
	 * aspect ration.
	 */
	private OrthographicCamera camera;

	/**
	 * Network controller. All networking is implemented through the
	 * {@link INetworkHandler} interface. (e.g. see BluetoothHandler class in
	 * android project for example implementation)
	 */
	private INetworkHandler networkHandler;

	/**
	 * All drawing is normally done on this canvas.
	 */
	public SpriteBatch spriteBatch;

	/**
	 * @param networkHandler
	 *            Platform-specific implementation of the network communication.
	 */
	public Tendu(INetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
		EventBus.INSTANCE.addListener(this);
	}

	@Override
	public void create() {

		// Update Player singleton with this device's MAC.
		Player.getInstance().setMac(networkHandler.getMacAddress());
		Gdx.app.debug(TAG, Player.getInstance().getMac());

		spriteBatch = new SpriteBatch();

		// First screen is the MainMenuScreen.
		setScreen(new MainMenuScreen(this));

		// setup the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.SCREEN_WIDTH,
				Constants.SCREEN_HEIGHT);

		// Input is adjusted to the device's aspect ratio and resolution
		input = new InputController(camera);
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		networkHandler.destroy();
	}

	// **The games main loop, everything but early setup happens here
	@Override
	public void render() {

		// Clear screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// Gdx.gl.glClearColor(0.12f, 0.6f, 0.98f, 1);
		// Gdx.gl.glClearColor(1f, 1f, 0f, 1);
		Gdx.gl.glClearColor(Constants.BG_RED, Constants.BG_GREEN,
				Constants.BG_BLUE, 1); // TODO: what does this do?

		// Lock FPS at max of 60.
		accum += Gdx.graphics.getDeltaTime();
		while (accum > 1.0f / 60.0f) {
			screen.tick(input); // runs tick in the current screen which should
								// handle all input and game logic for that
								// specific minigame/menu
			input.tick(); // updates input
			accum -= 1.0f / 60.0f;
		}

		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);

		// Initiate spriteBatch.
		spriteBatch.begin();
		// Let Screen manipulate the batch.
		screen.render();
		// Render batch.
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	/**
	 * @param newScreen
	 *            the new Screen which will replace the old one.
	 */
	public void setScreen(Screen newScreen) {
		if (screen != null) { // TODO: will screen ever be null? if not, this
								// check should be removed in order to have a
								// fail-fast mechanism.
			screen.dispose();
		}
		screen = newScreen;
	}

	// the screens need access to the camera to handle translations between
	// actual screen pixels and our defined in game pixels
	public OrthographicCamera getCamera() {
		return camera;
	}

	// screens need access to the network
	public INetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {
			if (message.msg == C.Msg.CREATE_SCREEN) {
				MiniGame game = (MiniGame) message.content;
				Screen screen = MiniGameScreenFactory.createMiniGameScreen(
						this, game);
				setScreen(screen);
				EventMessage msg = new EventMessage(C.Tag.TO_SELF,
						C.Msg.WAITING_TO_START_GAME, Player.getInstance()
								.getMac());
				EventBus.INSTANCE.broadcast(msg);

			} else if (message.msg == C.Msg.SHOW_INTERIM_SCREEN) {
				SessionResult sessionResult = (SessionResult) message.content;
				Screen screen = new InterimScreen(this, sessionResult);
				setScreen(screen);

			} else if (message.msg == C.Msg.SHOW_GAME_OVER_SCREEN) {
				SessionResult sessionResult = (SessionResult) message.content;
				Screen screen = new GameOverScreen(this, sessionResult);
				setScreen(screen);

			} else if (message.msg == C.Msg.RESTART) {
				networkHandler.resetNetwork();
				Screen screen = new MainMenuScreen(this);
				setScreen(screen);

			} else if (message.msg == C.Msg.STOP_ACCEPTING_CONNECTIONS) {
				networkHandler.stopAcceptingConnections();
			}
		}
	}

	@Override
	public void unregister() {
		// TODO: Will this ever be called? ( maybe on dispose() )
		EventBus.INSTANCE.removeListener(this);
	}
}
