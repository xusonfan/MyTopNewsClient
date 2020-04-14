package com.dyt.mytopnews.fragments;

import com.dyt.mytopnews.gson.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;

public class FirstFragmentViewModel extends ViewModel {
    List<Data> mData = new ArrayList<>();
    Boolean isFirstEnter = true;
    Integer firstVisibleItem = 0;
}
