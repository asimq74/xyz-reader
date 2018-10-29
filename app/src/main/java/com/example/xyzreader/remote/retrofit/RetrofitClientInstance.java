package com.example.xyzreader.remote.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit.Builder;

public class RetrofitClientInstance {
	private static Retrofit retrofit;
	private static final String BASE_URL = "https://go.udacity.com";

	public static Retrofit getRetrofitInstance() {
		if (retrofit == null) {
			retrofit = new Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}
		return retrofit;
	}
}
