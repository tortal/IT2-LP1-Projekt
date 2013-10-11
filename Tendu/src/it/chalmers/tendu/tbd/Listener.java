package it.chalmers.tendu.tbd;

/**
 * Any object of Listener type may be added to the {@link EventBus}.
 * Be sure to add the listener in the {@link EventBus} and unregister it when it not used anymore.
 *
 */
public interface Listener {

	/**
	 * This method is called when a call is made to {@link EventBus} 
	 * @param message that was broadcasted.
	 */
	void onBroadcast(EventMessage message);
	
	/**
	 * Remove this listener from the {@link EventBus}.
	 */
	void unregister(); 

}
