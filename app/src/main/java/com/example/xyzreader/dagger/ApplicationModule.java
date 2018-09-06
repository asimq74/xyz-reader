package com.example.xyzreader.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.xyzreader.data.BookItemDatabase;

import javax.inject.Singleton;

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

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    BookItemDatabase provideBookItemDatabase(Context context) {
        return Room.databaseBuilder(context,
                BookItemDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }


}
