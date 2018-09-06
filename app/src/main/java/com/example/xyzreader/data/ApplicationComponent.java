package com.example.xyzreader.data;

import android.app.Application;
import android.content.Context;

import com.example.xyzreader.MyApplication;

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
