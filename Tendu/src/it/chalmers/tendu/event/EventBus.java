package it.chalmers.tendu.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;

/**
 * EventBus broadcasts {@link EventMessage}s to any registered {@link Listener}.
 * Connects Model-View-Controller (especially network controllers on separate
 * threads)
 * 
 */
public enum EventBus {

	INSTANCE;

	public final static String TAG = "EventBus";

	private List<Listener> listeners;

	private EventBus() {
		List<Listener> l = new ArrayList<Listener>();
		listeners = Collections.synchronizedList(l);
	}

	/**
	 * Sends a message to all listeners registered to this singleton.
	 * 
	 * @param message
	 *            to broadcast
	 */
	public void broadcast(EventMessage message) {
		synchronized (listeners) {
			Gdx.app.log(TAG, "broadcasting: " + message);
			// for (Listener l : listeners){
			// l.onBroadcast(message);
			// }

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
}

// /////////////////////////////////////////////////////

// public enum EventBus {
//
// INSTANCE;
//
// public final static String TAG = "EventBus";
//
// private Map<Listener, Void> listeners;
//
// EventBus() {
// WeakHashMap<Listener, Void> l = new WeakHashMap<Listener, Void>();
// listeners = Collections.synchronizedMap(l);
// }
//
// public void broadcast(EventMessage message) {
// synchronized (this) {
// Gdx.app.log(TAG, "broadcasting" + message);
//
// Set<Listener> allListeners = listeners.keySet();
// synchronized (listeners) {
// for (Listener l : allListeners) {
// l.onBroadcast(message);
// }
// }
//
// }
// }
//
// public void addListener(Listener l) {
// synchronized (this) {
// listeners.put(l, null);
// }
// }
//
// public void removeListener(Listener l) {
// synchronized (this) {
// listeners.remove(l);
// }
//
// }
// }