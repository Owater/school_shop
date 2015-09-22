package com.siso.app.ui;

import com.google.gson.Gson;
import com.siso.app.adapter.UserDetailAdapter;
import com.siso.app.common.Constants;
import com.siso.app.common.URLs;
import com.siso.app.entity.DataJson;
import com.siso.app.entity.UserEntity;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.AccountInfoUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UserDetailEditActivity extends BaseActionBarActivity {
	
	private ListView listView;
	private UserEntity userEntity;
	private UserDetailAdapter adapter;
	
	String[] user_info_list_first;
	String[] user_info_list_second;
	String[] sexs;
	
	public final static int RESULT_EDIT_LIST = 111;
	public final static int USERINFO_REturn_NAME = 11;
	public final static int USERINFO_NAME = 0;
    public final static int USERINFO_SEX = 1;
    public final static int USERINFO_SCHOOL = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail_edit);
		initToolbar("个人信息");
		initView();
	}

	private void initView() {
		listView = (ListView)findViewById(R.id.user_detai_edit_list);
		
		userEntity = (UserEntity)getIntent().getSerializableExtra("userEntity");
		sexs = getResources().getStringArray(R.array.sexs);
		user_info_list_first = getResources().getStringArray(R.array.user_info_list_first);
		if(userEntity!=null){
			getUserInfoRows();
		}
		adapter = new UserDetailAdapter(this, user_info_list_first, user_info_list_second, R.layout.item_user_detail_edit);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(itemClickListener);
	}
	
	void getUserInfoRows(){
		user_info_list_second = new String[]{
				userEntity.getUserName(),
				sexs[userEntity.getSex()],
				userEntity.getSchoolName()
		};
	}
	
	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch ((int) id) {
			case USERINFO_NAME:
				Intent intent = new Intent(UserDetailEditActivity.this,SetUserInfoActivity.class);
				intent.putExtra("title", "昵称");
				intent.putExtra("value", userEntity.getUserName());
				startActivityForResult(intent,RESULT_EDIT_LIST);
				break;
			case USERINFO_SEX:
				setSexs();
				break;
			case USERINFO_SCHOOL:
				Intent intent2 = new Intent(UserDetailEditActivity.this,ChooseSchoolActivity.class);
				intent2.putExtra("isFirst", false);
				startActivityForResult(intent2,RESULT_EDIT_LIST);
				break;
			default:break;
			}
		}
		
	};
	
	private void setSexs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别")
                .setItems(R.array.sexs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user_info_list_second[USERINFO_SEX]=sexs[which];
                        userEntity.setSex(which);
                        submit();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
	
	private void submit(){
		loadingProgressDialog.show();
		networkHelper.postData(URLs.USER_UPDATEMSG, userEntity);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_EDIT_LIST) {
			if(resultCode==USERINFO_REturn_NAME){
				userEntity.setUserName(data.getExtras().getString("value"));
				user_info_list_second[USERINFO_NAME]=data.getExtras().getString("value");
				submit();
			}else if(resultCode==USERINFO_SCHOOL) {
				userEntity.setSchoolId(Constants.SCID);
				user_info_list_second[USERINFO_SCHOOL]=data.getExtras().getString("schoolName");
				submit();
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(UserDetailActivity.RESULT_OK);
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}
	
	@Override
	public void onRequestSuccess(String response) {
		super.onRequestSuccess(response);
		Gson gson = new Gson();
		DataJson dataJson = gson.fromJson(response, DataJson.class);
		if (dataJson.isSuccess()) {
			showButtomToast(dataJson.getMsg());
			adapter.notifyDataSetChanged();
			AccountInfoUtils.saveSharedPreferences(this, userEntity);
		}else {
			showButtomToast(dataJson.getMsg());
		}
	}
	
	@Override
	public void onRequestFail(String response) {
		super.onRequestFail(response);
		showButtomToast("修改失败");
	}
}
