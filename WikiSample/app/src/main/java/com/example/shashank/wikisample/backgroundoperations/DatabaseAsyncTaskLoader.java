package com.example.shashank.wikisample.backgroundoperations;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.example.shashank.wikisample.ViewModel.ItemViewModel;
import com.example.shashank.wikisample.database.ItemDatabase;
import com.example.shashank.wikisample.model.SearchItem;
import com.example.shashank.wikisample.model.SearchItemDao;

import java.util.ArrayList;

import static com.example.shashank.wikisample.model.SearchItem.ITEM_COLUMN_DESCRIPTION;
import static com.example.shashank.wikisample.model.SearchItem.ITEM_COLUMN_ID;
import static com.example.shashank.wikisample.model.SearchItem.ITEM_COLUMN_NAME;
import static com.example.shashank.wikisample.model.SearchItem.ITEM_COLUMN_THUMBNAIL;
import static com.example.shashank.wikisample.provider.CustomContentProvider.URI_SEARCH_ITEM;

public class DatabaseAsyncTaskLoader extends AsyncTaskLoader<ArrayList<ItemViewModel>>{

	private SearchItemDao dao;
	private ArrayList<ItemViewModel> data;
	private CustomContentObserver contentObserver;

	public DatabaseAsyncTaskLoader(Context context) {
		super(context);
		dao = ItemDatabase.getInstance(context).itemDao();
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if(data == null)
			forceLoad();
		if(contentObserver == null)
			contentObserver = new CustomContentObserver();
		getContext().getContentResolver().registerContentObserver(URI_SEARCH_ITEM,true,contentObserver);
	}

	@Override
	public ArrayList<ItemViewModel> loadInBackground() {
		data = new ArrayList<>();
		synchronized (this) {
			Cursor cursor = dao.getAllItem();
			while (cursor.moveToNext()) {
				SearchItem item = new SearchItem();
				item.setId(cursor.getInt(cursor.getColumnIndex(ITEM_COLUMN_ID)));
				item.setName(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_NAME)));
				item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_DESCRIPTION)));
				item.setThumbnail(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_THUMBNAIL)));
				ItemViewModel viewModel = new ItemViewModel(item);
				viewModel.setLocal(true);
				data.add(viewModel);
			}
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
