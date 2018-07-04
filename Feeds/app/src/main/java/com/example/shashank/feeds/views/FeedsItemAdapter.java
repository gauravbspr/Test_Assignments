package com.example.shashank.feeds.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.shashank.feeds.Presenter.ClickEvent;
import com.example.shashank.feeds.R;
import com.example.shashank.feeds.Utility.Utils;
import com.example.shashank.feeds.ViewModel.ItemViewModel;
import com.example.shashank.feeds.database.ItemDatabase;
import com.example.shashank.feeds.databinding.ItemBinding;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import static android.support.v7.widget.PopupMenu.OnMenuItemClickListener;

public class FeedsItemAdapter extends RecyclerView.Adapter<FeedsItemAdapter.ItemViewHolder>{

	private ImageLoader imageLoader;
	public ArrayList<ItemViewModel> data;
	private Activity activity;
	private LayoutInflater inflater;
	private ItemDatabase db;

	public FeedsItemAdapter(ArrayList<ItemViewModel> data, Activity activity) {
		this.data = data;
		this.activity = activity;
		imageLoader = Utils.getImageLoader(activity.getApplicationContext());
		db = ItemDatabase.getInstance(activity.getApplicationContext());
	}

	@NonNull
	@Override
	public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if(inflater == null)
			inflater = LayoutInflater.from(parent.getContext());
		ItemBinding binding = ItemBinding.inflate(inflater,parent,false);
		return new ItemViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
		holder.bind(data.get(position),position);
	}

	@Override
	public int getItemCount() {
		if(data != null)
			return data.size();
		return 0;
	}

	class ItemViewHolder extends RecyclerView.ViewHolder{

		private ItemBinding itemBinding;
		public ItemViewHolder(ItemBinding itemBinging) {
			super(itemBinging.getRoot());
			this.itemBinding = itemBinging;
		}

		/*
		Binding feed data to view holder
		 */
		public void bind(final ItemViewModel item, final int position){
			itemBinding.setItem(item);
			itemBinding.setEvent(new ClickEvent() {
				@Override
				public void onClick() {

				}

				@Override
				public boolean onLongClick() {
					if(item.isLocal()) {
						handlePopup(itemBinding.getRoot(), position);
						return false;
					}
					return false;
				}
			});
			itemBinding.image.setImageBitmap(null);
			if(item.getThumbnail() != null && !item.getThumbnail().isEmpty()) {
				imageLoader.loadImage(item.getThumbnail(), new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						itemBinding.image.setImageResource(R.drawable.ic_launcher_foreground);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						itemBinding.image.setImageBitmap(loadedImage);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});
			}
		}

	}


	/*
		Displays popup to delete in long press
	 */
	private void handlePopup(View v,final int position) {
		PopupMenu popupMenu = new PopupMenu(activity, v);
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {


			@SuppressLint("StaticFieldLeak")
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				switch (arg0.getItemId()) {
					case R.id.item_delete:
						new AsyncTask<Void,Void,Void>(){
							@Override
							protected Void doInBackground(Void... voids) {
								db.itemDao().delete(data.get(position).getItem());
								return null;
							}

							@Override
							protected void onPostExecute(Void aVoid) {
								super.onPostExecute(aVoid);
								data.remove(position);
								notifyItemRemoved(position);
								notifyItemRangeChanged(position, data.size());
							}
						}.execute();
						return false;
					default:
						return false;
				}
			}

		});
		popupMenu.inflate(R.menu.popup_delete);
		popupMenu.show();
	}
}
