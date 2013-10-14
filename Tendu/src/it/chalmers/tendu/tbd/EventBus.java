package it.chalmers.tendu.tbd;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.badlogic.gdx.Gdx;

public enum EventBus {

	INSTANCE;

	public final static String TAG = "EventBus";

	private List<Listener> liners;
	private Map<Listener, Void> listeners;

	EventBus() {
		WeakHashMap<Listener, Void> l = new WeakHashMap<Listener, Void>();
		listeners = Collections.synchronizedMap(l);
	}

	public void broadcast(EventMessage message) {
		synchronized (this) {
			Gdx.app.log(TAG, "broadcasting" + message);

			Iterator<Listener> iter = listeners.keySet().iterator();
			while (iter.hasNext()) {
				iter.next().onBroadcast(message);
			}

		}
	}

	public void addListener(Listener l) {
		synchronized (this) {
			listeners.put(l, null);
		}
	}

	public void removeListener(Listener l) {
		synchronized (this) {
			listeners.remove(l);
		}

	}
}

// public enum EventBus {
//
// INSTANCE;
//
// private List<Listener> listeners = new ArrayList<Listener>();
//
// public void broadcast(EventMessage message) {
// Gdx.app.log("EventBus", "broadcasting" + message);
// synchronized (listeners) {
// // for (Listener l : listeners){
// // l.onBroadcast(message);
// // }
//
// for (int i = 0; i < listeners.size(); i++) {
// listeners.get(i).onBroadcast(message);
// }
//
// }
// }
//
// public void addListener(Listener l) {
// listeners.add(l);
// }
//
// public void removeListener(Listener l) {
// listeners.remove(l);
// }
// }
