package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.controllers.LobbyController;
import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.gamemodel.Player;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class LobbyScreen implements Screen {
	private LobbyController lobbyController;
	private Tendu tendu;
	private TextWidget statusText;
	private TextWidget readyText;
	private TextWidget playerText;
	private TextWidget waitingText;
	private BitmapFont font;
	private int playersConnected;
	private final int maximumPlayers;
	private boolean ready;

	public LobbyScreen(Tendu tendu, boolean isHost) {
		maximumPlayers = 4;
		this.tendu = tendu;
		LobbyModel model = new LobbyModel(maximumPlayers);
		lobbyController = new LobbyController(model);
		
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);
		
		readyText = new TextWidget("I'm ready", new Vector2(640, 150), Constants.MENU_FONT_COLOR);
		waitingText = new TextWidget("Waiting for other players...", new Vector2(65, 150), Constants.MENU_FONT_COLOR);		
		playerText = new TextWidget("Players", new Vector2(65, 450), Constants.MENU_FONT_COLOR);
		
		ready = false;

		if (isHost)
			initHost();
		else
			initClient();
	}

	private void initHost() {
		Player.getInstance().setHost(true);
		tendu.getNetworkHandler().hostSession();

		String myMac = Player.getInstance().getMac();
		lobbyController.getModel().addPlayer(myMac);

		statusText = new TextWidget("Waiting for connections...", new Vector2(40,
				620), Constants.MENU_FONT_COLOR);
	}

	private void initClient() {
		tendu.getNetworkHandler().joinGame();
		statusText = new TextWidget("Searching for game session...", new Vector2(
				40, 620), Constants.MENU_FONT_COLOR);
	}

	public void tick(InputController input) {
		playersConnected = getModel().getLobbyMembers().entrySet().size();

		if (!Player.getInstance().isHost() && playersConnected > 0) {
			statusText.setText("Connected to game session");
		} else if (Player.getInstance().isHost() && playersConnected == maximumPlayers) {
			statusText.setText("Maximum players connected");
		}

		if (input.isTouchedDown()) {
			if (readyText.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				readyText.setColor(Constants.MENU_FONT_COLOR_PRESSED);
			}
		} else if (input.isTouchedUp()) {
			if (readyText.collided(input.getCoordinates())) {
				ready = true;
				EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.TO_SELF,
						C.Msg.PLAYER_READY, Player.getInstance().getMac()));
			}

			readyText.setColor(Constants.MENU_FONT_COLOR);
		}
	}

	@Override
	public void render() {

		statusText.draw(tendu.spriteBatch, font);
		
		playerText.setY(580);

		for (Map.Entry<String, Integer> p : getModel().getLobbyMembers()
				.entrySet()) {
			playerText.setText("Player: " + p.getValue());
			playerText.addToY(-50);
			playerText.draw(tendu.spriteBatch, font);
		}
		
		if (playersConnected > 0 && !ready) {
			readyText.drawAtCenterPoint(tendu.spriteBatch, font);
		} else if(playersConnected > 0 && ready) {
			waitingText.draw(tendu.spriteBatch, font);
		}
	}

	private LobbyModel getModel() {
		return lobbyController.getModel();
	}

	@Override
	public void removed() {
		font.dispose();
		lobbyController.unregister();
	}

}
