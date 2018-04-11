package com.ashlikun.easypopup.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ashlikun.easypopup.EasyPopup;

public class MainActivity extends AppCompatActivity {
    EasyPopup popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popup = new EasyPopup(this, R.layout.popup_view);
    }

    public void onClick(View view) {

    }

    public void onClick2(View view) {

    }
}
