package com.siso.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.siso.app.adapter.MarketListAdapter;
import com.siso.app.common.Constants;
import com.siso.app.common.URLs;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.ui.common.FootUpdate;
import com.siso.app.ui.common.RefreshBaseFragment;
import com.siso.app.widget.FloatingActionButton;
import com.siso.app.widget.ShowHideOnScroll;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MarketListFragment extends RefreshBaseFragment implements FootUpdate.LoadMore{
	
	private static final String TAG = "MarketListFragment";
	
	public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";
	
	private View mView;
	
	/**
	 * 商品列表适配器
	 */
	private MarketListAdapter marketListAdapter;
	/**
	 * 商品集合
	 */
	private List<GoodsEntity> goodsList;
	/**
	 * 商品列表组件
	 */
	private ListView listView;
	/**
	 * 服务器返回json
	 */
	private String jsonData;
	/**
	 * 最新的
	 */
	public static final int NEWEST = 0;
	/**
	 * 适配器是否已经初始化
	 */
	private boolean isInited = false;
	/**
	 * 是否上拉加载
	 */
//	private boolean isLoading = false;
	/**
	 * 是否下拉加载
	 */
	private boolean isPullLoading = false;
	private int mNum;
	private int schoolId;
	private FloatingActionButton pubBtn;
	
//	static MarketListFragment newInstance(int num){
//		MarketListFragment f = new MarketListFragment();
//		Bundle args = new Bundle();
//        args.putInt("num", num);
//        f.setArguments(args);
////        f.setInitialSavedState(MarketFragment.fState);
//        return f;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments() != null ? getArguments().getInt(MarketListFragment.BUNDLE_KEY_CATALOG) : 1;
		SharedPreferences userPreferences = getActivity().getSharedPreferences(Constants.SHAREPREFER_UERINFO_NAME, Context.MODE_PRIVATE);
		schoolId = userPreferences.getInt("scId", 0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragment_market_list, null);
		initView();
		return mView;
	}
	
	protected void init() {
		super.init(mView);
		mfooUpdate.initfooter(listView,mInflater,this);
	}

	int count=0;
	private void initView() {
		listView = (ListView)mView.findViewById(R.id.market_goods_listView);
		swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayout);
		pubBtn = (FloatingActionButton) mView.findViewById(R.id.market_list_floatButton);
		pubBtn.setColor(getResources().getColor(R.color.statusbar_bg));
		goodsList = new ArrayList<GoodsEntity>();
		
		init();
		loadCacheData(mNum,NEWEST);
		listView.setOnTouchListener(new ShowHideOnScroll(pubBtn));
		pubBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (MyApplication.userInfo!=null) startActivity(new Intent(getActivity(),MarketPubActivity.class));
				else showButtomToast(getStringByRId(R.string.has_no_login));
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				Intent intent = new Intent(getActivity(),GoodsDetailActivity.class);
				intent.putExtra("entity", (Serializable)goodsList.get(position));
				startActivity(intent);
			}
		});
		
		/**
		 * 监听下拉刷新
		 */
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				isPullLoading = true;
				mState = STATE_NONE;
				loadingData(mNum, NEWEST);//请求最新的数据
			}
		});
		
		/**
		 * 监听上拉刷新
		 */
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				if (view.getLastVisiblePosition()>=(totalItemCount-1)&&(visibleItemCount<totalItemCount)&&mState==STATE_NONE) {
					mfooUpdate.showLoading();
					loadingData(mNum,goodsList.get(goodsList.size()-1).getId());
//					loadCacheData(1,goodsList.get(goodsList.size()-1).getId());
					count++;
					mState = STATE_LOADMORE;
				}
			}
		});
		
	}
	
	private OnScrollListener mOnScrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	};
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-28 上午12:14:43
	 * @Decription 加载缓存
	 *
	 */
	private void loadCacheData(int position,int lastId){
		new MyAsyncTask(position,lastId).execute();
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-24 下午5:59:09
	 * @Decription 开始加载数据
	 *
	 * @param postion viewpager的位置,代表类别
	 */
	private void loadingData(int postion,int beforeId){
		jsonData = null;
		String userId = null;
		if(MyApplication.userInfo!=null){
			userId = MyApplication.userInfo[1];
		}
		String url = URLs.MARKET_GOODSLIST_URL+postion+"&beforeId="+beforeId+"&schoolId="+schoolId+"&userId="+userId;
		Log.i(TAG,"MARKET_GOODSLIST_URL=="+url);
		stringRequest = new StringRequest(url, 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i(TAG,"response=="+response);
						jsonData=response;
						parseJson();
						resetLoading();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToastError(error.toString());
						mfooUpdate.showFail("加载失败，点击重新加载");
						resetLoading();
					}
				});
		stringRequest.setTag(stringRequestTag);
		requestQueue.add(stringRequest);
	}
	
	private void resetLoading(){
		if(mState!=STATE_DONE) mState = STATE_NONE;
		isPullLoading = false;
		swipeRefreshLayout.setRefreshing(false);
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-25 上午10:33:49
	 * @Decription 解析json
	 *
	 */
	public void parseJson(){
		if (jsonData!=null) {
			if (jsonData.length()>10) {
				Gson gson = new Gson();
				List<GoodsEntity> tmpList = gson.fromJson(jsonData, new TypeToken<List<GoodsEntity>>(){}.getType());
				Log.i(TAG, "mState==========================================================" + mState + "isPullLoading=======" + isPullLoading);
//						mergeList(goodsList, tmpList);

				if (mState == STATE_LOADMORE) {
					/**
					 * 判断是否已到尾部,false才addAll
					 */
						goodsList.addAll(tmpList);
				}else if (isPullLoading) {
					goodsList.clear();
					goodsList.addAll(tmpList);
				}
				/**
				 * 判断是否已经初始化
				 */
				if (!isInited) {
					goodsList = tmpList;
					marketListAdapter = new MarketListAdapter(getActivity(), goodsList, R.layout.item_market_list,0);
					listView.setAdapter(marketListAdapter);
					isInited=true;
				}else {
//					updateAdapterList(tmpList);
					marketListAdapter.notifyDataSetChanged();
				}
			}else if (jsonData.length()>0) {
				mfooUpdate.showDone("没有啦");
				mState = STATE_DONE;
			}
		}
	}
	
	@Override
	public void loadMore() {
		loadingData(mNum, goodsList.get(goodsList.size()-1).getId());
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-26 下午8:50:12
	 * @Decription list1和list2合并
	 *
	 * @param goodsList2
	 * @param tmpList
	 * @return tmpList
	 */
	private List<GoodsEntity> mergeList(List<GoodsEntity> goodsList2,List<GoodsEntity> tmpList){
		if(tmpList.size()>goodsList2.size()) return tmpList;
		/**
		 * 如果是上拉，直接add
		 */
		if (mState == STATE_LOADMORE) {
			/**
			 * 判断是否已到尾部,false才addAll
			 */
			if(goodsList2.get(goodsList2.size()-1).getId()!=tmpList.get(tmpList.size()-1).getId())
				goodsList2.addAll(tmpList);
			return goodsList2;
		}else if (isPullLoading) {
			goodsList2.clear();
			goodsList2.addAll(tmpList);
		}
		return tmpList;
	}
	
	private class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
		private int lastId;
		private int position;

		public MyAsyncTask(int position,int lastId) {
			this.lastId=lastId;
			this.position=position;
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			String userId = null;
			if(MyApplication.userInfo!=null){
				userId = MyApplication.userInfo[1];
			}
			String url = URLs.MARKET_GOODSLIST_URL+mNum+"&beforeId="+NEWEST+"&schoolId="+schoolId+"&userId="+userId;
			jsonData = getCache(url);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				if (jsonData!=null) {
					parseJson();
				}else {
					swipeRefreshLayout.setRefreshing(true);
					loadingData(position, NEWEST);
				}
			}
		}

	}
	
}
