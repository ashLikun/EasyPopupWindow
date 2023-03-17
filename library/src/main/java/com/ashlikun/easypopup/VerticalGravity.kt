package com.ashlikun.easypopup

import androidx.annotation.IntDef
import com.ashlikun.easypopup.VerticalGravity
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
@IntDef(VerticalGravity.CENTER, VerticalGravity.ABOVE, VerticalGravity.BELOW, VerticalGravity.ALIGN_TOP, VerticalGravity.ALIGN_BOTTOM)
@Retention(
    RetentionPolicy.SOURCE
)
annotation class VerticalGravity {
    companion object {
        const val CENTER = 0
        const val ABOVE = 1
        const val BELOW = 2
        const val ALIGN_TOP = 3
        const val ALIGN_BOTTOM = 4
    }
}