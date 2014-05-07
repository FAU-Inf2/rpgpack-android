package de.fau.mad.sqlite;

public class KoboldEntryDAO {

	protected String entryID;
	protected String feature;
	protected String value;
	protected String description;

	public KoboldEntryDAO() {
	}

	public KoboldEntryDAO(String entryID, String feature, String value,
			String description) {
		this.entryID = entryID;
		this.feature = feature;
		this.value = value;
		this.description = description;
	}

	public String getEntryID() {
		return entryID;
	}

	public void setEntryID(String entryID) {
		this.entryID = entryID;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		String result = "";
		result += "Entry id: " + this.getEntryID() + "\n";
		result += "Feature: " + this.getFeature() + "\n";
		result += "Value: " + this.getValue() + "\n";
		result += "Description: " + this.getDescription() + "\n";

		return result;
	}

}
