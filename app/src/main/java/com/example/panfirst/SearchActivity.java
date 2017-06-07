package com.example.panfirst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.panfirst.gson.Company;
import com.example.panfirst.gson.CompanyName;
import com.example.panfirst.gson.Express;
import com.example.panfirst.gson.ExpressContent;
import com.example.panfirst.util.HttpUtil;
import com.example.panfirst.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private List<ExpressContent> mExpressContentList;
    private ExpressAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        String sel = intent.getStringExtra("sel");
        String num = intent.getStringExtra("num");
        String [] temp ;
        temp = sel.split("-");
        String comN = temp[0];
        String comS = temp[1];



        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView comImageView = (ImageView) findViewById(R.id.image_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        collapsingToolbarLayout.setTitle(comN+"-"+num);

        SharedPreferences prefss = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
        String expressString = prefss.getString("express",null);
        if(expressString != null){
            //有缓存时直接解析公司数据
            System.out.println("=====================================");
            Express express = Utility.handleExpressResponse(expressString);
            queryExpress(express);
            setAdapterIn();
        }else {
            //无缓存时去服务器查询公司
//            String address = "http://v.juhe.cn/exp/com?key=0d3a9f102a6ccc3627129e5f3522893e";
            System.out.println("=====================================");
            String address = "http://10.0.2.2:8087/express.json";
            queryFromService(address);
        }

        Glide.with(this).load(R.drawable.tt).into(comImageView);



//







    }

    private void queryFromService(final String address) {

        String addresss = address;

        HttpUtil.sendOkHttpRequest(addresss, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,"获取express失败，请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Express express =  Utility.handleExpressResponse(responseText);
                Log.d("SearchActivity",express.result.status);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(express != null){
                        if( "1".equals(express.result.status)){
                            SharedPreferences.Editor editors = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this).edit();
                            editors.putString("express",responseText);
                            editors.apply();
                            queryExpress(express);
                            setAdapterIn();
                        }else if("0".equals(express.result.status)){
                            Toast.makeText(SearchActivity.this,"获取express失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                        }
                        else {
                            Toast.makeText(SearchActivity.this,"获取express失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void queryExpress(Express express) {


        mExpressContentList = express.result.expressContentList;
//        for (CompanyName companyName : companyNameList){
//            dateList.add(companyName.getCom()+"-"+companyName.getNo());
//        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapterIn(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExpressAdapter(mExpressContentList);
        recyclerView.setAdapter(adapter);
    }


}
