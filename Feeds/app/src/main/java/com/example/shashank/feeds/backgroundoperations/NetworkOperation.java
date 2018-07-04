package com.example.shashank.feeds.backgroundoperations;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.shashank.feeds.Utility.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.shashank.feeds.Utility.AppConstants.DESCRIPTION;
import static com.example.shashank.feeds.Utility.AppConstants.IMAGE_HREF;
import static com.example.shashank.feeds.Utility.AppConstants.TITLE;
import static com.example.shashank.feeds.Utility.AppConstants.ROWS;
import static com.example.shashank.feeds.Utility.AppConstants.THUMBNAIL;
import static com.example.shashank.feeds.provider.CustomContentProvider.URI_FEED_ITEM;

public class NetworkOperation extends AsyncTask<String,Void,String> {

	private Context context;
	private ResponseHandle callback;

	public NetworkOperation(Context context,ResponseHandle responseHandle){
		this.context = context;
		this.callback = responseHandle;
	}

	@Override
	protected String doInBackground(String... params) {
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String api_url = params[0];
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
			return getResponseData(response);
		} catch (IOException e) {
			Log.e("NetworkOperation", Log.getStackTraceString(e));
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
		callback.onResponse(result);
	}

	public String getResponseData(String result) {
		JSONObject response = Utils.parseJSON(result);
		String title = response.optString(TITLE);
		JSONArray rows = response.optJSONArray(ROWS);
		if (rows != null)
			for (int i = 0; i < rows.length(); i++) {
				JSONObject object = rows.optJSONObject(i);
				ContentValues values = new ContentValues();
				values.put(TITLE,object.optString(TITLE));
				values.put(THUMBNAIL,object.optString(IMAGE_HREF));
				values.put(DESCRIPTION,object.optString(DESCRIPTION));
				context.getContentResolver().insert(URI_FEED_ITEM,values);
			}
		return title;
	}
}
