package com.example.shashank.feeds.backgroundoperations;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.example.shashank.feeds.ViewModel.ItemViewModel;
import com.example.shashank.feeds.database.ItemDatabase;
import com.example.shashank.feeds.model.FeedItem;
import com.example.shashank.feeds.model.FeedItemDao;
import java.util.ArrayList;

import static com.example.shashank.feeds.model.FeedItem.ITEM_COLUMN_DESCRIPTION;
import static com.example.shashank.feeds.model.FeedItem.ITEM_COLUMN_ID;
import static com.example.shashank.feeds.model.FeedItem.ITEM_COLUMN_THUMBNAIL;
import static com.example.shashank.feeds.model.FeedItem.ITEM_COLUMN_TITLE;
import static com.example.shashank.feeds.provider.CustomContentProvider.URI_FEED_ITEM;


public class DatabaseAsyncTaskLoader extends AsyncTaskLoader<ArrayList<ItemViewModel>>{

	private FeedItemDao dao;
	private ArrayList<ItemViewModel> data;
	private CustomContentObserver contentObserver;

	public DatabaseAsyncTaskLoader(Context context) {
		super(context);
		dao = ItemDatabase.getInstance(context).itemDao();
	}

	/* Adding a content observer to monitor
	   any changes made to the database
	 */
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if(data == null)
			forceLoad();
		if(contentObserver == null)
			contentObserver = new CustomContentObserver();
		getContext().getContentResolver().registerContentObserver(URI_FEED_ITEM,true,contentObserver);
	}


	/*
	   Loading data from database
	 */
	@Override
	public ArrayList<ItemViewModel> loadInBackground() {
		data = new ArrayList<>();
		synchronized (this) {
			Cursor cursor = dao.getAllItem();
			while (cursor.moveToNext()) {
				FeedItem item = new FeedItem();
				item.setId(cursor.getInt(cursor.getColumnIndex(ITEM_COLUMN_ID)));
				item.setName(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_TITLE)));
				item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_DESCRIPTION)));
				item.setThumbnail(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_THUMBNAIL)));
				ItemViewModel viewModel = new ItemViewModel(item);
				viewModel.setLocal(true);
				data.add(viewModel);
			}
			cursor.close();
		}
		return data;
	}

	@Override
	public void deliverResult(ArrayList<ItemViewModel> data) {
		super.deliverResult(data);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		getContext().getContentResolver().unregisterContentObserver(contentObserver);
	}

	public class CustomContentObserver extends ContentObserver{

		public CustomContentObserver() {
			super(null);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			onContentChanged();
		}

		@Override
		public boolean deliverSelfNotifications() {
			return true;
		}
	}
}
