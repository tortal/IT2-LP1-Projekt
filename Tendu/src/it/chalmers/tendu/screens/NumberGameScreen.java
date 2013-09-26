package it.chalmers.tendu.screens;

import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.gamemodel.MiniGame;
import it.chalmers.tendu.gamemodel.NumberGame;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;


public class NumberGameScreen extends GameScreen {
	private ShapeRenderer shapeRenderer;
	private BitmapFont numberFont;
	private NumberGame model;

	
	private ArrayList<Color> colors;
	
	private ArrayList<Integer> selectionNumbers;
	private ArrayList<Integer> correctNumbers;
	private ArrayList<NumberCircle> numberCircles;
	
    private Vector3 touchPos = new Vector3();


	public NumberGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.MAGENTA);
		
		numberFont = new BitmapFont();
		numberFont.scale(2);


		this.model = (NumberGame)model;
		correctNumbers = this.model.getAnswerList();
		selectionNumbers = this.model.getDummyList();
		
		numberCircles = new ArrayList<NumberCircle>();
		
		for(int i = 0; i < selectionNumbers.size(); i++) {
			numberCircles.add(new NumberCircle(selectionNumbers.get(i), (90+95*i), 120, 35));
		}
				
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
//	public void drawNumberCircle(int number, int x, int y, int radius, Color color) {
//		shapeRenderer.setColor(color);
//		numberFont.setColor(color);
//
//		for(int i = 0; i < 5; i++) {
//			shapeRenderer.circle(x, y, radius-i);
//		}
//		
//		numberFont.draw(spriteBatch, "" + number, x-12, y+20);
//	}
	
	public void drawNumberCircle(NumberCircle circle, Color color) {
		shapeRenderer.setColor(color);
		numberFont.setColor(color);
		
		for(int i = 0; i < 5; i++) {
			shapeRenderer.circle(circle.getX(), circle.getY(), circle.getRadius()-i);
		}		
		numberFont.draw(spriteBatch, "" + circle.getNumber(), circle.getNumberX(), circle.getNumberY());
}

	/** Draw all graphics here */
	@Override
	public void render() {
		spriteBatch.setProjectionMatrix(game.getCamera().combined);
		spriteBatch.begin();
	
		shapeRenderer.setProjectionMatrix(game.getCamera().combined);
		shapeRenderer.begin(ShapeType.Circle);
		
		numberFont.scale(2);
		for(int i = 0; i < correctNumbers.size(); i++) {
			numberFont.setColor(colors.get(i));
			numberFont.draw(spriteBatch, "" + correctNumbers.get(i), 100+i*85, 300);
		}
		numberFont.scale(-2);
		
		numberFont.scale(-0.8f);
		for(int i = 0; i < numberCircles.size(); i++) {
			//drawNumberCircle(selectionNumbers.get(i), 90+95*i, 120, 35, colors.get(i));
			drawNumberCircle(numberCircles.get(i), colors.get(i));
		}
		numberFont.scale(0.8f);


		shapeRenderer.end();
		spriteBatch.end();
		
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
        if (Gdx.input.justTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.getCamera().unproject(touchPos);
            
            for(NumberCircle circle: numberCircles) {            	
            	if(touchPos.x > circle.leftX && touchPos.x < circle.rightX) {
                	Gdx.app.log("Number = ", "" + circle.getNumber());
            	}
            }
        }
  
   	}

	@Override
	public void removed() {
		super.removed();
//		testTexture.dispose();
		shapeRenderer.dispose();
		numberFont.dispose();
	}

}
