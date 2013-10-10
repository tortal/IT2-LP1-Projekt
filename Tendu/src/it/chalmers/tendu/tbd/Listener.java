package it.chalmers.tendu.tbd;

public interface Listener {

	/**
	 * Is called when a message is sent over the EventBus
	 * 
	 * @param message that was sent
	 */
	public void onBroadcast(EventMessage message);
	
	/**
	 * Unregister this listener from the EventBus
	 */
	public void unregister();

}
