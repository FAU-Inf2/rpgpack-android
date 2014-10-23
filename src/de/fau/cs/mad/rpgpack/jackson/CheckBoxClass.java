package de.fau.cs.mad.rpgpack.jackson;

public class CheckBoxClass extends AbstractColumnEntry{
	public static final String TYPE_STRING = "chkbox";
	
	public boolean checked;
	
	public CheckBoxClass() {
		checked = false;
	}
	
	public CheckBoxClass(boolean isChecked) {
		checked = isChecked;
	}
	
	public CheckBoxClass(String value) {
		// true if value == "true", false otherwise. no exception thrown!
		checked = Boolean.parseBoolean(value);		
	}

	@Override
	public String getType() {
		return TYPE_STRING;
	}
	
	@Override
	public String toString() {
		return String.valueOf(checked);
	}

	@Override
	public void setContent(String content) {
		// true if content == "true", false otherwise. no exception thrown!
		checked = Boolean.parseBoolean(content);
	}

	@Override
	public String getContent() {
		return String.valueOf(checked);
	}
}
