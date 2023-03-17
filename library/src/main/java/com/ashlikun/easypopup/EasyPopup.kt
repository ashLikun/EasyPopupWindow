package com.ashlikun.easypopup

import android.R
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.IdRes

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/17　14:53
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：是对官方的PopupWindows的封装，使得使用方便
 */
open class EasyPopup(
    //context
    open val context: Context,
    //布局
    open var layouView: View? = null,
    //布局文件
    open val layoutId: Int? = null,
    open var width: Int,
    open var height: Int,
    open var anchorView: View? = null,
    @VerticalGravity open var verticalGravity: Int = VerticalGravity.BELOW,
    @HorizontalGravity open var horizontalGravity: Int = HorizontalGravity.LEFT,
    open var offsetX: Int = 0,
    open var offsetY: Int = 0,
    open val animationStyle: Int? = null,
    //是否触摸之外dismiss
    open var outsideTouchable: Boolean = true,

    //弹出时候的背景
    open var backgoundView: View? = null,
    //弹出pop时，背景是否变暗
    open var isBackgroundAlpha: Boolean = false,
    open var mBackgroundAlpha: Float = .6f,
    //获取焦点
    open var focusable: Boolean = true,
    open var onDismissListener: PopupWindow.OnDismissListener? = null
) : PopupWindow.OnDismissListener {
    //PopupWindow对象
    val popupWindow: PopupWindow by lazy {
        PopupWindow()
    }

    //是否只是获取宽高
    //getViewTreeObserver监听时
    private var isOnlyGetWH = true

    init {
        init()
    }

    /**
     * 在构造函数外设置属性，请从新初始化
     */
    fun init() {
        if (layouView == null) {
            if (layoutId != null) {
                layouView = LayoutInflater.from(context).inflate(layoutId!!, null)
            } else {
                throw IllegalArgumentException("The content view is null")
            }
        }
        popupWindow.contentView = layouView
        onPopupWindowCreated(layouView!!)
        if (width != 0) {
            popupWindow.width = width
        } else {
            popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        if (height != 0) {
            popupWindow.height = height
        } else {
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        if (animationStyle != null) {
            popupWindow.animationStyle = animationStyle!!
        }
        popupWindow.isFocusable = focusable
        popupWindow.isOutsideTouchable = outsideTouchable
        popupWindow.setBackgroundDrawable(if (outsideTouchable) ColorDrawable(Color.TRANSPARENT) else null)
        popupWindow.setOnDismissListener(this)

    }

    /**
     * contentView创建完成，这个时候就可以开启进入动画
     */
    protected fun onPopupWindowCreated(contentView: View) {}

    /**
     * contentView创建完成，这个时候就可以开启退出动画
     */
    protected fun onPopupWindowDismiss() {}


    /**
     * 设置按下消失
     */
    fun setDismissWhenTouchOuside(dismissWhenTouchOuside: Boolean) {
        outsideTouchable = dismissWhenTouchOuside
        focusable = dismissWhenTouchOuside
    }


    fun show(
        anchor: View,
        gravity: Int? = null,
        offsetX: Int? = null,
        offsetY: Int? = null,
        @VerticalGravity vertGravity: Int? = null,
        @HorizontalGravity horizGravity: Int? = null
    ) {
        anchorView = anchor
        if (offsetX != null) {
            this.offsetX = offsetX
        }
        if (offsetY != null) {
            this.offsetY = offsetY
        }
        if (vertGravity != null) {
            verticalGravity = vertGravity
        }
        if (horizGravity != null) {
            horizontalGravity = horizGravity
        }

        isOnlyGetWH = true
        layouView!!.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        handleBackgroundAlpha(true)
        if (gravity != null) {
            popupWindow.showAtLocation(anchor, gravity, offsetX ?: 0, offsetX ?: 0)
        } else {
            if (offsetX != null || offsetY != null) {
                popupWindow.showAsDropDown(anchorView, offsetX ?: 0, offsetX ?: 0)
            } else {
                popupWindow.showAsDropDown(anchor)
            }
        }

    }


    /**
     * 处理背景变暗
     *
     * @param isShow 是否是弹出时候调用的
     */
    private fun handleBackgroundAlpha(isShow: Boolean) {
        if (isBackgroundAlpha) {
            val activity = getActivity(context)
            if (activity != null) {
                val decorView = activity.window.decorView
                val rootView = decorView.findViewById<View>(R.id.content) as FrameLayout
                if (isShow) {
                    backgoundView = View(activity)
                    //设置宽高为全屏
                    val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                    backgoundView!!.layoutParams = layoutParams
                    //设置背景颜色为黑色，加上透明度，就会有半透明的黑色蒙版效果
                    backgoundView!!.setBackgroundColor(-0x1000000)
                    //1.0f 不透明/0.0f 透明
                    backgoundView!!.alpha = mBackgroundAlpha
                    rootView.addView(backgoundView)
                } else {
                    rootView.removeView(backgoundView)
                }
            }
        }
    }

    /**
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    private fun getActivity(context: Context?): Activity? {
        if (context == null) {
            return null
        }
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return getActivity(context.baseContext)
        }
        return null
    }

    /**
     * 根据垂直gravity计算y偏移
     */
    private fun calculateY(anchor: View, vertGravity: Int, measuredH: Int, y: Int): Int {
        var y = y
        when (vertGravity) {
            //anchor view之上
            VerticalGravity.ABOVE -> y -= measuredH + anchor.height
            //anchor view底部对齐
            VerticalGravity.ALIGN_BOTTOM -> y -= measuredH
            //anchor view垂直居中
            VerticalGravity.CENTER -> y -= anchor.height / 2 + measuredH / 2
            //anchor view顶部对齐
            VerticalGravity.ALIGN_TOP -> y -= anchor.height
            VerticalGravity.BELOW -> {}
        }
        return y
    }

    /**
     * 根据水平gravity计算x偏移
     */
    private fun calculateX(anchor: View, horizGravity: Int, measuredW: Int, x: Int): Int {
        var x = x
        when (horizGravity) {
            //anchor view左侧
            HorizontalGravity.LEFT -> x -= measuredW
            //与anchor view右边对齐
            HorizontalGravity.ALIGN_RIGHT -> x -= measuredW - anchor.width
            //anchor view水平居中
            HorizontalGravity.CENTER -> x += anchor.width / 2 - measuredW / 2
            HorizontalGravity.ALIGN_LEFT -> {}
            //anchor view右侧
            HorizontalGravity.RIGHT -> x += anchor.width
        }
        return x
    }


    //监听器，用于PopupWindow弹出时获取准确的宽高
    private val onGlobalLayoutListener: OnGlobalLayoutListener = object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            width = layouView!!.getWidth()
            height = layouView!!.getHeight()
            //只获取宽高时，不执行更新操作
            if (isOnlyGetWH) {
                removeGlobalLayoutListener()
                return
            }
            if (popupWindow == null) {
                return
            }
            /**
             * 更新PopupWindow位置，校验PopupWindow位置
             * 修复高度或者宽度写死时或者内部有ScrollView时，弹出的位置不准确问题
             */
            val x = calculateX(anchorView!!, horizontalGravity, width, offsetX)
            val y = calculateY(anchorView!!, verticalGravity, height, offsetY)
            popupWindow.update(anchorView!!, x, y, width, height)
            removeGlobalLayoutListener()
        }
    }

    /**
     * 获取view
    </T> */
    fun <T : View?> getView(@IdRes viewId: Int) = layouView!!.findViewById(viewId) as? T

    /**
     * 消失
     */
    fun dismiss() {
        popupWindow.dismiss()
    }

    override fun onDismiss() {
        handleDismiss()
    }

    /**
     * PopupWindow消失后处理一些逻辑
     */
    private fun handleDismiss() {
        onDismissListener?.onDismiss()
        removeGlobalLayoutListener()
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
        onPopupWindowDismiss()
        handleBackgroundAlpha(false)
    }

    private fun removeGlobalLayoutListener() {
        if (layouView != null) {
            layouView!!.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }
}