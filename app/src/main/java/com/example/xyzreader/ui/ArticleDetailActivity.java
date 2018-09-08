package com.example.xyzreader.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xyzreader.R;
import com.example.xyzreader.data.BookItem;
import com.example.xyzreader.data.SingleBookItemLoader;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
		implements LoaderManager.LoaderCallbacks<BookItem> {

	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		private final BookItem bookItem;

		public MyPagerAdapter(FragmentManager fm, @NonNull BookItem bookItem) {
			super(fm);
			this.bookItem = bookItem;
		}

		@Override
		public int getCount() {
			return (bookItem != null) ? 1 : 0;
		}

		@Override
		public Fragment getItem(int position) {
			return ArticleDetailFragment.newInstance(bookItem);
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			super.setPrimaryItem(container, position, object);
			ArticleDetailFragment fragment = (ArticleDetailFragment) object;
		}
	}
	public static final String BOOK_ITEM = "BOOK_ITEM";
	private ViewPager mPager;
	private MyPagerAdapter mPagerAdapter;
	private long mSelectedItemId;
	private long mStartId;
	private int mTopInset;
	private View mUpButton;
	private View mUpButtonContainer;
	private View toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		}
		setContentView(R.layout.activity_article_detail);
		if (savedInstanceState == null) {
			if (getIntent() != null && getIntent().getExtras() != null) {
				mStartId = getIntent().getLongExtra(ArticleListActivity.ITEM_ID, 0);
				mSelectedItemId = mStartId;
			}
		}
		prepareLoader(24);

//		mUpButtonContainer = findViewById(R.id.up_container);
//
//		mUpButton = findViewById(R.id.action_up);
//		mUpButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				onSupportNavigateUp();
//			}
//		});

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			mUpButtonContainer.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//				@Override
//				public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
//					view.onApplyWindowInsets(windowInsets);
//					mTopInset = windowInsets.getSystemWindowInsetTop();
//					mUpButtonContainer.setTranslationY(mTopInset);
//					return windowInsets;
//				}
//			});
//		}

	}

	@Override
	public Loader<BookItem> onCreateLoader(int i, Bundle bundle) {
		return SingleBookItemLoader.newBookItemLoaderInstance(this, mStartId);
	}

	@Override
	public void onLoadFinished(Loader<BookItem> loader, BookItem data) {
		toolbar = findViewById(R.id.toolbar);
		Glide.with(this)
				.load(data.getThumb())
				.into((ImageView) findViewById(R.id.photo));
		TextView title = findViewById(R.id.title);
		title.setText(data.getTitle());
		TextView author = findViewById(R.id.author);
		author.setText(data.getAuthor());
		mPagerAdapter = new MyPagerAdapter(getFragmentManager(), data);
		mPagerAdapter.notifyDataSetChanged();
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mPagerAdapter);
		mPager.setPageMargin((int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
		mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				super.onPageScrollStateChanged(state);
				mUpButton.animate()
						.alpha((state == ViewPager.SCROLL_STATE_IDLE) ? 1f : 0f)
						.setDuration(300);
			}

		});

		// Select the start ID
		mPager.setCurrentItem(0, false);

		mStartId = 0;
	}

	@Override
	public void onLoaderReset(Loader<BookItem> loader) {
		mPagerAdapter.notifyDataSetChanged();
	}


	protected void prepareLoader(@NonNull final int loaderId) {
		if (getSupportLoaderManager().getLoader(loaderId) == null) {
			getSupportLoaderManager().initLoader(loaderId, null, this).forceLoad();
			return;
		}
		getSupportLoaderManager().restartLoader(loaderId, null, this).forceLoad();
	}

}
