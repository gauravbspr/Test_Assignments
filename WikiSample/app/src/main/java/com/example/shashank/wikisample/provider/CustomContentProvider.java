package com.example.shashank.wikisample.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.shashank.wikisample.database.ItemDatabase;
import com.example.shashank.wikisample.model.SearchItem;

import static com.example.shashank.wikisample.model.SearchItem.SEARCH_ITEM_TABLE;

public class CustomContentProvider extends ContentProvider{

	public static final String AUTHORITY = "com.example.shashank.wikisample.provider";
	public static final Uri URI_SEARCH_ITEM = Uri.parse(
			"content://" + AUTHORITY + "/" + SEARCH_ITEM_TABLE);

	@Override
	public boolean onCreate() {
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		final Cursor cursor;
		final Context context = getContext();
		if(context == null)
			return null;
		if (uri.toString().equals(URI_SEARCH_ITEM.toString())){
			ItemDatabase db = ItemDatabase.getInstance(context);
			cursor = db.itemDao().getAllItem();
			return cursor;
		}
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		final Context context = getContext();
		if(context == null)
			return null;
		if (uri.toString().equals(URI_SEARCH_ITEM.toString())){
			ItemDatabase db = ItemDatabase.getInstance(context);
			SearchItem item = new SearchItem(values);
			long id;
			if(db.itemDao().getItemById(item.getId()).moveToNext()) {
				db.itemDao().update(item);
				id = item.getId();
			}else
				id = db.itemDao().insert(item);
			context.getContentResolver().notifyChange(uri,null);
			return ContentUris.withAppendedId(uri, id);
		}
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		final Context context = getContext();
		if(context == null)
			return 0;
		context.getContentResolver().notifyChange(uri,null);
		return 0;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		final Context context = getContext();
		if(context == null)
			return 0;
		if (uri.toString().equals(URI_SEARCH_ITEM.toString())){
			ItemDatabase db = ItemDatabase.getInstance(context);
			int count = db.itemDao().update(new SearchItem(values));
			context.getContentResolver().notifyChange(uri,null);
			return count;
		}
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}
}
