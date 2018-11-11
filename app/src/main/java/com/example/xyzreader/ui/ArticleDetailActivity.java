package com.example.xyzreader.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.R;
import com.example.xyzreader.dagger.ProjectViewModelFactory;
import com.example.xyzreader.data.BookItem;
import com.example.xyzreader.viewmodels.BookItemViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.float_header_view)
    HeaderView floatHeaderView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_header_view)
    HeaderView toolbarHeaderView;
    @BindView(R.id.body_text_recycler_view)
    RecyclerView mRecyclerView;
    @Inject
    ProjectViewModelFactory viewModelFactory;
    private String publishedDate;
    private CardView cardView;
    private BookItem data;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
    private boolean isHideToolbarView = false;
    private long mStartId;
    private BookItemViewModel viewModel;
    private SnackBarListener snackBarListener = new SnackBarListener();
    ;

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.theme_primary_dark);
        int primary = getResources().getColor(R.color.theme_primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground(findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @OnClick(R.id.fab)
    public void onClickFab(View view) {
        Snackbar mySnackbar = Snackbar.make(view, R.string.thanksForSharing, Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.shareArticle, snackBarListener);
        TextView text = mySnackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(getResources().getColor(R.color.ltgray));
        mySnackbar.setActionTextColor(Color.WHITE);
        mySnackbar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        final MyApplication application = (MyApplication) getApplicationContext();
        application.getApplicationComponent().inject(this);
        collapsingToolbarLayout.setTitle(" ");
        cardView = findViewById(R.id.cardView);
        appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                mStartId = getIntent().getLongExtra(ArticleListActivity.ITEM_ID, 0);
            }
        } else {
            mStartId = savedInstanceState.getLong(ArticleListActivity.ITEM_ID);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            toolbarHeaderView.findViewById(R.id.header_view_author).setVisibility(View.GONE);
            toolbarHeaderView.findViewById(R.id.header_view_published_date).setVisibility(View.GONE);
            final TextView titleView = toolbarHeaderView.findViewById(R.id.header_view_title);
            titleView.setTextAppearance(this,
                    getResources().getBoolean(R.bool.isTablet) ?
                            android.R.style.TextAppearance_Material_Headline :
                            android.R.style.TextAppearance_Material_Subhead);
            titleView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(ArticleListActivity.ITEM_ID, mStartId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BookItemViewModel.class);
        final int bookId = (int) this.mStartId;
        viewModel.getBookItem(bookId).observe(this, bookItem -> populateInitialView(bookItem));
        viewModel.getBody(bookId).observe(this, body -> populateBody(body));
    }

    private Date parsePublishedDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    private void populateUI(@NonNull String[] paragraphs) {
        Adapter adapter = new Adapter(paragraphs);
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
    }

    protected void populateBody(String body) {
        cardView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        populateUI(body.split("(\r\n\r\n)"));
        progressBar.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
    }

    private void populateInitialView(@NonNull BookItem bookItem) {
        if (bookItem != null) {
            long start = System.currentTimeMillis();
            data = bookItem;
            publishedDate = DateUtils.getRelativeTimeSpanString(
                    parsePublishedDate(bookItem.getPublishedDate()).getTime(),
                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString();
            toolbarHeaderView.bindTo(bookItem.getTitle(), bookItem.getAuthor(), publishedDate);
            floatHeaderView.bindTo(bookItem.getTitle(), bookItem.getAuthor(), publishedDate);
            final ImageView photoView = findViewById(R.id.photo);
            final String photoUrl = bookItem.getPhoto();
            Picasso.with(ArticleDetailActivity.this).load(photoUrl).into(photoView, new Callback() {
                @Override
                public void onError() {
                    Log.e(TAG, "error loading photoView with url: " + photoUrl);
                }

                private void onGenerated(Palette palette) {
                    applyPalette(palette);
                }

                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                    Palette.from(bitmap).generate(this::onGenerated);
                    viewModel.getBody(data.getId()).observe(ArticleDetailActivity.this, body -> populateBody(body));
                }
            });
            progressBar.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            Log.i(TAG, "timeElapsed to populate initial view: " + timeElapsed + " mS");
            viewModel.setHeaderLoaded(true);
        }
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.theme_primary_light));
        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bodyParagraphView;

        public ViewHolder(View view) {
            super(view);
            bodyParagraphView = view.findViewById(R.id.article_body);
        }
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private String[] paragraphs;

        public Adapter(String[] paragraphs) {
            this.paragraphs = paragraphs;
        }

        @Override
        public int getItemCount() {
            return paragraphs.length;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bodyParagraphView.setText(Html.fromHtml(paragraphs[position].replaceAll("(\r\n)", " ")));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.list_item_detail, parent, false));
        }

    }

    public class SnackBarListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, data.getTitle());
            StringBuilder builder = new StringBuilder(data.getTitle()).append("\n")
                    .append(data.getAuthor()).append("\n").append(publishedDate).append("\n\n")
                    .append(data.getThumb()).append("\n").append(data.getPhoto());
            shareIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.shareArticle)));
        }
    }

}
