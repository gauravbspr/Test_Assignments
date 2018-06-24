package com.example.shashank.wikisample.Utility;

import android.content.Context;
import android.graphics.Bitmap;

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
					.cacheInMemory(true)  .cacheOnDisk(true)  .bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)  .resetViewBeforeLoading(true)
					.considerExifParams(true) .displayer(new FadeInBitmapDisplayer(100, true, true, false))
					.build();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration  .Builder(ctx)  .memoryCache(new WeakMemoryCache())  .defaultDisplayImageOptions(defaultOptions)  .build();
			imageLoader.init(config);
		}else{
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true)  .cacheOnDisk(true)  .bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)  .resetViewBeforeLoading(true)
					.considerExifParams(true) .displayer(new FadeInBitmapDisplayer(250, true, true, false))
					.build();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration  .Builder(ctx)  .memoryCache(new WeakMemoryCache())  .defaultDisplayImageOptions(defaultOptions)  .build();
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
}
