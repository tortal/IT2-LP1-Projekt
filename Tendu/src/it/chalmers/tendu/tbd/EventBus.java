package it.chalmers.tendu.tbd;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public enum EventBus {

	INSTANCE;

	public final static String TAG = "EventBus";

	private List<Listener> listeners = new ArrayList<Listener>();

	public void broadcast(EventMessage message) {
		synchronized (listeners) {
			Gdx.app.log(TAG, "broadcasting" + message);
			// for (Listener l : listeners){
			// l.onBroadcast(message);
			// }

			for (int i = 0; i < listeners.size(); i++) {
				listeners.get(i).onBroadcast(message);
			}

		}
	}

	public synchronized void addListener(Listener l) {
		Gdx.app.log(TAG, "added listener: " + l);
		listeners.add(l);
	}

	public synchronized void removeListener(Listener l) {
		Gdx.app.log(TAG, "removed listener: " + l);
		listeners.remove(l);
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