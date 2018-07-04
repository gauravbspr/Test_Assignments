package com.example.shashank.feeds;

import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

import static com.example.shashank.feeds.Utility.AppConstants.BASE_URL;

public class LandingActivity extends AppCompatActivity implements ResponseHandle{

	public static final int DATABASE_ACCESS_ID = 1;

	private LandingBinding activityBinding;
	private FeedsItemAdapter adapter;
	private ArrayList<ItemViewModel> dataItems;
	private ItemDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_landing);
		db = ItemDatabase.getInstance(getApplicationContext());
		initView();

		//initiating loader callback
		getSupportLoaderManager().initLoader(DATABASE_ACCESS_ID,null, loaderCallback);

		if(db.itemDao().getCount() == 0) {
			activityBinding.refreshLayout.setRefreshing(true);
			makeApiCall();
		}
	}


	private void initView(){
		setSupportActionBar(activityBinding.toolbar);
		adapter = new FeedsItemAdapter(dataItems,LandingActivity.this);
		activityBinding.recyclerView.setAdapter(adapter);
		activityBinding.refreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
		activityBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if(!activityBinding.refreshLayout.isRefreshing())
					makeApiCall();
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
	public void onResponse(String title) {
		if(title != null && !title.isEmpty())
			getSupportActionBar().setTitle(title);
		activityBinding.refreshLayout.setRefreshing(false);
	}
}
