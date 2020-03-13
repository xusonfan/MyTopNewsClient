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

    private static final int NORMAL_ITEM = 0;
    private static final int LAST_ITEM = 1;
    private List<Data> mDataList;

    public NewsListAdapter(List<Data> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMAL_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_card, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, parent, false);

        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            return;
        } else {
            final Data data = mDataList.get(position);
            //设置新闻序号
//            holder.news_id.setText(Integer.toString(position + 1));
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
//            holder.go_to_detail_news.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), DetailPage.class);
//                    intent.putExtra("url", data.getUrl());
//                    intent.putExtra("title", data.getTitle());
//                    intent.putExtra("type", data.getType());
//                    v.getContext().startActivity(intent);
//                }
//            });
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return LAST_ITEM;
        } else {
            return NORMAL_ITEM;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //        TextView news_id;
        TextView news_title;
        TextView news_createTime;
        TextView news_type;
//        ImageView go_to_detail_news;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
//            news_id = itemView.findViewById(R.id.news_id);
            news_title = itemView.findViewById(R.id.news_title);
            news_createTime = itemView.findViewById(R.id.news_createTime);
            news_type = itemView.findViewById(R.id.news_type);
//            go_to_detail_news = itemView.findViewById(R.id.go_to_detail_news);
        }
    }
}
