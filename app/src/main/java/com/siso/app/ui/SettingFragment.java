package com.siso.app.ui;

import com.easemob.EMCallBack;
import com.siso.app.common.Constants;
import com.siso.app.ui.common.BaseFragment;
import com.siso.app.utils.AccountInfoUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SettingFragment extends BaseFragment implements OnClickListener {
	
	private static final String TAG = "SettingFragment";
	
	private View mView;
	private RelativeLayout setAccount;
	private RelativeLayout feedback;
	private RelativeLayout aboutus;
	private Button loginoutBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragment_setting, null);
		initView();
		return mView;
	}

	private void initView() {
		setAccount = (RelativeLayout) mView.findViewById(R.id.setting_account);
		loginoutBtn = (Button)mView.findViewById(R.id.loginOut);
		feedback = (RelativeLayout) mView.findViewById(R.id.feedback);
		aboutus = (RelativeLayout) mView.findViewById(R.id.aboutus);
		
		setAccount.setOnClickListener(this);
		loginoutBtn.setOnClickListener(this);
		feedback.setOnClickListener(this);
		aboutus.setOnClickListener(this);
		if(MyApplication.userInfo==null){
			loginoutBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.setting_account:
			if (Constants.ISLOGIN) startActivity(new Intent(getActivity(),AccountSetting.class));
			else showButtomToast("亲，你还没登录~");
			break;
		case R.id.loginOut:
			loadingProgressDialog.show();
			MyApplication.getInstance().logout(new EMCallBack() {
				
				@Override
				public void onSuccess() {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							loadingProgressDialog.dismiss();
							AccountInfoUtils.loginOut(getActivity());
							showButtomToast(getStringByRId(R.string.loginout_success));
							Intent intent = new Intent(getActivity(),ChooseSchoolActivity.class);
							intent.putExtra("isFirst", true);
							startActivity(intent);
							getActivity().finish();
						}
					});
				}
				
				@Override
				public void onProgress(int progress, String status) {
					loadingProgressDialog.setMessage(getString(R.string.loginouting));
				}
				
				@Override
				public void onError(int code, String message) {
					
				}
			});
			break;
		case R.id.feedback:
			startActivity(new Intent(getActivity(),FeedbackActivity.class));
			break;
		case R.id.aboutus:
			startActivity(new Intent(getActivity(),AboutActivity.class));
			break;
		default:
			break;
		}
	}
	
}
