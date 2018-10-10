package com.example.xyzreader.dagger;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.example.xyzreader.data.BookItemRepository;
import com.example.xyzreader.viewmodels.AllItemsViewModel;

@Singleton
public class AllItemsViewModelFactory implements ViewModelProvider.Factory {

	private final Application application;
	private final BookItemRepository bookItemRepository;

	@Inject
	public AllItemsViewModelFactory(Application application, BookItemRepository bookItemRepository) {
		this.application = application;
		this.bookItemRepository = bookItemRepository;
	}

	@Override
	public AllItemsViewModel create(Class modelClass) {
		return new AllItemsViewModel(application, bookItemRepository);
	}
}
