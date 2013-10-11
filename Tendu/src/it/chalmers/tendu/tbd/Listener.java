package it.chalmers.tendu.tbd;

/**
 * A listener receives events from the {@link EventBus}. Be sure to register the
 * listener with the {@link EventBus} addListener() method.
 * 
 */
public interface Listener {

	/**
	 * This method is called when {@link EventBus} broadcast() method is called.
	 * 
	 * @param message
	 *            that was broadcasted
	 */
	void onBroadcast(EventMessage message);

	/**
	 * Unregister this listener from the {@link EventBus}
	 */
	void unregister();

}
