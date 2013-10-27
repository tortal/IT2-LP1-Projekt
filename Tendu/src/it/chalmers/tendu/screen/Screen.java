package it.chalmers.tendu.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.chalmers.tendu.controller.InputController;

/**
 * A Screen represents the presentation layer. (As in Model-View-Presentation)
 * 
 * Every presentation-layer (View-Controller) in Tendu should implement this
 * interface.
 * 
 */
public interface Screen {

	/**
	 * Renders the graphic of the implemented Screen.
	 * @param camera 
	 * @param spriteBatch 
	 */
	public void render(SpriteBatch spriteBatch, OrthographicCamera camera);

	/**
	 * Every tick will perform a logic update.
	 * 
	 * @param input
	 *            controller associated with this screen.
	 */
	public void tick(InputController input);

	/**
	 * Releases any resources associated with this screen.
	 */
	public void dispose();

}