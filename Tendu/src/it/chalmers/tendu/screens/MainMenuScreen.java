package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends GameScreen {
	
	private BitmapFont font;
	private BitmapFont testFont;
    private Vector3 touchPos = new Vector3();


	public MainMenuScreen(Tendu game, MiniGame model) {
		super(game, model);
		
        font = new BitmapFont();
        font.scale(5);
        
        testFont = new BitmapFont();
        testFont.scale(2);
        
        
	}

	public void tick(InputController input) {
        // process user input
		//TODO refactor and use inputclass etc. Works for now...
        if (Gdx.input.justTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                game.getCamera().unproject(touchPos);
                
                if(touchPos.x > 35 && touchPos.x < 435) {
                	if(touchPos.y >= 180 && touchPos.y < 250) {
                		Gdx.app.log("Testing", "Host");
                		game.setScreen(new LobbyScreen(game, true));
                	}
                	
                	if(touchPos.y >= 80 && touchPos.y < 150) {
                		Gdx.app.log("Testing", "Join");
                		game.setScreen(new LobbyScreen(game, false));
                	}
                } else if(touchPos.x > 600 && touchPos.y > 390) {
            			Gdx.app.log("Testing", "test test");
            			game.getNetworkHandler().testStuff();
                }
        }
    }

	@Override
	public void render() {


		font.draw(game.spriteBatch, "Host game", 35, 250);
        font.draw(game.spriteBatch, "Join game", 47, 150);
        
        testFont.draw(game.spriteBatch, "test stuff", 600, 450);

   	}
	
	@Override
	public void removed() {
		super.removed();
		font.dispose();
		testFont.dispose();
	}

}
