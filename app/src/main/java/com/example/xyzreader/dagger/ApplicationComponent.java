package com.example.xyzreader.dagger;

import javax.inject.Singleton;

import android.app.Application;
import android.content.Context;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.data.AllBookItemsLoader;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.ui.ArticleDetailActivity;
import com.example.xyzreader.ui.ArticleListActivity;
import com.example.xyzreader.viewmodels.AllItemsViewModel;
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

	Context context();

	// allow to inject into our Main class
	// method name not important
	void inject(AllBookItemsLoader allBookItemsLoader);

	void inject(MyApplication application);

	void inject(UpdaterService updaterService);

	void inject(ArticleDetailActivity articleDetailActivity);

	void inject(ArticleListActivity articleListActivity);

	void inject(BookItemViewModel bookItemViewModel);

	void inject(AllItemsViewModel allItemsViewModel);

}
