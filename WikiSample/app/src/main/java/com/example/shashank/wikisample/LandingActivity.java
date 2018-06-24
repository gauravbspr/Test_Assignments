package com.example.shashank.wikisample;

import android.support.v4.app.LoaderManager;
import android.databinding.DataBindingUtil;

import com.example.shashank.wikisample.ViewModel.ItemViewModel;
import com.example.shashank.wikisample.backgroundoperations.DatabaseAsyncTaskLoader;
import com.example.shashank.wikisample.backgroundoperations.NetworkOperation;
import com.example.shashank.wikisample.backgroundoperations.ResponseHandle;
import com.example.shashank.wikisample.databinding.LandingBinding;
import com.example.shashank.wikisample.views.SearchItemAdapter;

import android.os.AsyncTask;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LandingActivity extends AppCompatActivity implements ResponseHandle{

	public static final int DATABASE_ACCESS_ID = 1;

	private final long DELAY = 200;
	private Timer timer;
	private LandingBinding activityBinding;
	private SearchItemAdapter adapter;
	private ArrayList<ItemViewModel> dataItems;
	private NetworkOperation operation;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_landing);
		setView();

	}

	@Override
	protected void onResume() {
		super.onResume();
		getSupportLoaderManager().initLoader(DATABASE_ACCESS_ID,null, loaderCallback).forceLoad();
	}

	private void setView(){
		setSupportActionBar(activityBinding.toolbar);
		dataItems = new ArrayList<>();
		adapter = new SearchItemAdapter(dataItems,getApplicationContext());
		activityBinding.recyclerView.setAdapter(adapter);
		activityBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				runNetworkOperation(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if(timer != null)
					timer.cancel();
				if(newText.isEmpty())
					setData(dataItems);
				else
					if(newText.length()>=3){
						timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								runNetworkOperation(activityBinding.searchView.getQuery().toString());
							}
						}, DELAY);
					}

				return false;
			}
		});
	}


	private void runNetworkOperation(String keyword){
		if (operation != null){
			if(operation.getStatus() != AsyncTask.Status.FINISHED)
				operation.cancel(true);
		}
		operation = new NetworkOperation(LandingActivity.this);
		operation.execute(keyword);
	}

	private LoaderManager.LoaderCallbacks<ArrayList<ItemViewModel>> loaderCallback = new LoaderManager.LoaderCallbacks<ArrayList<ItemViewModel>>() {
		@Override
		public Loader<ArrayList<ItemViewModel>> onCreateLoader(int id, Bundle args) {
			return new DatabaseAsyncTaskLoader(getApplicationContext());
		}

		@Override
		public void onLoadFinished(Loader<ArrayList<ItemViewModel>> loader, ArrayList<ItemViewModel> data) {
			if(data.size()>0)
				activityBinding.setVisibility(false);
			else
				activityBinding.setVisibility(true);
			dataItems = data;
			setData(dataItems);
		}

		@Override
		public void onLoaderReset(Loader<ArrayList<ItemViewModel>> loader) {
		}
	};


	private void setData(ArrayList<ItemViewModel> data){
		adapter.data = data;
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResponse(ArrayList<ItemViewModel> data) {
		setData(data);
	}
}
