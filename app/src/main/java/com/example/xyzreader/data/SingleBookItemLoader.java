package com.example.xyzreader.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.example.xyzreader.MyApplication;

import javax.inject.Inject;

public class SingleBookItemLoader extends AsyncTaskLoader<BookItem> {
    private final long itemId;
    @Inject BookItemDatabase bookItemDatabase;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    public SingleBookItemLoader(@NonNull Context context, long itemId) {
        super(context);
        this.itemId = itemId;
        final MyApplication application = (MyApplication) context.getApplicationContext();
        application.getApplicationComponent().inject(this);
    }

    public static AsyncTaskLoader<BookItem> newBookItemLoaderInstance(@NonNull Context context, long itemId) {
        return new SingleBookItemLoader(context, itemId);
    }

    @Override
    public BookItem loadInBackground() {
        return bookItemDatabase.daoAccess().fetchOneBookItembyId((int) (long) itemId);
    }
}
