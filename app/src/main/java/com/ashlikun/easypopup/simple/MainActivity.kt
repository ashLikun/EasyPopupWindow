package com.ashlikun.easypopup.simple

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ashlikun.easypopup.EasyPopup

class MainActivity : AppCompatActivity() {
    var popup: EasyPopup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        popup = EasyPopup(this, R.layout.popup_view)
        popup!!.setWidth<EasyPopup>(1920)
        popup!!.setBackgroundAlpha<EasyPopup>(true, 1f)
        popup!!.create<EasyPopup>()
    }

    fun onClick(view: View?) {
        popup!!.showAsDropDown(view)
    }

    fun onClick2(view: View?) {}
}