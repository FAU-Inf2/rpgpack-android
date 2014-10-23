package de.fau.cs.mad.rpgpack.jackson;

public class StringClass extends AbstractColumnEntry {
	public static final String TYPE_STRING = "string";
	
	public String mainText;
	
	public StringClass() {
		mainText = "";
	}
	
	public StringClass(String text) {
		this.mainText = text;
	}
	
	@Override
	public String toString() {
		return mainText;
	}
	
	@Override
	public String getType() {
		return TYPE_STRING;
	}

	@Override
	public void setContent(String content) {
		mainText = content;
	}

	@Override
	public String getContent() {
		return mainText;
	}
}
