package it.chalmers.tendu.tbd;

public interface EventBusListener {

	public void onBroadcast(EventMessage message);
	
}
