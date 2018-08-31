package com.example.xyzreader.data;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.xyzreader.remote.retrofit.GetBookItems;
import com.example.xyzreader.remote.retrofit.RetrofitClientInstance;

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

				// Don't even inspect the intent, we only do one thing, and that's fetch content.
				ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

				Uri dirUri = ItemsContract.Items.buildDirUri();

				// Delete all items
				cpo.add(ContentProviderOperation.newDelete(dirUri).build());
				for (BookItem item : bookItems) {
					ContentValues values = new ContentValues();
					values.put(ItemsContract.Items.SERVER_ID, item.getId());
					values.put(ItemsContract.Items.AUTHOR, item.getAuthor());
					values.put(ItemsContract.Items.TITLE, item.getTitle());
					values.put(ItemsContract.Items.BODY, item.getBody());
					values.put(ItemsContract.Items.THUMB_URL, item.getThumb());
					values.put(ItemsContract.Items.PHOTO_URL, item.getPhoto());
					values.put(ItemsContract.Items.ASPECT_RATIO, "" + item.getAspectRatio());
					values.put(ItemsContract.Items.PUBLISHED_DATE, item.getPublishedDate());
					cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
				}
				try {
					getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
				} catch (RemoteException | OperationApplicationException e) {
					Log.e(TAG, "Error updating content.", e);
				}
			}
		});

		sendStickyBroadcast(
				new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
	}
}
