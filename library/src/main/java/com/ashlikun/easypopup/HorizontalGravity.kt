package com.ashlikun.easypopup

import androidx.annotation.IntDef
import com.ashlikun.easypopup.HorizontalGravity
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/17　14:55
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
@IntDef(
    HorizontalGravity.CENTER,
    HorizontalGravity.LEFT,
    HorizontalGravity.RIGHT,
    HorizontalGravity.ALIGN_LEFT,
    HorizontalGravity.ALIGN_RIGHT
)
@Retention(RetentionPolicy.SOURCE)
annotation class HorizontalGravity {
    companion object {
        const val CENTER = 0
        const val LEFT = 1
        const val RIGHT = 2
        const val ALIGN_LEFT = 3
        const val ALIGN_RIGHT = 4
    }
}