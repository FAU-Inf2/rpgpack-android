package de.fau.cs.mad.gamekobold.matrix;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.fau.cs.mad.gamekobold.jackson.SelectAndFavorableItem;

/**
 * This class represents an item in the special so called matrix view (UI). This
 * view allows to represent a lot of independent elements at the same time.
 * Matrix Item will be used typically for a RPG Character Attribute like
 * Strength, Health, Dexterity etc.
 * 
 */
public class MatrixItem extends SelectAndFavorableItem {
	private String itemName;
	private String value;
	private int rangeMin;
	private int rangeMax;
	private String modificator;
	private String description;
	private int visibility;

	/**
	 * needed for jackson, set values to default values
	 */
	public MatrixItem() {
		this.itemName = "";
		this.value = "";
		this.rangeMin = Integer.MIN_VALUE;
		this.rangeMax = Integer.MAX_VALUE;
		this.modificator = "";
		this.description = "";
		this.visibility = 15; // from, to, value, mod
	}

	public MatrixItem(String itemName, String value, int rangeMin,
			int rangeMax, String modificator, String description, int visibility) {
		this.itemName = itemName;
		this.value = value;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.modificator = modificator;
		this.isSelected = false;
		this.description = description;
		this.visibility = visibility;
	}

	public MatrixItem(String itemName, String value, String modificator) {
		this.itemName = itemName;
		this.value = value;
		this.rangeMin = Integer.MIN_VALUE;
		this.rangeMax = Integer.MAX_VALUE;
		this.modificator = modificator;
		this.isSelected = false;
		this.description = "";
		this.visibility = 15;
	}

	public String getItemName() {
		return itemName;
	}

	@JsonProperty("name")
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getValue() {
		return value;
	}

	@JsonProperty("val")
	public void setValue(String value) {
		this.value = value;
	}

	public int getRangeMin() {
		return rangeMin;
	}

	@JsonProperty("min")
	public void setRangeMin(int rangeMin) {
		this.rangeMin = rangeMin;
	}

	public int getRangeMax() {
		return rangeMax;
	}

	@JsonProperty("max")
	public void setRangeMax(int rangeMax) {
		this.rangeMax = rangeMax;
	}

	public String getModificator() {
		return modificator;
	}

	@JsonProperty("mod")
	public void setModificator(String modificator) {
		this.modificator = modificator;
	}

	public String getDescription() {
		return description;
	}

	@JsonProperty("des")
	public void setDescription(String description) {
		this.description = description;
	}

	public int getVisibility() {
		return visibility;
	}

	@JsonProperty("vis")
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
}
