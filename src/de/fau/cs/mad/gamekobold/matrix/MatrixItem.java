package de.fau.cs.mad.gamekobold.matrix;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.fau.cs.mad.gamekobold.jackson.SelectAndFavorableItem;

public class MatrixItem extends SelectAndFavorableItem{
	private String itemName;
	private String value;
	private int rangeMin;
	private int rangeMax;
	private String modificator;

	// needed for jackson
	public MatrixItem() {
		this.itemName= ""; 
		this.value = "";
		this.rangeMin = Integer.MIN_VALUE; 
		this.rangeMax = Integer.MAX_VALUE;
		this.modificator = "";		
	}

	public MatrixItem(String itemName,
						String value,
						int rangeMin,
						int rangeMax,
						String modificator) {
		this.itemName = itemName;
		this.value = value;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.modificator = modificator;
		this.isSelected = false;
	}

	public MatrixItem(String itemName, String value, String modificator) {
		this.itemName = itemName;
		this.value = value;
		this.rangeMin = Integer.MIN_VALUE;
		this.rangeMax = Integer.MAX_VALUE;
		this.modificator = modificator;
		this.isSelected = false;
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
}
