package com.dyt.mytopnews.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    private static final String address = "https://www.tophub.fun:8888/GetRandomInfo?is_follow=0&time=";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FirstFragment";
    private RecyclerView mRecyclerView;
    private TextView title;
    private BottomNavigationView mBottomNavigationView;
    private List<Data> mDataList = new ArrayList<>();
    private NewsListAdapter mAdapter;
    private Integer pageNumber = 0;
    private LinearLayoutManager manager;
    private boolean isLoading;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        return new FirstFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化消息
        loadNews(pageNumber);


    }

    //请求新闻的方法
    private void loadNews(Integer pageNumber) {
        HttpUtils.getNewsList(address + pageNumber, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "加载失败，请检查你的网络状态", Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        mSwipeRefreshLayout.setRefreshing(false);
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
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MyApplication.getContext(), "加载成功", Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        mSwipeRefreshLayout.setRefreshing(false);

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
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        title = view.findViewById(R.id.title);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置列表样式和数据

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewsListAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);

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
//                    Log.d(TAG, "onScrollChange: 滑动执行");
                    //添加加载更多功能
                    int lastItemPosition = manager.findLastVisibleItemPosition();
                    Log.d(TAG, "onScrollChange: 最后一项位置" + lastItemPosition);
                    int count = manager.getItemCount();
                    Log.d(TAG, "onScrollChange: 得到了列表数" + count);
                    if (lastItemPosition == count - 1 & !isLoading) {
                        isLoading = true;
                        loadNews(++pageNumber);
                        Log.d(TAG, "onScrollChange: 加载的页面页数" + pageNumber);
                    }

                    //设置列表处于非顶端时底栏的图标置为刷新样式
                    int firstItemPosition = manager.findFirstVisibleItemPosition();
                    Log.d(TAG, "onScrollChange: 第一项的序号是" + firstItemPosition);
                    if (firstItemPosition != 0 & !isLoading) {
                        mBottomNavigationView
                                .getMenu()
                                .findItem(R.id.firstFragment)
                                .setIcon(R.drawable.ic_refresh_black_24dp)
                                .setTitle("刷新")
                                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        mSwipeRefreshLayout.setRefreshing(true);
                                        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host);
                                        controller.navigate(R.id.firstFragment);
                                        if (item.getItemId() == R.id.firstFragment) {
                                            Timer timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    mDataList.clear();
                                                    loadNews(pageNumber);
                                                }
                                            }, 1000);
                                            mRecyclerView.scrollToPosition(0);
                                            mBottomNavigationView
                                                    .getMenu()
                                                    .findItem(R.id.firstFragment)
                                                    .setIcon(R.drawable.ic_fiber_new_black_24dp)
                                                    .setTitle("新闻");
                                            return true;
                                        }
                                        return true;
                                    }
                                });

                    }
                } else {
                    mBottomNavigationView.animate().translationY(0).setDuration(500).start();
                }
            }
        });

        //设置下拉更新列表操作
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mDataList.clear();
                        loadNews(0);
                    }
                }, 1000);
            }
        });

    }
}



