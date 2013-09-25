package it.chalmers.tendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;

public class NumberGameScreen extends GameScreen {

	Sprite testSprite;
	Texture testTexture;

	public NumberGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		testTexture = new Texture(Gdx.files.internal("titlescreen.png"));
		testSprite = new Sprite(testTexture, 0, 0, 512, 512);

	}

	/** All graphics are drawn here */
	@Override
	public void render() {
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();

		testSprite.draw(spriteBatch);

		spriteBatch.end();
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removed() {
		super.removed();
		testTexture.dispose();
	}

}
