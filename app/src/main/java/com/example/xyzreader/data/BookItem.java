package com.example.xyzreader.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "bookItems")
public class BookItem implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public BookItem createFromParcel(Parcel in) {
            return new BookItem(in);
        }

        @Override
        public BookItem[] newArray(int size) {
            return new BookItem[size];
        }
    };
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

    public BookItem(Parcel parcel) {
        aspectRatio = parcel.readFloat();
        author = parcel.readString();
        body = parcel.readString();
        id = parcel.readInt();
        photo = parcel.readString();
        publishedDate = parcel.readString();
        thumb = parcel.readString();
        title = parcel.readString();
    }

    public BookItem() {
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public String toString() {
        return "BookItem{" +
                "aspectRatio=" + aspectRatio +
                ", author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", id=" + id +
                ", photo='" + photo + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", thumb='" + thumb + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(aspectRatio);
        parcel.writeString(author);
        parcel.writeString(body);
        parcel.writeInt(id);
        parcel.writeString(photo);
        parcel.writeString(publishedDate);
        parcel.writeString(thumb);
        parcel.writeString(title);
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
