package com.example.xyzreader.data;

import javax.inject.Inject;

import com.example.xyzreader.dao.BookItemDao;

public class BookItemDataSource implements BookItemRepository {

	private BookItemDao bookItemDao;

	@Inject
	public BookItemDataSource(BookItemDao bookItemDao) {
		this.bookItemDao = bookItemDao;
	}

	@Override
	public String getBodyById(int id) {
		return bookItemDao.fetchBodyById(id);
	}
}
