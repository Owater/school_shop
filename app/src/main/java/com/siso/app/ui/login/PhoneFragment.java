package com.siso.app.ui.login;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.siso.app.common.URLs;
import com.siso.app.ui.R;
import com.siso.app.ui.common.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Owater on 2015/6/10.
 */
@EFragment(R.layout.fragment_phone)
public class PhoneFragment extends BaseFragment {

    private static String TAG = "PhoneFragment";
    private PhoneFragmentCallBack callBack;
    private String phone;

    @ViewById
    Button codeBtn;
    @ViewById
    EditText phoneNum;
    @ViewById
    CheckBox argree;

    @AfterViews
    void init(){
        EventHandler eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                loadingProgressDialog.dismiss();
                //回调完成
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        callBack.changeFragment(2, phone);
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        callBack.changeFragment(1, phone);
                    }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else {
                    ((Throwable) data).printStackTrace();
                    showMiddleToast("验证码错误");
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (phoneNum.getText().toString().length()==11){
                    codeBtn.setBackgroundResource(R.drawable.btn_blue_solid_selector);
                    codeBtn.setEnabled(true);
                }else {
                    codeBtn.setBackgroundResource(R.drawable.btn_disable_selector);
                    codeBtn.setEnabled(false);
                }
            }
        };
        phoneNum.addTextChangedListener(textWatcher);

        argree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    getActivity().finish();
            }
        });
    }

    @Click
    void codeBtn(){
        loadingProgressDialog.show();
        RequestParams params = new RequestParams();
        phone = phoneNum.getText().toString();
        params.put("userPhone", phone);
        postNetwork(URLs.CHECK_PHONE, params, URLs.CHECK_PHONE);
    }

    @Override
    public void parseJson(int code, String msg, String tag, Object data) {
        super.parseJson(code, msg, tag, data);
        if (code==1006){
            loadingProgressDialog.dismiss();
            showMiddleToast(msg);
        }else if (code==0){
            SMSSDK.getVerificationCode("86", phone);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callBack = (PhoneFragmentCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement PhoneFragmentCallBack.");
        }
    }

    public static interface PhoneFragmentCallBack {
        void changeFragment(int position, String phone);
    }

}
