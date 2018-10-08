package com.example.xyzreader.data;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.example.xyzreader.dao.BookItemDao;

public class BookItemDataSource implements BookItemRepository {

	private BookItemDao bookItemDao;
	private MediatorLiveData<String> mBodyLive = new MediatorLiveData<>();
	private MediatorLiveData<BookHeaderTuple> mHeaderTupleLive = new MediatorLiveData<>();
	private MediatorLiveData<BookItem> mBookItemLive = new MediatorLiveData<>();

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

	@Override
	public LiveData<BookHeaderTuple> getHeaderTupleById(int id) {
		final LiveData<BookHeaderTuple> headerTuple = bookItemDao.fetchBookHeaderTupleById(id);
		mHeaderTupleLive.addSource(headerTuple, new Observer<BookHeaderTuple>() {
			@Override
			public void onChanged(@Nullable BookHeaderTuple bookHeaderTuple) {
				mHeaderTupleLive.setValue(bookHeaderTuple);
			}
		});
		return mHeaderTupleLive;
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
}
