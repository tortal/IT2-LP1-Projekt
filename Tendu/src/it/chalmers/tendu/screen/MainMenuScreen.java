package it.chalmers.tendu.screen;

import it.chalmers.tendu.controller.InputController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.TextLabels;
import it.chalmers.tendu.event.C;
import it.chalmers.tendu.event.EventBus;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.network.INetwork;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * The starting screen and the main menu of the game It simply offers the
 * options of either to host or join a game Also contains an option to choose
 * between bluetooth and the exeperimental wifi peer to peer implementation
 * 
 */
public class MainMenuScreen implements Screen {

	private INetwork networkHandler;
	private BitmapFont font;
	private BitmapFont menuFont;
	private TextWidget hostGame;
	private TextWidget joinGame;
	private TextWidget testStuff;
	private TextWidget hostType;

	private TextWidget wifi;
	private TextWidget bluetooth;
	private TextWidget selected;
	private int hostNumber; // used for testing, will always be 1 in user
							// version

	private boolean dark; // used for a secret option to change the colors of
							// the game

	public MainMenuScreen(INetwork networkHandler) {
		hostNumber = 1;

		this.networkHandler = networkHandler;

		// load resources and create som TextWidgets for on screen text
		font = new BitmapFont(Gdx.files.internal("fonts/mainMenuTendu.fnt"),
				Gdx.files.internal("fonts/mainMenuTendu.png"), false);

		menuFont = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);

		hostGame = new TextWidget(TextLabels.HOST, new Vector2(90, 270),
				Constants.MENU_FONT_COLOR);
		joinGame = new TextWidget(TextLabels.JOIN, new Vector2(90, 150),
				Constants.MENU_FONT_COLOR);
		testStuff = new TextWidget("test stuff", new Vector2(785, 680),
				Constants.MENU_FONT_COLOR);

		hostType = new TextWidget("Host = " + hostNumber,
				new Vector2(925, 130), Constants.MENU_FONT_COLOR);

		bluetooth = new TextWidget("BLUETOOTH", new Vector2(90,
				Constants.SCREEN_HEIGHT - 50), Constants.MENU_FONT_COLOR);
		wifi = new TextWidget("WIFI", new Vector2(90,
				Constants.SCREEN_HEIGHT - 140), Constants.MENU_FONT_COLOR);

		selected = new TextWidget(">", new Vector2(bluetooth.getX() - 55,
				bluetooth.getY() + 5), Constants.MENU_FONT_COLOR);

		dark = true;
	}

	@Override
	public void tick(InputController input) {
		// process user input
		// Touch up!
		if (input.isTouchedUp()) {
			if (hostGame.collided(input.getCoordinates())) {
				// start the lobby as host
				Player.getInstance().setHost(true);
				networkHandler.hostSession();
				EventMessage message = new EventMessage(C.Tag.TO_SELF,
						C.Msg.CREATE_LOBBY_SCREEN);
				EventBus.INSTANCE.broadcast(message);
			}

			if (joinGame.collided(input.getCoordinates())) {
				// start the lobby as client
				Player.getInstance().setHost(false);
				networkHandler.joinLobby();
				EventMessage message = new EventMessage(C.Tag.TO_SELF,
						C.Msg.CREATE_LOBBY_SCREEN);
				EventBus.INSTANCE.broadcast(message);
			}

			// used for testing
			if (testStuff.collided(input.getCoordinates())) {
				networkHandler.testSendMessage();
			}

			// used to change host type. Only for testing
			if (hostType.collided(input.getCoordinates())) {
				networkHandler.toggleHostNumber();

				if (hostNumber == 1) {
					hostNumber = 2;
				} else {
					hostNumber = 1;
				}

				hostType.setText("Host = " + hostNumber);
			}

			// bluetooth selected
			if (bluetooth.collided(input.getCoordinates())) {
				networkHandler.selectBluetooth();
				selected.setY(bluetooth.getY() + 5);
			}

			// wifi selected
			if (wifi.collided(input.getCoordinates())) {
				if (networkHandler.isWifip2pAvailable()) {
					networkHandler.selectWifi();
					selected.setY(wifi.getY() + 5);
				}
			}

			// Hidden color option
			if (input.x < 100 && input.y > Constants.SCREEN_HEIGHT - 100) {

				if (dark == true) {
					Constants.BG_RED = 0.8f;
					Constants.BG_GREEN = 0.8f;
					Constants.BG_BLUE = 0.8f;
					Constants.MENU_FONT_COLOR = Color.WHITE;
					Constants.MENU_FONT_COLOR_PRESSED = Color.GRAY;
					dark = false;
				} else {
					Constants.BG_RED = 0f;
					Constants.BG_GREEN = 0f;
					Constants.BG_BLUE = 0f;
					Constants.MENU_FONT_COLOR = Color.WHITE;
					Constants.MENU_FONT_COLOR_PRESSED = Color.LIGHT_GRAY;
					dark = true;
				}
			}

			// reset all font colors in case they've been changed by on press
			hostGame.setColor(Constants.MENU_FONT_COLOR);
			joinGame.setColor(Constants.MENU_FONT_COLOR);
			testStuff.setColor(Constants.MENU_FONT_COLOR);
			wifi.setColor(Constants.MENU_FONT_COLOR);
			bluetooth.setColor(Constants.MENU_FONT_COLOR);

		}
		// touch down
		// give haptic and visual feedback when touching down on different
		// options
		else if (input.isTouchedDown()) {
			if (hostGame.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				hostGame.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}

			if (joinGame.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				joinGame.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}

			if (testStuff.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				testStuff.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}

			if (bluetooth.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				bluetooth.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}

			if (wifi.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				wifi.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}
		}
	}

	@Override
	public void render(SpriteBatch spriteBatch, OrthographicCamera camera) {
		// draw all textWidgets
		hostGame.draw(spriteBatch, font);
		joinGame.draw(spriteBatch, font);

		// These three lines left for when we want to reinstate the wifi/bluetooth choice
		//bluetooth.draw(spriteBatch, menuFont);
		//wifi.draw(spriteBatch, menuFont);
		//selected.draw(spriteBatch, menuFont);

		// only for testing
		// hostType.draw(tendu.spriteBatch, menuFont);
		// testStuff.draw(tendu.spriteBatch, font);

	}

	@Override
	public void dispose() {
		font.dispose();
		menuFont.dispose();
	}

}
