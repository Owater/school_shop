package com.siso.app.adapter;

import com.siso.app.common.Constants;
import com.siso.app.ui.R;
import com.siso.app.utils.Bimp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-3-16 上午11:50:19
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-3-16 上午11:50:19 
 *
 */
public class ImgGridAdapter extends BaseAdapter {
	
	private Context context;//运行上下文
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private UpdateGridAdapterListener updateGridAdapterListener;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public ImgGridAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void update() {
		loading();
	}

	public int getCount() {
		if(Bimp.tempSelectBitmap.size() == Constants.UPLOAD_PIC_NUMS){
			return Constants.UPLOAD_PIC_NUMS;
		}
		return (Bimp.tempSelectBitmap.size() + 1);
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_img_gridview,parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_imgGridview_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Log.i("ss", "Bimp.tempSelectBitmap.size()=="+Bimp.tempSelectBitmap.size());

		if (position ==Bimp.tempSelectBitmap.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_addpic_normal));
			if (position == Constants.UPLOAD_PIC_NUMS) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				updateGridAdapterListener.updateImgGridData();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.tempSelectBitmap.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						Bimp.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}
	
	/**
	 * 
	 * description : 回调函数
	 *
	 * @version 1.0
	 * @author Owater
	 * @createtime : 2015-3-16 下午1:23:48
	 * 
	 * 修改历史:
	 * 修改人                                          修改时间                                                  修改内容
	 * --------------- ------------------- -----------------------------------
	 * Owater        2015-3-16 下午1:23:48
	 *
	 */
	public static interface UpdateGridAdapterListener{
		void updateImgGridData();
	}

}

	