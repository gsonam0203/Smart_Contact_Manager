package com.smart.helper;

public class Message {
	
	private String content;
	private String Type;
	
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message(String content, String type) {
		super();
		this.content = content;
		Type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	@Override
	public String toString() {
		return "Message [content=" + content + ", Type=" + Type + "]";
	}
	
	
	

}
