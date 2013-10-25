package it.chalmers.tendu.screens;

import it.chalmers.tendu.controllers.InputController;

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
	 */
	public void render();

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