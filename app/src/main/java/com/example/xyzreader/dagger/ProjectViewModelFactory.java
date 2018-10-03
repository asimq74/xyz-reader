package com.example.xyzreader.dagger;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.example.xyzreader.data.BookItemRepository;
import com.example.xyzreader.viewmodels.BookItemViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProjectViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final BookItemRepository bookItemRepository;

    @Inject
    public ProjectViewModelFactory(Application application, BookItemRepository bookItemRepository) {
        this.application = application;
        this.bookItemRepository = bookItemRepository;
    }

    @Override
    public BookItemViewModel create(Class modelClass) {
        return new BookItemViewModel(application, bookItemRepository);
    }
}
