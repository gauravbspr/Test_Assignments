package com.example.shashank.feeds;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.shashank.feeds.Utility.Utils;
import com.example.shashank.feeds.ViewModel.ItemViewModel;
import com.example.shashank.feeds.backgroundoperations.DatabaseAsyncTaskLoader;
import com.example.shashank.feeds.backgroundoperations.NetworkOperation;
import com.example.shashank.feeds.backgroundoperations.ResponseHandle;
import com.example.shashank.feeds.database.ItemDatabase;
import com.example.shashank.feeds.databinding.LandingBinding;
import com.example.shashank.feeds.views.FeedsItemAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static com.example.shashank.feeds.Utility.AppConstants.BASE_URL;
import static java.net.HttpURLConnection.HTTP_OK;

public class LandingActivity extends AppCompatActivity implements ResponseHandle{

	public static final int DATABASE_ACCESS_ID = 1;

	private LandingBinding activityBinding;
	private FeedsItemAdapter adapter;
	private ArrayList<ItemViewModel> dataItems;
	private ItemDatabase db;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_landing);
		db = ItemDatabase.getInstance(getApplicationContext());
		imageLoader = Utils.getImageLoader(getApplicationContext());
		initView();

		//initiating loader callback
		getSupportLoaderManager().initLoader(DATABASE_ACCESS_ID,null, loaderCallback);

		//default api call happens if no data it available
		checkForData();
	}


	private void initView(){
		setSupportActionBar(activityBinding.toolbar);
		adapter = new FeedsItemAdapter(dataItems,LandingActivity.this);
		activityBinding.recyclerView.setAdapter(adapter);
		activityBinding.refreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
		activityBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				makeApiCall();
			}
		});
		activityBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if(newState == RecyclerView.SCROLL_STATE_DRAGGING)
					imageLoader.pause();
				else if(newState == RecyclerView.SCROLL_STATE_SETTLING)
					imageLoader.resume();
			}
		});
	}


	/* Loader Callback for fetching data
	   whenever any change takes place in the database
	*/
	private LoaderManager.LoaderCallbacks<ArrayList<ItemViewModel>> loaderCallback = new LoaderManager.LoaderCallbacks<ArrayList<ItemViewModel>>() {
		@Override
		public Loader<ArrayList<ItemViewModel>> onCreateLoader(int id, Bundle args) {
			return new DatabaseAsyncTaskLoader(getApplicationContext());
		}

		@Override
		public void onLoadFinished(Loader<ArrayList<ItemViewModel>> loader, ArrayList<ItemViewModel> data) {
			dataItems = data;
			setData(dataItems);
		}

		@Override
		public void onLoaderReset(Loader<ArrayList<ItemViewModel>> loader) {
			Log.e("reset-->","reset");
		}
	};

	private void setData(ArrayList<ItemViewModel> data){
		if(data.size()>0)
			activityBinding.setVisibility(false);
		else
			activityBinding.setVisibility(true);
		adapter.data = data;
		adapter.notifyDataSetChanged();
	}


	private void makeApiCall(){
		if(Utils.networkAvailable(getApplicationContext()))
			new NetworkOperation(getApplicationContext(),LandingActivity.this).execute(BASE_URL);
		else
			showSnackBar(getString(R.string.no_connectivity));
	}

	private void showSnackBar(String msg){
		Snackbar.make(activityBinding.getRoot(), msg, Snackbar.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onResponse(int responseCode, String title) {
		if(responseCode == HTTP_OK)
			if(title != null && !title.isEmpty())
				getSupportActionBar().setTitle(title);
			else
				showSnackBar(getString(R.string.error));
		else
			showSnackBar(getString(R.string.error));
		activityBinding.refreshLayout.setRefreshing(false);
	}


	@SuppressLint("StaticFieldLeak")
	private void checkForData(){
		new AsyncTask<Void,Void,Integer>(){
			@Override
			protected Integer doInBackground(Void... voids) {
				return db.itemDao().getCount();
			}

			@Override
			protected void onPostExecute(Integer value) {
				super.onPostExecute(value);
				if(value == 0)
					makeApiCall();
			}
		}.execute();
	}
}
