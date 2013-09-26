package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.NumberGame;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class NumberGameScreen extends GameScreen {
	private ShapeRenderer shapeRenderer;
	private BitmapFont numberFont;
	private NumberGame model;

	
	private ArrayList<Color> colors;
	
	//temp
	ArrayList<Integer> selectionNumbers;
	ArrayList<Integer> correctNumbers;

	public NumberGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.MAGENTA);
		
		numberFont = new BitmapFont();
		numberFont.scale(4); 
		
		numberFont.scale(-2); 

		this.model = (NumberGame)model;
		correctNumbers= this.model.getAnswerList();
		selectionNumbers = this.model.getDummyList();

//		for(int i = 1; i <= 8; i++) {
//			selectionNumbers.add(i);
//			correctNumbers.add(i);
//		}
		
//		correctNumbers.add(1);
//		correctNumbers.add(5);
//		correctNumbers.add(7);
//		correctNumbers.add(3);


		
		colors = new ArrayList<Color>();
		colors.add(Color.BLUE);
		colors.add(Color.MAGENTA);
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		colors.add(Color.ORANGE);
		colors.add(Color.WHITE);
		colors.add(Color.PINK);
		colors.add(Color.RED);
		
		Collections.shuffle(colors);
//		Collections.shuffle(selectionNumbers);
	}
	
	//called after shapeRender.begin
	public void drawNumberCircle(int number, int x, int y, int radius, Color color, float scale) {
		shapeRenderer.setColor(color);
		numberFont.setColor(color);

		for(int i = 0; i < 5; i++) {
			shapeRenderer.circle(x, y, radius-i);
		}
		
		numberFont.draw(spriteBatch, "" + number, x, y);
	}

	/** Draw all graphics here */
	@Override
	public void render() {
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();
	
		shapeRenderer.setProjectionMatrix(game.getCamera().combined);
		shapeRenderer.begin(ShapeType.Circle);
		
		for(int i = 0; i < selectionNumbers.size(); i++) {
			drawNumberCircle(selectionNumbers.get(i), 90+95*i, 120, 35, colors.get(i), 1);
		}
		
		for(int i = 0; i < correctNumbers.size(); i++) {
			numberFont.setColor(colors.get(i));
			numberFont.draw(spriteBatch, "" + correctNumbers.get(i), 100+i*85, 300);
		}

		shapeRenderer.end();
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
//		testTexture.dispose();
		shapeRenderer.dispose();
		numberFont.dispose();
	}

}
