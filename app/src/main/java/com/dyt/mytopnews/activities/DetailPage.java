package com.dyt.mytopnews.activities;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dyt.mytopnews.MyApplication;
import com.dyt.mytopnews.R;
import com.dyt.mytopnews.gson.MyNewsKeep;

import androidx.annotation.Nullable;

public class DetailPage extends BaseWebActivity {

    private String url;
    private Intent intent;
    private static final String TAG = "DetailPage";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        intent = getIntent();
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getUrl() {
        Log.d(TAG, "getUrl: 进入网址过滤");
        url = intent.getStringExtra("url");
        if (!url.startsWith("http")) {
            StringBuilder sb = new StringBuilder(url);
            sb.insert(0, "http:");
            return sb.toString();
        }
        return url;

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.browsermenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.keep_news:
                keepnews();
                break;
            case R.id.copy_link:
                copyLink();
                break;
            case R.id.jumpToBrowser:
                jumpToBrowser();
                break;
            case R.id.share:
                share();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        String url = intent.getStringExtra("url");
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.setType("text/plian");
        intent1.putExtra(Intent.EXTRA_TEXT, url);
        Intent intent2 = Intent.createChooser(intent1, "选择分享的应用");
        startActivity(intent2);
    }

    private void jumpToBrowser() {
        String url = intent.getStringExtra("url");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    private void copyLink() {
        String url = intent.getStringExtra("url");
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(url);
        Toast.makeText(MyApplication.getContext(), "复制成功！", Toast.LENGTH_SHORT).show();
    }

    private void keepnews() {
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");
        MyNewsKeep myNewsKeep = new MyNewsKeep();
        myNewsKeep.setTitle(title);
        myNewsKeep.setUrl(url);
        myNewsKeep.setType(type);
        boolean isSave = myNewsKeep.save();
        if(isSave){
            Toast.makeText(MyApplication.getContext(),"收藏成功！",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MyApplication.getContext(),"收藏失败！",Toast.LENGTH_SHORT).show();
        }
    }
}

