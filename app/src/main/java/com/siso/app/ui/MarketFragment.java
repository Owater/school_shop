package com.siso.app.ui;

import com.siso.app.entity.MarketTab;
import com.siso.app.ui.common.BaseFragment;
import com.siso.app.widget.PagerSlidingTabStrip;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MarketFragment extends BaseFragment {
	
	private static final String TAG = "MarketFragment";
	
	private View mView;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private MyPagerAdapter myPagerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.activity_market_fragment, null);
		initView();
		return mView;
	}
	
	private void initView() {
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) mView.findViewById(R.id.market_tabs);
		mViewPager = (ViewPager) mView.findViewById(R.id.market_pager);
		myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
		
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.setOffscreenPageLimit(myPagerAdapter.getCount());
		initTab();
		initClick();
	}

	private void initTab() {
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip.setShouldExpand(true);
		mPagerSlidingTabStrip.setIndicatorHeight(7);
		mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
		mPagerSlidingTabStrip.setBackgroundColor(getResources().getColor(R.color.statusbar_bg));
		mPagerSlidingTabStrip.setTextColor(getResources().getColor(R.color.statusbar_tab_text));
		mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
		mPagerSlidingTabStrip.setIndicatorColor(Color.WHITE);
	}

	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-14 下午5:27:42
	 * @Decription 监听事件
	 *
	 */
	private void initClick() {
		mPagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int positon) {
//				switch (positon) {
//				case 0:secondHandMarket.loadCacheData(positon,MarketListFragment.NEWEST);break;
//				case 1:gapMarket.loadCacheData(positon,MarketListFragment.NEWEST);requestQueue.cancelAll("stringRequestTag");break;
//				case 2:rentMarket.loadCacheData(positon,MarketListFragment.NEWEST);break;
//				case 3:otherMarket.loadCacheData(positon,MarketListFragment.NEWEST);break;
//				default:
//					break;
//				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				Log.i(TAG, "arg0="+arg0+",arg1=="+arg1+",arg2=="+arg2);
			}

			@Override
			public void onPageScrollStateChanged(int positon) {
			}
		});
		
	}

	public class MyPagerAdapter extends FragmentStatePagerAdapter {
		
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			MarketTab tab = MarketTab.getTabByIdx(position);
			int resId = 0;
			CharSequence title = "";
			if (tab != null)
				resId = tab.getTitle();
			if (resId != 0)
				title = getResources().getString(resId);
			return title;
		}

		@Override
		public int getCount() {
			return MarketTab.values().length;
		}
		
		@Override
		public Fragment getItem(int position) {
			final int pattern = position % 5;
			MarketTab values[] = MarketTab.values();
			Fragment f = null;
			try {
				f = (Fragment) values[pattern].getClz().newInstance();
				Bundle args = new Bundle();
	            args.putInt(MarketListFragment.BUNDLE_KEY_CATALOG, position+1);
	            f.setArguments(args);
			} catch (java.lang.InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	        return f;
		}

	}
}
