package com.ashlikun.easypopup.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ashlikun.easypopup.BaseEasyPopup
import com.ashlikun.easypopup.simple.databinding.PopupViewBinding

class MainActivity : AppCompatActivity() {
    val popBinding by lazy {
        PopupViewBinding.inflate(LayoutInflater.from(this))
    }
    val popup: BaseEasyPopup by lazy {
        BaseEasyPopup(
            this, width = dip2px(180f), maxHeight = dip2px(280f), dimAmount = 0.6f, layouView = popBinding.root
        ){

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    fun onClick(view: View) {
        popup!!.show(view)
    }

    fun dip2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun onClick2(view: View) {}
    fun onClick3(view: View) {
        popup!!.show(view)
    }
}