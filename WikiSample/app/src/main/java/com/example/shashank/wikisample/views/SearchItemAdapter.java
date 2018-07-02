package com.example.shashank.wikisample.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.shashank.wikisample.Presenter.ClickEvent;
import com.example.shashank.wikisample.R;
import com.example.shashank.wikisample.Utility.Utils;
import com.example.shashank.wikisample.ViewModel.ItemViewModel;
import com.example.shashank.wikisample.WebViewActivity;
import com.example.shashank.wikisample.database.ItemDatabase;
import com.example.shashank.wikisample.databinding.ItemBinding;
import com.example.shashank.wikisample.model.SearchItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static android.support.v7.widget.PopupMenu.*;
import static com.example.shashank.wikisample.Utility.AppConstants.KEY;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ItemViewHolder>{

	private ImageLoader imageLoader;
	public ArrayList<ItemViewModel> data;
	private Activity activity;
	private LayoutInflater inflater;
	private ItemDatabase db;

	public SearchItemAdapter(ArrayList<ItemViewModel> data, Activity activity) {
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

		public void bind(final ItemViewModel item, final int position){
			itemBinding.setItem(item);
			itemBinding.setEvent(new ClickEvent() {
				@Override
				public void onClick() {
					activity.startActivity(new Intent(activity, WebViewActivity.class).putExtra(KEY,item.toString()));
				}

				@Override
				public boolean onLongClick() {
					Log.e("position-->",position+"");
					if(item.isLocal()) {
						handlePopup(itemBinding.getRoot(), position);
						return false;
					}
					return false;
				}
			});
			if(item.getThumbnail() != null && !item.getThumbnail().isEmpty())
				imageLoader.displayImage(item.getThumbnail(),itemBinding.image);
		}

	}

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
