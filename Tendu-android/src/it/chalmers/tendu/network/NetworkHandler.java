package it.chalmers.tendu.network;

import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBus;
import it.chalmers.tendu.tbd.EventBusListener;
import it.chalmers.tendu.tbd.EventMessage;
import android.content.Context;

public abstract class NetworkHandler implements INetworkHandler, EventBusListener {
	protected Context context;
	
	public static final int MAX_NUMBER_OF_PLAYERS = 3;
	public static final int CONNECTION_DELAY = 5000;
	
	public NetworkHandler(Context ctx) {
		context = ctx;
		
		// Register as listener on the eventbus
		EventBus.INSTANCE.addListener(this);
	}
	
	@Override
	public void onBroadcast(EventMessage message) {
		// TODO Hook up to event bus	
		switch (message.tag) {
		case COMMAND_AS_HOST: 
			message.setTag(C.Tag.HOST_COMMANDED); 	// Set new tag to prevent
													// feedback loop
			broadcastMessageOverNetwork(message);
			break;
		case REQUEST_AS_CLIENT: 
				message.setTag(C.Tag.CLIENT_REQUESTED);
				broadcastMessageOverNetwork(message);
			break;
		default:
			break;
		}
	}

}
