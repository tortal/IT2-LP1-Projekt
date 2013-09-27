package it.chalmers.tendu.screens;

//TODO needs refactoring

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
	
    private Vector3 touchPos;
    
    private int time;


	public NumberGameScreen(Tendu game, MiniGame model) {
		super(game, model);
		
		shapeRenderer = new ShapeRenderer();
		
		numberFont = new BitmapFont();
		numberFont.scale(2);
	    touchPos = new Vector3();
		this.model = (NumberGame)model;
		
		setUpGame();
	}
	
	private void setUpGame() {
		time = 0;
		correctNumbers = this.model.getAnswerList();
		selectionNumbers = this.model.getDummyList();
		
		numberCircles = new ArrayList<NumberCircle>();
		
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
		
		for(int i = 0; i < selectionNumbers.size(); i++) {
			numberCircles.add(new NumberCircle(selectionNumbers.get(i), (90+95*i), 120, 35, colors.get(i)));
		}
	}
	
	public void drawNumberCircle(NumberCircle circle) {
		shapeRenderer.setColor(circle.color);
		numberFont.setColor(circle.color);
		
		for(int i = 0; i < 5; i++) {
			shapeRenderer.circle(circle.getX(), circle.getY(), (circle.getRadius()-i)*circle.scale);
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
		
		if(time < 240) {
			numberFont.setColor(Color.BLUE);
			numberFont.draw(spriteBatch, "Memorize the numbers", 200, 400);
			
			numberFont.scale(2);
			for(int i = 0; i < correctNumbers.size(); i++) {
				numberFont.setColor(colors.get(i));
				numberFont.draw(spriteBatch, "" + correctNumbers.get(i), 180+i*130, 300);
			}
			numberFont.scale(-2);
			
		} else {
			numberFont.setColor(Color.BLUE);
			numberFont.draw(spriteBatch, "Enter the numbers in the correct order", 60, 400);
			
				numberFont.scale(-0.8f);
				for(int i = 0; i < numberCircles.size(); i++) {
					drawNumberCircle(numberCircles.get(i));
				}
				numberFont.scale(0.8f);
		}
		
		shapeRenderer.end();
		spriteBatch.end();
		
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		if(time < 240) {
			time++;
		} else {
	        if (input.isTouchedUp()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	            game.getCamera().unproject(touchPos);
	            
	            for(NumberCircle circle: numberCircles) {            	
	            	if(touchPos.x > circle.leftX && touchPos.x < circle.rightX) {
	                	if (touchPos.y < circle.topY && touchPos.y > circle.bottomY) {
	                    	Gdx.input.vibrate(25);
	                	}
	            	}      	
	            	circle.scale=1;
	            }
	        } 
	        
	        if (Gdx.input.isTouched()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	            game.getCamera().unproject(touchPos);
	            
	            for(NumberCircle circle: numberCircles) {            	
	            	if(touchPos.x > circle.leftX && touchPos.x < circle.rightX) {
	                	if (touchPos.y < circle.topY && touchPos.y > circle.bottomY) {
	                		circle.scale = 1.5f;
	                    	Gdx.app.log("Number = ", "" + circle.getNumber());
	                	}
	            	}
	            }
	        }
		}
  
   	}

	@Override
	public void removed() {
		super.removed();
		shapeRenderer.dispose();
		numberFont.dispose();
	}
}
