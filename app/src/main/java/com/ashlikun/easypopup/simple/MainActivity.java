package com.ashlikun.easypopup.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ashlikun.easypopup.EasyPopup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        EasyPopup popup = new EasyPopup(this, R.layout.popup_view);
        popup.setHeight(500);
        popup.create();
        popup.setBackgroundAlpha(true,.4f);
        popup.showAsDropDown(view);
    }
}
