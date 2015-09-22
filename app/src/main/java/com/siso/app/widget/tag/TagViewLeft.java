package com.siso.app.widget.tag;

import com.siso.app.entity.Position;
import com.siso.app.entity.TagInfo;
import com.siso.app.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-5-1 上午11:55:13
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-5-1 上午11:55:13
 *
 */
public class TagViewLeft extends TagView {
	
	private int screenWidth,screenHeight;
    private int lastX,lastY;
    private RelativeLayout rLayout;
	
	public TagViewLeft(Context context) {
		super(context);
	}

	public TagViewLeft(Context context, AttributeSet attrs,int screenWidth,int screenHeight,Position position,TagInfo tagInfo,boolean isMove) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.tag_view_left, this);
		this.screenWidth=screenWidth;
		this.screenHeight=screenHeight;
		this.position = position;
		this.tagInfo = tagInfo;
		init(isMove);
	}
	
	private void init(boolean isMove){
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(position.getXplace(), position.getYplace(), 0, 0);
        this.setLayoutParams(lp);
		textView = (TextView)findViewById(R.id.text);
		textView.setText(tagInfo.getTitle());
		rLayout =  this;
		this.setClickable(true);
		icon_brandimg = (ImageView) findViewById(R.id.icon_brand);
		blackIconimg1 = (ImageView) findViewById(R.id.blackIcon1);
		blackIconimg2 = (ImageView) findViewById(R.id.blackIcon2);
		if(isMove) initMove();
	}

	private void initMove() {
		OnTouchListener onTouchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX=(int)event.getRawX();
		            lastY=(int)event.getRawY();
//		            System.out.println("lastX="+lastX);
					break;
				case MotionEvent.ACTION_MOVE:
					int area = rLayout.getHeight();
					int dx=(int)event.getRawX()-lastX;
		            int dy=(int)event.getRawY()-lastY;
		            int top=v.getTop()+dy;
		            int left=v.getLeft()+dx;
		            if(top<=0) top=0;
		            if(left<=0) left=0;
//		            System.out.println("screenHeight="+screenHeight);
		            if(top>=screenHeight-area)
		            {
		                top=screenHeight-area;
		            }
		            if(left>=screenWidth-rLayout.getWidth())
		            {
		                left=screenWidth-rLayout.getWidth();
		            }
//		            System.out.println("left="+left+"top="+top);
//		            v.layout(left, top, left+rLayout.getWidth(), top+area);
		            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		            lp.setMargins(left, top, left+rLayout.getWidth(), top+area);
		            v.setLayoutParams(lp);
		            position.setXplace(left);
		            position.setYplace(top);
		            lastX=(int)event.getRawX();
		            lastY=(int)event.getRawY();
		            break;
		          case MotionEvent.ACTION_UP:
		        	  break;
				}
				return false;
			}
		};
		
		rLayout.setOnTouchListener(onTouchListener);
	}
	
}
