package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectAndFavorableItem {
	protected boolean isFavorite = false;
	protected boolean isSelected = true;

	@JsonIgnore
	public boolean isFavorite() {
		return isFavorite;
	}

	@JsonIgnore
	public void setFavorite(boolean favorite) {
		isFavorite = favorite;
	}
	@JsonIgnore
	public boolean isSelected() {
		return isSelected;
	}

	@JsonIgnore
	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	@JsonProperty("fav")
	private int jsonFavGetter() {
		if(isFavorite) {
			return 1;
		}
		return 0;
	}
	
	@JsonProperty("fav")
	private void jsonFavSetter(int value) {
		if(value == 0) {
			isFavorite = false;
		}
		else {
			isFavorite = true;
		}
	}

	@JsonProperty("sel")
	private int jsonSelectedGetter() {
		if(isSelected) {
			return 1;
		}
		return 0;
	}

	@JsonProperty("sel")
	private void jsonSelectedSetter(int value) {
		if(value == 0) {
			isSelected = false;
		}
		else {
			isSelected = true;	 
		}
	}
}
