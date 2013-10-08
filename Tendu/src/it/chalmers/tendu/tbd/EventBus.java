package it.chalmers.tendu.tbd;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public enum EventBus {

	INSTANCE;

	private List<EventBusListener> listeners = new ArrayList<EventBusListener>();

	public void broadcast(EventMessage message){
		Gdx.app.log("EventBus", "broadcasting" + message);
		synchronized(listeners) {
//			for (Listener l : listeners){
//				l.onBroadcast(message);
//			}
			
			for (int i = 0; i < listeners.size(); i++){
				listeners.get(i).onBroadcast(message);
			}
			
		}
	}

	public void addListener(EventBusListener l) {
		listeners.add(l);
	}

	public void removeListener(EventBusListener l){
		listeners.remove(l);
	}
}
