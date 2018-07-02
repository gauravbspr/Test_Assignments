package com.example.shashank.wikisample.backgroundoperations;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.shashank.wikisample.Utility.Utils;
import com.example.shashank.wikisample.ViewModel.ItemViewModel;
import com.example.shashank.wikisample.database.ItemDatabase;
import com.example.shashank.wikisample.model.SearchItem;
import com.example.shashank.wikisample.provider.CustomContentProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.shashank.wikisample.Utility.AppConstants.BASE_QUERY_URL;
import static com.example.shashank.wikisample.Utility.AppConstants.DESCRIPTION;
import static com.example.shashank.wikisample.Utility.AppConstants.NAME;
import static com.example.shashank.wikisample.Utility.AppConstants.PAGEID;
import static com.example.shashank.wikisample.Utility.AppConstants.PAGES;
import static com.example.shashank.wikisample.Utility.AppConstants.QUERY;
import static com.example.shashank.wikisample.Utility.AppConstants.SOURCE;
import static com.example.shashank.wikisample.Utility.AppConstants.TERMS;
import static com.example.shashank.wikisample.Utility.AppConstants.THUMBNAIL;
import static com.example.shashank.wikisample.Utility.AppConstants.TITLE;
import static com.example.shashank.wikisample.Utility.AppConstants._ID;
import static com.example.shashank.wikisample.provider.CustomContentProvider.URI_SEARCH_ITEM;

public class NetworkOperation extends AsyncTask<String,Void,String> {

	private ResponseHandle responseHandle;
	private ArrayList<ItemViewModel> responseData;

	public NetworkOperation(ResponseHandle responseHandle) {
		this.responseHandle = responseHandle;
	}

	@Override
	protected String doInBackground(String... params) {
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String parameters = "&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpslimit=10&gpssearch=";
		String api_url = BASE_QUERY_URL+parameters+params[0];
		String line;
		try {
			URL url = new URL(api_url);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			// Read the input stream into a String
			InputStream inputStream = urlConnection.getInputStream();
			StringBuffer buffer = new StringBuffer();
			if (inputStream == null) return null;

			reader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = reader.readLine()) != null) buffer.append(line);

			if (buffer.length() == 0) return null;
			String response = buffer.toString();
			responseData = getResponseData(response);
		} catch (IOException e) {
			Log.e("NetworkOperation", Log.getStackTraceString(e));
			return "";
		} finally {
			if (urlConnection != null) urlConnection.disconnect();
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					Log.e("ErrorClosing", Log.getStackTraceString(e));
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(responseData != null)
			responseHandle.onResponse(responseData);
	}

	public ArrayList<ItemViewModel> getResponseData(String result) {
		ArrayList<ItemViewModel> data = new ArrayList<>();
		JSONObject response = Utils.parseJSON(result);
		JSONObject query = response.optJSONObject(QUERY);
		if(query != null) {
			JSONArray pages = query.optJSONArray(PAGES);
			if (pages != null)
				for (int i = 0; i < pages.length(); i++) {
					JSONObject object = pages.optJSONObject(i);
					SearchItem item = new SearchItem();
					ContentValues values = new ContentValues();
					item.setId(object.optInt(PAGEID));
					values.put(_ID,object.optInt(PAGEID));
					item.setName(object.optString(TITLE));
					values.put(NAME,object.optString(TITLE));
					JSONObject thumbnail = object.optJSONObject(THUMBNAIL);
					if(thumbnail != null) {
						item.setThumbnail(thumbnail.optString(SOURCE));
						values.put(THUMBNAIL,thumbnail.optString(SOURCE));
					}
					JSONObject terms = object.optJSONObject(TERMS);
					if(terms != null){
						JSONArray description = terms.optJSONArray(DESCRIPTION);
						StringBuilder descriptionText = new StringBuilder("");
						for (int j =0;j<description.length();j++)
							descriptionText.append(description.optString(j));
						item.setDescription(descriptionText.toString());
						values.put(DESCRIPTION,descriptionText.toString());
					}
					data.add(new ItemViewModel(item));
				}
		}
		return data;
	}
}
