package com.example.shashank.wikisample;

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
		new InsertItem(keyItem,getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);
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

	static class InsertItem extends AsyncTask<Void,Void,Void>{
		private SearchItem item;
		private Context context;
		public InsertItem(SearchItem item, Context context){
			this.item = item;
			this.context = context;
		}
		@Override
		protected Void doInBackground(Void... voids) {
			try {
				ItemDatabase db = ItemDatabase.getInstance(context);
				db.itemDao().insert(item);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		if(activityBinding.webView.canGoBack())
			activityBinding.webView.goBack();
		else
			super.onBackPressed();
	}
}
