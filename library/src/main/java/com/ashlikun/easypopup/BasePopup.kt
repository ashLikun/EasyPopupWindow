package com.ashlikun.easypopup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.*
import android.view.View.OnTouchListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.PopupWindow
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/17　14:53
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：是对官方的PopupWindows的封装，使得使用方便
 */
open class BasePopup(
    //context
    open val context: Context,
    //布局
    open var layouView: View? = null,
    //布局文件
    open val layoutId: Int? = null,
    open var width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    open var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    open var maxHeight: Int? = null,
    open var maxWidth: Int? = null,
    open var anchorView: View? = null,
    open val animStyle: Int? = R.style.EasyPopup_Anim_TopToBottom,
    //是否触摸之外dismiss
    open var outsideTouchable: Boolean = true,
    //弹出pop时，背景是否变暗,null：不操作
    open var dimAmount: Float? = .6f,
    //获取焦点
    open var focusable: Boolean = true,
    //偏移量
    open var offsetX: Int = 0,
    open var offsetY: Int = 0,
    //相对于描点的方向
    open var direction: Int = PopupDirection.AUTO_TOP_BOTTOM,
    open var gravity: Int = Gravity.CENTER,
    val isCallCreate: Boolean = true,
    open var onDismissListener: PopupWindow.OnDismissListener? = null
) {
    companion object {
        fun dip2px(context: Context, dipValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }
    }

    //PopupWindow对象
    val popupWindow: PopupWindow by lazy {
        PopupWindow(context)
    }
    val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    val decorRootView by lazy {
        DecorRootView(layouView!!.context, this).also {
            it.contentView = layouView!!
        }
    }

    init {
        if (isCallCreate) {
            onCreate()
        }
    }


    private val mOutsideTouchDismissListener = OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_OUTSIDE) {
            popupWindow.dismiss()
            return@OnTouchListener true
        }
        false
    }

    /**
     * 在构造函数外设置属性，请从新初始化
     */
    open protected fun onCreate() {
        if (layouView == null) {
            if (layoutId != null) {
                layouView = LayoutInflater.from(context).inflate(layoutId!!, null)
            } else {
                throw IllegalArgumentException("The content view is null")
            }
        }
        popupWindow.contentView = decorRootView
        onPopupWindowCreated(decorRootView)
        popupWindow.isTouchable = true
        popupWindow.isFocusable = focusable
        popupWindow.isOutsideTouchable = outsideTouchable
        if (outsideTouchable) {
            popupWindow.setTouchInterceptor(mOutsideTouchDismissListener)
        } else {
            popupWindow.setTouchInterceptor(null)
        }
        popupWindow.setBackgroundDrawable(if (outsideTouchable) ColorDrawable(Color.TRANSPARENT) else null)
        popupWindow.setOnDismissListener {
            handleDismiss()
        }
    }


    /**
     * contentView创建完成，这个时候就可以开启进入动画
     */
    open protected fun onPopupWindowCreated(contentView: View) {}

    /**
     * contentView创建完成，这个时候就可以开启退出动画
     */
    open protected fun onPopupWindowDismiss() {}

    /**
     * 设置Window参数
     */
    open protected fun onWindowLayoutParams(lp: WindowManager.LayoutParams) {}

    /**
     * 设置按下消失
     */
    open fun setDismissWhenTouchOuside(dismissWhenTouchOuside: Boolean) {
        outsideTouchable = dismissWhenTouchOuside
        focusable = dismissWhenTouchOuside
    }


    open fun show(anchor: View) {
        if (isShowing()) return
        val showInfo = ShowInfo(anchor, this)
        anchorView = anchor
        layouView!!.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        //测量布局
        showInfo.measureLayoutView()
        //从新设置宽高
        showInfo.reSetSize()
        showInfo.calculateXY()
        onShowInfoCreate(showInfo)
        decorRootView.showInfo = showInfo
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, offsetX + showInfo.x, offsetY + showInfo.y)
        //创建完成处理背景变暗
        updateDimAmount()
    }

    open fun getDecorView() = runCatching {
        if (popupWindow.background == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) popupWindow.contentView.parent as View else popupWindow.contentView
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) popupWindow.contentView.parent.parent as View else popupWindow.contentView.parent as View
        }
    }.getOrNull()

    /**
     * 当ShowInfo创建后
     */
    protected open fun onShowInfoCreate(showInfo: ShowInfo) {
        popupWindow.animationStyle = animStyle ?: -1
    }

    protected open fun updateDimAmount() {
        if (dimAmount != null) {
            getDecorView()?.also {
                val p = it.layoutParams as WindowManager.LayoutParams
                p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
                p.dimAmount = dimAmount!!
                //全屏Flag
                p.flags = p.flags or (WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR)
                onWindowLayoutParams(p)
                windowManager.updateViewLayout(it, p)
            }
        }
    }


    open fun isShowing() = popupWindow.isShowing

    //监听器，用于PopupWindow弹出时获取准确的宽高
    private val onGlobalLayoutListener: OnGlobalLayoutListener = object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
//            width = layouView!!.width
//            height = layouView!!.height
            //只获取宽高时，不执行更新操作
//            if (!isAutoUpdateLocation) {
//                removeGlobalLayoutListener()
//                return
//            }
//            /**
//             * 更新PopupWindow位置，校验PopupWindow位置
//             * 修复高度或者宽度写死时或者内部有ScrollView时，弹出的位置不准确问题
//             */
//            val offsetX = calculateX(anchorView!!, horizontalGravity, width, offsetX)
//            val offsetY = calculateY(anchorView!!, verticalGravity, height, offsetY)
//            popupWindow.update(anchorView!!, offsetX, offsetY, width, height)
//
//            removeGlobalLayoutListener()
        }
    }

    /**
     * 获取view
     * */
    fun <T : View?> getView(@IdRes viewId: Int) = layouView!!.findViewById(viewId) as? T

    /**
     * 消失
     */
    fun dismiss() {
        popupWindow.dismiss()
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
    }

    private fun removeGlobalLayoutListener() {
        if (layouView != null) {
            layouView!!.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }


}