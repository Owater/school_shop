package com.siso.app.ui;

import com.siso.app.utils.AccountInfoUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		final View view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		
		AlphaAnimation aa = new AlphaAnimation(0.4f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}
	
	private void redirectTo() {
		int guide = AccountInfoUtils.getGuide(this);
		int scId = AccountInfoUtils.getScId(this);
		Intent intent = null;
		if(guide==0){
			intent = new Intent(this,GuideActivity.class);
		}else if(scId==0){
			intent = new Intent(this,ChooseSchoolActivity.class);
			intent.putExtra("isFirst", true);
		}else {
			intent = new Intent(this,MainActivity.class);
		}
		startActivity(intent);
		finish();
	}
}
