package com.siso.app.ui.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.siso.app.ui.R;
import com.siso.app.ui.common.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import cn.smssdk.SMSSDK;

/**
 * Created by Owater on 2015/6/10.
 */
@EFragment(R.layout.fragment_verify)
public class Verifyfragment extends BaseFragment {

    private static String TAG = "Verifyfragment";

    @ViewById
    EditText verifyCode;

    @ViewById
    Button verifyBtn;

    @FragmentArg
    String phone;

    @AfterViews
    void init(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (verifyCode.getText().toString().length()==4){
                    verifyBtn.setBackgroundResource(R.drawable.btn_blue_solid_selector);
                    verifyBtn.setEnabled(true);
                }else {
                    verifyBtn.setBackgroundResource(R.drawable.btn_disable_selector);
                    verifyBtn.setEnabled(false);
                }
            }
        };
        verifyCode.addTextChangedListener(textWatcher);
    }

    @Click
    void verifyBtn(){
        if(!TextUtils.isEmpty(verifyCode.getText().toString())){
            SMSSDK.submitVerificationCode("86", phone, verifyCode.getText().toString());
        }
    }
}
