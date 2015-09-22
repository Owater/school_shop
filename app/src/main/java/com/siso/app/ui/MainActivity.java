package com.siso.app.ui;

import com.siso.app.common.SystemBarTintManager;
import com.siso.app.ui.R;
import com.siso.app.ui.find.FindListFragment;
import com.umeng.update.UmengUpdateAgent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
		implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	//沉侵式状态栏
	private SystemBarTintManager mTintManager;
	
	private DrawerLayout mDrawerLayout;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
		setContentView(R.layout.activity_main);
		mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(R.color.statusbar_bg);
		initView();
	}
	
	private void initView(){
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(getResources().getString(R.string.app_name));// 标题的文字需在setSupportActionBar之前，不然会无效
		
		setSupportActionBar(mToolbar);
		
		/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
//		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				switch (item.getItemId()) {
//				case R.id.action_search:
//					Toast.makeText(MainActivity.this, "action_settings", 0).show();
//					break;
//				default:
//					break;
//				}
//				return true;
//			}
//		});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//设置侧滑
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,mDrawerLayout,mToolbar);
		MyApplication.initScreen(this);

        //检查更新
        UmengUpdateAgent.update(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			startActivity(new Intent(MainActivity.this,SearchGoodsActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}
	
	@TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
	
	int currentPos = -1;
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if(currentPos==position&&position!=3) return;
		Fragment fragment = null;
		switch(position){
		    case 0:
		    	fragment = new MarketFragment();
		    	break;
		    case 1:
		    	fragment = new ShareListFragment();
		    	break;
		    case 2:
		    	fragment = new FindListFragment();
		    	break;
		    case 3:
		    	fragment = new UserMsgFragment();
		    	break;
		    case 4:
		    	fragment = new SettingFragment();
		    	break;
		    case 5:
		    	startActivity(new Intent(this,FeedbackActivity.class));
		    	break;
		}
        if(fragment!=null){
        	restoreActionBar(position);
        	getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment,"MY_FRAGMENT").commit();
        }
        currentPos = position;
	}
	
	private void restoreActionBar(int position){
		String title = null;
		switch (position) {
		case 0:title = getResources().getString(R.string.app_name);break;
		case 1:title = getResources().getString(R.string.title_activity_share_pub_edit);break;
		case 2:title = getResources().getString(R.string.title_fragment_find_list);break;
		case 3:title = getResources().getString(R.string.title_activity_user_msg_fragment);break;
		case 4:title = getResources().getString(R.string.title_activity_setting_fragment);break;
		default:getResources().getString(R.string.app_name);break;
		}
		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null){
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(title);
		}
	}
	
	private long exitTime = 0;

    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            showButtomToast("再按一次退出Coding");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
	
}
