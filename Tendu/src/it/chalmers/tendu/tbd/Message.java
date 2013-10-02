package it.chalmers.tendu.tbd;

public class Message {
	public String tag;
	public String msg;
	public Object content;
	
	public Message(String tag, String msg, Object content){
		this.tag = tag;
		this.msg = msg;
		this.content = content;
	}
	
	public Message(String tag, String msg){
		this(tag, msg, null);
	}
	
}
