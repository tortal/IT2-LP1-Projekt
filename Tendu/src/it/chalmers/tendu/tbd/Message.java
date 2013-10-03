package it.chalmers.tendu.tbd;

public class Message {
	public C.Tag tag;
	public C.Msg msg;
	public Object content;
	
	public Message(C.Tag tag, C.Msg msg, Object content){
		this.tag = tag;
		this.msg = msg;
		this.content = content;
	}
	
	public Message(C.Tag tag, C.Msg msg){
		this(tag, msg, null);
	}
	
}
