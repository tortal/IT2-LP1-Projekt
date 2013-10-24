package it.chalmers.tendu.event;

/**
 * A listener receives events from the {@link EventBus}. Be sure to register the
 * listener with the {@link EventBus#addListener(Listener)} method.
 */
public interface Listener {

	/**
	 * This method will be called on all listeners when
	 * {@link EventBus#broadcast(EventMessage)} is invoked.
	 * 
	 * @param message
	 *            that was broadcasted
	 */
	public void onBroadcast(EventMessage message);

	/**
	 * Unregister this listener from the {@link EventBus}
	 */
	public void unregister();

}
