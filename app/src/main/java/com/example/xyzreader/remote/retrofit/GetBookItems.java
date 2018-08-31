package com.example.xyzreader.remote.retrofit;

import java.util.List;

import com.example.xyzreader.data.BookItem;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetBookItems {

	@GET("/xyz-reader-json")
	Call<List<BookItem>> getAllBookItems();
}
