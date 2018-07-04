package com.example.shashank.feeds.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.shashank.feeds.model.FeedItem;
import com.example.shashank.feeds.model.FeedItemDao;

import static com.example.shashank.feeds.database.ItemDatabase.DATABASE_VERSION;

/*
	Abstract class, which act as an abstract layer over the SQLite database
 */

@Database(entities = FeedItem.class , version = DATABASE_VERSION)
public abstract class ItemDatabase extends RoomDatabase {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "app_database";

	private static ItemDatabase database;

	public abstract FeedItemDao itemDao();

	public static synchronized ItemDatabase getInstance(Context context){
		if(database == null){
			database = Room.databaseBuilder(context.getApplicationContext(),ItemDatabase.class,DATABASE_NAME).build();
		}
		return database;
	}
}
