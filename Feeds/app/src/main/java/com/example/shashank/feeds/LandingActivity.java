package com.example.shashank.feeds;

import android.databinding.DataBindingUtil;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.shashank.feeds.ViewModel.ItemViewModel;
import com.example.shashank.feeds.backgroundoperations.DatabaseAsyncTaskLoader;
import com.example.shashank.feeds.databinding.LandingBinding;
import com.example.shashank.feeds.views.FeedsItemAdapter;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

	public static final int DATABASE_ACCESS_ID = 1;

	private LandingBinding activityBinding;
	private FeedsItemAdapter adapter;
	private ArrayList<ItemViewModel> dataItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_landing);
		setView();
		getSupportLoaderManager().initLoader(DATABASE_ACCESS_ID,null, loaderCallback);
	}

	private void setView(){
		setSupportActionBar(activityBinding.toolbar);
		adapter = new FeedsItemAdapter(dataItems,LandingActivity.this);
		activityBinding.recyclerView.setAdapter(adapter);
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
}
