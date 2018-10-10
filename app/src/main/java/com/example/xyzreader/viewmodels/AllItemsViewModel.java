package com.example.xyzreader.viewmodels;

import java.util.List;

import javax.inject.Inject;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.xyzreader.data.BookItem;
import com.example.xyzreader.data.BookItemRepository;

/**
 * Created by Md. Sifat-Ul Haque on 5/26/2017.
 */
public class AllItemsViewModel extends AndroidViewModel {

	private final BookItemRepository bookItemRepository;
	private LiveData<List<BookItem>> mLiveBookItems;

	@Inject
	public AllItemsViewModel(Application application, BookItemRepository bookItemRepository) {
		super(application);
		this.bookItemRepository = bookItemRepository;
	}

	public LiveData<List<BookItem>> getAllBookItems() {
		if (mLiveBookItems == null) {
			mLiveBookItems = bookItemRepository.getAllBookItems();
		}
		return mLiveBookItems;
	}

}
