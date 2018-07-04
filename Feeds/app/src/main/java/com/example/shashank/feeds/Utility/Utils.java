package com.example.shashank.feeds.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

	private static ImageLoader imageLoader;

	public static ImageLoader getImageLoader(Context ctx) {
		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).resetViewBeforeLoading(true)
					.considerExifParams(true).displayer(new FadeInBitmapDisplayer(200, true, true, false))
					.build();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).
					memoryCache(new WeakMemoryCache())
					.diskCacheSize(100 * 1024 * 1024)
					.defaultDisplayImageOptions(defaultOptions).build();
			imageLoader.init(config);
		}
		return imageLoader;
	}


	public static JSONObject parseJSON(String str){
		JSONObject object;
		try {
			object = new JSONObject(str);
		}catch (JSONException e){
			object = new JSONObject();
		}
		return object;
	}

	public static boolean networkAvailable(Context context) {

		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager
					.getActiveNetworkInfo();
			return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
