package com.ashlikun.easypopup

import android.R
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.core.widget.PopupWindowCompat

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/17　14:53
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：是对官方的PopupWindows的封装，使得使用方便
 */
class EasyPopup(
    /**
     * 获取context
     *
     * @return
     */
    //context
    val context: Context?, //contentView
    protected var mContentView: View?, //布局id
    @param:LayoutRes protected var mLayoutId: Int, //宽高
    protected var mWidth: Int, protected var mHeight: Int
) : PopupWindow.OnDismissListener {
    /**
     * 获取PopupWindow对象
     *
     * @return
     */
    //PopupWindow对象
    var popupWindow: PopupWindow? = null
        private set

    //获取焦点
    protected var mFocusable = true

    //是否触摸之外dismiss
    protected var mOutsideTouchable = true

    //弹出pop时，背景是否变暗
    protected var isBackgroundAlpha = false
    protected var mBackgroundAlpha = .6f

    //弹出时候的背景
    protected var backgoundView: View? = null
    protected var mAnimationStyle = 0
    private var mOnDismissListener: PopupWindow.OnDismissListener? = null
    private var mAnchorView: View? = null

    @VerticalGravity
    private var mVerticalGravity = VerticalGravity.BELOW

    @HorizontalGravity
    private var mHorizontalGravity = HorizontalGravity.LEFT
    private var mOffsetX = 0
    private var mOffsetY = 0

    //是否只是获取宽高
    //getViewTreeObserver监听时
    private var isOnlyGetWH = true

    @JvmOverloads
    constructor(
        context: Context?,
        @LayoutRes layoutId: Int,
        width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
        height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    ) : this(context, null, layoutId, width, height) {
    }

    @JvmOverloads
    constructor(contentView: View, width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT) : this(
        contentView.context,
        contentView,
        0,
        width,
        height
    ) {
    }

    //设置完参数调用得创建方法
    fun <T : EasyPopup?> create(): T {
        if (popupWindow == null) {
            popupWindow = PopupWindow()
        }
        onPopupWindowCreated()
        if (mContentView == null) {
            mContentView = if (mLayoutId != 0) {
                LayoutInflater.from(context).inflate(mLayoutId, null)
            } else {
                throw IllegalArgumentException("The content view is null")
            }
        }
        popupWindow!!.contentView = mContentView
        if (mWidth != 0) {
            popupWindow!!.width = mWidth
        } else {
            popupWindow!!.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        if (mHeight != 0) {
            popupWindow!!.height = mHeight
        } else {
            popupWindow!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        onPopupWindowViewCreated(mContentView)
        if (mAnimationStyle != 0) {
            popupWindow!!.animationStyle = mAnimationStyle
        }
        popupWindow!!.isFocusable = mFocusable
        popupWindow!!.isOutsideTouchable = mOutsideTouchable
        popupWindow!!.setBackgroundDrawable(if (mOutsideTouchable) ColorDrawable(Color.TRANSPARENT) else null)
        popupWindow!!.setOnDismissListener(this)
        return this as T
    }
    /****自定义生命周期方法 */
    /**
     * PopupWindow对象创建完成
     */
    protected fun onPopupWindowCreated() {}

    /**
     * contentView创建完成，这个时候就可以开启进入动画
     *
     * @param contentView
     */
    protected fun onPopupWindowViewCreated(contentView: View?) {}

    /**
     * contentView创建完成，这个时候就可以开启退出动画
     */
    protected fun onPopupWindowDismiss() {}

    /****设置属性方法 */
    fun <T : EasyPopup?> setWidth(width: Int): T {
        mWidth = width
        return this as T
    }

    fun <T : EasyPopup?> setHeight(height: Int): T {
        mHeight = height
        return this as T
    }

    fun <T : EasyPopup?> setAnchorView(view: View?): T {
        mAnchorView = view
        return this as T
    }

    fun <T : EasyPopup?> setVerticalGravity(@VerticalGravity verticalGravity: Int): T {
        mVerticalGravity = verticalGravity
        return this as T
    }

    fun <T : EasyPopup?> setHorizontalGravity(@VerticalGravity horizontalGravity: Int): T {
        mHorizontalGravity = horizontalGravity
        return this as T
    }

    fun <T : EasyPopup?> setOffsetX(offsetX: Int): T {
        mOffsetX = offsetX
        return this as T
    }

    fun <T : EasyPopup?> setOffsetY(offsetY: Int): T {
        mOffsetY = offsetY
        return this as T
    }

    fun <T : EasyPopup?> setAnimationStyle(@StyleRes animationStyle: Int): T {
        mAnimationStyle = animationStyle
        return this as T
    }

    /**
     * 设置按下消失
     */
    fun <T : EasyPopup?> setDismissWhenTouchOuside(dismissWhenTouchOuside: Boolean): T {
        mOutsideTouchable = dismissWhenTouchOuside
        mFocusable = dismissWhenTouchOuside
        return this as T
    }

    /**
     * 设置是否可以获取焦点
     */
    fun <T : EasyPopup?> setFocusable(mFocusable: Boolean): T {
        this.mFocusable = mFocusable
        return this as T
    }

    /**
     * 设置点击空白地方消失
     */
    fun <T : EasyPopup?> setOutsideTouchable(mOutsideTouchable: Boolean): T {
        this.mOutsideTouchable = mOutsideTouchable
        return this as T
    }

    fun <T : EasyPopup?> setBackgroundAlpha(backgroundDim: Boolean, mBackgroundAlpha: Float): T {
        isBackgroundAlpha = backgroundDim
        this.mBackgroundAlpha = mBackgroundAlpha
        return this as T
    }

    /**
     * 使用此方法需要在创建的时候调用setAnchorView()等属性设置{@see setAnchorView()}
     */
    fun showAsDropDown() {
        if (mAnchorView == null) {
            return
        }
        showAsDropDown(mAnchorView, mOffsetX, mOffsetY)
    }

    /**
     * PopupWindow自带的显示方法
     *
     * @param anchor
     * @param offsetX
     * @param offsetY
     */
    fun showAsDropDown(anchor: View?, offsetX: Int, offsetY: Int) {
        if (popupWindow != null) {
            isOnlyGetWH = true
            mAnchorView = anchor
            mOffsetX = offsetX
            mOffsetY = offsetY
            addGlobalLayoutListener(popupWindow!!.contentView)
            handleBackgroundAlpha(true)
            popupWindow!!.showAsDropDown(anchor, offsetX, offsetY)
        }
    }

    fun showAsDropDown(anchor: View?) {
        if (popupWindow != null) {
            mAnchorView = anchor
            isOnlyGetWH = true
            addGlobalLayoutListener(popupWindow!!.contentView)
            handleBackgroundAlpha(true)
            popupWindow!!.showAsDropDown(anchor)
        }
    }

    fun showAtLocation(parent: View?, gravity: Int, offsetX: Int, offsetY: Int) {
        if (popupWindow != null) {
            mAnchorView = parent
            mOffsetX = offsetX
            mOffsetY = offsetY
            isOnlyGetWH = true
            addGlobalLayoutListener(popupWindow!!.contentView)
            handleBackgroundAlpha(true)
            popupWindow!!.showAtLocation(parent, gravity, offsetX, offsetY)
        }
    }

    /**
     * 相对anchor view显示
     *
     *
     * 使用此方法需要在创建的时候调用setAnchorView()等属性设置{@see setAnchorView()}
     */
    fun showAtAnchorView() {
        if (mAnchorView == null) {
            return
        }
        showAtAnchorView(mAnchorView!!, mVerticalGravity, mHorizontalGravity)
    }
    /**
     * 相对anchor view显示，适用 宽高不为match_parent
     *
     * @param anchor
     * @param vertGravity  垂直方向的对齐方式
     * @param horizGravity 水平方向的对齐方式
     * @param x            水平方向的偏移
     * @param y            垂直方向的偏移
     */
    /**
     * 相对anchor view显示，适用 宽高不为match_parent
     *
     * @param anchor
     * @param vertGravity
     * @param horizGravity
     */
    @JvmOverloads
    fun showAtAnchorView(anchor: View, @VerticalGravity vertGravity: Int, @HorizontalGravity horizGravity: Int, x: Int = 0, y: Int = 0) {
        var x = x
        var y = y
        if (popupWindow == null) {
            return
        }
        mAnchorView = anchor
        mOffsetX = x
        mOffsetY = y
        mVerticalGravity = vertGravity
        mHorizontalGravity = horizGravity
        isOnlyGetWH = false
        val contentView = mContentView
        addGlobalLayoutListener(contentView)
        contentView!!.measure(0, View.MeasureSpec.UNSPECIFIED)
        val measuredW = contentView.measuredWidth
        val measuredH = contentView.measuredHeight
        x = calculateX(anchor, horizGravity, measuredW, x)
        y = calculateY(anchor, vertGravity, measuredH, y)
        handleBackgroundAlpha(true)
        PopupWindowCompat.showAsDropDown(popupWindow!!, anchor, x, y, Gravity.NO_GRAVITY)
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
                //                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//                if (isShow && mWindowAlphaOrgin == DEFAULT_WINDOW_ALPHA_ORGIN) {
//                    mWindowAlphaOrgin = lp.alpha;
//                }
////                lp.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                if (isShow) {
//                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                } else {
//                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                }
//                lp.alpha = isShow ? mBackgroundAlpha : mWindowAlphaOrgin;
//                activity.getWindow().setAttributes(lp);
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
     *
     * @param anchor
     * @param vertGravity
     * @param measuredH
     * @param y
     * @return
     */
    private fun calculateY(anchor: View, vertGravity: Int, measuredH: Int, y: Int): Int {
        var y = y
        when (vertGravity) {
            VerticalGravity.ABOVE ->                 //anchor view之上
                y -= measuredH + anchor.height
            VerticalGravity.ALIGN_BOTTOM ->                 //anchor view底部对齐
                y -= measuredH
            VerticalGravity.CENTER ->                 //anchor view垂直居中
                y -= anchor.height / 2 + measuredH / 2
            VerticalGravity.ALIGN_TOP ->                 //anchor view顶部对齐
                y -= anchor.height
            VerticalGravity.BELOW -> {}
        }
        return y
    }

    /**
     * 根据水平gravity计算x偏移
     *
     * @param anchor
     * @param horizGravity
     * @param measuredW
     * @param x
     * @return
     */
    private fun calculateX(anchor: View, horizGravity: Int, measuredW: Int, x: Int): Int {
        var x = x
        when (horizGravity) {
            HorizontalGravity.LEFT ->                 //anchor view左侧
                x -= measuredW
            HorizontalGravity.ALIGN_RIGHT ->                 //与anchor view右边对齐
                x -= measuredW - anchor.width
            HorizontalGravity.CENTER ->                 //anchor view水平居中
                x += anchor.width / 2 - measuredW / 2
            HorizontalGravity.ALIGN_LEFT -> {}
            HorizontalGravity.RIGHT ->                 //anchor view右侧
                x += anchor.width
        }
        return x
    }

    /**
     * 更新PopupWindow位置，校验PopupWindow位置
     * 修复高度或者宽度写死时或者内部有ScrollView时，弹出的位置不准确问题
     *
     * @param width
     * @param height
     * @param anchor
     * @param vertGravity
     * @param horizGravity
     * @param x
     * @param y
     */
    private fun updateLocation(
        width: Int,
        height: Int,
        anchor: View,
        @VerticalGravity vertGravity: Int,
        @HorizontalGravity horizGravity: Int,
        x: Int,
        y: Int
    ) {
        var x = x
        var y = y
        x = calculateX(anchor, horizGravity, width, x)
        y = calculateY(anchor, vertGravity, height, y)
        popupWindow!!.update(anchor, x, y, width, height)
    }

    //监听器，用于PopupWindow弹出时获取准确的宽高
    private val mOnGlobalLayoutListener: OnGlobalLayoutListener = object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            mWidth = contentView.getWidth()
            mHeight = contentView.getHeight()
            //只获取宽高时，不执行更新操作
            if (isOnlyGetWH) {
                removeGlobalLayoutListener()
                return
            }
            if (popupWindow == null) {
                return
            }
            updateLocation(mWidth, mHeight, mAnchorView!!, mVerticalGravity, mHorizontalGravity, mOffsetX, mOffsetY)
            removeGlobalLayoutListener()
        }
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    fun <T : EasyPopup?> setOnDismissListener(listener: PopupWindow.OnDismissListener?): T {
        mOnDismissListener = listener
        return this as T
    }

    /**
     * 获取PopupWindow中加载的view
     *
     * @return
     */
    val contentView: View?
        get() = if (popupWindow != null) {
            popupWindow!!.contentView
        } else {
            null
        }

    /**
     * 获取view
     *
     * @param viewId
     * @param <T>
     * @return
    </T> */
    fun <T : View?> getView(@IdRes viewId: Int): T? {
        var view: View? = null
        if (mContentView != null) {
            view = mContentView!!.findViewById(viewId)
        }
        return view as T?
    }

    /**
     * 消失
     */
    fun dismiss() {
        if (popupWindow != null) {
            popupWindow!!.dismiss()
        }
    }

    override fun onDismiss() {
        handleDismiss()
    }

    /**
     * PopupWindow消失后处理一些逻辑
     */
    private fun handleDismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener!!.onDismiss()
        }
        removeGlobalLayoutListener()
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
        }
        onPopupWindowDismiss()
        handleBackgroundAlpha(false)
    }

    private fun addGlobalLayoutListener(contentView: View?) {
        contentView!!.viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    private fun removeGlobalLayoutListener() {
        if (mContentView != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                mContentView!!.viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
            } else {
                mContentView!!.viewTreeObserver.removeGlobalOnLayoutListener(mOnGlobalLayoutListener)
            }
        }
    }
}