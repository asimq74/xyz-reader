package com.example.xyzreader.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import com.example.xyzreader.dao.BookItemDao;

import java.util.List;

public class BookItemDataSource implements BookItemRepository {

	private MediatorLiveData<String> mBodyLive = new MediatorLiveData<>();

	private BookItemDao bookItemDao;

	@Inject
	public BookItemDataSource(BookItemDao bookItemDao) {
		this.bookItemDao = bookItemDao;
	}

	@Override
	public LiveData<String> getBodyById(int id) {
		final LiveData<String> body = bookItemDao.fetchBodyById(id);
		mBodyLive.addSource(body, new Observer<String>() {
			@Override
			public void onChanged(@Nullable String bodyString) {
				mBodyLive.setValue(bodyString);
			}
		});
		return mBodyLive;
	}
}
