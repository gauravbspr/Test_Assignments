package com.example.shashank.wikisample.ViewModel;

import android.databinding.BaseObservable;

import com.example.shashank.wikisample.model.SearchItem;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.shashank.wikisample.Utility.AppConstants.DESCRIPTION;
import static com.example.shashank.wikisample.Utility.AppConstants.NAME;
import static com.example.shashank.wikisample.Utility.AppConstants.THUMBNAIL;
import static com.example.shashank.wikisample.Utility.AppConstants._ID;

public class ItemViewModel extends BaseObservable {

	private int id;
	private String name;
	private String thumbnail;
	private String description;
	private boolean local;

	private SearchItem item;

	public ItemViewModel(SearchItem item) {
		this.item = item;
		this.id = item.getId();
		this.name = item.getName();
		this.thumbnail = item.getThumbnail();
		this.description = item.getDescription();
		local = false;
	}

	public String toString(){
		JSONObject object = new JSONObject();
		try {
			object.put(_ID, this.id);
			object.put(NAME,this.name);
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

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SearchItem getItem() {
		return item;
	}
}
