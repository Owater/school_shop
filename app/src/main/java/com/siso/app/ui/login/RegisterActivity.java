package com.siso.app.ui.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.siso.app.ui.R;
import com.siso.app.ui.common.BaseActionBarActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.smssdk.SMSSDK;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActionBarActivity implements PhoneFragment.PhoneFragmentCallBack {

    private static String TAG = "RegisterActivity";
    private static String APPKEY = "81c7af6cbeb8";
    private static String APPSECRET = "8258fe33fd7121b3e9252168c12c165e";

    @ViewById(R.id.register_guide_first)
    TextView guide1;
    @ViewById(R.id.register_guide_second)
    TextView guide2;
    @ViewById(R.id.register_guide_third)
    TextView guide3;

    @AfterViews
    void init(){
        initToolbar(getStringByRId(R.string.register));
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        changeFragment(0,null);
    }

    @Override
    public void changeFragment(int position,String phone) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new PhoneFragment_();
                guide1.setTextColor(getResources().getColor(R.color.statusbar_bg));
                break;
            case 1:
                fragment = new Verifyfragment_();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        guide1.setTextColor(getResources().getColor(R.color.statusbar_bg));
                        guide2.setTextColor(getResources().getColor(R.color.statusbar_bg));
                    }
                });
                break;
            case 2:
                fragment = new RegisterFragment_();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        guide1.setTextColor(getResources().getColor(R.color.statusbar_bg));
                        guide2.setTextColor(getResources().getColor(R.color.statusbar_bg));
                        guide3.setTextColor(getResources().getColor(R.color.statusbar_bg));
                    }
                });
                break;
            default:
                break;
        }

        if(phone!=null){
            Bundle bundle = new Bundle();
            bundle.putString("phone",phone);
            fragment.setArguments(bundle);
        }

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.registerContent,fragment,"SHARE_FRAGMENT").commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
