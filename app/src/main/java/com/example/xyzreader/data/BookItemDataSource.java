package com.example.xyzreader.data;

import java.util.List;

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
	public LiveData<BookItem> getBookItemById(int id) {
		return bookItemDao.fetchOneBookItembyId(id);
	}

	@Override
	public LiveData<List<BookItem>> getAllBookItems() {
		return bookItemDao.fetchAllLiveBookItems();
	}
}
