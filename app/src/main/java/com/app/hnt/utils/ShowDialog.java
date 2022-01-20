package com.app.hnt.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.hnt.R;
import com.cy.dialog.BaseDialog;

/**
 * Created by Mr.Z on 2021/7/29.
 * 公共Dialog
 */
public class ShowDialog {
    /**
     * 单个按钮弹窗
     *
     * @param activity 用于判断是否销毁，不能用Content
     * @param dialog   不用再次初始化
     * @param msg      提示信息
     */
    public static void show(Activity activity, BaseDialog dialog, String msg) {
        if (!activity.isFinishing()) {
            if (dialog.isShowing() == false) {
                dialog.contentView(R.layout.dialog_base1)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();

                TextView textView2 = dialog.findViewById(R.id.textView2);
                textView2.setText(msg);
                dialog.findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    /**
     * 单个按钮弹窗 - 有监听事件
     *
     * @param activity 用于判断是否销毁，不能用Content
     * @param dialog   不用再次初始化
     * @param msg      提示信息
     */
    public static void show(Activity activity, BaseDialog dialog, String msg, View.OnClickListener listener) {
        if (!activity.isFinishing()) {
            if (dialog.isShowing() == false) {
                dialog.contentView(R.layout.dialog_base1)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();
                TextView textView2 = dialog.findViewById(R.id.textView2);
                textView2.setText(msg);
                dialog.findViewById(R.id.textView3).setOnClickListener(listener);
            }
        }
    }


    /**
     * 多个按钮弹窗 - 有监听事件
     *
     * @param activity 用于判断是否销毁，不能用Content
     * @param dialog   不用再次初始化
     * @param title    标题
     * @param msg      提示信息
     */
    public static void show(Activity activity, BaseDialog dialog, String title, String msg, View.OnClickListener leftlistener, View.OnClickListener rightlistener) {
        if (!activity.isFinishing()) {
            if (dialog.isShowing() == false) {
                dialog.contentView(R.layout.dialog_base2)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();
                TextView textView1 = dialog.findViewById(R.id.textView1);
                textView1.setText(title);
                TextView textView2 = dialog.findViewById(R.id.textView2);
                textView2.setText(msg);
                TextView textView3 = dialog.findViewById(R.id.textView3);
                TextView textView4 = dialog.findViewById(R.id.textView4);
                textView3.setText("取消");
                textView4.setText("确认");
                textView3.setOnClickListener(leftlistener);
                textView4.setOnClickListener(rightlistener);
            }
        }
    }

    /**
     * 多个按钮弹窗 - 有监听事件
     *
     * @param activity    用于判断是否销毁，不能用Content
     * @param dialog      不用再次初始化
     * @param title       标题
     * @param msg         提示信息
     * @param leftbtntxt  左边按钮文字
     * @param rightbtntxt 右边按钮文字
     */
    public static void show(Activity activity, BaseDialog dialog, String title, String msg, String leftbtntxt, String rightbtntxt, View.OnClickListener leftlistener, View.OnClickListener rightlistener) {
        if (!activity.isFinishing()) {
            if (dialog.isShowing() == false) {
                dialog.contentView(R.layout.dialog_base2)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();
                TextView textView1 = dialog.findViewById(R.id.textView1);
                textView1.setText(title);
                TextView textView2 = dialog.findViewById(R.id.textView2);
                textView2.setText(msg);
                TextView textView3 = dialog.findViewById(R.id.textView3);
                TextView textView4 = dialog.findViewById(R.id.textView4);
                textView3.setText(leftbtntxt);
                textView4.setText(rightbtntxt);
                textView3.setOnClickListener(leftlistener);
                textView4.setOnClickListener(rightlistener);
            }
        }
    }
}
