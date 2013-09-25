package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;

import com.badlogic.gdx.Screen;

public class NumberGameScreen extends GameScreen {
	
	public NumberGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		// TODO Auto-generated constructor stub
	}

	/**All graphics are drawn here*/
	@Override
	public void render() {
		spriteBatch.begin();
		
		spriteBatch.end();		
	}
	
	/**All game logic goes here*/
	@Override
	public void tick(InputController input) {
		// TODO Auto-generated method stub
		
	}
	
}
