package it.chalmers.tendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;

public class MainMenuScreen extends GameScreen {
	
	private BitmapFont font;
    private Vector3 touchPos = new Vector3();


	public MainMenuScreen(Tendu game, MiniGame model) {
		super(game, model);
		
        font = new BitmapFont();
        font.scale(5);

	}

	public void tick(InputController input) {
        // process user input
        if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                game.getCamera().unproject(touchPos);
                
                if(touchPos.x > 35 && touchPos.x < 435) {
                	if(touchPos.y >= 180 && touchPos.y < 250) {
                		Gdx.app.log("Testing", "Host");
                		game.getNetworkHandler().hostSession();
                	}
                	
                	if(touchPos.y >= 80 && touchPos.y < 150) {
                		Gdx.app.log("Testing", "Join");
                		game.getNetworkHandler().joinGame();
                	}
                }
        }
    }

	@Override
	public void render() {
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();
        font.draw(spriteBatch, "Host game", 35, 250);
        font.draw(spriteBatch, "Join game", 47, 150);

		spriteBatch.end();
	}
	
	@Override
	public void removed() {
		super.removed();
		font.dispose();
	}

}
