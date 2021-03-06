package com.example.shashank.wikisample.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.shashank.wikisample.Utility.AppConstants.DESCRIPTION;
import static com.example.shashank.wikisample.Utility.AppConstants.NAME;
import static com.example.shashank.wikisample.Utility.AppConstants.THUMBNAIL;
import static com.example.shashank.wikisample.Utility.AppConstants._ID;
import static com.example.shashank.wikisample.model.SearchItem.SEARCH_ITEM_TABLE;

@Entity(tableName = SEARCH_ITEM_TABLE)
public class SearchItem {

	public static final String SEARCH_ITEM_TABLE = "search_item";
	public static final String ITEM_COLUMN_ID = BaseColumns._ID;
	public static final String ITEM_COLUMN_NAME = "name";
	public static final String ITEM_COLUMN_THUMBNAIL = "thumbnail";
	public static final String ITEM_COLUMN_DESCRIPTION = "description";

	@PrimaryKey()
	@ColumnInfo(name = ITEM_COLUMN_ID,index = true)
	private long id;

	@ColumnInfo(name = ITEM_COLUMN_NAME)
	private String name;

	@ColumnInfo(name = ITEM_COLUMN_THUMBNAIL)
	private String thumbnail;

	@ColumnInfo(name = ITEM_COLUMN_DESCRIPTION)
	private String description;



	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	public SearchItem(JSONObject object){
		this.setId(object.optInt(_ID));
		this.setName(object.optString(NAME));
		this.setDescription(object.optString(DESCRIPTION));
		this.setThumbnail(object.optString(THUMBNAIL));
	}

	public SearchItem(){
		this.thumbnail = "";
		this.description = "";
	}

	public SearchItem(ContentValues values){
		if(values !=null) {
			if (values.containsKey(_ID))
				this.id = values.getAsInteger(_ID);
			if (values.containsKey(NAME))
				this.name = values.getAsString(NAME);
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
		values.put(_ID,id);
		values.put(NAME,name);
		values.put(THUMBNAIL,thumbnail);
		values.put(DESCRIPTION,description);
		return values;
	}
}
