package com.siso.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.siso.app.adapter.NavigationDrawerAdapter;
import com.siso.app.common.Constants;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.entity.DrawerList;
import com.siso.app.entity.UserEntity;
import com.siso.app.ui.common.BaseFragment;
import com.siso.app.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v4.widget.DrawerLayout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class NavigationDrawerFragment extends BaseFragment implements OnClickListener{

	/**
	 * 回调
	 */
	private NavigationDrawerCallbacks mCallbacks;

	private View mView;
	private int mCurrentSelectedPosition = 0;
	
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView drawerListView;//侧滑列表
	private TextView userNameTextView;
	private CircleImageView avatar;

	public NavigationDrawerFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectItem(mCurrentSelectedPosition);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		 
		initDrawerList();
		initOnClickListener();
		checkLogin();
		return mView;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null&& mDrawerLayout.isDrawerOpen(mView);
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-13 下午11:49:31
	 * @Decription 初始化侧滑列表
	 *
	 */
	private void initDrawerList(){
//		no_login = (LinearLayout) mView.findViewById(R.id.no_login);
//		yes_login = (LinearLayout) mView.findViewById(R.id.yes_login);
//		login = (Button) mView.findViewById(R.id.login);
//		regirster = (Button) mView.findViewById(R.id.regirster);
		userNameTextView = (TextView)mView.findViewById(R.id.drawer_login_username);
		avatar = (CircleImageView)mView.findViewById(R.id.drawer_login_avatar);
		
		List<DrawerList> drawerLists  = new ArrayList<DrawerList>();
		drawerLists.add(new DrawerList("首页", R.drawable.ic_tab_home_normal2));
		drawerLists.add(new DrawerList("分享", R.drawable.ic_tab_share));
		drawerLists.add(new DrawerList("发现", R.drawable.ic_tab_find));
		drawerLists.add(new DrawerList("消息", R.drawable.message_icon_normal));
		drawerLists.add(new DrawerList("设置", R.drawable.setting_icon_normal));
		drawerLists.add(new DrawerList(getResources().getString(R.string.title_activity_feedback), R.drawable.navigation_icon_feedback));
		drawerListView = (ListView)mView.findViewById(R.id.drawer_list);
		drawerListView.setAdapter(new NavigationDrawerAdapter(getActivity(), drawerLists, R.layout.item_drawer_list));
		drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				selectItem(position);
			}
		});
		drawerListView.setItemChecked(mCurrentSelectedPosition, true);
		userNameTextView.setOnClickListener(this);
		avatar.setOnClickListener(this);
	}
	
	private void setAvatar(){
		RequestParams params = new RequestParams();
		params.add("userId", MyApplication.userInfo[1]);
		params.add("authCode", MyApplication.userInfo[0]);
		networkHelper.getNetJson(URLs.USERINFO, params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				Gson gson = new Gson();
				DataJson dataJson = gson.fromJson(new String(responseBody), DataJson.class);
				if (dataJson.isSuccess()) {
					if(dataJson.getData()!=null){
						UserEntity userEntity = gson.fromJson(gson.toJson(dataJson.getData()), UserEntity.class);
						if(userEntity.getAvatarUrl()!=null){
							Picasso picasso = Picasso.with(avatar.getContext());
							picasso.load(userEntity.getAvatarUrl()).placeholder(R.drawable.ic_avatar).into(avatar);
						}
					}else {
						showButtomToast(getStringByRId(R.string.getUserInfo_fail));
					}
				}else {
					showButtomToast("用户信息有误");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				loadingProgressDialog.dismiss();
			}
		});
	}
	
	public void initOnClickListener(){
//		login.setOnClickListener(this);
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout,Toolbar mToolbar) {
		mView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, mToolbar, R.string.drawer_open,R.string.drawer_close);
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
	}
	
	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (drawerListView != null) {
			drawerListView.setItemChecked(position, true);
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}
	
	private void checkLogin(){
//		SharedPreferences userPreferences = getActivity().getSharedPreferences(Constants.SHAREPREFER_UERINFO_NAME, Context.MODE_PRIVATE);
//		String umeng = userPreferences.getString("umeng", null);
		if(MyApplication.userInfo!=null){
			userNameTextView.setText(MyApplication.userInfo[2]);
			Constants.ISLOGIN = true;
			setAvatar();
		}
	}
	
	private void startLoginActivity(){
		if (Constants.ISLOGIN) {
			Intent intent = new Intent(getActivity(),UserDetailActivity.class);
			intent.putExtra("userId", "");
			startActivityForResult(intent,UserDetailActivity.RESULT_EDIT);
		}else {
			startActivityForResult(new Intent(getActivity(),LoginActivity.class),100);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.drawer_login_username:
			startLoginActivity();
			break;
		case R.id.drawer_login_avatar:
			startLoginActivity();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==LoginActivity.LOGIN_SUCCESS) {
			UserEntity userEntity = (UserEntity) data.getExtras().getSerializable("entity");
			if(userEntity!=null) userNameTextView.setText(userEntity.getUserName());
		}else if (requestCode==UserDetailActivity.RESULT_EDIT) {
			userNameTextView.setText(MyApplication.userInfo[2]);
			setAvatar();
		}
	}
}
