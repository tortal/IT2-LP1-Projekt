package it.chalmers.tendu.tbd;

import it.chalmers.tendu.gamemodel.GameId;

/**
 * An Event Message can be broadcasted through the {@link EventBus}.
 * All listeners will get this broadcast.
 *
 */
public class EventMessage {
	public final static String TAG = "EventMesssage";
	

	public final C.Tag tag;
	public final C.Msg msg;
	public final GameId gameId;
	public final Object content;
	public final String mac;

	public EventMessage(String mac, C.Tag tag, C.Msg msg, GameId gameId, Object content) {
		this.mac = mac;
		this.tag = tag;
		this.msg = msg;
		this.gameId = gameId;
		this.content = content;
	}
	
	public EventMessage(EventMessage message, C.Tag newTag) {
		this.tag = newTag;
		this.msg = message.msg;
		this.mac = message.mac;
		this.gameId = message.gameId;
		this.content = message.content;
	}

	public EventMessage(C.Tag tag, C.Msg msg, Object content) {
		this(null, tag, msg, null, content);
	}

	public EventMessage(C.Tag tag, C.Msg msg, GameId gameId) {
		this(null, tag, msg, gameId, null);
	}
	
	public EventMessage(C.Tag tag, C.Msg msg, GameId gameId, Object content){
		this(null, tag, msg, gameId, content);
	}

	public EventMessage(C.Tag tag, C.Msg msg) {
		this(null, tag, msg, null, null);
	}

	@Override
	public String toString() {
		return TAG + ":" + tag.toString() + " - " + msg.toString();
	}

}
