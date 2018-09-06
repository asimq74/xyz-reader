package com.example.xyzreader.data;

import java.util.List;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.remote.retrofit.GetBookItems;
import com.example.xyzreader.remote.retrofit.RetrofitClientInstance;

import javax.inject.Inject;

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

	@Inject
	BookItemDatabase bookItemDatabase;

	@Override
	protected void onHandleIntent(Intent intent) {
		Time time = new Time();
		final MyApplication application = (MyApplication) getApplicationContext();
		application.getApplicationComponent().inject(this);
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

			@Override
			public void onFailure(Call<List<BookItem>> call, Throwable t) {
				Log.e(this.getClass().getSimpleName(), "error calling service", t);
				Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResponse(Call<List<BookItem>> call, Response<List<BookItem>> response) {
				final List<BookItem> bookItems = response.body();
				if (bookItems == null || bookItems.size() == 0) {
					return;
				}

				new Thread(new Runnable() {
					@Override
					public void run() {
						bookItemDatabase.daoAccess().insertMultipleBookItems(bookItems);
						sendStickyBroadcast(
								new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
					}
				}).start();
			}
		});

	}
}
