package com.example.shashank.wikisample.backgroundoperations;

import com.example.shashank.wikisample.ViewModel.ItemViewModel;

import java.util.ArrayList;

public interface ResponseHandle {

	void onResponse(ArrayList<ItemViewModel> data);
}
