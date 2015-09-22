package com.siso.app.ui;

import com.siso.app.utils.AccountInfoUtils;
import com.siso.app.widget.viewpager.JazzyViewPager;
import com.siso.app.widget.viewpager.JazzyViewPager.TransitionEffect;
import com.siso.app.widget.viewpager.OutlineContainer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideActivity extends Activity {
	
	private JazzyViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_guide);
		initView();
	}
	
	private void initView() {
		setupJazziness(TransitionEffect.CubeOut);
	}

	private void setupJazziness(TransitionEffect effect) {
		viewPager = (JazzyViewPager) findViewById(R.id.guide_viewPager);
		viewPager.setTransitionEffect(effect);
		viewPager.setAdapter(new MainAdapter());
	}
	
	private class MainAdapter extends PagerAdapter {
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
//			TextView text = new TextView(GuideActivity.this);
//			text.setGravity(Gravity.CENTER);
//			text.setTextSize(30);
//			text.setTextColor(Color.WHITE);
//			text.setText("Page " + position);
//			text.setPadding(30, 30, 30, 30);
//			int bg = Color.rgb((int) Math.floor(Math.random()*128)+64, 
//					(int) Math.floor(Math.random()*128)+64,
//					(int) Math.floor(Math.random()*128)+64);
//			text.setBackgroundColor(bg);
//			container.addView(text, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			View view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.item_guide, null);
			ImageView imageView = (ImageView)view.findViewById(R.id.guide_img);
			if(position==0){
				imageView.setBackgroundResource(R.drawable.guide_1);
			}else if (position==1) {
				Button button = (Button)view.findViewById(R.id.guide_btn);
				button.setVisibility(View.VISIBLE);
				imageView.setBackgroundResource(R.drawable.guide_2);
				button.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(GuideActivity.this,ChooseSchoolActivity.class);
						intent.putExtra("isFirst", true);
						AccountInfoUtils.saveGuide(GuideActivity.this);
						startActivity(intent);
						finish();
					}
				});
			}
			//设置效果
			viewPager.setObjectForPosition(view, position);
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
//			container.removeView(mJazzy.findViewFromObject(position));
		}
		@Override
		public int getCount() {
			return 2;
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}		
	}
}
