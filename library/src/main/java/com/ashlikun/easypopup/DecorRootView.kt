package com.ashlikun.easypopup

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * 作者　　: 李坤
 * 创建时间: 2023/3/24　14:26
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：pop根布局
 */
class DecorRootView(context: Context, val popup: BasePopup) : FrameLayout(context) {
    //
    var myBackground: Drawable? = null

    private val arrowSaveRect = RectF()
    private val arrowAlignMode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    private val arrowPaint by lazy {
        Paint().also {
            it.isAntiAlias = true
        }
    }
    private val arrowPath by lazy {
        Path()
    }
    var contentView: View? = null
        set(value) {
            if (field != null) removeView(field)
            if (value!!.parent != null) (value.parent as ViewGroup).removeView(value)
            field = value
            addView(value, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }
    var showInfo: ShowInfo? = null
        set(value) {
            field = value
            requestFocus()
        }
    private var pendingWidth = 0
    private var pendingHeight = 0
    private val updateWindowAction = Runnable {
        showInfo!!.width = pendingWidth
        showInfo!!.height = pendingHeight
        showInfo!!.calculateXY()
        popup.popupWindow.update(showInfo!!.x, showInfo!!.y, showInfo!!.width, showInfo!!.height)
    }

    override fun setBackground(background: Drawable?) {
        myBackground = background
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        removeCallbacks(updateWindowAction)
//        if (showInfo == null) {
//            setMeasuredDimension(0, 0)
//            return
//        }
//        if (contentView != null) {
//            contentView!!.measure(showInfo!!.contentWidthMeasureSpec, showInfo!!.contentHeightMeasureSpec)
//            val measuredWidth = contentView!!.measuredWidth
//            val measuredHeight = contentView!!.measuredHeight
//            if (showInfo!!.width != measuredWidth || showInfo!!.height != measuredHeight) {
//                pendingWidth = measuredWidth
//                pendingHeight = measuredHeight
//                post(updateWindowAction)
//            }
//        }
//        setMeasuredDimension(showInfo!!.width, showInfo!!.height)
//    }

//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        val contentView = contentView
//        val showInfo = showInfo
//        if (contentView != null && showInfo != null) {
//            if (!popup.showArrow) {
//                contentView!!.layout(0, 0, showInfo.width, showInfo.height)
//            } else {
//                when (showInfo.direction) {
//                    1 -> contentView.layout(0, 0, showInfo.width - popup.arrowWidth - popup.anchorMargin, showInfo.height)
//                    2 -> contentView.layout(0, 0, showInfo.width, showInfo.height - popup.arrowHeight - popup.anchorMargin)
//                    3 -> contentView.layout(popup.arrowWidth + popup.anchorMargin, 0, showInfo.width, showInfo.height)
//                    4 -> contentView.layout(0, popup.arrowHeight + popup.anchorMargin, showInfo.width, showInfo.height)
//                }
//            }
//        }
//    }

    /**
     * 绘制箭头相关
     */
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val showInfo = showInfo ?: return
        if (popup is BaseCommonPopup && popup.showArrow) {
            //上下
            canvas.save()
            arrowPaint.style = Paint.Style.FILL
            arrowPaint.color = popup.bgColor
            arrowPaint.xfermode = null
//            var l = showInfo.anchorFrame.left - showInfo.x + (showInfo.anchorWidth / 2)
//            val t = maxOf(0, showInfo.anchorFrame.bottom - showInfo.y)
//            //移动到箭头顶点
//            canvas.translate(l.toFloat(), t.toFloat())
//            canvas.drawCircle(0f, 0f, 100f, Paint().also {
//                it.isAntiAlias = true
//                it.style = Paint.Style.FILL
//                it.color = 0xffff0000.toInt()
//            })

            when (showInfo.direction) {
                //左边
                1 -> {
                    canvas.translate(showInfo.width.toFloat(), maxOf(0, showInfo.anchorFrame.bottom - showInfo.y) - showInfo.anchorHeight / 2f)
                    canvas.rotate(90f)
                }
                //上方
                2 -> {
                    canvas.translate(showInfo.anchorFrame.left - showInfo.x + (showInfo.anchorWidth / 2f), showInfo.height.toFloat())
                    canvas.rotate(180f)
                }
                //右方
                3 -> {
                    canvas.translate(0f, maxOf(0, showInfo.anchorFrame.bottom - showInfo.y) - showInfo.anchorHeight / 2f)
                    canvas.rotate(-90f)
                }
                //下方
                4 -> {
                    canvas.translate(showInfo.anchorFrame.left - showInfo.x + (showInfo.anchorWidth / 2f), 0f)
                }
            }

            arrowPath.reset()
            arrowPath.lineTo(-popup.arrowWidth.toFloat() * 3 / 4, popup.arrowHeight.toFloat() + 2)
            arrowPath.lineTo(popup.arrowWidth.toFloat() * 3 / 4, popup.arrowHeight.toFloat() + 2)
            arrowPath.close()
            //绘制箭头背景
            canvas.drawPath(arrowPath, arrowPaint)
            arrowSaveRect.set(
                -popup.arrowWidth.toFloat() * 3 / 4, 0f,
                popup.arrowWidth.toFloat() * 3 / 4, popup.arrowHeight.toFloat() - popup.borderWidth.toFloat()
            )
            val saveLayer = canvas.saveLayer(arrowSaveRect, arrowPaint, Canvas.ALL_SAVE_FLAG)
            arrowPaint.strokeWidth = popup.borderWidth.toFloat()
            arrowPaint.color = popup.borderColor
            arrowPaint.style = Paint.Style.STROKE
            //三角形边框
            canvas.drawPath(arrowPath, arrowPaint)
            arrowPaint.xfermode = arrowAlignMode
            arrowPaint.style = Paint.Style.FILL
            //顶点圆润
            canvas.drawRect(0f, 0f, popup.arrowWidth.toFloat(), popup.borderWidth.toFloat(), arrowPaint)
            canvas.restoreToCount(saveLayer)
            canvas.restore()
        }
//        //绘制背景
//        myBackground?.setBounds(0, 0, showInfo!!.width, showInfo!!.height)
//        myBackground?.draw(canvas)
    }
}