package com.siso.app.adapter;

import java.util.List;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.siso.app.ui.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TagSearchAdapter extends BaseAdapter {
	
	private Context context;//运行上下文
	private LayoutInflater listContainer;//视图容器
	private int itemViewResource;//自定义项视图源
	private List<String>list;
	static class ViewHolder{
		EmojiconTextView textView;
	}
	
	public TagSearchAdapter(Context context,int resource , List<String> list){
		this.context = context;
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int item) {
		return item;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = listContainer.inflate(this.itemViewResource, null);
			viewHolder.textView = (EmojiconTextView)convertView.findViewById(R.id.search_brand_item);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.textView.setText(list.get(position));
		return convertView;
	}

}
