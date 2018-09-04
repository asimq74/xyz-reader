package com.example.xyzreader.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.AllBookItemsLoader;
import com.example.xyzreader.data.BookItem;
import com.example.xyzreader.data.UpdaterService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<BookItem>> {

    public static final String ITEM_ID = "ITEM_ID";
    private static final String TAG = ArticleListActivity.class.toString();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);
    private boolean mIsRefreshing = false;
    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                updateRefreshingUI();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        prepareLoader(23);
        if (savedInstanceState == null) {
            refresh();
        }
    }

    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }

    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

    @Override
    public Loader<List<BookItem>> onCreateLoader(int i, Bundle bundle) {
        return AllBookItemsLoader.newBookItemLoaderInstance(this);
    }


    protected void prepareLoader(final int loaderId) {
        if (getSupportLoaderManager().getLoader(loaderId) == null) {
            getSupportLoaderManager().initLoader(loaderId, null, this).forceLoad();
            return;
        }
        getSupportLoaderManager().restartLoader(loaderId, null, this).forceLoad();
    }

    @Override
    public void onLoadFinished(Loader<List<BookItem>> loader, List<BookItem> data) {
        Adapter adapter = new Adapter(data);
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<BookItem>> loader) {
        mRecyclerView.setAdapter(null);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public DynamicHeightNetworkImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;
        public ImageButton expandButtonView;
        public TextView supportingTextView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = view.findViewById(R.id.thumbnail);
            titleView = view.findViewById(R.id.article_title);
            subtitleView = view.findViewById(R.id.article_subtitle);
            expandButtonView = view.findViewById(R.id.expand_button);
            supportingTextView = view.findViewById(R.id.supporting_text);
            expandButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (supportingTextView.getVisibility() == View.VISIBLE) {
                        expandButtonView.setImageResource(R.drawable.ic_expand_less_black_36dp);
                        supportingTextView.setVisibility(View.GONE);
                    } else {
                        expandButtonView.setImageResource(R.drawable.ic_expand_more_black_36dp);
                        supportingTextView.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<BookItem> data;

        public Adapter(List<BookItem> data) {
            this.data = data;
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_item_article_constraint2, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int adapterPosition = vh.getAdapterPosition();
                    final long itemId = getItemId(adapterPosition);
                    final Intent intent = new Intent(ArticleListActivity.this, ArticleDetailActivity.class);
                    intent.putExtra(ITEM_ID, itemId);
                    startActivity(intent);
                }
            });
            return vh;
        }

        private Date parsePublishedDate(int position) {
            try {
                String date = data.get(position).getPublishedDate();
                return dateFormat.parse(date);
            } catch (ParseException ex) {
                Log.e(TAG, ex.getMessage());
                Log.i(TAG, "passing today's date");
                return new Date();
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BookItem bookItem = data.get(position);
            holder.titleView.setText(bookItem.getTitle());
            Date publishedDate = parsePublishedDate(position);
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {

                holder.subtitleView.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + "<br/>" + " by "
                                + bookItem.getAuthor()));
            } else {
                holder.subtitleView.setText(Html.fromHtml(
                        outputFormat.format(publishedDate)
                                + "<br/>" + " by "
                                + bookItem.getAuthor()));
            }
            holder.thumbnailView.setImageUrl(
                    bookItem.getThumb(),
                    ImageLoaderHelper.getInstance(ArticleListActivity.this).getImageLoader());
            holder.thumbnailView.setAspectRatio(bookItem.getAspectRatio());
            holder.supportingTextView.setText(bookItem.getBody());
            holder.expandButtonView.setImageResource(R.drawable.ic_expand_less_black_36dp);
            holder.supportingTextView.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

}
