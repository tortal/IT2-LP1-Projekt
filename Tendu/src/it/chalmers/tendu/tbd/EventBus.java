package it.chalmers.tendu.tbd;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public enum EventBus {
	
	INSTANCE;
	
	private List<Listener> listeners = new ArrayList<Listener>();

	public void broadcast(EventMessage message){
		Gdx.app.log("EventBus", "broadcasting" + message);
		for (Listener l : listeners){
			l.onBroadcast(message);
		}
	}
	
	public void addListener(Listener l) {
		listeners.add(l);
	}
	
	public void removeListener(Listener l){
		listeners.remove(l);
	}
}
