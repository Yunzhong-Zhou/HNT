package com.app.hnt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hnt.R;
import com.app.hnt.activity.MainActivity;
import com.app.hnt.base.BaseFragment;
import com.app.hnt.model.Fragment2Model;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;
import com.blankj.utilcode.util.BarUtils;
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
 * Created by fafukeji01 on 2016/1/6.
 * 设备
 */
public class Fragment2 extends BaseFragment {
    private RecyclerView recyclerView;
    List<Fragment2Model.ListBean> list = new ArrayList<>();
    CommonAdapter<Fragment2Model.ListBean> mAdapter;
    int page = 1;
    boolean loading = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        /*if (MainActivity.item == 1) {
            requestServer();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (MainActivity.item == 1) {
            page = 1;
            requestServer();
        }*/
    }

    /*@Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (MainActivity.item == 1) {
            requestServer();
        }
    }*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (MainActivity.isOver) {
            if (getUserVisibleHint()) {//此处不能用isVisibleToUser进行判断，因为setUserVisibleHint会执行多次，而getUserVisibleHint才是判断真正是否可见的
                if (MainActivity.item == 1) {
                    showLoadingPage();
                    requestServer();
                }
            }
        }
    }

    @Override
    protected void initView(View view) {
        findViewByID_My(R.id.headView).setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        setSpringViewMore(true);//需要加载更多
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
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


        //列表
        recyclerView = findViewByID_My(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
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

    @Override
    protected void updateView() {

    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();
        params.clear();
        params.put("page", page + "");
        params.put("count", "10");
        requestList(params);
    }

    private void requestList(Map<String, String> params) {
        OkhttpUtil.okHttpGet(URLs.Fragment2, params, headerMap, new CallBackUtil<Fragment2Model>() {
            @Override
            public Fragment2Model onParseResponse(Call call, Response response) {
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
            public void onResponse(Fragment2Model response) {
                hideProgress();
                if (page > 1) {
                    //加载更多
                    List<Fragment2Model.ListBean> newlist = new ArrayList<>();
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
                        mAdapter = new CommonAdapter<Fragment2Model.ListBean>(getActivity(),
                                R.layout.item_fragment2, list) {
                            @Override
                            protected void convert(ViewHolder holder, Fragment2Model.ListBean model, int position) {
                                holder.setText(R.id.textView1,model.getTitle());
                                holder.setText(R.id.textView2,model.getSn());
                                holder.setText(R.id.textView4,model.getEarning_money()+"HNT");
                            }
                        };
                        recyclerView.setAdapter(mAdapter);

                    } else
                        showEmptyPage();
                }
            }
        });
    }
}
