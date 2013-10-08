package it.chalmers.tendu.network.wifip2p;

import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.tbd.C;
import it.chalmers.tendu.tbd.EventBusListener;
import it.chalmers.tendu.tbd.EventMessage;

public abstract class NetworkHandler implements INetworkHandler, EventBusListener {
	
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
