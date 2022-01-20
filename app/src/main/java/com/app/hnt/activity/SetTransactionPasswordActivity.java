package com.app.hnt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.hnt.R;
import com.app.hnt.base.BaseActivity;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by zyz on 2017/9/4.
 * 设置交易密码
 */

public class SetTransactionPasswordActivity extends BaseActivity {
    EditText  editText1,editText3, editText4;
    LinearLayout linearLayout;
    String  oldpassword = "",password1 = "", password2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settransactionpassword);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        editText1 = findViewByID_My(R.id.editText1);
        editText3 = findViewByID_My(R.id.editText3);
        editText4 = findViewByID_My(R.id.editText4);

        linearLayout = findViewByID_My(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (match()) {
                    showProgress(true, getString(R.string.app_loading1));
                    params.clear();
                    params.put("password", oldpassword);
                    params.put("trade_password", password1);//交易密码（不能小于6位数）
                    RequestSetTransactionPassword(params);//设置交易密码
                }
            }
        });
    }

    private void RequestSetTransactionPassword(Map<String, String> params) {
        OkhttpUtil.okHttpPost(URLs.TransactionPassword, params, headerMap, new CallBackUtil<String>() {
            @Override
            public String onParseResponse(Call call, Response response) {
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e, String err) {
                hideProgress();
                myToast(err);
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                myToast("交易密码设置成功");
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    private boolean match() {
        oldpassword = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(oldpassword)) {
            myToast(getString(R.string.changepassword_h5));
            return false;
        }
        password1 = editText3.getText().toString().trim();
        if (TextUtils.isEmpty(password1)) {
            myToast(getString(R.string.settransactionpassword_h7));
            return false;
        }
        password2 = editText4.getText().toString().trim();
        if (TextUtils.isEmpty(password2)) {
            myToast(getString(R.string.settransactionpassword_h9));
            return false;
        }

        if (!password1.equals(password2)) {
            myToast(getString(R.string.settransactionpassword_h10));
            return false;
        }
        return true;
    }

    @Override
    protected void updateView() {
        titleView.setTitle(getString(R.string.settransactionpassword_h1));
    }
}
