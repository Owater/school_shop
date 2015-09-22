package com.siso.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.siso.app.adapter.NavigationDrawerAdapter;
import com.siso.app.chat.ui.ChatAllHistoryActivity;
import com.siso.app.entity.DrawerList;
import com.siso.app.ui.common.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class UserMsgFragment extends BaseFragment {
	
	private View mView;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragment_user_msg, null);
		initView();
		return mView;
	}

	private void initView() {
		listView = (ListView)mView.findViewById(R.id.user_msg_list);
		
		List<DrawerList> lists  = new ArrayList<DrawerList>();
		lists.add(new DrawerList("我的回复", R.drawable.ic_message_at_me));
		lists.add(new DrawerList("我参与的", R.drawable.ic_message_comment));
		lists.add(new DrawerList("最近联系人", R.drawable.contact_msg));
		
		listView.setAdapter(new NavigationDrawerAdapter(getActivity(), lists, R.layout.item_drawer_list));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(MyApplication.userInfo==null){
					showButtomToast(getStringByRId(R.string.has_no_login));
					return;
				}
				if(position==2){
					startActivity(new Intent(getActivity(),ChatAllHistoryActivity.class));
				}else {
					Intent intent = new Intent(getActivity(),UserReplyActivity.class);
					intent.putExtra("choose", position);
					startActivity(intent);
				}
			}
		});
	}
}
