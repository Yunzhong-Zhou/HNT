package com.app.hnt.activity;

import android.os.Bundle;

import com.app.hnt.R;
import com.app.hnt.base.BaseActivity;
import com.app.hnt.model.ShouYiModel;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;
import com.liaoinstan.springview.widget.SpringView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zyz on 2017/9/5.
 * 收益明细
 */

public class ShouYiActivity extends BaseActivity {
    private RecyclerView recyclerView;
    List<ShouYiModel.ListBean> list = new ArrayList<>();
    CommonAdapter<ShouYiModel.ListBean> mAdapter;
    int page = 1;
    boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytakecash);
    }

    @Override
    protected void initView() {
        setSpringViewMore(true);//需要加载更多
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                page = 1;
                requestServer();
            }

            @Override
            public void onLoadmore() {
                loading = true;
                page++;
                requestServer();
            }
        });

        recyclerView = findViewByID_My(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!loading && !recyclerView.canScrollVertically(0)){
                    loading = true;
                    page++;
                    requestServer();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //向下滚动
                {
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount = manager.getItemCount();
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        page++;
                        requestServer();
                    }
                }
            }
        });

    }

    @Override
    protected void initData() {
        //获取列表数据
        showLoadingPage();
        requestServer();
    }
    private void requestList(Map<String, String> params) {
        OkhttpUtil.okHttpGet(URLs.ShouYi, params, headerMap, new CallBackUtil<ShouYiModel>() {
            @Override
            public ShouYiModel onParseResponse(Call call, Response response) {
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e, String err) {
                hideProgress();
                if (page > 1) page--;
                else showErrorPage();
                myToast(err);
            }

            @Override
            public void onResponse(ShouYiModel response) {
                hideProgress();
                if (page > 1) {
                    //加载更多
                    List<ShouYiModel.ListBean> newlist = new ArrayList<>();
                    newlist = response.getList();
                    if (newlist != null && newlist.size() > 0) {
                        list.addAll(newlist);
                        mAdapter.notifyDataSetChanged();
//                        adapter.notifyItemRangeInserted(list.size() - 1, newlist.size());
                    } else {
                        page--;
                        myToast("没有更多了");
                    }
                    loading = false;

                } else {
                    //更新
                    list = response.getList();
                    if (list != null && list.size() > 0) {
                        showContentPage();
                        mAdapter = new CommonAdapter<ShouYiModel.ListBean>(ShouYiActivity.this,
                                R.layout.item_mytakecash, list) {
                            @Override
                            protected void convert(ViewHolder holder, ShouYiModel.ListBean model, int position) {
                                holder.setText(R.id.textView1,model.getSn());
                                holder.setText(R.id.textView2,model.getEarning_money());
                                holder.setText(R.id.textView3,model.getCreatedAt());
//                                holder.setText(R.id.textView4,model.getId());
                            }
                        };
                        recyclerView.setAdapter(mAdapter);

                    } else
                        showEmptyPage();
                }
            }
        });
    }
    @Override
    protected void updateView() {
        titleView.setTitle("收益明细");
    }

    @Override
    public void requestServer() {
        super.requestServer();
        params.clear();
        params.put("page", page + "");
        params.put("count", "10");
        requestList(params);
    }
}
