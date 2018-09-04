package com.example.xyzreader.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

public class SingleBookItemLoader extends AsyncTaskLoader<BookItem> {
    private static final String DATABASE_NAME = "bookitems_db";
    private final long itemId;
    private BookItemDatabase bookItemDatabase;

    public SingleBookItemLoader(@NonNull Context context, long itemId) {
        super(context);
        this.itemId = itemId;
        bookItemDatabase = Room.databaseBuilder(context,
                BookItemDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    public static AsyncTaskLoader<BookItem> newBookItemLoaderInstance(@NonNull Context context, long itemId) {
        return new SingleBookItemLoader(context, itemId);
    }

    @Override
    public BookItem loadInBackground() {
        return bookItemDatabase.daoAccess().fetchOneBookItembyId((int) (long) itemId);
    }
}
