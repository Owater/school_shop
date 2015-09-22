package com.siso.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.siso.app.adapter.MarketListAdapter;
import com.siso.app.common.URLs;
import com.siso.app.entity.GoodsEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.AccountInfoUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchGoodsActivity extends BaseActionBarActivity {
	
	private Spinner spinner;
	private EditText searchEdittext;
	private LinearLayout loadingView;
	private TextView progressBarTitle;
	private MarketListAdapter marketListAdapter;
	private List<GoodsEntity> goodsList;
	private String jsonData;
	private int scId;
	private ListView listView;
	private Button searchBtn;
	private double lat;
	private double lont;
	private final int localfail=0;
	private final int localing=1;
	private final int localsuccess=2;
	private int isLocalSuccess = localfail;
	
	/**
	 * 百度定位
	 */
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_goods);
		initView();
	}

	private void initView() {
		initToolbar(getStringByRId(R.string.title_activity_search_goods));
		searchEdittext = (EditText)findViewById(R.id.search_edittext);
		loadingView = (LinearLayout)findViewById(R.id.search_loading_view);
		searchBtn = (Button)findViewById(R.id.search_btn);
		listView = (ListView)findViewById(R.id.search_listView);
		progressBarTitle = (TextView)findViewById(R.id.progressBar_title);
		
		scId = AccountInfoUtils.getScId(this);
		
		mLocationClient = new LocationClient(this);
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);
		initLocation();
		
		setupSpinner();
//		textWatcher = new TextWatcher() {
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				
//			}
//
//		};
//		searchEdittext.addTextChangedListener(textWatcher);
		searchBtn.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SearchGoodsActivity.this,GoodsDetailActivity.class);
				intent.putExtra("entity", (Serializable)goodsList.get(position));
				startActivity(intent);
			}
		});
	}
	
	private void setUpListView(int type) {
		goodsList = new ArrayList<GoodsEntity>();
		marketListAdapter = new MarketListAdapter(this, goodsList, R.layout.item_market_list,type);
		listView.setAdapter(marketListAdapter);
	}

	private void initSearchList(String content) {
		loadingView.setVisibility(View.VISIBLE);
		progressBarTitle.setText(getStringByRId(R.string.hint_loading));
		jsonData = null;
		//解决url传值乱码问题
		content = Uri.encode(content);
		int position = spinner.getSelectedItemPosition();
		String url = null;
		if(position==0){
			url = URLs.SEARCH+"?s="+content+"&scId="+scId;
		}else {
			url = URLs.SEARCH+"?s="+content+"&lat="+lat+"&lont="+lont+"&type=location";
		}
		
		stringRequest = new StringRequest(url, 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						jsonData=response;
						parseJson();
						loadingView.setVisibility(View.GONE);
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToastError(error.toString());
						loadingView.setVisibility(View.GONE);
					}
				});
		stringRequest.setTag(stringRequestTag);
		requestQueue.add(stringRequest);
	}
	
	private void parseJson() {
		Gson gson = new Gson();
		List<GoodsEntity> tmpList = gson.fromJson(jsonData, new TypeToken<List<GoodsEntity>>(){}.getType());
		goodsList.clear();
		goodsList.addAll(tmpList);
		marketListAdapter.notifyDataSetChanged();
	}
	
	private void setupSpinner() {
		spinner = (Spinner) findViewById(R.id.search_spinner);

		spinner.setAdapter(ArrayAdapter.createFromResource(
                this, R.array.search_list,
                android.R.layout.simple_spinner_dropdown_item
        ));
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            	TextView textView = (TextView)view;
            	textView.setTextColor(getResources().getColor(R.color.white));
            	textView.setTextSize(18.0f);
            	setUpListView(position);
            	if (position==1) {
            		mLocationClient.start();
            		progressBarTitle.setText(getStringByRId(R.string.hint_locating));
            		isLocalSuccess = localing;
				}else {
					isLocalSuccess = localfail;
					mLocationClient.stop();
				}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-5-10 下午3:30:31
	 * @Decription 初始化定位
	 *
	 */
	private void initLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		int span=1000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
	}
	
	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		mLocationClient.stop();
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_btn:
			String content = searchEdittext.getText().toString();
			if (content.length()>0) {
				if (spinner.getSelectedItemPosition()==0) {
					initSearchList(content);
				}else if (spinner.getSelectedItemPosition()==1&&isLocalSuccess==localsuccess) {
					initSearchList(content);
				}else {
					showButtomToast(getStringByRId(R.string.hint_locating));
				}
			}else {
				showButtomToast(getStringByRId(R.string.search_edit_tip));
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 百度地图 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			lat = location.getLatitude();
			lont = location.getLongitude();
//			showButtomToast("lat="+location.getLatitude()+" , lont="+location.getLocType());
			//定位成功
			if (location.getLocType()==161||location.getLocType()==61) {
				isLocalSuccess = localsuccess;
				mLocationClient.stop();
				progressBarTitle.setText(getStringByRId(R.string.localSuccess));
				showButtomToast(getStringByRId(R.string.localSuccess));
			}else if (location.getLocType()==167||location.getLocType()==62) {
				isLocalSuccess = localfail;
				showButtomToast(getStringByRId(R.string.localFail));
				mLocationClient.stop();
			}
		}
	}
	
}
