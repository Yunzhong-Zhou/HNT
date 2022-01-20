package com.app.hnt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * Created by zyz on 2020/7/5.
 * 当外层竖向滑动ScrollView里面嵌套横向的滑动布局时，会发先内层横向滑动很卡顿
 * <p>
 * 原因：左右滑动操作被外层的scrollView处理掉了
 * <p>
 * 解决：只要让scrollview对左右滑动事件不监听，让其子控件处理左右滑动事件
 */
public class MyNestedScrollView extends NestedScrollView {

    private float xDistance, yDistance, xLast, yLast;

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
