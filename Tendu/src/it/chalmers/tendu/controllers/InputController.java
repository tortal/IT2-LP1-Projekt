package it.chalmers.tendu.controllers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;

public class InputController implements InputProcessor {

	private boolean touchedUp = false;
	private boolean touchedDown = false;
	private int x, y;

	
	public void tick() {
		touchedUp = false;		
		touchedDown = false;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		x = screenX;
		y = screenY;
		touchedDown = true;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		x = screenX;
		y = screenY;
		touchedUp = true;		
		return true;
	}
	
	public boolean isTouchedUp () {
		return touchedUp;
	}
	
	public boolean isTouchedDown () {
		return touchedDown;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//Gdx.app.log("TouchedDrag" , " x = " + screenX + " y = " + screenY);
		x = screenX;
		y = screenY;
		return false;
	}
	
	public List<Integer> getDraggedXY(){
		List<Integer> l = new ArrayList<Integer>();
		l.add(x);
		l.add(y);
		return l;
		
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
