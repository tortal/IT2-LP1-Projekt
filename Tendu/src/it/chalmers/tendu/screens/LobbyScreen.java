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
	private OnScreenText status;
	private OnScreenText ready;
	private BitmapFont font;
	private int playersConnected;
	private final int maximumPlayers;
	private OnScreenText playerText;

	public LobbyScreen(Tendu tendu, boolean isHost) {
		maximumPlayers = 4;
		this.tendu = tendu;
		LobbyModel model = new LobbyModel(maximumPlayers);
		lobbyController = new LobbyController(model);

		font = new BitmapFont(Gdx.files.internal("fonts/menuFont.fnt"),
				Gdx.files.internal("fonts/menuFont.png"), false);
		ready = new OnScreenText("Ready", new Vector2(65, 130));
		
		playerText = new OnScreenText("Players", new Vector2(65, 450), -0.25f);

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

		status = new OnScreenText("Waiting for connections...", new Vector2(40,
				460), -0.25f);
	}

	private void initClient() {
		tendu.getNetworkHandler().joinGame();
		status = new OnScreenText("Searching for game session...", new Vector2(
				40, 460), -0.25f);
	}

	public void tick(InputController input) {
		playersConnected = getModel().getLobbyMembers().entrySet().size();

		if (!Player.getInstance().isHost() && playersConnected > 0) {
			status.setText("Connected to game session");
		} else if (Player.getInstance().isHost() && playersConnected == maximumPlayers) {
			status.setText("Maximum players connected");
		}

		if (input.isTouchedDown()) {
			if (ready.collided(input.getCoordinates())) {
				Gdx.input.vibrate(25);
				ready.setColor(Color.LIGHT_GRAY);
			}
		} else if (input.isTouchedUp()) {
			if (ready.collided(input.getCoordinates())) {
				EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.TO_SELF,
						C.Msg.PLAYER_READY, Player.getInstance().getMac()));
			}

			ready.setColor(Color.WHITE);
		}
	}

	@Override
	public void render() {

		status.draw(tendu.spriteBatch, font);
		
		playerText.setY(450);

		for (Map.Entry<String, Integer> p : getModel().getLobbyMembers()
				.entrySet()) {
			playerText.setText("Player: " + p.getValue());
			playerText.addToY(-50);
			playerText.draw(tendu.spriteBatch, font);
		}
		
		if (playersConnected > 0) {
			ready.draw(tendu.spriteBatch, font);
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
