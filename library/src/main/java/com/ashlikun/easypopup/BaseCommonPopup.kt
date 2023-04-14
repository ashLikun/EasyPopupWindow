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
import kotlin.math.min

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/17　14:53
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：是对官方的PopupWindows的封装，使得使用方便
 */
open class BaseCommonPopup(
    //context
    override val context: Context,
    //布局
    override var layouView: View? = null,
    //布局文件
    override val layoutId: Int? = null,
    override var width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    override var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    override var maxHeight: Int? = null,
    override var maxWidth: Int? = null,
    override var anchorView: View? = null,
    override val animStyle: Int? = R.style.EasyPopup_Anim_TopToBottom,
    //当direction 为Auto 时候 2个方向的动画，如AUTO_TOP_BOTTOM：0：top，1：Bottom   AUTO_LEFT_RIGHT：0：Left，1：Right   优先级比animStyle高
    open val animStyleDirection: Array<Int>? = null,
    //是否触摸之外dismiss
    override var outsideTouchable: Boolean = true,
    //弹出pop时，背景是否变暗,null：不操作
    override var dimAmount: Float? = .6f,
    //获取焦点
    override var focusable: Boolean = true,
    //偏移量
    override var offsetX: Int = 0,
    override var offsetY: Int = 0,
    //相对于描点的方向
    override var direction: Int = PopupDirection.AUTO_TOP_BOTTOM,
    override var gravity: Int = Gravity.CENTER,
    //背景颜色
    open var bgColor: Int = Color.WHITE,
    //圆角 px
    open var radius: Int = dip2px(context, 10f),
    //圆角半径左上，右上，右下，左下或者8个值也可以4个值得一个 px
    open var radiusArr: IntArray? = null,
    //是否显示箭头
    open var showArrow: Boolean = false,
    open var arrowWidth: Int = dip2px(context, 10f),
    open var arrowHeight: Int = dip2px(context, 10f),
    open var borderWidth: Int = 1,
    open var borderColor: Int = Color.BLACK,
    //与anchor 的距离
    open var anchorMargin: Int = 0,
    isCallCreate: Boolean = true,
    open var elevation: Int = dip2px(context, 1f),
    override var onDismissListener: PopupWindow.OnDismissListener? = null
) : BasePopup(
    context,
    layouView,
    layoutId,
    width,
    height,
    maxHeight,
    maxWidth,
    anchorView,
    animStyle,
    outsideTouchable,
    dimAmount,
    focusable,
    offsetX,
    offsetY,
    direction,
    gravity,
    false,
    onDismissListener
) {

    init {
        if (isCallCreate) {
            onCreate()
        }
    }

    /**
     * contentView创建完成，这个时候就可以开启进入动画
     */
    override fun onPopupWindowCreated(contentView: View) {
        layouView!!.background = GradientDrawable().apply {
            setColor(bgColor)
            setStroke(borderWidth, borderColor)
            when {
                //使用全角
                radius > 0 -> cornerRadius = radius.toFloat()
                radiusArr?.size == 8 -> cornerRadii = radiusArr!!.map { it.toFloat() }.toFloatArray()
                radiusArr?.size == 4 -> cornerRadii = floatArrayOf(
                    radiusArr!![0].toFloat(), radiusArr!!!![0].toFloat(),
                    radiusArr!![1].toFloat(), radiusArr!![1].toFloat(),
                    radiusArr!![2].toFloat(), radiusArr!![2].toFloat(),
                    radiusArr!![3].toFloat(), radiusArr!![3].toFloat()
                )
            }
        }
        ViewCompat.setElevation(layouView!!, elevation.toFloat())
    }

    override fun show(anchor: View) {
        super.show(anchor)
    }
    override fun onShowInfoCreate(showInfo: ShowInfo) {
        super.onShowInfoCreate(showInfo)
        if (animStyleDirection?.size ?: 0 >= 2) {
            when (showInfo.direction) {
                //上方
                2 -> popupWindow.animationStyle = animStyleDirection!![0]
                //下方
                4 -> popupWindow.animationStyle = animStyleDirection!![1]
                //左边
                1 -> popupWindow.animationStyle = animStyleDirection!![0]
                //右方
                3 -> popupWindow.animationStyle = animStyleDirection!![1]
            }
        }

        var paddingLeft = 0
        var paddingTop = 0
        var paddingRight = 0
        var paddingBottom = 0
        when (showInfo.direction) {
            //左边
            1 -> {
                showInfo.x = showInfo.x - anchorMargin
                if (showArrow) {
                    paddingRight += arrowHeight
                }
            }
            //上方
            2 -> {
                showInfo.y = showInfo.y - anchorMargin
                if (showArrow) {
                    paddingBottom += arrowHeight
                }
            }
            //右方
            3 -> {
                showInfo.x = showInfo.x + anchorMargin
                if (showArrow) {
                    paddingLeft += arrowHeight
                }
            }
            //下方
            4 -> {
                showInfo.y = showInfo.y + anchorMargin
                if (showArrow) {
                    paddingTop += arrowHeight
                }
            }
        }
        showInfo.setViewFrame()
        //重新设置宽高,去除边界
        showInfo.width = showInfo.width + paddingLeft + paddingRight
        showInfo.height = showInfo.height + paddingTop + paddingBottom
        showInfo.reSetSize()
        decorRootView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }
}