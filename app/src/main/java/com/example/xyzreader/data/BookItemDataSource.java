package com.example.xyzreader.data;

import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.example.xyzreader.dao.BookItemDao;

public class BookItemDataSource implements BookItemRepository {

	private BookItemDao bookItemDao;
	private MediatorLiveData<BookItem> mBookItemLive = new MediatorLiveData<>();
	private MediatorLiveData<List<BookItem>> mAllBookItemsLive = new MediatorLiveData<>();

	@Inject
	public BookItemDataSource(BookItemDao bookItemDao) {
		this.bookItemDao = bookItemDao;
	}

	@Override
	public LiveData<BookItem> getBookItemById(int id) {
		final LiveData<BookItem> bookItem = bookItemDao.fetchOneBookItembyId(id);
		mBookItemLive.addSource(bookItem, new Observer<BookItem>() {
			@Override
			public void onChanged(@Nullable BookItem bookItem) {
				mBookItemLive.setValue(bookItem);
			}
		});
		return mBookItemLive;
	}

	@Override
	public LiveData<List<BookItem>> getAllBookItems() {
		final LiveData<List<BookItem>> allBookItems = bookItemDao.fetchAllLiveBookItems();
		mAllBookItemsLive.addSource(allBookItems, new Observer<List<BookItem>>() {
			@Override
			public void onChanged(@Nullable List<BookItem> bookItems) {
				mAllBookItemsLive.setValue(bookItems);
			}
		});
		return mAllBookItemsLive;
	}
}
