package it.chalmers.tendu.tbd;

public interface Listener {

	/**
	 * This method is called when a call is made to {@link EventBus} 
	 * @param message that was broadcasted.
	 */
	public void onBroadcast(EventMessage message);

}
