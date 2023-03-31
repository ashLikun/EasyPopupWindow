package com.ashlikun.easypopup

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/17　14:56
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
@IntDef(
    PopupDirection.LEFT,
    PopupDirection.RIGHT,
    PopupDirection.TOP,
    PopupDirection.BOTTOM,
    PopupDirection.AUTO_LEFT_RIGHT,
    PopupDirection.AUTO_TOP_BOTTOM
)
@Retention(RetentionPolicy.SOURCE)
annotation class PopupDirection {
    companion object {
        const val AUTO_LEFT_RIGHT = 0
        const val AUTO_TOP_BOTTOM = 1
        const val TOP = 3
        const val BOTTOM = 4
        const val LEFT = 5
        const val RIGHT = 6
    }
}