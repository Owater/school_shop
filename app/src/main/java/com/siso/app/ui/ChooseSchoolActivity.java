package com.siso.app.ui;

import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.siso.app.adapter.SchoolListAdapter;
import com.siso.app.common.Constants;
import com.siso.app.common.URLs;
import com.siso.app.entity.SchoolEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.SharedPreferencesUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ChooseSchoolActivity extends BaseActionBarActivity {
	
	private static final String TAG = "ChooseSchoolActivity";
	
	/**
	 * 服务器返回json
	 */
	private String jsonData;
	private SchoolListAdapter schoolListAdapter;
	private LinearLayout loadingLayout;
	private ListView listView;
	private List<SchoolEntity> schoolList;
	private TextWatcher textWatcher;
	private EditText searchEditText;
	private boolean isFirst=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_choose_school;
	}
	
	private void initView() {
		listView = (ListView)findViewById(R.id.activity_choose_school_listView);
		loadingLayout = (LinearLayout)findViewById(R.id.common_loading);
		searchEditText = (EditText)findViewById(R.id.choose_school_search_text);
		initToolbar(getStringByRId(R.string.title_activity_choose_school));
		
		isFirst = getIntent().getExtras().getBoolean("isFirst");
		textWatcher = new TextWatcher() {
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				loadingLayout.setVisibility(View.VISIBLE);
				initSchoolList(URLs.SEARCH_CHOOSE_SCHOOL+searchEditText.getText().toString());
			}  
		
		};
		
		searchEditText.addTextChangedListener(textWatcher);
		initSchoolList(URLs.CHOOSE_SCHOOL);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SharedPreferencesUtils.saveSharedPreferInfo(ChooseSchoolActivity.this, Constants.SHAREPREFER_UERINFO_NAME, "Integer", 
						"scId", schoolList.get(position).getId());
				Constants.SCID = schoolList.get(position).getId();
				if(isFirst){
					showButtomToast("你选择了："+schoolList.get(position).getSchoolName());
					startActivity(new Intent(ChooseSchoolActivity.this,MainActivity.class));
				} else {
					Intent data = new Intent();
					data.putExtra("schoolName", schoolList.get(position).getSchoolName());
					setResult(UserDetailEditActivity.USERINFO_SCHOOL,data);
				}
				finish();
			}
		});
	}

	private void initSchoolList(String url) {
		jsonData = null;
		stringRequest = new StringRequest(url, 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						jsonData=response;
						parseJson();
						loadingLayout.setVisibility(View.GONE);
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToastError(error.toString());
						loadingLayout.setVisibility(View.GONE);
					}
				});
		stringRequest.setTag(stringRequestTag);
		requestQueue.add(stringRequest);
	}
	
	private void parseJson() {
		if (jsonData!=null) {
			if (jsonData.length()>0) {
				Gson gson = new Gson();
				List<SchoolEntity> tmpList = gson.fromJson(jsonData, new TypeToken<List<SchoolEntity>>(){}.getType());
				if(schoolList==null) schoolList = tmpList;
				else {
					schoolList.clear();
					for (SchoolEntity tmpEntity:tmpList) {
						schoolList.add(tmpEntity);
					}
				}
				if (schoolListAdapter==null) {
					schoolListAdapter = new SchoolListAdapter(this, schoolList, R.layout.item_school);
					listView.setAdapter(schoolListAdapter);
				}else {
					schoolListAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}

}
