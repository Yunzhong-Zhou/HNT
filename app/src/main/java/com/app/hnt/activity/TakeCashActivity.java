package com.app.hnt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.hnt.R;
import com.app.hnt.base.BaseActivity;
import com.app.hnt.model.Fragment3Model;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;
import com.blankj.utilcode.util.ActivityUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by zyz on 2017/9/4.
 * 提币
 */

public class TakeCashActivity extends BaseActivity {
    Fragment3Model model;
    LinearLayout linearLayout_addr;
    TextView textView1, textView2, textView3, textView4, textView5;
    EditText editText1, editText2;

    String money = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takecash);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        requestServer();
    }

    @Override
    protected void initView() {
        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);
        textView5 = findViewByID_My(R.id.textView5);

        editText1 = findViewByID_My(R.id.editText1);
        editText2 = findViewByID_My(R.id.editText2);

        /*//动态设置linearLayout的高度为屏幕高度的1/4
        linearLayout_addr = findViewByID_My(R.id.linearLayout_addr);
        ViewGroup.LayoutParams lp = linearLayout_addr.getLayoutParams();
        lp.height = (int)  ScreenUtils.getScreenHeight() / 4;*/


    }

    @Override
    protected void initData() {
        model = (Fragment3Model) getIntent().getSerializableExtra("Fragment3Model");
        textView4.setText(getString(R.string.takecash_h3) + model.getAmount());
        if (model.getWalletAddr().equals("")) {
            textView1.setVisibility(View.VISIBLE);
            textView3.setVisibility(View.GONE);
        } else {
            textView1.setVisibility(View.GONE);
            textView3.setVisibility(View.VISIBLE);
            textView3.setText(model.getWalletAddr());

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView1:
                Bundle bundle = new Bundle();
                bundle.putSerializable("Fragment3Model", model);
                ActivityUtils.startActivity(SetAddressActivity.class, bundle);
                break;
            case R.id.textView5:
                if (match()) {
                    textView5.setClickable(false);
                    showProgress(true, getString(R.string.app_loading1));
                    HashMap<String, String> params = new HashMap<>();
                    params.put("trade_password", password);//交易密码（不能小于6位数）
                    params.put("input_money", money);//提现金额
                    RequestTakeCash(params);//提现
                }
                break;
        }
    }

    //提现
    private void RequestTakeCash(Map<String, String> params) {
        OkhttpUtil.okHttpPost(URLs.TakeCash, params, headerMap, new CallBackUtil<String>() {
            @Override
            public String onParseResponse(Call call, Response response) {
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e, String err) {
                hideProgress();
                textView5.setClickable(true);
                myToast(err);
                /*if (!info.equals("")) {
                    if (info.contains(getString(R.string.password_h1))) {
                        showToast(getString(R.string.password_h2),
                                getString(R.string.password_h5), getString(R.string.password_h6),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        CommonUtil.gotoActivity(SetAddressActivity.this, SetTransactionPasswordActivity.class, false);
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                    } else if (info.contains(getString(R.string.password_h3))) {
                        showToast(getString(R.string.password_h4),
                                getString(R.string.password_h5), getString(R.string.password_h6),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        CommonUtil.gotoActivity(SetAddressActivity.this, SetAddressActivity.class, false);
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        showToast(info);
                    }
                }*/
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                textView5.setClickable(true);
                ActivityUtils.startActivity(MyTakeCashActivity.class);
                finish();
            }
        });
    }


    private boolean match() {
        money = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            myToast(getString(R.string.takecash_h8));
            return false;
        }
        password = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            myToast(getString(R.string.takecash_h10));
            return false;
        }
        return true;
    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();

    }

    @Override
    protected void updateView() {
        titleView.setTitle(getString(R.string.takecash_h1));
    }
}
