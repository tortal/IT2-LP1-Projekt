package it.chalmers.tendu.tbd;

public class EventMessage {
	public C.Tag tag;
	public C.Msg msg;
	public Object content;
	
	/** No args constructor for reflection */
	EventMessage() {
	}
	
	public EventMessage(C.Tag tag, C.Msg msg, Object content){
		this.tag = tag;
		this.msg = msg;
		this.content = content;
	}	
	
	public EventMessage(C.Tag tag, C.Msg msg){
		this(tag, msg, null);
	}
	
	@Override
	public String toString() {
		return tag.toString() + " - " + msg.toString();
	}
	
}
