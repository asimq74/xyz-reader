package com.example.xyzreader.dagger;

import javax.inject.Singleton;

import android.app.Application;
import android.content.Context;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.dao.BookItemDao;
import com.example.xyzreader.data.AllBookItemsLoader;
import com.example.xyzreader.data.BookItemDatabase;
import com.example.xyzreader.data.BookItemRepository;
import com.example.xyzreader.data.SingleBookItemLoader;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.ui.ArticleDetailActivity;
import com.example.xyzreader.viewmodels.BookItemViewModel;

import dagger.Component;

/**
 * Provides application resources module
 *
 * @author Asim Qureshi
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

	Application application();

	BookItemDatabase bookItemDatabase();

	BookItemDao bookItemDao();

	BookItemRepository bookItemRepository();

	Context context();

	// allow to inject into our Main class
	// method name not important
	void inject(AllBookItemsLoader allBookItemsLoader);

	void inject(MyApplication application);

	void inject(UpdaterService updaterService);

	void inject(SingleBookItemLoader singleBookItemLoader);

	void inject(ArticleDetailActivity articleDetailActivity);

	void inject(BookItemViewModel bookItemViewModel);

}
