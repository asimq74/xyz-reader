package com.example.xyzreader.ui;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.BookItem;
import com.example.xyzreader.data.SingleBookItemLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<BookItem> {

    private static final String EXTRA_IMAGE = "extraImage";
    private final String TAG = this.getClass().getSimpleName();
    private long mStartId;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressBar progressBar;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        initActivityTransitions();
        setContentView(R.layout.activity_article_detail);
        progressBar = findViewById(R.id.progressBar);
        cardView = findViewById(R.id.cardView);
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        supportPostponeEnterTransition();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                mStartId = getIntent().getLongExtra(ArticleListActivity.ITEM_ID, 0);
            }
        } else {
            mStartId = savedInstanceState.getLong(ArticleListActivity.ITEM_ID);
        }
        prepareLoader(24);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(ArticleListActivity.ITEM_ID, mStartId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<BookItem> onCreateLoader(int i, Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        return SingleBookItemLoader.newBookItemLoaderInstance(this, mStartId);
    }

    @Override
    public void onLoadFinished(Loader<BookItem> loader, BookItem data) {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(data.getTitle());
//        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        final ImageView photoView = (ImageView) findViewById(R.id.photo);
        final String photoUrl = data.getPhoto();
        final String body = data.getBody();
        Picasso.with(this).load(photoUrl).into(photoView, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
                final TextView articleBodyView = findViewById(R.id.article_body);
                final String bodyRawText = body;
                articleBodyView.setText(Html.fromHtml(bodyRawText
                        .replaceAll("(\r\n\r\n)", "<p />")
                        .replaceAll("(\r\n)", " ")));
                progressBar.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                Log.e(TAG, "error loading photoView with url: " + photoUrl);
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<BookItem> loader) {
        //do nothing
    }


    protected void prepareLoader(@NonNull final int loaderId) {
        if (getSupportLoaderManager().getLoader(loaderId) == null) {
            getSupportLoaderManager().initLoader(loaderId, null, this).forceLoad();
            return;
        }
        getSupportLoaderManager().restartLoader(loaderId, null, this).forceLoad();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, false);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.theme_primary_dark);
        int primary = getResources().getColor(R.color.theme_primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.theme_accent));
        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

}
