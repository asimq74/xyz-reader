package com.example.xyzreader.viewmodels;

import javax.inject.Inject;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.xyzreader.data.BookItem;
import com.example.xyzreader.data.BookItemRepository;

/**
 * Created by Md. Sifat-Ul Haque on 5/26/2017.
 */
public class BookItemViewModel extends AndroidViewModel {

	private final BookItemRepository bookItemRepository;
	private LiveData<BookItem> mLiveBookItem;
	private LiveData<String> mLiveBody;
	private MutableLiveData<Boolean> mLiveHeaderLoaded = new MutableLiveData<>();

	@Inject
	public BookItemViewModel(Application application, BookItemRepository bookItemRepository) {
		super(application);
		this.bookItemRepository = bookItemRepository;
	}

	public LiveData<Boolean> getHeaderLoaded() {
		return mLiveHeaderLoaded;
	}

	public void setHeaderLoaded(boolean headerLoaded) {
		this.mLiveHeaderLoaded.setValue(headerLoaded);
	}

	public LiveData<BookItem> getBookItem(int id) {
		if (mLiveBookItem == null) {
			mLiveBookItem = bookItemRepository.getBookItemById(id);
		}
		return mLiveBookItem;
	}

	public LiveData<String> getBody(int id) {
		if (mLiveBody == null) {
			mLiveBody = bookItemRepository.getBodyById(id);
		}
		return mLiveBody;
	}

}
