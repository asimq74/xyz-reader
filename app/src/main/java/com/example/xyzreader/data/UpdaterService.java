package com.example.xyzreader.data;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.xyzreader.remote.retrofit.GetBookItems;
import com.example.xyzreader.remote.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdaterService extends IntentService {

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.example.xyzreader.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.example.xyzreader.intent.extra.REFRESHING";
    private static final String TAG = "UpdaterService";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time time = new Time();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        /*Create handle for the RetrofitInstance interface*/
        GetBookItems service = RetrofitClientInstance.getRetrofitInstance().create(GetBookItems.class);
        Call<List<BookItem>> call = service.getAllBookItems();
        call.enqueue(new Callback<List<BookItem>>() {
            private static final String DATABASE_NAME = "bookitems_db";
            private BookItemDatabase bookItemDatabase;

            @Override
            public void onFailure(Call<List<BookItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call<List<BookItem>> call, Response<List<BookItem>> response) {
                final List<BookItem> bookItems = response.body();
                if (bookItems == null || bookItems.size() == 0) {
                    return;
                }


                bookItemDatabase = Room.databaseBuilder(getApplicationContext(),
                        BookItemDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();


                // Don't even inspect the intent, we only do one thing, and that's fetch content.
                ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

                Uri dirUri = ItemsContract.Items.buildDirUri();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bookItemDatabase.daoAccess().insertMultipleBookItems(bookItems);
                    }
                }).start();
            }
        });

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }
}
