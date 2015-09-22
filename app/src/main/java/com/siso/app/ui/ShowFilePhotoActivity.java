package com.siso.app.ui;

import java.util.ArrayList;

import com.siso.app.adapter.AlbumGridViewAdapter;
import com.siso.app.common.Constants;
import com.siso.app.entity.ImageItem;
import com.siso.app.ui.common.BaseActionBarActivity;
import com.siso.app.utils.Bimp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ToggleButton;

public class ShowFilePhotoActivity extends BaseActionBarActivity {
	
	private static final String TAG = "ShowFilePhotoActivity";
	
	private GridView showGridView;
	private AlbumGridViewAdapter gridImageAdapter;
	public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_file_photo);
		
		initView();
	}

	private void initView() {
		showGridView = (GridView)findViewById(R.id.showfilephoto_grid);
		initToolbar(getStringByRId(R.string.title_activity_show_picture_file));
		
		String folderName = getIntent().getStringExtra("folderName");
		if (folderName.length() > 8) {
			folderName = folderName.substring(0, 9) + "...";
		}
		gridImageAdapter = new AlbumGridViewAdapter(this,dataList,Bimp.tempSelectBitmap);
		showGridView.setAdapter(gridImageAdapter);
		initClick();
	}

	private void initClick() {
		gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(ToggleButton view, int position, boolean isChecked,
					Button chooseBt) {
				if (isChecked) {
					chooseBt.setVisibility(View.VISIBLE);
					Bimp.tempSelectBitmap.add(dataList.get(position));
				} else {
					chooseBt.setVisibility(View.GONE);
					Bimp.tempSelectBitmap.remove(dataList.get(position));
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.set_password,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.setpwd_submit:
			setResult(Constants.CHOOSE_PIC);
			finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
         }
	}
	
}
