package de.fau.cs.mad.gamekobold.templatestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

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
	@JsonProperty("description")
	private String description = "no description available";;
	@JsonProperty("num_ratings")
	private int num_ratings = 0;
	@JsonProperty("rating")
	private float rating = 0F;
	@JsonProperty("insert_timestamp")
	private String inserted_at;
	@JsonProperty("image_data")
	private String image_data = null;
	
	@JsonIgnore
	private Bitmap bm;
	
	public String getImage_data() {
		return image_data;
	}

	public void setImage_data(String image_data) {
		this.image_data = image_data;
	}
	
	@JsonIgnore
	public Bitmap getBm() {
		return this.bm;
	}
	
	@JsonIgnore
	public void setBm(String image_data) {
		byte[] decodedString = Base64.decode(this.getImage_data(), Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
		this.bm = decodedByte;
	}
	
	
	
	public String getDescription() {
		if(description == null) {
			return "no description available";
		}
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

		// TODO after much trial and error, this works, but does not look right.. 
		// Maybe use JodaTime like suggested on StackOverflow
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.GERMANY);
		try {
			return sdf.format(sdf.parse(this.inserted_at));
		} catch (ParseException e) {
			Log.e("template_store", "Could not parse date");
		}
			
		return this.inserted_at;
		
	}
	
	@JsonProperty("insert_timestamp")
	public void setDate(String inserted_at) {
		this.inserted_at = inserted_at;
	}
	

	public boolean hasImage() {
		return this.image_data == null ?  false : true;
	}
}
