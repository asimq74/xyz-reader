package com.example.xyzreader.data;

import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.xyzreader.MyApplication;

public class AllBookItemsLoader extends AsyncTaskLoader<List<BookItem>> {
    private static final String DATABASE_NAME = "bookitems_db";
    @Inject
    BookItemDatabase bookItemDatabase;

    public static AsyncTaskLoader<List<BookItem>> newBookItemLoaderInstance(Context context) {
        return new AllBookItemsLoader(context);
    }

    public AllBookItemsLoader(Context context) {
        super(context);
        final MyApplication application = (MyApplication) context.getApplicationContext();
        application.getApplicationComponent().inject(this);
    }

    @Override
    public List<BookItem> loadInBackground() {
        return bookItemDatabase.bookItemDao().fetchAllBookItems();
    }
}
