package com.example.xyzreader.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.R;
import com.example.xyzreader.data.BookItem;
import com.example.xyzreader.viewmodels.BookItemViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
		implements AppBarLayout.OnOffsetChangedListener {

	public class SnackBarListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Uri pictureUri = Uri.parse(data.getPhoto());
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_TEXT, data.getAuthor());
			shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
			shareIntent.setType("image/*");
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			startActivity(Intent.createChooser(shareIntent, "Share images..."));
		}
	}

	private final String TAG = this.getClass().getSimpleName();
	@BindView(R.id.app_bar_layout)
	AppBarLayout appBarLayout;
	private CardView cardView;
	@BindView(R.id.collapsing_toolbar)
	CollapsingToolbarLayout collapsingToolbarLayout;
	private BookItem data;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);
	@BindView(R.id.float_header_view)
	HeaderView floatHeaderView;
	private boolean isHideToolbarView = false;
	private long mStartId;
	private ProgressBar progressBar;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.toolbar_header_view)
	HeaderView toolbarHeaderView;
	private BookItemViewModel viewModel;
	@Inject
	ViewModelProvider.Factory viewModelFactory;

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
		Snackbar mySnackbar = Snackbar.make(view,
				"Here's a Snackbar", Snackbar.LENGTH_LONG);
		mySnackbar.setAction("Action", new SnackBarListener());
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
		viewModel = ViewModelProviders.of(this, viewModelFactory)
				.get(BookItemViewModel.class);
		collapsingToolbarLayout.setTitle(" ");
		progressBar = findViewById(R.id.progressBar);
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
		final int bookId = (int) this.mStartId;
		viewModel.getBookItemById(bookId).observe(this, new Observer<BookItem>() {
			@Override
			public void onChanged(@Nullable BookItem bookItem) {
				Log.i(TAG, "bookItem="+bookItem);
				populateInitialView(bookItem);
			}
		});
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
		int maxScroll = appBarLayout.getTotalScrollRange();
		float percentage = (float) Math.abs(offset) / (float) maxScroll;

		if (percentage == 1f && isHideToolbarView) {
			toolbarHeaderView.setVisibility(View.VISIBLE);
			toolbarHeaderView.findViewById(R.id.header_view_author).setVisibility(View.GONE);
			toolbarHeaderView.findViewById(R.id.header_view_published_date).setVisibility(View.GONE);
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

	private Date parsePublishedDate(String dateString) {
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException ex) {
			Log.e(TAG, ex.getMessage());
			Log.i(TAG, "passing today's date");
			return new Date();
		}
	}

	private void populateInitialView(@NonNull BookItem bookItem) {
		final String publishedDate = DateUtils.getRelativeTimeSpanString(
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
			}
		});
		final TextView articleBodyView = findViewById(R.id.article_body);
		articleBodyView.setText(Html.fromHtml(bookItem.getBody()
				.replaceAll("(\r\n\r\n)", "<p />")
				.replaceAll("(\r\n)", " ")));
		progressBar.setVisibility(View.GONE);
		cardView.setVisibility(View.VISIBLE);
	}


	private void updateBackground(FloatingActionButton fab, Palette palette) {
		int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
		int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.theme_accent));
		fab.setRippleColor(lightVibrantColor);
		fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
	}

}
