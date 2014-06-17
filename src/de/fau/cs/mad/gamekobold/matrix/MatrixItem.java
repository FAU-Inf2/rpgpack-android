package de.fau.cs.mad.gamekobold.matrix;

public class MatrixItem {
	private String itemName;
	private String value;
	private int rangeMin;
	private int rangeMax;
	private String modificator;
	
	public MatrixItem(String itemName, String value, int rangeMin, int rangeMax, String modificator){
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
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getRangeMin() {
		return rangeMin;
	}
	public void setRangeMin(int rangeMin) {
		this.rangeMin = rangeMin;
	}
	public int getRangeMax() {
		return rangeMax;
	}
	public void setRangeMax(int rangeMax) {
		this.rangeMax = rangeMax;
	}
	public String getModificator() {
		return modificator;
	}
	public void setModificator(String modificator) {
		this.modificator = modificator;
	}
	
}
