package it.chalmers.tendu.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;

/**
 * The EventBus broadcasts {@link EventMessage}s to any registered
 * {@link Listener}. Parallel connects Model-View-Controller in such way that
 * these OOP objects may communicated with each other through a "dynamic"
 * self-defined protocol. Tendu uses the bus mainly for inter-controller
 * 
 */
public enum EventBus {

	/**
	 * The {@link EventBus} singleton
	 */
	INSTANCE;

	public static final String TAG = "EventBus";

	private List<Listener> listeners;

	private EventBus() {
		List<Listener> l = new ArrayList<Listener>();
		listeners = Collections.synchronizedList(l);
	}

	/**
	 * Sends a message to all listeners registered to the EventBus.
	 * 
	 * @param message
	 *            to broadcast
	 */
	public void broadcast(EventMessage message) {
		synchronized (listeners) {
			Gdx.app.log(TAG, "broadcasting: " + message);

			for (int i = 0; i < listeners.size(); i++) {
				listeners.get(i).onBroadcast(message);
			}

		}
	}

	/**
	 * @param l
	 *            listener to be added.
	 */
	public void addListener(Listener l) {
		synchronized (listeners) {
			listeners.add(l);
		}
	}

	/**
	 * @param l
	 *            listener to be removed.
	 */
	public void removeListener(Listener l) {
		synchronized (listeners) {
			listeners.remove(l);
		}
	}

	/**
	 * @return a list of all currently registered {@link Listener}s.
	 */
	public List<Listener> getListeners() {
		return new ArrayList<Listener>(listeners);
	}
}