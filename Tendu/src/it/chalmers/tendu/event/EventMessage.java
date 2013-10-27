package it.chalmers.tendu.event;

import it.chalmers.tendu.gamemodel.GameId;

/**
 * An EventMessage may be sent using the
 * {@link EventBus#broadcast(EventMessage)} method. Any {@link EventBusListener}
 * that is registered on the bus will receive that EventMessage.
 * 
 * {@link EventBusListener}s and broadcasters can define their internal protocol
 * based on isolated needs specific for the context. Some of the EventMessages
 * member variables have a generic re-usable protocol defined in {@link C}.
 */
public class EventMessage {

	// TOOD: These are the new member vars. underscore is used to distinguish
	// them from the old protocol.
	public String _tag;
	public String _msg;
	public Object _obj;

	// TODO old protocol, let's replace this with the above!
	public C.Tag tag;
	public C.Msg msg;
	public GameId gameId;
	public Object content;

	/** Only used by reflection */
	@SuppressWarnings("unused")
	private EventMessage() {
		tag = null;
		msg = null;
		gameId = null;
		content = null;
	}

	/**
	 * Creates a complete EventMessage that can be used in the
	 * {@link EventBus#broadcast(EventMessage)} method. See {@link C} for the
	 * generic protocol of Tendu.
	 */
	public EventMessage(String tag, String msg, Object obj) {
		this._tag = tag;
		this._msg = msg;
		this._obj = obj;
	}

	/**
	 * Convenience constructor for when an EventMessage will only contain
	 * Strings. {@link EventMessage#_obj} will be set to null.
	 */
	public EventMessage(String tag, String msg) {
		this(tag, msg, null);
	}

	// TODO: Below here is the old protocol. i call it Tendu Legacy. :)

	public EventMessage(C.Tag tag, C.Msg msg, GameId gameId, Object content) {
		this.tag = tag;
		this.msg = msg;
		this.gameId = gameId;
		this.content = content;
	}

	public EventMessage(EventMessage message, C.Tag newTag) {
		this.tag = newTag;
		this.msg = message.msg;
		// this.mac = message.mac;
		this.gameId = message.gameId;
		this.content = message.content;
	}

	public EventMessage(C.Tag tag, C.Msg msg, Object content) {
		this(tag, msg, null, content);
	}

	public EventMessage(C.Tag tag, C.Msg msg, GameId gameId) {
		this(tag, msg, gameId, null);
	}

	// public EventMessage(C.Tag tag, C.Msg msg, GameId gameId, Object content){
	// this(tag, msg, gameId, content);
	// }

	public EventMessage(C.Tag tag, C.Msg msg) {
		this(tag, msg, null, null);
	}

}
