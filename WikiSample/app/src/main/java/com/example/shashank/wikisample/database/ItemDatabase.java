package com.example.shashank.wikisample.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.shashank.wikisample.model.SearchItem;
import com.example.shashank.wikisample.model.SearchItemDao;

import static com.example.shashank.wikisample.database.ItemDatabase.DATABASE_VERSION;

@Database(entities = SearchItem.class , version = DATABASE_VERSION)
public abstract class ItemDatabase extends RoomDatabase {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "app_database";

	private static ItemDatabase database;

	public abstract SearchItemDao itemDao();

	public static synchronized ItemDatabase getInstance(Context context){
		if(database == null){
			database = Room.databaseBuilder(context.getApplicationContext(),ItemDatabase.class,DATABASE_NAME).build();
		}
		return database;
	}
}
