package com.dyt.mytopnews.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.android.swipe.SmartSwipeRefresh;
import com.billy.android.swipe.consumer.SlidingConsumer;
import com.dyt.mytopnews.MyApplication;
import com.dyt.mytopnews.R;
import com.dyt.mytopnews.adapter.NewsListAdapter;
import com.dyt.mytopnews.gson.Data;
import com.dyt.mytopnews.gson.News;
import com.dyt.mytopnews.util.HttpUtils;
import com.dyt.mytopnews.util.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class FirstFragment extends Fragment {
    private static final String address = "https://www.tophub.fun:8888/GetRandomInfo?is_follow=0&time=";
    private static final String TAG = "FirstFragment";
    private RecyclerView mRecyclerView;
    private TextView title;
    private BottomNavigationView mBottomNavigationView;
    private List<Data> mDataList = new ArrayList<>();
    private NewsListAdapter mAdapter;
    private Integer pageNumber = 0;
    private LinearLayoutManager manager;
    private boolean isLoading;
    private SmartSwipeRefresh mSmartSwipeRefresh;
    private FirstFragmentViewModel viewModel;

    public FirstFragment() {
        // Required empty public constructor
    }

    //请求新闻的方法
    private void loadNews(Integer pageNumber, final SmartSwipeRefresh ssr) {
        HttpUtils.getNewsList(address + pageNumber, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "加载失败，请检查你的网络状态", Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        ssr.finished(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: 返回的数据是：" + responseData);
                News news = Utility.handleNewsResponse(responseData);
                List<Data> dataList = news.getData();
                mDataList.addAll(dataList);
                viewModel.mData = mDataList;
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MyApplication.getContext(), "加载成功", Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        ssr.finished(true);
                    }
                });
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        mRecyclerView = view.findViewById(R.id.news_list_recycler_view);
        title = view.findViewById(R.id.title);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置ViewModel初始化数据
        viewModel = ViewModelProviders.of(requireActivity()).get(FirstFragmentViewModel.class);
        mDataList = viewModel.mData;
        //设置列表样式和数据
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewsListAdapter(mDataList);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        layoutManager.scrollToPosition(viewModel.firstVisibleItem);
        //设置点击顶部返回事件
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.scrollToPosition(0);
            }
        });
        //设置底栏滚动自动隐藏
        mBottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0) {
                    mBottomNavigationView.animate().translationY(mBottomNavigationView.getHeight()).setDuration(500).start();
                } else {
                    mBottomNavigationView.animate().translationY(0).setDuration(500).start();
                }
                //记录列表阅读位置
                viewModel.firstVisibleItem = manager.findFirstVisibleItemPosition();
            }
        });
        SmartSwipeRefresh.SmartSwipeRefreshDataLoader loader = new SmartSwipeRefresh.SmartSwipeRefreshDataLoader() {
            @Override
            public void onRefresh(SmartSwipeRefresh ssr) {
                mDataList.clear();
                mAdapter.notifyDataSetChanged();
                loadNews(pageNumber, ssr);
            }

            @Override
            public void onLoadMore(SmartSwipeRefresh ssr) {
                loadNews(++pageNumber, ssr);
            }
        };
        //设置刷新样式
        mSmartSwipeRefresh = SmartSwipeRefresh.behindMode(mRecyclerView, false).setDataLoader(loader);
        mSmartSwipeRefresh.getSwipeConsumer().as(SlidingConsumer.class).setEdgeAffinity(true);
        Boolean isFirstEnter = viewModel.isFirstEnter;
        if (isFirstEnter) {
            mSmartSwipeRefresh.startRefresh();
            viewModel.isFirstEnter = false;
        }

    }


}



