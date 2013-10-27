package it.chalmers.tendu.gamemodel.shapesgame;

/**
 * Used when sending a shape over the network
 * 
 */
public class NetworkShape {

	public final int player;
	public final Shape shape;

	public NetworkShape() {
		player = 0;
		shape = null;
	}

	/**
	 * 
	 * @param player
	 *            player the shape is from
	 * @param shape
	 *            the actual shape
	 */
	public NetworkShape(int player, Shape shape) {
		this.player = player;
		this.shape = shape;
	}
}
