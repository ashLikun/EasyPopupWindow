package com.ashlikun.easypopup.simple;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.ashlikun.easypopup.EasyPopup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EasyPopup popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        popup = new EasyPopup(this, R.layout.popup_view);
        popup.setWidth(1920);
        popup.setBackgroundAlpha(true, 1f);
        popup.create();
    }

    public void onClick(View view) {
        popup.showAsDropDown(view);
    }

    public void onClick2(View view) {

    }
}
