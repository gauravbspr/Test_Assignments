package com.example.shashank.feeds.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import static com.example.shashank.feeds.model.FeedItem.ITEM_COLUMN_ID;
import static com.example.shashank.feeds.model.FeedItem.FEEDS_ITEM_TABLE;

@Dao
public interface FeedItemDao {

	@Query("SELECT * FROM "+FEEDS_ITEM_TABLE)
	Cursor getAllItem();

	@Query("SELECT * FROM "+FEEDS_ITEM_TABLE+" WHERE "+ITEM_COLUMN_ID+"=:id")
	Cursor getItemById(long id);

	@Query("SELECT COUNT(*) FROM "+FEEDS_ITEM_TABLE)
	int getCount();

	@Insert
	long insert(FeedItem items);

	@Update
	int update(FeedItem item);

	@Delete
	int delete(FeedItem... items);

	@Query("DELETE FROM "+FEEDS_ITEM_TABLE+" WHERE "+ITEM_COLUMN_ID +"=:id")
	int delete(long id);
}
