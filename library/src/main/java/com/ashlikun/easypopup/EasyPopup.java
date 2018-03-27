package com.ashlikun.easypopup;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/17　14:53
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：是对官方的PopupWindows的封装，使得使用方便
 */

public class EasyPopup implements PopupWindow.OnDismissListener {

    private static final float DEFAULT_DIM = 0.7f;
    private static final float DEFAULT_WINDOW_ALPHA_ORGIN = 1.666f;

    //PopupWindow对象
    private PopupWindow mPopupWindow;

    //context
    private Context mContext;
    //contentView
    protected View mContentView;
    //布局id
    protected int mLayoutId;
    //获取焦点
    protected boolean mFocusable = true;
    //是否触摸之外dismiss
    protected boolean mOutsideTouchable = true;
    //弹出pop时，背景是否变暗
    protected boolean isBackgroundAlpha = false;
    protected float mBackgroundAlpha = .6f;
    protected float mWindowAlphaOrgin = DEFAULT_WINDOW_ALPHA_ORGIN;
    //宽高
    protected int mWidth;
    protected int mHeight;

    protected int mAnimationStyle;

    private PopupWindow.OnDismissListener mOnDismissListener;


    private View mAnchorView;
    @VerticalGravity
    private int mVerticalGravity = VerticalGravity.BELOW;
    @HorizontalGravity
    private int mHorizontalGravity = HorizontalGravity.LEFT;
    private int mOffsetX;
    private int mOffsetY;

    //是否只是获取宽高
    //getViewTreeObserver监听时
    private boolean isOnlyGetWH = true;


    public EasyPopup(Context context, View contentView, @LayoutRes int layoutId, int width, int height) {
        this.mContext = context;
        this.mContentView = contentView;
        this.mLayoutId = layoutId;
        this.mWidth = width;
        this.mHeight = height;
    }

    public EasyPopup(Context context, @LayoutRes int layoutId, int width, int height) {
        this(context, null, layoutId, width, height);
    }

    public EasyPopup(View contentView, int width, int height) {
        this(contentView.getContext(), contentView, 0, width, height);
    }

    public EasyPopup(View contentView) {
        this(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public EasyPopup(Context context, @LayoutRes int layoutId) {
        this(context, layoutId, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    //设置完参数调用得创建方法
    public <T extends EasyPopup> T create() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow();
        }
        onPopupWindowCreated();
        if (mContentView == null) {
            if (mLayoutId != 0) {
                mContentView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
            } else {
                throw new IllegalArgumentException("The content view is null");
            }
        }
        mPopupWindow.setContentView(mContentView);

        if (mWidth != 0) {
            mPopupWindow.setWidth(mWidth);
        } else {
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        if (mHeight != 0) {
            mPopupWindow.setHeight(mHeight);
        } else {
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        onPopupWindowViewCreated(mContentView);
        if (mAnimationStyle != 0) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }
        mPopupWindow.setFocusable(mFocusable);
        mPopupWindow.setOutsideTouchable(mOutsideTouchable);
        mPopupWindow.setBackgroundDrawable(mOutsideTouchable ? new ColorDrawable(Color.TRANSPARENT) : null);
        mPopupWindow.setOnDismissListener(this);

        return (T) this;
    }

    /****自定义生命周期方法****/

    /**
     * PopupWindow对象创建完成
     */
    protected void onPopupWindowCreated() {
    }

    /**
     * contentView创建完成，这个时候就可以开启进入动画
     *
     * @param contentView
     */
    protected void onPopupWindowViewCreated(View contentView) {
    }

    /**
     * contentView创建完成，这个时候就可以开启退出动画
     */
    protected void onPopupWindowDismiss() {
    }

    /****设置属性方法****/


    public <T extends EasyPopup> T setWidth(int width) {
        this.mWidth = width;
        return (T) this;
    }

    public <T extends EasyPopup> T setHeight(int height) {
        this.mHeight = height;
        return (T) this;
    }

    public <T extends EasyPopup> T setAnchorView(View view) {
        this.mAnchorView = view;
        return (T) this;
    }

    public <T extends EasyPopup> T setVerticalGravity(@VerticalGravity int verticalGravity) {
        this.mVerticalGravity = verticalGravity;
        return (T) this;
    }

    public <T extends EasyPopup> T setHorizontalGravity(@VerticalGravity int horizontalGravity) {
        this.mHorizontalGravity = horizontalGravity;
        return (T) this;
    }

    public <T extends EasyPopup> T setOffsetX(int offsetX) {
        this.mOffsetX = offsetX;
        return (T) this;
    }

    public <T extends EasyPopup> T setOffsetY(int offsetY) {
        this.mOffsetY = offsetY;
        return (T) this;
    }

    public <T extends EasyPopup> T setAnimationStyle(@StyleRes int animationStyle) {
        this.mAnimationStyle = animationStyle;
        return (T) this;
    }

    /**
     * 设置按下消失
     */
    public <T extends EasyPopup> T setDismissWhenTouchOuside(boolean dismissWhenTouchOuside) {
        this.mOutsideTouchable = dismissWhenTouchOuside;
        this.mFocusable = dismissWhenTouchOuside;
        return (T) this;
    }

    /**
     * 设置是否可以获取焦点
     */
    public <T extends EasyPopup> T setFocusable(boolean mFocusable) {
        this.mFocusable = mFocusable;
        return (T) this;
    }

    /**
     * 设置点击空白地方消失
     */
    public <T extends EasyPopup> T setOutsideTouchable(boolean mOutsideTouchable) {
        this.mOutsideTouchable = mOutsideTouchable;
        return (T) this;
    }


    public <T extends EasyPopup> T setBackgroundAlpha(boolean backgroundDim, float mBackgroundAlpha) {
        this.isBackgroundAlpha = backgroundDim;
        this.mBackgroundAlpha = mBackgroundAlpha;
        return (T) this;
    }


    /**
     * 使用此方法需要在创建的时候调用setAnchorView()等属性设置{@see setAnchorView()}
     */
    public void showAsDropDown() {
        if (mAnchorView == null) {
            return;
        }

        showAsDropDown(mAnchorView, mOffsetX, mOffsetY);
    }

    /**
     * PopupWindow自带的显示方法
     *
     * @param anchor
     * @param offsetX
     * @param offsetY
     */
    public void showAsDropDown(View anchor, int offsetX, int offsetY) {
        if (mPopupWindow != null) {
            isOnlyGetWH = true;
            mAnchorView = anchor;
            mOffsetX = offsetX;
            mOffsetY = offsetY;
            addGlobalLayoutListener(mPopupWindow.getContentView());
            handleBackgroundAlpha(true);
            mPopupWindow.showAsDropDown(anchor, offsetX, offsetY);

        }
    }

    public void showAsDropDown(View anchor) {
        if (mPopupWindow != null) {
            mAnchorView = anchor;
            isOnlyGetWH = true;
            addGlobalLayoutListener(mPopupWindow.getContentView());
            handleBackgroundAlpha(true);
            mPopupWindow.showAsDropDown(anchor);
        }
    }


    public void showAtLocation(View parent, int gravity, int offsetX, int offsetY) {
        if (mPopupWindow != null) {
            mAnchorView = parent;
            mOffsetX = offsetX;
            mOffsetY = offsetY;
            isOnlyGetWH = true;
            addGlobalLayoutListener(mPopupWindow.getContentView());
            handleBackgroundAlpha(true);
            mPopupWindow.showAtLocation(parent, gravity, offsetX, offsetY);
        }
    }

    /**
     * 相对anchor view显示
     * <p>
     * 使用此方法需要在创建的时候调用setAnchorView()等属性设置{@see setAnchorView()}
     */
    public void showAtAnchorView() {
        if (mAnchorView == null) {
            return;
        }
        showAtAnchorView(mAnchorView, mVerticalGravity, mHorizontalGravity);
    }

    /**
     * 相对anchor view显示，适用 宽高不为match_parent
     *
     * @param anchor
     * @param vertGravity
     * @param horizGravity
     */
    public void showAtAnchorView(@NonNull View anchor, @VerticalGravity int vertGravity, @HorizontalGravity int horizGravity) {
        showAtAnchorView(anchor, vertGravity, horizGravity, 0, 0);
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
    public void showAtAnchorView(@NonNull View anchor, @VerticalGravity final int vertGravity, @HorizontalGravity int horizGravity, int x, int y) {
        if (mPopupWindow == null) {
            return;
        }
        mAnchorView = anchor;
        mOffsetX = x;
        mOffsetY = y;
        mVerticalGravity = vertGravity;
        mHorizontalGravity = horizGravity;
        isOnlyGetWH = false;
        final View contentView = getContentView();
        addGlobalLayoutListener(contentView);
        contentView.measure(0, View.MeasureSpec.UNSPECIFIED);
        final int measuredW = contentView.getMeasuredWidth();
        final int measuredH = contentView.getMeasuredHeight();

        x = calculateX(anchor, horizGravity, measuredW, x);
        y = calculateY(anchor, vertGravity, measuredH, y);
        handleBackgroundAlpha(true);
        PopupWindowCompat.showAsDropDown(mPopupWindow, anchor, x, y, Gravity.NO_GRAVITY);
    }

    /**
     * 处理背景变暗
     *
     * @param isShow 是否是弹出时候调用的
     */
    private void handleBackgroundAlpha(boolean isShow) {
        if (isBackgroundAlpha) {
            Activity activity = getActivity(mContext);
            if (activity != null) {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                if (isShow && mWindowAlphaOrgin == DEFAULT_WINDOW_ALPHA_ORGIN) {
                    mWindowAlphaOrgin = lp.alpha;
                }
                lp.alpha = isShow ? mBackgroundAlpha : mWindowAlphaOrgin;
                activity.getWindow().setAttributes(lp);
            }
        }
    }

    /**
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    private Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
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
    private int calculateY(View anchor, int vertGravity, int measuredH, int y) {
        switch (vertGravity) {
            case VerticalGravity.ABOVE:
                //anchor view之上
                y -= measuredH + anchor.getHeight();
                break;
            case VerticalGravity.ALIGN_BOTTOM:
                //anchor view底部对齐
                y -= measuredH;
                break;
            case VerticalGravity.CENTER:
                //anchor view垂直居中
                y -= anchor.getHeight() / 2 + measuredH / 2;
                break;
            case VerticalGravity.ALIGN_TOP:
                //anchor view顶部对齐
                y -= anchor.getHeight();
                break;
            case VerticalGravity.BELOW:
                //anchor view之下
                // Default position.
                break;
        }

        return y;
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
    private int calculateX(View anchor, int horizGravity, int measuredW, int x) {
        switch (horizGravity) {
            case HorizontalGravity.LEFT:
                //anchor view左侧
                x -= measuredW;
                break;
            case HorizontalGravity.ALIGN_RIGHT:
                //与anchor view右边对齐
                x -= measuredW - anchor.getWidth();
                break;
            case HorizontalGravity.CENTER:
                //anchor view水平居中
                x += anchor.getWidth() / 2 - measuredW / 2;
                break;
            case HorizontalGravity.ALIGN_LEFT:
                //与anchor view左边对齐
                // Default position.
                break;
            case HorizontalGravity.RIGHT:
                //anchor view右侧
                x += anchor.getWidth();
                break;
        }

        return x;
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
    private void updateLocation(int width, int height, @NonNull View anchor, @VerticalGravity final int vertGravity, @HorizontalGravity int horizGravity, int x, int y) {
        final int measuredW = width;
        final int measuredH = height;
        x = calculateX(anchor, horizGravity, measuredW, x);
        y = calculateY(anchor, vertGravity, measuredH, y);
        mPopupWindow.update(anchor, x, y, width, height);
    }

    //监听器，用于PopupWindow弹出时获取准确的宽高
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mWidth = getContentView().getWidth();
            mHeight = getContentView().getHeight();
            //只获取宽高时，不执行更新操作
            if (isOnlyGetWH) {
                removeGlobalLayoutListener();
                return;
            }
            if (mPopupWindow == null) {
                return;
            }
            updateLocation(mWidth, mHeight, mAnchorView, mVerticalGravity, mHorizontalGravity, mOffsetX, mOffsetY);
            removeGlobalLayoutListener();
        }
    };

    /**
     * 设置监听器
     *
     * @param listener
     */
    public <T extends EasyPopup> T setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mOnDismissListener = listener;
        return (T) this;
    }

    /**
     * 获取PopupWindow中加载的view
     *
     * @return
     */
    public View getContentView() {
        if (mPopupWindow != null) {
            return mPopupWindow.getContentView();
        } else {
            return null;
        }
    }

    /**
     * 获取context
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取PopupWindow对象
     *
     * @return
     */
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    /**
     * 获取view
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = null;
        if (getContentView() != null) {
            view = getContentView().findViewById(viewId);
        }
        return (T) view;
    }

    /**
     * 消失
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onDismiss() {
        handleDismiss();
    }

    /**
     * PopupWindow消失后处理一些逻辑
     */
    private void handleDismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }

        removeGlobalLayoutListener();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        onPopupWindowDismiss();
        handleBackgroundAlpha(false);
    }

    private void addGlobalLayoutListener(View contentView) {
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private void removeGlobalLayoutListener() {
        if (getContentView() != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            } else {
                getContentView().getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            }
        }
    }

}