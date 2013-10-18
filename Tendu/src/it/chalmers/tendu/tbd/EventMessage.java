package it.chalmers.tendu.tbd;

import it.chalmers.tendu.gamemodel.GameId;

public class EventMessage {
	

	public C.Tag tag;
	public C.Msg msg;
	public GameId gameId;
	public Object content;
	public String mac;

	/** No args constructor for reflection */
	EventMessage() {
//		tag = null;
//		msg = null;
//		gameId = null;
//		content = null;
//		mac = null;
	}

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
		return tag.toString() + " - " + msg.toString();
	}

}
