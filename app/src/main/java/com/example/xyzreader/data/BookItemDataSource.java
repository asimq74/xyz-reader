package com.example.xyzreader.data;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;

import com.example.xyzreader.dao.BookItemDao;

public class BookItemDataSource implements BookItemRepository {

	private BookItemDao bookItemDao;

	@Inject
	public BookItemDataSource(BookItemDao bookItemDao) {
		this.bookItemDao = bookItemDao;
	}

	@Override
	public LiveData<String> getBodyById(int id) {
		return bookItemDao.fetchBodyById(id);
	}
}
