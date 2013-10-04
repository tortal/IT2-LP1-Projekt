package it.chalmers.tendu.network;

public class NetworkMessage {
	private String type;
	private String content;
	
	public NetworkMessage () {
		type = "Default Message";
		content = "Just cruising";
	}
	
	public NetworkMessage(String type, String content) {
		this.type = type;
		this.content = content;
	}
	
	public String getType() {
		return type;
	}
	
	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return type + ": " + content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetworkMessage other = (NetworkMessage) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
}
