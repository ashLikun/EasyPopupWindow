package com.ashlikun.easypopup

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlin.math.max
import kotlin.math.min

/**
 * 作者　　: 李坤
 * 创建时间: 2023/3/24　13:50
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class ShowInfo(anchor: View, val popup: BasePopup) {

    //锚点View 宽度
    val anchorHeight = anchor.height

    //锚点View 高度
    val anchorWidth = anchor.width

    //锚点View Rect
    val anchorFrame = Rect()

    //锚点的中心点坐标
    var anchorCenterX = 0
    var anchorCenterY = 0

    private val anchorRootLocation = IntArray(2)

    var visibleWindowFrame = Rect()

    //显示控件的宽度
    var width = 0

    //显示控件的高度
    var height = 0

    //显示得view 区域
    val viewFrame = Rect()
    var x = 0
    var y = 0
    var contentWidthMeasureSpec = 0
    var contentHeightMeasureSpec = 0

    //显示在锚点的方向 1：左边，2：上边，3：右边，4：下边
    var direction = 0
        private set

    val metric: DisplayMetrics by lazy {
        val m = DisplayMetrics()
        val wm = anchor.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(m)
        m
    }

    init {
        // for muti window
        anchor.rootView.getLocationOnScreen(anchorRootLocation)
        val anchorLocation = IntArray(2)
        anchor.getLocationOnScreen(anchorLocation)
        anchorCenterX = anchorLocation[0] + anchorWidth / 2
        anchorCenterY = anchorLocation[1] + anchorHeight / 2
        anchor.getWindowVisibleDisplayFrame(visibleWindowFrame)
        anchorFrame.left = anchorLocation[0]
        anchorFrame.top = anchorLocation[1]
        anchorFrame.right = anchorLocation[0] + anchorWidth
        anchorFrame.bottom = anchorLocation[1] + anchorHeight


    }


    fun measureLayoutView() {
        var needMeasureForWidth = false
        var needMeasureForHeight = false
        //清空Decor padding
        popup.decorRootView.setPadding(0, 0, 0, 0)
        val pHeight = popup.maxHeight ?: popup.height
        val pWidth = popup.maxWidth ?: popup.width
        width = pWidth
        height = pHeight
        if (pWidth > 0) {
            width = pWidth
            contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        } else {
            if (pWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                //屏幕宽度
                width = metric.widthPixels
                contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            } else {
                //内容包裹,需要测量
                needMeasureForWidth = true
                contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(metric.widthPixels, View.MeasureSpec.AT_MOST)
            }
        }
        if (pHeight > 0) {
            height = pHeight
            contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        } else {
            if (pHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                //屏幕高度
                height = metric.heightPixels
                contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            } else {
                //内容包裹,需要测量
                needMeasureForHeight = true
                contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(metric.widthPixels, View.MeasureSpec.AT_MOST)
            }
        }
        //从新测量
        if (needMeasureForWidth || needMeasureForHeight) {
            popup.decorRootView!!.measure(contentWidthMeasureSpec, contentHeightMeasureSpec)
            width = popup.decorRootView!!.measuredWidth
            height = popup.decorRootView!!.measuredHeight
        }
    }

    /**
     * 设置大小
     */
    fun reSetSize() {
        width = min(metric.widthPixels, width)
        height = min(metric.heightPixels, height)
        popup.popupWindow.width = width
        popup.popupWindow.height = height
    }

    /**
     * 计算X，Y
     */
    fun calculateXY() {

        fun hasGravity(gravity: Int) = popup.gravity and gravity == gravity

        if (popup.direction == PopupDirection.LEFT || popup.direction == PopupDirection.LEFT || popup.direction == PopupDirection.AUTO_LEFT_RIGHT) {
            //左右
            if (hasGravity(Gravity.CENTER)) {
                //垂直居中
                y = if (height > anchorHeight) {
                    max(0, anchorFrame.top - (height - anchorHeight) / 2)
                } else {
                    max(0, anchorFrame.top + (anchorHeight - height) / 2)
                }
            } else if (hasGravity(Gravity.TOP)) {
                //顶部对齐
                y = anchorFrame.top
            } else if (hasGravity(Gravity.BOTTOM)) {
                //底部对齐
                y = max(0, anchorFrame.bottom - height)
            }
            val isLeft = if (popup.direction == PopupDirection.AUTO_LEFT_RIGHT) {
                //自动左右,左边不够去右边
                //右边
                if (width <= metric.widthPixels - anchorFrame.right) false
                //左边
                else if (width <= anchorFrame.left) true
                //两边都不够，显示在大的地方
                else anchorFrame.left > metric.widthPixels - anchorFrame.right
            } else {
                //固定
                popup.direction == PopupDirection.LEFT
            }
            x = if (isLeft) {
                direction = 1
                max(0, anchorFrame.left - width)
            } else {
                direction = 3
                anchorFrame.right
            }
        } else if (popup.direction == PopupDirection.TOP || popup.direction == PopupDirection.BOTTOM || popup.direction == PopupDirection.AUTO_TOP_BOTTOM) {
            //上下
            if (hasGravity(Gravity.CENTER)) {
                //水平居中
                x = if (width > anchorWidth) {
                    max(0, anchorFrame.left - (width - anchorWidth) / 2)
                } else {
                    max(0, anchorFrame.left + (anchorWidth - width) / 2)
                }
            } else if (hasGravity(Gravity.LEFT)) {
                //左边对齐
                y = anchorFrame.left
            } else if (hasGravity(Gravity.RIGHT)) {
                //右边对齐
                y = max(0, anchorFrame.right - width)
            }
            val isTop = if (hasGravity(PopupDirection.AUTO_TOP_BOTTOM)) {
                //自动上下,上边不够去下边
                //下边
                if (height <= metric.heightPixels - anchorFrame.bottom) false
                //上边
                else if (height <= anchorFrame.top) true
                //两边都不够，显示在大的地方
                else anchorFrame.top > metric.heightPixels - anchorFrame.bottom
            } else {
                //固定
                hasGravity(PopupDirection.TOP)
            }
            y = if (isTop) {
                direction = 2
                max(0, anchorFrame.top - height)
            } else {
                direction = 4
                anchorFrame.bottom
            }
        }
        setViewFrame()
    }

    fun setViewFrame() {
        viewFrame.left = x
        viewFrame.top = y
        viewFrame.right = x + width
        viewFrame.bottom = y + height
    }

}