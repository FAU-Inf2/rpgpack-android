package de.fau.cs.mad.gamekobold.templatestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreTemplate {
	
	@JsonProperty("author")
	private String author;
	@JsonProperty("name")
	private String name;
	@JsonProperty("worldname")
	private String worldname;
	@JsonProperty("id")
	private int id;
	private String description = "no description available";;
	@JsonProperty("num_ratings")
	private int num_ratings = 0;
	@JsonProperty("rating")
	private float rating = 0F;
	@JsonProperty("insert_timestamp")
	private String inserted_at;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	StoreTemplate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("d.M.y", Locale.GERMANY);
		this.inserted_at = format.format(date);
	}

	public String getWorldname() {
		return worldname;
	}
	
	public void setWorldname(String worldname) {
		this.worldname = worldname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.author + " , " + this.name + " , " + this.worldname + ", " + this.description;
	}

	public int getNum_ratings() {
		return num_ratings;
	}

	public void setNum_ratings(int num_ratings) {
		this.num_ratings = num_ratings;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
	
	@JsonProperty("insert_timestamp")
	public String getDate() {
		// TODO Auto-generated method stub
		return this.inserted_at;
	}
	
	@JsonProperty("insert_timestamp")
	public void setDate(String inserted_at) {
		this.inserted_at = inserted_at;
	}
	

}
