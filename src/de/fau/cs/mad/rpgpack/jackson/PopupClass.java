package de.fau.cs.mad.rpgpack.jackson;

public class PopupClass extends AbstractColumnEntry{
	public static final String TYPE_STRING = "popup";
	
	String content;
	
	public PopupClass() {
		content = "";
	}
	
	public PopupClass(String content) {
		this.content = content;
	}

	@Override
	public String getType() {
		return TYPE_STRING;
	}

	@Override
	public String toString() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}
}
