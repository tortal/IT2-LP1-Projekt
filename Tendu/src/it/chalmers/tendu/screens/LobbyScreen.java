package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.controllers.LobbyController;
import it.chalmers.tendu.gamemodel.LobbyModel;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventMessage;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

public class LobbyScreen implements Screen {

	private BitmapFont bigFont;
	private BitmapFont smallFont;
	private Vector3 touchPos = new Vector3();
	private LobbyModel lobbyModel;
	private Tendu tendu;

	public LobbyScreen(Tendu tendu, boolean isHost) {
		this.tendu = tendu;

		lobbyModel = new LobbyModel();
		new LobbyController(lobbyModel);

		if (isHost)
			initHost();
		else
			initClient();

		bigFont = new BitmapFont();
		bigFont.scale(4);

		smallFont = new BitmapFont();
		smallFont.scale(2);
	}

	private void initHost() {
		lobbyModel.addHost(tendu.getNetworkHandler().getMacAddress());
		tendu.getNetworkHandler().hostSession();
	}

	private void initClient() {
		tendu.getNetworkHandler().joinGame();
	}

	public void tick(InputController input) {
		
		// TODO Get a ready button
		 if (Gdx.input.justTouched()) {
			 EventBus.INSTANCE.broadcast(new EventMessage(C.Tag.ACCESS_MODEL, C.Msg.PLAYER_READY));
//		 touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//		 tendu.getCamera().unproject(touchPos);
//		
//		 if (touchPos.x > 35 && touchPos.x < 435) {
//		 if (touchPos.y >= 180 && touchPos.y < 250) {
//		 Gdx.app.log("Testing", "Host");
//		 }
//		
//		 if (touchPos.y >= 80 && touchPos.y < 150) {
//		 Gdx.app.log("Testing", "Join");
//		 }
//		 } else if (touchPos.x > 600 && touchPos.y > 390) {
//		 Gdx.app.log("Testing", "test test");
//		 }
		 }
	}

	@Override
	public void render() {

		bigFont.draw(tendu.spriteBatch, "HOSTING", 20, 460);

		float x = 40f;
		float y = 410f;
		for (Map.Entry<Integer, String> p : lobbyModel.getLobbyMembers()
				.entrySet()) {
			bigFont.draw(tendu.spriteBatch,
					"Player: " + p.getKey() + ":" + p.getValue(), x, y);
			y -= 50;
		}

	}

	@Override
	public void removed() {
		// TODO Auto-generated method stub

	}

}
