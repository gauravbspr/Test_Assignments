package com.example.shashank.feeds.ViewModel;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.shashank.feeds.model.FeedItem;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.shashank.feeds.Utility.AppConstants.DESCRIPTION;
import static com.example.shashank.feeds.Utility.AppConstants.TITLE;
import static com.example.shashank.feeds.Utility.AppConstants.THUMBNAIL;

public class ItemViewModel extends BaseObservable implements Parcelable{

	private long id;
	private String title;
	private String thumbnail;
	private String description;

	private FeedItem item;

	public ItemViewModel(FeedItem item) {
		this.item = item;
		this.title = item.getTitle();
		this.thumbnail = item.getThumbnail();
		this.description = item.getDescription();
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



	@Override
	public int describeContents() {
		return 0;
	}

	public ItemViewModel(Parcel in) {
		String[] data = new String[3];
		in.readStringArray(data);
		this.title = data[0];
		this.description = data[1];
		this.thumbnail = data[2];
		this.id = in.readLong();
		this.item = new FeedItem(title,description,thumbnail);
		this.item.setId(this.id);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{this.title,this.description,this.thumbnail});
		dest.writeLong(this.id);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public ItemViewModel createFromParcel(Parcel in) {
			return new ItemViewModel(in);
		}

		public ItemViewModel[] newArray(int size) {
			return new ItemViewModel[size];
		}
	};
}
