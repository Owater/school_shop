package com.siso.app.ui;

import com.siso.app.ui.common.BaseActionBarActivity;

import android.os.Bundle;

public class AboutActivity extends BaseActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initToolbar(getStringByRId(R.string.title_activity_about));
	}
}
