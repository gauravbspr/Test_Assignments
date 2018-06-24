package com.example.shashank.wikisample.backgroundoperations;

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

public class DatabaseAsyncTaskLoader extends AsyncTaskLoader<ArrayList<ItemViewModel>>{

	private SearchItemDao dao;

	public DatabaseAsyncTaskLoader(Context context) {
		super(context);
		dao = ItemDatabase.getInstance(context).itemDao();
	}

	@Override
	public ArrayList<ItemViewModel> loadInBackground() {
		ArrayList<ItemViewModel> data = new ArrayList<>();
		Cursor cursor= dao.getAllItem();
		while (cursor.moveToNext()){
			SearchItem item = new SearchItem();
			item.setId(cursor.getInt(cursor.getColumnIndex(ITEM_COLUMN_ID)));
			item.setName(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_NAME)));
			item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_DESCRIPTION)));
			item.setThumbnail(cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_THUMBNAIL)));
			ItemViewModel viewModel = new ItemViewModel(item);
			data.add(viewModel);
		}
		return data;
	}

	@Override
	public void deliverResult(ArrayList<ItemViewModel> data) {
		super.deliverResult(data);
	}
}
