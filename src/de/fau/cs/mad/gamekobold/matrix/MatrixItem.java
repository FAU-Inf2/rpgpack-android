package de.fau.cs.mad.gamekobold.matrix;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatrixItem {
	//@JsonProperty(value="name")
	private String itemName;
	//@JsonProperty("val")
	private String value;
	//@JsonProperty("min")
	private int rangeMin;
	//@JsonProperty("max")
	private int rangeMax;
	//@JsonProperty("mod")
	private String modificator;
	
	@JsonCreator
	public MatrixItem(@JsonProperty("itemName") String itemName,
						@JsonProperty("value") String value,
						@JsonProperty("rangeMin") int rangeMin,
						@JsonProperty("rangeMax") int rangeMax,
						@JsonProperty("modificator") String modificator) {
		this.itemName = itemName;
		this.value = value;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.modificator = modificator;
	}
	
	public MatrixItem(String itemName, String value, String modificator){
		this.itemName = itemName;
		this.value = value;
		this.rangeMin = Integer.MIN_VALUE;
		this.rangeMax = Integer.MAX_VALUE;
		this.modificator = modificator;
	}
	
	//@JsonProperty("name")
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(/*@JsonProperty("name")*/ String itemName) {
		this.itemName = itemName;
	}
	
	//@JsonProperty("val")
	public String getValue() {
		return value;
	}
	
	//@JsonProperty("val")
	public void setValue(String value) {
		this.value = value;
	}
	
	//@JsonProperty("min")
	public int getRangeMin() {
		return rangeMin;
	}
	
	//@JsonProperty("min")
	public void setRangeMin(int rangeMin) {
		this.rangeMin = rangeMin;
	}
	
	//@JsonProperty("max")
	public int getRangeMax() {
		return rangeMax;
	}
	
	//@JsonProperty("max")
	public void setRangeMax(int rangeMax) {
		this.rangeMax = rangeMax;
	}
	
	//@JsonProperty("mod")
	public String getModificator() {
		return modificator;
	}
	
	//@JsonProperty("mod")
	public void setModificator(String modificator) {
		this.modificator = modificator;
	}
	
}
