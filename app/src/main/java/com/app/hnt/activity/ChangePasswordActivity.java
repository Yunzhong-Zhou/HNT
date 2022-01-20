package com.app.hnt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.app.hnt.R;
import com.app.hnt.base.BaseActivity;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;
import com.app.hnt.utils.FileUtil;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by fafukeji01 on 2017/4/25.
 * 修改密码
 */

public class ChangePasswordActivity extends BaseActivity {
    EditText editText1, editText2, editText3;
    String oldpassword = "", password1 = "", password2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        editText1 = findViewByID_My(R.id.editText1);
        editText2 = findViewByID_My(R.id.editText2);
        editText3 = findViewByID_My(R.id.editText3);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout:
                //确认
                if (match()) {
                    this.showProgress(true, getString(R.string.app_loading1));
                    HashMap<String, String> params = new HashMap<>();
                    params.put("new_password", password1);//密码（不能小于6位数）
                    params.put("password", oldpassword);
                    Request(params);
                }
                break;
        }
    }

    private boolean match() {
        oldpassword = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(oldpassword)) {
            myToast(getString(R.string.changepassword_h5));
            return false;
        }
        password1 = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(password1)) {
            myToast(getString(R.string.changepassword_h6));
            return false;
        }
        password2 = editText3.getText().toString().trim();
        if (TextUtils.isEmpty(password2)) {
            myToast(getString(R.string.changepassword_h7));
            return false;
        }
        if (!password1.equals(password2)) {
            myToast(getString(R.string.changepassword_h8));
            return false;
        }
        return true;
    }

    private void Request(Map<String, String> params) {
        OkhttpUtil.okHttpPost(URLs.ChagePassWord, params, headerMap, new CallBackUtil<String>() {
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
                myToast("密码修改成功，请重新登录");
                requestOut(params);
                //清空数据
                localUserInfo.deleteUserInfo();
                //清除文件-压缩过的文件、拍照的文件
                FileUtils.deleteFilesInDir(FileUtil.getImageDownloadDir(ChangePasswordActivity.this));
                ActivityUtils.startActivity(LoginActivity.class);
                ActivityUtils.finishAllActivitiesExceptNewest();//结束除最新之外的所有 Activity
            }
        });

    }
    private void requestOut(Map<String, String> params) {
        OkhttpUtil.okHttpPost(URLs.LoginOut, params, headerMap, new CallBackUtil<String>() {
            @Override
            public String onParseResponse(Call call, Response response) {
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e, String err) {
                hideProgress();
//                myToast(err);
            }

            @Override
            public void onResponse(String response) {
                hideProgress();

            }
        });
    }

    @Override
    protected void updateView() {
        titleView.setTitle(getString(R.string.changepassword_h1));
    }
}
