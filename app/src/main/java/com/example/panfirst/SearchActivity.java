package com.example.panfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String sel = intent.getStringExtra("sel");
        String num = intent.getStringExtra("num");
        String [] temp ;
        temp = sel.split("-");
        String a = temp[0];
        String b = temp[1];




    }
}
