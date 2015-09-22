package com.siso.app.ui.common;

import com.siso.app.chat.applib.controller.HXSDKHelper;
import com.siso.app.common.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 
 * description :基础Activity
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-7 下午4:04:53
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-7 下午4:04:53
 *
 */
public class BaseActivity extends Activity {

	/**
	 * 沉侵式状态栏管理
	 */
	protected SystemBarTintManager mTintManager;
	/**
	 * 标题
	 */
	protected TextView title;
	/**
	 * 返回按钮
	 */
	protected ImageButton backBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mTintManager = new SystemBarTintManager(this);
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
    
}
