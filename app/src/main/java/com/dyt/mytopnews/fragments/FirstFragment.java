package com.dyt.mytopnews.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dyt.mytopnews.MyApplication;
import com.dyt.mytopnews.R;
import com.dyt.mytopnews.Util.HttpUtils;
import com.dyt.mytopnews.Util.Utility;
import com.dyt.mytopnews.adapter.NewsListAdapter;
import com.dyt.mytopnews.gson.Data;
import com.dyt.mytopnews.gson.News;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    public static final String address = "https://www.tophub.fun:8888/GetRandomInfo?is_follow=0&time=";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FirstFragment";
    private RecyclerView mRecyclerView;
    private TextView title;
    private BottomNavigationView mBottomNavigationView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Data> mDataList = new ArrayList<>();
    private NewsListAdapter mAdapter;
    private Integer pageNumber = 1;

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
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void initNews() {
        HttpUtils.getNewsList(address + pageNumber, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "加载失败，请检查你的网络状态", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: 返回的数据是：" + responseData);
                News news = Utility.handleNewsResponse(responseData);
                List<Data> dataList = news.getData();
                for (Data data : dataList) {
                    mDataList.add(data);
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MyApplication.getContext(), "加载成功", Toast.LENGTH_SHORT).show();
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
        //初始化消息
        initNews();


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
        mRecyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0){
                    mBottomNavigationView.animate().translationY(mBottomNavigationView.getHeight()).setDuration(500).start();
                    Log.d(TAG, "onScrollChange: 滑动执行");
                }else {
                    mBottomNavigationView.animate().translationY(0).setDuration(500).start();
                }
            }
        });
    }
}
