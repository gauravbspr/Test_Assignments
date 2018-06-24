package com.example.shashank.wikisample.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import static com.example.shashank.wikisample.model.SearchItem.SEARCH_ITEM_TABLE;

@Dao
public interface SearchItemDao {

	@Query("SELECT * FROM "+SEARCH_ITEM_TABLE)
	Cursor getAllItem();

	@Query("SELECT COUNT(*) FROM "+SEARCH_ITEM_TABLE)
	int getCount();

	@Insert
	void insert(SearchItem item);

	@Delete
	void delete(SearchItem item);

}
