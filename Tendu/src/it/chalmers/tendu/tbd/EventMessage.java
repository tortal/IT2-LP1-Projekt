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
	}

	public EventMessage(String mac, C.Tag tag, C.Msg msg, GameId gameId, Object content) {
		this.mac = mac;
		this.tag = tag;
		this.msg = msg;
		this.gameId = gameId;
		this.content = content;
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

	public void setTag(C.Tag tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return tag.toString() + " - " + msg.toString();
	}

}
