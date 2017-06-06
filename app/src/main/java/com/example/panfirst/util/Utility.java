package com.example.panfirst.util;

import android.text.TextUtils;

import com.example.panfirst.gson.Company;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by asus-pc on 2017/6/5.
 */

public class Utility {
    /**
    *解析处理服务返回的数据
     */
    public static Company handleCompanyResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {

                return new Gson().fromJson(response,Company.class);

//                JSONObject jsonObject = new JSONObject(response);
//                JSONArray jsonArray = jsonObject.getJSONArray("result");
//                String company = jsonArray.getJSONObject(0).toString();
//                    return new Gson().fromJson(company, Company.class);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  null;
    }
}
