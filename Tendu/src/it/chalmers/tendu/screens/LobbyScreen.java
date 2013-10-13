package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.controllers.LobbyController;
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
	private OnScreenText statusText;
	private OnScreenText readyText;
	private OnScreenText playerText;
	private OnScreenText waitingText;
	private BitmapFont font;
	private int playersConnected;
	private final int maximumPlayers;
	private boolean ready;

	public LobbyScreen(Tendu tendu, boolean isHost) {
		maximumPlayers = 2;
		this.tendu = tendu;
		LobbyModel model = new LobbyModel(maximumPlayers);
		lobbyController = new LobbyController(model);
		
		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);
		
		readyText = new OnScreenText("I'm ready", new Vector2(65, 130));
		waitingText = new OnScreenText("Waiting for other players...", new Vector2(65, 130), -0.25f);		
		playerText = new OnScreenText("Players", new Vector2(65, 450), -0.25f);
		
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

		statusText = new OnScreenText("Waiting for connections...", new Vector2(40,
				460), -0.25f);
	}

	private void initClient() {
		tendu.getNetworkHandler().joinGame();
		statusText = new OnScreenText("Searching for game session...", new Vector2(
				40, 460), -0.25f);
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
				readyText.setColor(Color.LIGHT_GRAY);
			}
		} else if (input.isTouchedUp()) {
			if (readyText.collided(input.getCoordinates())) {
				ready = true;
				EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.TO_SELF,
						C.Msg.PLAYER_READY, Player.getInstance().getMac()));
			}

			readyText.setColor(Color.WHITE);
		}
	}

	@Override
	public void render() {

		statusText.draw(tendu.spriteBatch, font);
		
		playerText.setY(450);

		for (Map.Entry<String, Integer> p : getModel().getLobbyMembers()
				.entrySet()) {
			playerText.setText("Player: " + p.getValue());
			playerText.addToY(-50);
			playerText.draw(tendu.spriteBatch, font);
		}
		
		if (playersConnected > 0 && !ready) {
			readyText.draw(tendu.spriteBatch, font);
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
