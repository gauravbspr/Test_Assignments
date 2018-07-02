package com.example.shashank.wikisample;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.shashank.wikisample.Utility.Utils;
import com.example.shashank.wikisample.database.ItemDatabase;
import com.example.shashank.wikisample.databinding.WebViewBinding;
import com.example.shashank.wikisample.model.SearchItem;

import static com.example.shashank.wikisample.Utility.AppConstants.BASE_PAGE_URL;
import static com.example.shashank.wikisample.Utility.AppConstants.KEY;
import static com.example.shashank.wikisample.provider.CustomContentProvider.URI_SEARCH_ITEM;

public class WebViewActivity extends AppCompatActivity {

	private WebViewBinding activityBinding;
	private SearchItem keyItem;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_web_view);
		keyItem = new SearchItem(Utils.parseJSON(getIntent().getStringExtra(KEY)));
		setSupportActionBar(activityBinding.toolbar);
		getSupportActionBar().setTitle(keyItem.getName());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initializeView();
		String url = BASE_PAGE_URL+keyItem.getName();
		activityBinding.webView.loadUrl(url);
		insetItem(keyItem);
	}

	private void initializeView(){
		activityBinding.webView.getSettings().setBuiltInZoomControls(true);
		activityBinding.webView.getSettings().setSupportZoom(true);
		activityBinding.webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				activityBinding.setVisibility(true);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				activityBinding.setVisibility(false);
			}
		});
	}

	@SuppressLint("StaticFieldLeak")
	private void insetItem(final SearchItem item){
		new AsyncTask<Void,Void,Void>(){
			@Override
			protected Void doInBackground(Void... voids) {
				getContentResolver().insert(URI_SEARCH_ITEM,item.getContentValue());
				return null;
			}
		}.execute();
	}


	@Override
	public void onBackPressed() {
		if(activityBinding.webView.canGoBack())
			activityBinding.webView.goBack();
		else
			super.onBackPressed();
	}
}
