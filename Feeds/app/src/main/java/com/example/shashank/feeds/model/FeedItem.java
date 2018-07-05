package com.example.shashank.feeds.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.provider.BaseColumns;

import org.json.JSONObject;

import static com.example.shashank.feeds.Utility.AppConstants.DESCRIPTION;
import static com.example.shashank.feeds.Utility.AppConstants.TITLE;
import static com.example.shashank.feeds.Utility.AppConstants.THUMBNAIL;
import static com.example.shashank.feeds.model.FeedItem.FEEDS_ITEM_TABLE;

/*
	Plain Old Java Object(POJO) or the MODEL class for representing each feed item.
	It will also act as an ENTITY class representing each row in the feeds table in
	the database
 */


@Entity(tableName = FEEDS_ITEM_TABLE)
public class FeedItem {

	public static final String FEEDS_ITEM_TABLE = "feed_item";
	public static final String ITEM_COLUMN_ID = BaseColumns._ID;
	public static final String ITEM_COLUMN_TITLE = "name";
	public static final String ITEM_COLUMN_THUMBNAIL = "thumbnail";
	public static final String ITEM_COLUMN_DESCRIPTION = "description";

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = ITEM_COLUMN_ID,index = true)
	private long id;

	@ColumnInfo(name = ITEM_COLUMN_TITLE)
	private String title;

	@ColumnInfo(name = ITEM_COLUMN_THUMBNAIL)
	private String thumbnail;

	@ColumnInfo(name = ITEM_COLUMN_DESCRIPTION)
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public FeedItem(String title, String description, String thumbnail) {
		this.title = title;
		this.thumbnail = thumbnail;
		this.description = description;
	}

	public FeedItem(){
		this.thumbnail = "";
		this.description = "";
	}

	public FeedItem(ContentValues values){
		if(values !=null) {
			if (values.containsKey(TITLE))
				this.title = values.getAsString(TITLE);
			if (values.containsKey(THUMBNAIL))
				this.thumbnail = values.getAsString(THUMBNAIL);
			if (values.containsKey(DESCRIPTION))
				this.description = values.getAsString(DESCRIPTION);
		}else{
			this.thumbnail = "";
			this.description = "";
		}
	}

	public ContentValues getContentValue(){
		ContentValues values = new ContentValues();
		values.put(TITLE, title);
		values.put(THUMBNAIL,thumbnail);
		values.put(DESCRIPTION,description);
		return values;
	}
}
