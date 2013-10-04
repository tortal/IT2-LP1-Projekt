//***Main entry of the libgdx-project****
package it.chalmers.tendu;

import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.GameSession;
import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.screens.MainMenuScreen;
import it.chalmers.tendu.screens.Screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tendu implements ApplicationListener {
	private Screen screen; // contains whats shown on device screen in any
							// given moment. Changes depending current
							// minigame or if in a menu etc
	private float accum = 0; // used to help lock frame rate in 60 frames per
								// second
	private InputController input; // used for handling input (obviously)
	private OrthographicCamera camera; // The use of a camera helps us to work
										// on one screen size no matter the
										// actual screen sizes of different
										// devices

	private INetworkHandler networkHandler; // handle to all network related
											// stuff (Android specific, at least
											// for now)
	public SpriteBatch spriteBatch; // used for drawing of graphics

	private LobbyModel gameLobby;
	private GameSession gameSession;

	private String TAG = "Tendu"; // Tag for logging

	public Tendu(INetworkHandler netCom) {
		setNetworkHandler(netCom);
	}

	@Override
	public void create() {

		spriteBatch = new SpriteBatch();

		// here we should load the start screen of the game

		setScreen(new MainMenuScreen(this, null));
		// setScreen(new NumberGameScreen(this, new NumberGame(30000,
		// Constants.Difficulty.ONE)));
		// setScreen(new ShapesGameScreen(this, new ShapesGame(30000,
		// Constants.Difficulty.ONE)));

		// create an inputController and register it with Gdx
		// here we should load the start screen of the game
		// setScreen(new MainMenuScreen(this, null));
		// setScreen(new NumberGameScreen(this, new NumberGame(0,
		// Constants.Difficulty.ONE)));

		// create an inputController and register it with Gdx
		input = new InputController();
		Gdx.input.setInputProcessor(input);

		// setup the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.SCREEN_WIDTH,
				Constants.SCREEN_HEIGHT);

		// temp code
		// gameSession = new GameSession();
		// modelController = new ModelController(this, gameSession);
		// setScreen(MiniGameScreenFactory.createMiniGameScreen(this,
		// gameSession.getMiniGame(gameSession.getNextGameId())));
		// end temp

	}

	// clean up
	@Override
	public void dispose() {
		spriteBatch.dispose();
		networkHandler.destroy();
	}

	// **The games main loop, everything but early setup happens here
	@Override
	public void render() {

		// clear the entire screen
		// setScreenByNetworkState(); //changes to some error screen if
		// connections is lost?
		// clear the entire screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// makes sure the game runs in 60 fps
		accum += Gdx.graphics.getDeltaTime();
		while (accum > 1.0f / 60.0f) {
			screen.tick(input); // runs tick in the current screen witch should
								// handle all input and game logic for that
								// specific minigame/menu
			input.tick(); // updates input
			accum -= 1.0f / 60.0f;
		}

		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		screen.render(); // draw all graphic for the current frame
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

	// sets a new screen and cleans up the previous one
	public void setScreen(Screen newScreen) {
		if (screen != null) {
			screen.removed();
		}
		screen = newScreen;
	}

	// the screens need access to the camera to handle translations between
	// actual screen pixels and our defined in game pixels
	public OrthographicCamera getCamera() {
		return camera;
	}

	// TODO (maybe not), screens need access to the network
	public INetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	private void setNetworkHandler(INetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	// TODO: MOVE TO LOBBY CONTROLLER
	// public void onBroadcast(EventMessage message) {
	// // TODO Auto-generated method stub
	// switch (message.msg) {
	// case PLAYERS_CONNECTED:
	// Gdx.app.log(TAG, "PLAYERS_CONNECTED");
	// case LOBBY_READY:
	// Gdx.app.log(TAG, "LOBBY_READY");
	// gameSession = gameLobby.getGameSession();
	// modelController = new ModelController(this, gameSession);
	// if (host) {
	//
	// GameId gameId = gameSession.getNextGameId();
	// MiniGame game = gameSession.getMiniGame(gameId);
	// EventMessage evMsg = new EventMessage(C.Tag.DEFAULT,
	// C.Msg.LOAD_THIS_GAME, game);
	// EventBus.INSTANCE.broadcast(evMsg);
	// }
	// break;
	// case LOAD_THIS_GAME:
	// gameSession.setCurrentMiniGame((MiniGame)message.content);
	// setScreen(MiniGameScreenFactory.createMiniGameScreen(this,
	// (MiniGame)message.content));
	// break;
	// default:
	// break;
	// }
	// }
	//
	// public void setHost(boolean isHost) {
	// this.host = isHost;
	// }
	//
	// public boolean isHost() {
	// return host;
	// }
}
