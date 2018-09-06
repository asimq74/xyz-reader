package com.example.xyzreader.dagger;

import android.app.Application;
import android.content.Context;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.data.AllBookItemsLoader;
import com.example.xyzreader.data.BookItemDatabase;
import com.example.xyzreader.data.SingleBookItemLoader;
import com.example.xyzreader.data.UpdaterService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Provides application resources module
 *
 * @author Asim Qureshi
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application getApplication();

    Context getContext();

    BookItemDatabase getBookItemDatabase();

    // allow to inject into our Main class
    // method name not important
    void inject(AllBookItemsLoader allBookItemsLoader);

    void inject(MyApplication application);

    void inject(UpdaterService updaterService);

    void inject(SingleBookItemLoader singleBookItemLoader);

}
