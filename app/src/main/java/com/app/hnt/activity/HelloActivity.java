package com.app.hnt.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.app.hnt.utils.LocalUserInfo;
import com.blankj.utilcode.util.ActivityUtils;


/**
 *Created by Mr.Z on 2021/10/2.
 * 启动页
 */
public class HelloActivity extends Activity {
    private static final String SHARE_APP_TAG = "HelloActivity";
    private TimeCount time;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*//在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现，建议该方法放在Application的初始化方法中
        SDKInitializer.initialize(getApplicationContext());*/
        //设置无标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*//设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/


        // 判断是否是第一次开启应用
        SharedPreferences setting = getSharedPreferences(SHARE_APP_TAG, 0);
        Boolean user_first = setting.getBoolean("FIRST", true);

        // 如果是第一次启动，则先进入功能引导页
        /*if (user_first) {
            //弹出隐私
            BaseDialog dialog= new BaseDialog(this);
            dialog.contentView(R.layout.dialog_yinsi)
                    .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT))
                    .animType(BaseDialog.AnimInType.CENTER)
                    .canceledOnTouchOutside(false)
                    .gravity(Gravity.CENTER)
                    .dimAmount(0.8f)
                    .show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    finish();
                }
            });

            TextView textView = dialog.findViewById(R.id.textView2);
            final SpannableStringBuilder full = new SpannableStringBuilder();
            String head = "欢迎使用"+getString(R.string.app_name)+" APP。我门非常重视您的用户权益与个人信息保护，在您使用"+getString(R.string.app_name)+" APP前，请认真阅读";
            String text = "";
            String tail = "全部条款。我们将通过上述协议向您说明个人信息收集和使用的详细信息";

            String clickText1 = "《用户协议》";
            String clickText2 = "《隐私协议》";
            final SpannableStringBuilder before = new SpannableStringBuilder();
            final SpannableStringBuilder after = new SpannableStringBuilder();

            before.append(clickText1);
            before.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    //用户协议
                    Bundle bundle = new Bundle();
//                    bundle.putString("vkey", "regUser");
                    ActivityUtils.startActivity(bundle, WebContentActivity.class);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    //super.updateDrawState(ds); --> 注释掉，就没有下划线了
                }
            }, 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 1. click
            before.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, before.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2. color

            after.append(clickText2);
            after.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Bundle bundle1 = new Bundle();
//                    bundle1.putString("vkey", "regPrivate");
                    ActivityUtils.startActivity(bundle1, WebContentActivity.class);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    //super.updateDrawState(ds);
                }
            }, 0, after.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 1. click
            after.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, after.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2. color

            full.append(head).append(before).append(text).append(after).append(tail);
            // ##### end
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.append(full);

            dialog.findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    setting.edit().putBoolean("FIRST", false).commit();
                    Intent intent = new Intent(HelloActivity.this, GuideActivity.class);
                    startActivity(intent);
                }
            });
            dialog.findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } else {
            time = new TimeCount(3000, 1000);//构造CountDownTimer对象
            // 如果不是第一次启动app，则正常显示启动屏
            setContentView(R.layout.activity_hello);
            tv = findViewById(R.id.tv);
            time.start();//开始计时
            tv.setOnClickListener(v->{
                if (time != null)
                    time.cancel();
                enterHomeActivity();
            });
            *//*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHomeActivity();
                }
            }, 2000);*//*
        }*/
        enterHomeActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null)
            time.cancel();
    }
    private void enterHomeActivity() {
//        LocalUserInfo.getInstance(HelloActivity.this).setUserId("");
        if (LocalUserInfo.getInstance(HelloActivity.this).getToken().equals("")) {
            ActivityUtils.startActivity(LoginActivity.class);
        } else {
            ActivityUtils.startActivity(MainActivity.class);
        }
        finish();
    }
    //获取验证码倒计时
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tv.setText("跳过");
            enterHomeActivity();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tv.setText(millisUntilFinished / 1000  + "s | 跳过");
        }
    }


}
