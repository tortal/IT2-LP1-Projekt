package it.chalmers.tendu.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class InputController implements InputProcessor {

	private boolean touchedUp = false;
	private boolean touchedDown = false;

	
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
		touchedDown = true;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
		return false;
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
