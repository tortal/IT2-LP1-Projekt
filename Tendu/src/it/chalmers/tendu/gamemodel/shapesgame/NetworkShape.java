package it.chalmers.tendu.gamemodel.shapesgame;

public class NetworkShape {

	public final int player;
	public final Shape shape;

	public NetworkShape() {
		player = 0;
		shape = null;
	}

	public NetworkShape(int player, Shape shape) {
		this.player = player;
		this.shape = shape;
	}
}
