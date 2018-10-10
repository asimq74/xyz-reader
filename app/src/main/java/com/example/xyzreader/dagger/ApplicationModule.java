package com.example.xyzreader.dagger;

import javax.inject.Singleton;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.xyzreader.dao.BookItemDao;
import com.example.xyzreader.data.BookItemDataSource;
import com.example.xyzreader.data.BookItemDatabase;
import com.example.xyzreader.data.BookItemRepository;

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
	private BookItemDatabase bookItemDatabase;
	private final Application mApplication;

	public ApplicationModule(Application app) {
		mApplication = app;
		bookItemDatabase = Room.databaseBuilder(mApplication,
				BookItemDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
	}

	@Provides
	@Singleton
	public BookItemRepository bookItemRepository(BookItemDao bookItemDao) {
		return new BookItemDataSource(bookItemDao);
	}

	@Provides
	public Application provideApplication() {
		return mApplication;
	}

	@Provides
	@Singleton
	public BookItemDatabase provideBookItemDatabase() {
		return bookItemDatabase;
	}

	@Provides
	public Context provideContext() {
		return mApplication;
	}

	@Provides
	@Singleton
	ProjectViewModelFactory provideProjectViewModelFactory(BookItemRepository bookItemRepository) {
		return new ProjectViewModelFactory(mApplication, bookItemRepository);
	}

	@Provides
	@Singleton
	AllItemsViewModelFactory provideAllItemsViewModelFactory(BookItemRepository bookItemRepository) {
		return new AllItemsViewModelFactory(mApplication, bookItemRepository);
	}

	@Provides
	@Singleton
	public BookItemDao providesBookItemDao(BookItemDatabase bookItemDatabase) {
		return bookItemDatabase.bookItemDao();
	}
}
