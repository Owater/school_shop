package com.siso.app.ui.login;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.siso.app.common.URLs;
import com.siso.app.ui.R;
import com.siso.app.ui.common.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Owater on 2015/6/10.
 */
@EFragment(R.layout.fragment_register)
public class RegisterFragment extends BaseFragment {

    @ViewById
    Button submit;
    @ViewById
    EditText editUsername;
    @ViewById
    EditText editPassword;
    @ViewById
    EditText rePassword;
    @FragmentArg
    String phone;

    @AfterViews
    void init(){
    }

    @Click
    void submit(){
        RequestParams params = new RequestParams();
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        String repasswordstr = rePassword.getText().toString();
        if (TextUtils.isEmpty(username)){
            showMiddleToast("昵称不能为空");
        }else if (password.length()<6){
            showMiddleToast("密码不能少于6位");
        }else if(!repasswordstr.equals(password)){
            showMiddleToast("两次输入密码不一致");
        }else {
            loadingProgressDialog.show();
            params.put("username",username);
            params.put("userpass",password);
            params.put("phone",phone);
            postNetwork(URLs.REGIRSTER,params,URLs.REGIRSTER);
        }
    }

    public void parseJson(int code, String msg, String tag, Object data) {
//        super.parseJson(code, msg, tag, data);
        loadingProgressDialog.dismiss();
        if (code==0){
            showButtomToast("注册成功");
            getActivity().finish();
        }else {
            showButtomToast("注册失败");
        }
    }
}
