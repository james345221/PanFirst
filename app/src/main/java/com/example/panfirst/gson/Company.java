package com.example.panfirst.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by asus-pc on 2017/6/5.
 */

public class Company {
//    public String reason;
//
//    public int resultcode;

    @SerializedName("result")
    public List<CompanyName> companyList;
}
