package com.example.shashank.feeds.ViewModel;

import android.databinding.BaseObservable;

import com.example.shashank.feeds.model.FeedItem;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.shashank.feeds.Utility.AppConstants.DESCRIPTION;
import static com.example.shashank.feeds.Utility.AppConstants.TITLE;
import static com.example.shashank.feeds.Utility.AppConstants.THUMBNAIL;

public class ItemViewModel extends BaseObservable {

	private long id;
	private String title;
	private String thumbnail;
	private String description;
	private boolean local;

	private FeedItem item;

	public ItemViewModel(FeedItem item) {
		this.item = item;
		this.title = item.getTitle();
		this.thumbnail = item.getThumbnail();
		this.description = item.getDescription();
		local = false;
	}

	public String toString(){
		JSONObject object = new JSONObject();
		try {
			object.put(TITLE,this.title);
			object.put(DESCRIPTION,this.description);
			object.put(THUMBNAIL,this.thumbnail);
		}catch (JSONException e){
			e.printStackTrace();
		}
		return object.toString();
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FeedItem getItem() {
		return item;
	}
}
