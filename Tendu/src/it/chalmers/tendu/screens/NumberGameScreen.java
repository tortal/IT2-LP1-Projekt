package it.chalmers.tendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;

public class NumberGameScreen extends GameScreen {
	
    Sprite testSprite;


	public NumberGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		// TODO Auto-generated constructor stub
		
        testSprite = new Sprite(new Texture(Gdx.files.internal("titlescreen.png")), 0, 0, 512, 512);

	}

	/** All graphics are drawn here */
	@Override
	public void render() {
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
		testSprite.draw(spriteBatch);
		
		spriteBatch.end();
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		// TODO Auto-generated method stub

	}

}
