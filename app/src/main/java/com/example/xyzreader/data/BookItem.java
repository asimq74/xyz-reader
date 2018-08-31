package com.example.xyzreader.data;

import com.google.gson.annotations.SerializedName;

public class BookItem {

	@SerializedName("aspect_ratio")
	private float aspectRatio;
	@SerializedName("author")
	private String author;
	@SerializedName("body")
	private String body;
	@SerializedName("id")
	private Integer id;
	@SerializedName("photo")
	private String photo;
	@SerializedName("published_date")
	private String publishedDate;
	@SerializedName("thumb")
	private String thumb;
	@SerializedName("title")
	private String title;

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
