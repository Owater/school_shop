package com.siso.app.ui.photopick;

import com.siso.app.widget.crop.CropHandler;
import com.siso.app.widget.crop.CropHelper;
import com.siso.app.widget.crop.CropParams;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class BasePhotoCropActivity extends Activity implements CropHandler {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onPhotoCropped(Uri uri) {
    }

    @Override
    public void onCropCancel() {
    }

    @Override
    public void onCropFailed(String message) {
    }

    @Override
    public CropParams getCropParams() {
        return null;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
//    	Log.i("onDestroy", "onDestroy--------------------");
//    	Toast.makeText(BasePhotoCropActivity.this, "onDestroy", Toast.LENGTH_SHORT).show();
//        if (getCropParams() != null)
//            CropHelper.clearCachedCropFile(getCropParams().uri);
        super.onDestroy();
    }
}
