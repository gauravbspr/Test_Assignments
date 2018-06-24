package com.example.shashank.wikisample.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shashank.wikisample.Presenter.ClickEvent;
import com.example.shashank.wikisample.Utility.Utils;
import com.example.shashank.wikisample.ViewModel.ItemViewModel;
import com.example.shashank.wikisample.WebViewActivity;
import com.example.shashank.wikisample.databinding.ItemBinding;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static com.example.shashank.wikisample.Utility.AppConstants.KEY;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ItemViewHolder>{

	private ImageLoader imageLoader;
	public ArrayList<ItemViewModel> data;
	private Context context;
	private LayoutInflater inflater;

	public SearchItemAdapter(ArrayList<ItemViewModel> data, Context context) {
		this.data = data;
		this.context = context;
		imageLoader = Utils.getImageLoader(context);
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
		holder.bind(data.get(position));
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

		public void bind(final ItemViewModel item){
			itemBinding.setItem(item);
			itemBinding.setEvent(new ClickEvent() {
				@Override
				public void onClick() {
					context.startActivity(new Intent(context, WebViewActivity.class).putExtra(KEY,item.toString()));
				}
			});


			if(item.getThumbnail() != null && !item.getThumbnail().isEmpty())
				imageLoader.displayImage(item.getThumbnail(),itemBinding.image);
		}

	}
}
