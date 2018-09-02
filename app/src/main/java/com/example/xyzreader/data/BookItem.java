package com.example.xyzreader.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "bookItems")
public class BookItem {

	@SerializedName("aspect_ratio")
	@ColumnInfo(name = "aspectRatio")
	private float aspectRatio;
	@ColumnInfo(name = "author")
	@SerializedName("author")
	private String author;
	@SerializedName("body")
	@ColumnInfo(name = "body")
	private String body;
	@SerializedName("id")
	@NonNull
	@PrimaryKey
	@ColumnInfo(name = "id")
	private Integer id;
	@SerializedName("photo")
	@ColumnInfo(name = "photo")
	private String photo;
	@SerializedName("published_date")
	@ColumnInfo(name = "published_date")
	private String publishedDate;
	@SerializedName("thumb")
	@ColumnInfo(name = "thumb")
	private String thumb;
	@SerializedName("title")
	@ColumnInfo(name = "title")
	private String title;

	public BookItem() {
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public String getAuthor() {
		return author;
	}

	public String getBody() {
		return body;
	}

	public Integer getId() {
		return id;
	}

	public String getPhoto() {
		return photo;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public String getThumb() {
		return thumb;
	}

	public String getTitle() {
		return title;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
