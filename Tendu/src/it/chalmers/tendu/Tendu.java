package it.chalmers.tendu;

import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventBusListener;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.gamemodel.SessionResult;
import it.chalmers.tendu.network.INetwork;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.screen.GameOverScreen;
import it.chalmers.tendu.screen.InterimScreen;
import it.chalmers.tendu.screen.LobbyScreen;
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
public class Tendu implements ApplicationListener, EventBusListener {
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
	private INetwork networkHandler;

	/**
	 * All drawing is normally done on this canvas.
	 */
	public SpriteBatch spriteBatch;

	/**
	 * @param networkHandler
	 *            Platform-specific implementation of the network communication.
	 */
	public Tendu(INetwork networkHandler) {
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
		setScreen(new MainMenuScreen(networkHandler));

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
		unregister();
	}

	// **The games main loop, everything but early setup happens here
	@Override
	public void render() {

		// Clear screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Sets the background colour of the entire app
		Gdx.gl.glClearColor(Constants.BG_RED, Constants.BG_GREEN,
				Constants.BG_BLUE, 1);

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
		screen.render(spriteBatch, camera);
		// Render batch.
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		networkHandler.onPause();
	}

	@Override
	public void resume() {
		networkHandler.onResume();

	}

	/**
	 * @param newScreen
	 *            the new Screen which will replace the old one.
	 */
	public void setScreen(Screen newScreen) {
		if (screen != null) { // TODO: will screen ever be null? if not, this
								// check should be removed in order to have a
								// fail-fast mechanism.
								// it is null the first time it's called.
								// it can be avoided by sett the Main Menu
								// screen directly the at game startup
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
	public INetwork getNetworkHandler() {
		return networkHandler;
	}

	@Override
	public void onBroadcast(EventMessage message) {
		if (message.tag == C.Tag.TO_SELF) {

			// Received from gameSession
			// creates the screen with the received mini game and broadcasts a
			// message that it's finished loading (handled by
			// GameSessionController)
			if (message.msg == C.Msg.CREATE_SCREEN) {
				MiniGame game = (MiniGame) message.content;
				Screen screen = MiniGameScreenFactory.createMiniGameScreen(game);
				setScreen(screen);
				EventMessage msg = new EventMessage(C.Tag.TO_SELF,
						C.Msg.WAITING_TO_START_GAME, Player.getInstance()
								.getMac());
				EventBus.INSTANCE.broadcast(msg);

			}
			// Show a screen between games with current results...
			else if (message.msg == C.Msg.SHOW_INTERIM_SCREEN) {
				SessionResult sessionResult = (SessionResult) message.content;
				Screen screen = new InterimScreen(sessionResult);
				setScreen(screen);

			}

			// Show the game over screen
			else if (message.msg == C.Msg.SHOW_GAME_OVER_SCREEN) {
				SessionResult sessionResult = (SessionResult) message.content;
				Screen screen = new GameOverScreen(sessionResult);
				setScreen(screen);

			}
			// Show the lobby screen.
			else if(message.msg == C.Msg.CREATE_LOBBY_SCREEN){
				setScreen(new LobbyScreen());
			}
			// Resets the network and loads the Main menu screen
			// The message is received when the connection to the other players
			// is lost (a better long time solution would be to show a connection lost
			// screen first)
			// It's also received if you go back from the lobby or pressing Main
			// menu when the game is over
			else if (message.msg == C.Msg.RESTART) {
				networkHandler.resetNetwork();
				Screen screen = new MainMenuScreen(networkHandler);
				setScreen(screen);
				
			}
			// Stop accepting more connections if a game session has started
			else if (message.msg == C.Msg.STOP_ACCEPTING_CONNECTIONS) {
				networkHandler.stopAcceptingConnections();
			}
		}
	}

	@Override
	public void unregister() {
		EventBus.INSTANCE.removeListener(this);
	}
}
