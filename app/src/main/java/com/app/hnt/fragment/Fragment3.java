package com.app.hnt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.hnt.R;
import com.app.hnt.activity.ChangePasswordActivity;
import com.app.hnt.activity.LoginActivity;
import com.app.hnt.activity.MainActivity;
import com.app.hnt.activity.MyTakeCashActivity;
import com.app.hnt.activity.SetAddressActivity;
import com.app.hnt.activity.SetTransactionPasswordActivity;
import com.app.hnt.activity.ShouYiActivity;
import com.app.hnt.activity.TakeCashActivity;
import com.app.hnt.base.BaseFragment;
import com.app.hnt.model.Fragment3Model;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;
import com.app.hnt.utils.FileUtil;
import com.app.hnt.utils.ShowDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FileUtils;
import com.liaoinstan.springview.widget.SpringView;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by fafukeji01 on 2016/1/6.
 * 我的
 */
public class Fragment3 extends BaseFragment {
    Fragment3Model model;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void onStart() {
//        MyLogger.i(">>>>>>>>onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        MyLogger.i(">>>>>>>>onResume");
        if (MainActivity.item == 2) {
            requestServer();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        MyLogger.i(">>>>>>>>onHiddenChanged>>>" + hidden);
        /*if (MainActivity.item == 4) {
            requestServer();
        }*/
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        MyLogger.i(">>>>>>>>setUserVisibleHint>>>" + isVisibleToUser);
        /*if (MainActivity.isOver) {
            if (getUserVisibleHint()) {//此处不能用isVisibleToUser进行判断，因为setUserVisibleHint会执行多次，而getUserVisibleHint才是判断真正是否可见的
                if (MainActivity.item == 2) {
                    requestServer();
                }
            }
        }*/
    }

    @Override
    protected void initView(View view) {
        findViewByID_My(R.id.relativeLayout).setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        //刷新
        setSpringViewMore(false);//不需要加载更多
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                requestCenter();
            }

            @Override
            public void onLoadmore() {

            }
        });


        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);

        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);
        textView5 = findViewByID_My(R.id.textView5);
        textView6 = findViewByID_My(R.id.textView6);
        textView7 = findViewByID_My(R.id.textView7);
        textView8 = findViewByID_My(R.id.textView8);
        textView9 = findViewByID_My(R.id.textView9);


        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        textView7.setOnClickListener(this);
        textView8.setOnClickListener(this);
        textView9.setOnClickListener(this);

        textView1.setText(localUserInfo.getNickname());
    }

    @Override
    protected void initData() {
        requestServer();
    }

    private void requestCenter() {
        OkhttpUtil.okHttpGet(URLs.Fragment3, params, headerMap, new CallBackUtil<Fragment3Model>() {
            @Override
            public Fragment3Model onParseResponse(Call call, Response response) {
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e, String err) {
                hideProgress();
                myToast(err);
            }

            @Override
            public void onResponse(Fragment3Model response) {
                hideProgress();
                model = response;
                textView1.setText(response.getNickname());
                textView2.setText(response.getAmount());
            }
        });
    }

    Bundle bundle = new Bundle();

    @Override
    public void onClick(View v) {
        bundle.clear();
        switch (v.getId()) {
            case R.id.textView3:
                //提币
                if (!model.isTradePassword()) {
                    ShowDialog.show(getActivity(), dialog, "交易密码",
                            "您还未设置交易密码，确认前往设置吗？",
                            "确认", "取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    ActivityUtils.startActivity(SetTransactionPasswordActivity.class);
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                }
                            });
                    return;
                }
                bundle.putSerializable("Fragment3Model", model);
                ActivityUtils.startActivity(bundle, TakeCashActivity.class);
                break;
            case R.id.textView4:
                //提币记录
                ActivityUtils.startActivity(MyTakeCashActivity.class);
                break;
            case R.id.textView5:
                //收益明细
                ActivityUtils.startActivity(ShouYiActivity.class);
                break;
            case R.id.textView6:
                //钱包地址
                if (!model.isTradePassword()) {
                    ShowDialog.show(getActivity(), dialog, "交易密码",
                            "您还未设置交易密码，确认前往设置吗？",
                            "确认", "取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    ActivityUtils.startActivity(SetTransactionPasswordActivity.class);
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                }
                            });
                    return;
                }
                bundle.putSerializable("Fragment3Model", model);
                ActivityUtils.startActivity(bundle, SetAddressActivity.class);
                break;
            case R.id.textView7:
                //修改密码
                ActivityUtils.startActivity(ChangePasswordActivity.class);
                break;
            case R.id.textView9:
                //交易密码
                ActivityUtils.startActivity(SetTransactionPasswordActivity.class);
                break;
            case R.id.textView8:
                ShowDialog.show(getActivity(), dialog, "退出",
                        "确认退出登录吗？",
                        "忍痛离开", "我再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
//                                showProgress(true, "正在注销登录，请稍候...");
                                requestOut(params);

                                //清空数据
                                localUserInfo.deleteUserInfo();

                                //清除文件-压缩过的文件、拍照的文件
                                FileUtils.deleteFilesInDir(FileUtil.getImageDownloadDir(getActivity()));
                                ActivityUtils.startActivity(LoginActivity.class);
                                ActivityUtils.finishAllActivitiesExceptNewest();//结束除最新之外的所有 Activity
                                getActivity().finish();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });
                break;
        }

    }

    @Override
    protected void updateView() {

    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();
//        showProgress(true, getString(R.string.app_loading));
        requestCenter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
}
