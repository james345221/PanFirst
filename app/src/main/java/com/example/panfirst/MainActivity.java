package com.example.panfirst;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.panfirst.gson.Company;
import com.example.panfirst.gson.CompanyName;
import com.example.panfirst.util.HttpUtil;
import com.example.panfirst.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;

    private List<CompanyName> companyNameList;

    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private List<String> dateList = new ArrayList<>();
    private Button button;
    public String item_select;
    private EditText editText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (Spinner) findViewById(R.id.Spinner01);
        button = (Button) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.edit);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.sss);
        }

        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return  true;
            }
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String companyString = prefs.getString("company",null);
        if(companyString!=null){
            //有缓存时直接解析公司数据
            Company company = Utility.handleCompanyResponse(companyString);
            queryCompanies(company);
            setAdapterInSpinner();
        }else {
            //无缓存时去服务器查询公司
//            String address = "http://v.juhe.cn/exp/com?key=0d3a9f102a6ccc3627129e5f3522893e";
            String address = "http://10.0.2.2:8087/company.json";
            queryFromService(address);
        }



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item_select = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i = editText.getText().toString();
                if(item_select!=null && i!=null){
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);

                    intent.putExtra("sel",item_select);
                    intent.putExtra("num",i);
                    startActivity(intent);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    private void queryFromService(String addresss) {
        HttpUtil.sendOkHttpRequest(addresss, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Company company =  Utility.handleCompanyResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(company != null ){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString("company",responseText);
                            editor.apply();
                            queryCompanies(company);
                            setAdapterInSpinner();
                        }else {
                            Toast.makeText(MainActivity.this,"获取company失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void queryCompanies(Company company) {


        companyNameList = company.companyList;
        for (CompanyName companyName : companyNameList){
            dateList.add(companyName.getCom()+"-"+companyName.getNo());
        }




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    private void setAdapterInSpinner(){
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dateList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
    }
}
