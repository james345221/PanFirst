package com.example.panfirst.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by asus-pc on 2017/6/6.
 */

public class Express {
    public String company;

    @SerializedName("com")
    public String shortName;

    @SerializedName("no")
    public String num;

    public String status;

    @SerializedName("list")
    public List<ExpressContent> expressContentList;

}
