package it.chalmers.tendu.screens;

//TODO needs refactoring
import it.chalmers.tendu.Tendu;
import it.chalmers.tendu.controllers.InputController;
import it.chalmers.tendu.defaults.GameState;
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


class Number {
	int number;
	boolean show;
	
	Number(int number, boolean show) {
		this.number = number;
		this.show = show;
	}
}

public class NumberGameScreen extends GameScreen {
	private ShapeRenderer shapeRenderer;
	private BitmapFont numberFont;
	private NumberGame model;

	
	private ArrayList<Color> colors;
	
	private ArrayList<Integer> selectionNumbers;
	private ArrayList<Integer> correctNumbers;
	private ArrayList<NumberCircle> numberCircles;
	private ArrayList<Number> numbers;
	
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
		correctNumbers = model.getAnswerList();
		selectionNumbers = model.getDummyList();
		
		numberCircles = new ArrayList<NumberCircle>();
		numbers = new ArrayList<Number>();
		
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
		
		for(Integer number: correctNumbers) {
			numbers.add(new Number(number.intValue(), false));
		}
		
		for(int i = 0; i < selectionNumbers.size(); i++) {
			numberCircles.add(new NumberCircle(selectionNumbers.get(i), (90+95*i), 120, 35, colors.get(i)));
		}
	}
	
	private void drawNumbers(boolean showAll) {
		numberFont.scale(2);
		
		if(showAll) {
			for(int i = 0; i < numbers.size(); i++) {
				numberFont.setColor(colors.get(i));
				numberFont.draw(spriteBatch, "" + numbers.get(i).number, 180+i*130, 300);
			}
		} else {
			for(int i = 0; i < numbers.size(); i++) {
				if(numbers.get(i).show == true) {
					numberFont.setColor(colors.get(i));
					numberFont.draw(spriteBatch, "" + numbers.get(i).number, 180+i*130, 300);
				}
			}
		}
		numberFont.scale(-2);
	}
	private void drawNumberCircle(NumberCircle circle) {
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
		
		if(model.checkGameState() == GameState.IN_PROGRESS) {
			if(time < 240) {
				numberFont.setColor(Color.BLUE);
				numberFont.draw(spriteBatch, "Memorize the numbers", 200, 400);
				drawNumbers(true);
				
			} else {
				numberFont.setColor(Color.BLUE);
				numberFont.draw(spriteBatch, "Enter the numbers in the correct order", 60, 400);
				
				drawNumbers(false);
	
				numberFont.scale(-0.8f);
				for(int i = 0; i < numberCircles.size(); i++) {
					drawNumberCircle(numberCircles.get(i));
				}
				numberFont.scale(0.8f);
			}
		}
		
		shapeRenderer.end();
		spriteBatch.end();
		
	}

	/** All game logic goes here */
	@Override
	public void tick(InputController input) {
		if(model.checkGameState() == GameState.IN_PROGRESS) {
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
		                    	if(model.checkNbr(circle.getNumber())) {
		                    		Gdx.app.log("Correct number = ", "" + circle.getNumber());
		                    		for(Number num: numbers) {
		                    			if(num.number == circle.getNumber()) {
		                    				num.show = true;
		                    			}
		                    		}
		                    	}
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
		                	}
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
