package com.app.hnt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.hnt.R;
import com.app.hnt.base.BaseActivity;
import com.app.hnt.model.Fragment3Model;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zyz on 2017/9/4.
 * 钱包地址
 */

public class SetAddressActivity extends BaseActivity {
    Fragment3Model model;
    EditText editText1, editText2;
    TextView textView1;
    String addr = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setaddress);
    }

    @Override
    protected void initView() {
        editText1 = findViewByID_My(R.id.editText1);
        editText2 = findViewByID_My(R.id.editText2);

        textView1 = findViewByID_My(R.id.textView1);

    }

    @Override
    protected void initData() {
        model = (Fragment3Model) getIntent().getSerializableExtra("Fragment3Model");
        editText1.setText(model.getWalletAddr());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView1:
                //提交
                if (match()) {
                    textView1.setClickable(false);
                    showProgress(true, getString(R.string.app_loading1));
                    params.clear();
                    params.put("address", addr);
                    params.put("trade_password", password);//交易密码（不能小于6位数）
                    RequestSetWalletAddress(params);//设置钱包地址
                }
                break;
        }
    }

    //钱包地址设置
    private void RequestSetWalletAddress(Map<String, String> params) {
        OkhttpUtil.okHttpPost(URLs.Address, params, headerMap, new CallBackUtil<String>() {
            @Override
            public String onParseResponse(Call call, Response response) {
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e, String err) {
                hideProgress();
                textView1.setClickable(true);
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
                textView1.setClickable(true);
                myToast("币地址设置成功");
                finish();
            }
        });
    }

    private boolean match() {
        addr = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(addr)) {
            myToast(getString(R.string.address_h3));
            return false;
        }
        password = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            myToast(getString(R.string.address_h9));
            return false;
        }
        return true;
    }

    @Override
    protected void updateView() {
        titleView.setTitle(getString(R.string.address_h1));
    }
}
