package com.example.panfirst;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panfirst.gson.Company;
import com.example.panfirst.gson.CompanyName;
import com.example.panfirst.util.HttpUtil;
import com.example.panfirst.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by asus-pc on 2017/6/5.
 */

public class ChooseAreaFragment extends Fragment{

    private TextView textView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dateList = new ArrayList<>();
    private List<CompanyName> companyNameList;
    private CompanyName selectedCompany;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        textView = (TextView) view.findViewById(R.id.title_text);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,dateList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String CompanyName = companyNameList.get(position).getCom();
                Toast.makeText(getActivity(),CompanyName,Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String companyString = prefs.getString("company",null);
        if(companyString!=null){
            //有缓存时直接解析公司数据
            Company company = Utility.handleCompanyResponse(companyString);
            queryCompanies(company);
        }else {
            //无缓存时去服务器查询公司
//            String address = "http://v.juhe.cn/exp/com?key=0d3a9f102a6ccc3627129e5f3522893e";
            String address = "http://10.0.2.2:8087/company.json";
            queryFromService(address);
        }

    }

//    public void queryCompanies(){
//        textView.setText("选择快递公司");
//
//        if(companyNameList.size()>0){
//            dateList.clear();
//            for(CompanyName companyName : companyNameList){
//                dateList.add(companyName.getCom());
//            }
//            adapter.notifyDataSetChanged();
//            listView.setSelection(0);
//        }else{
//            String addresss = "http://v.juhe.cn/exp/com?key=0d3a9f102a6ccc3627129e5f3522893e" ;
//            queryFromService(addresss);
//        }
//
//    }

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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(company != null ){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                            editor.putString("company",responseText);
                            editor.apply();
                            queryCompanies(company);
                        }else {
                            Toast.makeText(getActivity(),"获取company失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void queryCompanies(Company company) {
        textView.setText("选择快递公司");

        companyNameList = company.companyList;
        for (CompanyName companyName : companyNameList){
            dateList.add(companyName.getCom());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);

    }
}
