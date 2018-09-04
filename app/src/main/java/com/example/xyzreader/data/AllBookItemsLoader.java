package com.example.xyzreader.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class AllBookItemsLoader extends AsyncTaskLoader<List<BookItem>> {
    private static final String DATABASE_NAME = "bookitems_db";
    private BookItemDatabase bookItemDatabase;

    public static AsyncTaskLoader<List<BookItem>> newBookItemLoaderInstance(Context context) {
        return new AllBookItemsLoader(context);
    }

    public AllBookItemsLoader(Context context) {
        super(context);
        bookItemDatabase = Room.databaseBuilder(context,
                BookItemDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    @Override
    public List<BookItem> loadInBackground() {
        return bookItemDatabase.daoAccess().fetchAllBookItems();
    }
}
