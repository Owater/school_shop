package com.siso.app.ui;

import com.siso.app.ui.common.BaseActionBarActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends BaseActionBarActivity {
	
	private WebView webView;
	private ProgressBar progressBar;
	ClipboardManager clipboardManager;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		initToolbar(getStringByRId(R.string.go_back));
		initView();
	}

	private void initView() {
		webView = (WebView) findViewById(R.id.webview);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		
		clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		url = getIntent().getExtras().getString("url");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new WebChromeClient(){
			// 更新进度条
			@Override
			public void onProgressChanged(WebView view, int progress) {
				progressBar.setProgress(progress);
				if(progress==100)
					progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			ClipData clip = ClipData.newPlainText("simple text",url);
			clipboardManager.setPrimaryClip(clip);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class MyWebViewClient extends WebViewClient {  
        
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            view.loadUrl(url);  
            return true;  
        }  
    }
}
