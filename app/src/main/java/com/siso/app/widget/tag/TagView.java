package com.siso.app.widget.tag;

import com.siso.app.entity.Position;
import com.siso.app.entity.TagInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagView extends RelativeLayout {
	
	protected TextView textView;
	protected ImageView icon_brandimg;
	protected ImageView blackIconimg1;
	protected ImageView blackIconimg2;
	protected Position position;
//	protected String content;
//	public int orientation; //0代表左边 , 1代表右边
	public TagInfo tagInfo;
	
	public TagView(Context context) {
		super(context);
	}
	
	public TagView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

//	public String getContent() {
//		return content;
//	}
	
}
