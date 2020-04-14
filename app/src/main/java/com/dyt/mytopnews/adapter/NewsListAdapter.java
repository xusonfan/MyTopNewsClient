package com.dyt.mytopnews.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyt.mytopnews.R;
import com.dyt.mytopnews.activities.DetailPage;
import com.dyt.mytopnews.gson.Data;
import com.dyt.mytopnews.util.Utility;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<Data> mDataList;

    public NewsListAdapter(List<Data> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data data = mDataList.get(position);
        //设置标题
        String title = data.getTitle().trim().replaceAll(" ", "").replaceAll("\t", "");
        holder.news_title.setText(title);
        holder.news_title.getPaint().setFakeBoldText(true);//设置字号加粗
        //设置发布网站
        holder.news_type.setText(data.getType());
        //格式化传入的时间,并设置时间
        long l = Long.parseLong(data.getCreateTime());
        String day = Utility.parseTimeToDay(l);
        holder.news_createTime.setText(day);


        holder.news_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailPage.class);
                intent.putExtra("url", data.getUrl());
                intent.putExtra("title", data.getTitle());
                intent.putExtra("type", data.getType());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataList.get(position).hashCode();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView news_title;
        TextView news_createTime;
        TextView news_type;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            news_title = itemView.findViewById(R.id.news_title);
            news_createTime = itemView.findViewById(R.id.news_createTime);
            news_type = itemView.findViewById(R.id.news_type);
        }
    }
}
