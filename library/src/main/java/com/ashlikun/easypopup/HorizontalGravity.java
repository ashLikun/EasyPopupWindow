package com.ashlikun.easypopup;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/17　14:55
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
@IntDef({
        HorizontalGravity.CENTER,
        HorizontalGravity.LEFT,
        HorizontalGravity.RIGHT,
        HorizontalGravity.ALIGN_LEFT,
        HorizontalGravity.ALIGN_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface HorizontalGravity {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}
