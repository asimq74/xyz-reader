package com.example.xyzreader.data;

import android.arch.persistence.room.ColumnInfo;

public class BookHeaderTuple {
    @ColumnInfo(name = "author")
    private String author;
    @ColumnInfo(name = "photo")
    private String photo;
    @ColumnInfo(name = "published_date")
    private String publishedDate;
    @ColumnInfo(name = "thumb")
    private String thumb;
    @ColumnInfo(name = "title")
    private String title;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BookHeaderTuple{" +
                "author='" + author + '\'' +
                ", photo='" + photo + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", thumb='" + thumb + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
