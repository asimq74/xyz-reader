package com.example.xyzreader.dagger;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.dao.BookItemDao;
import com.example.xyzreader.data.BodyLoaderById;
import com.example.xyzreader.data.BookItemDataSource;
import com.example.xyzreader.data.BookItemDatabase;
import com.example.xyzreader.data.BookItemRepository;
import com.example.xyzreader.viewmodels.BookItemViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 Module that provides application resources
 *
 * @author Asim Qureshi
 */
@Module
public class ApplicationModule {

	private static final String DATABASE_NAME = "bookitems_db";
	private final Application mApplication;
	private BookItemDatabase bookItemDatabase;

	public ApplicationModule(Application app) {
		mApplication = app;
		bookItemDatabase = Room.databaseBuilder(mApplication,
				BookItemDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
	}

	@Provides
	public Application provideApplication() {
		return mApplication;
	}


	@Provides
	@Singleton
	public BodyLoaderById provideBodyLoaderById(BookItemDatabase database) {
		return new BodyLoaderById(database);
	}

	@Provides
	@Singleton
	public BookItemDatabase provideBookItemDatabase() {
		return bookItemDatabase;
	}

	@Provides
	@Singleton
	public BookItemDao providesBookItemDao(BookItemDatabase bookItemDatabase) {
		return bookItemDatabase.bookItemDao();
	}

	@Provides
	@Singleton
	public BookItemRepository bookItemRepository(BookItemDao bookItemDao) {
		return new BookItemDataSource(bookItemDao);
	}

	@Provides
	public Context provideContext() {
		return mApplication;
	}

    @Provides
	@Singleton
    ViewModelProvider.Factory provideViewModelFactory(BookItemRepository bookItemRepository) {

        return new ProjectViewModelFactory(mApplication, bookItemRepository);
    }
}
