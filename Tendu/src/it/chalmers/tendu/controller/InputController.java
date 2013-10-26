package it.chalmers.tendu.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Handles users' physical inputs. Should be added to Gdx input processor if to
 * be used.
 */
public class InputController implements InputProcessor {

	private boolean touchedUp;
	private boolean touchedDown;
	private boolean dragged;
	private OrthographicCamera camera;
	private Vector3 vector3;
	private Vector2 vector2;
	private boolean backPressed;

	// Last known position on the screen
	public int screenX, screenY, x, y;

	/**
	 * Creates a new input controller
	 * 
	 * @param camera Used to translate from screen-coordinates to game-coordinates. 
	 */
	public InputController(OrthographicCamera camera) {
		Gdx.input.setInputProcessor(this); //register inputController with Gdx
		Gdx.input.setCatchBackKey(true); //makes sure the android back button "belongs" to the game and not the system
		this.camera = camera;
		touchedUp = false;
		touchedDown = false;
		dragged = false;
		backPressed = false;
		vector3 = new Vector3(0, 0, 0);
		vector2 = new Vector2(0, 0);
	}

	/**
	 * Called on every frame of the game 
	 * to reset all fields. 
	 */
	public void tick() {
		touchedUp = false;
		touchedDown = false;
		backPressed = false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
        if(keycode == Keys.BACK){
    		backPressed = true;
         }
        return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		setCoordinates(screenX, screenY);
		touchedDown = true;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		setCoordinates(screenX, screenY);
		touchedUp = true;
		dragged = false;
		return touchedUp;
	}

	/**
	 * Returns true if the users has let go of the screen 
	 * 
	 * @return true if the users has let go of the screen 
	 */
	public boolean isTouchedUp() {
		return touchedUp;
	}


	/**
	 * Returns true if the users has touched the screen 
	 * 
	 * @return true if the users has touched the screen. 
	 */
	public boolean isTouchedDown() {
		return touchedDown;
	}
	

	/**
	 * Returns true if the users has pressed the back key 
	 * 
	 * @return true if the users has pressed the back key 
	 */
	public boolean isBackPressed() {
		return backPressed;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		setCoordinates(screenX, screenY);
		dragged = true;
		return dragged;
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

	/**
	 * Returns true if the user is dragging the screen.
	 * @return true if the user is dragging the screen.
	 */
	public boolean isDragged() {
		return dragged;
	}

	/**
	 * Sets the coordinates
	 * @param screenX the x value measured in screen-pixels.
	 * @param screenY the y value measured in screen-pixels.
	 */
	private void setCoordinates(int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
		vector3.set(screenX, screenY, 0);
		camera.unproject(vector3);
		x = (int) vector3.x;
		y = (int) vector3.y;

	}

	/**
	 * Gets the coordinates in game-units.
	 * @return the coordinates in game-units
	 */
	public Vector2 getCoordinates() {
		vector2.x = x;
		vector2.y = y;
		return vector2;
	}

	/**
	 * Gets the coordinates in screen-pixels.
	 * @return the coordinates in screen-pixels
	 */
	public Vector2 getScreenCoordinates() {
		vector2.x = screenX;
		vector2.y = screenY;
		return vector2;
	}
}
