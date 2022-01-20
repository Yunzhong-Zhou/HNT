package com.app.hnt.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hnt.R;
import com.app.hnt.activity.MainActivity;
import com.app.hnt.adapter.CircleImageAdapter;
import com.app.hnt.base.BaseFragment;
import com.app.hnt.model.Fragment1Model;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;
import com.app.hnt.utils.MyLogger;
import com.app.hnt.view.chart.DataHelper;
import com.app.hnt.view.chart.KChartAdapter;
import com.app.hnt.view.chart.KLineEntity;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.liaoinstan.springview.widget.SpringView;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by fafukeji01 on 2016/1/6.
 * 首页
 */
public class Fragment1 extends BaseFragment {
    Banner banner;
    TextView textView1, textView2, textView3, textView4, textView5;

    //走势图
    RelativeLayout rl_1min, rl_5min, rl_30min, rl_1h, rl_1day, rl_1mon;
    TextView tv_1min, tv_5min, tv_30min, tv_1h, tv_1day, tv_1mon;

    String url = "https://api1.binance.com/api/v3/klines",
            interval = "1m", symbol = "HNTUSDT", limit = "500";
    long endTime = TimeUtils.getNowMills(), startTime = TimeUtils.getNowMills() - 60 * 60 * 1000;

    KChartView mKChartView;
    private KChartAdapter mAdapter;
    List<KLineEntity> datas = new ArrayList<>();
    List<KLineEntity> newlist = new ArrayList<>();

    KLineEntity kLineEntity;
    Timer timer = new Timer();
    int count = 1;//计数60次

    private static final int COMPLETED = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                String[] str = open.split("\\.");
//                MyLogger.i(">>>>>>>>>最新数据:" + str[0]);
                textView1.setText(str[0]);
                textView2.setText("." + str[1]);
                textView3.setText("$" + lastPrice);

//                double zd = Double.valueOf(priceChangePercent);
                if (zhangdie >= 0) {
                    textView4.setText("+" + String.format("%.2f", zhangdie) + "%");
                    textView4.setTextColor(getResources().getColor(R.color.green));
                } else {
                    textView4.setText(String.format("%.2f", zhangdie) + "%");
                    textView4.setTextColor(getResources().getColor(R.color.red));
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.item == 0) {
//            requestServer();
//            requestWebSocket();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭连接
//        WebSocketManager.getInstance().close();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.stop();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /*if (MainActivity.item == 0) {
            requestServer();
        }*/
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (MainActivity.isOver) {
            if (getUserVisibleHint()) {//此处不能用isVisibleToUser进行判断，因为setUserVisibleHint会执行多次，而getUserVisibleHint才是判断真正是否可见的
                if (MainActivity.item == 0) {
                    //关闭连接
                    WebSocketManager.getInstance().close();
                    requestServer();
                    requestWebSocket();
                }
            }
        }*/
    }

    @Override
    protected void initView(View view) {
        findViewByID_My(R.id.headView).setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        setSpringViewMore(false);//需要加载更多
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                Request();
            }

            @Override
            public void onLoadmore() {
            }
        });
        banner = findViewByID_My(R.id.banner);
        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);
        textView5 = findViewByID_My(R.id.textView5);

        //k线图
        mKChartView = findViewByID_My(R.id.mKChartView);
        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        //设置表格行数
        mKChartView.setGridRows(4);
        //设置表格列数
        mKChartView.setGridColumns(4);
        mKChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
                /*KLineEntity data = (KLineEntity) point;
                MyLogger.i("index:" + index + " closePrice:" + data.getClosePrice());*/
            }
        });
        mKChartView.showLoading();//这里有调用onLoadMoreBegin，会加载一次数据
        mKChartView.setRefreshListener(new KChartView.KChartRefreshListener() {
            @Override
            public void onLoadMoreBegin(KChartView chart) {
                MyLogger.i(">>>加载更多");
                endTime = startTime;
                //获取历史数据
                switch (interval) {
                    case "1m":
                        startTime = endTime - 60 * 60 * 1000;
                        break;
                    case "5m":
                        startTime = endTime - 60 * 5 * 60 * 1000;
                        break;
                    case "30m":
                        startTime = endTime - 60 * 30 * 60 * 1000;
                        break;
                    case "1h":
                        startTime = endTime - 60 * 60 * 60 * 1000;
                        break;
                    case "1d":
                        startTime = endTime - 60 * 24 * 60 * 60 * 1000;
                        break;
                    case "1M":
                        startTime = endTime - 60 * 30 * 24 * 60 * 60 * 1000;
                        break;
                }
                requestKLineMore();
            }
        });
        //分时
        tv_1min = findViewByID_My(R.id.tv_1min);
        rl_1min = findViewByID_My(R.id.rl_1min);
        rl_1min.setOnClickListener(this);

        tv_5min = findViewByID_My(R.id.tv_5min);
        rl_5min = findViewByID_My(R.id.rl_5min);
        rl_5min.setOnClickListener(this);

        tv_30min = findViewByID_My(R.id.tv_30min);
        rl_30min = findViewByID_My(R.id.rl_30min);
        rl_30min.setOnClickListener(this);

        tv_1h = findViewByID_My(R.id.tv_1h);
        rl_1h = findViewByID_My(R.id.rl_1h);
        rl_1h.setOnClickListener(this);

        tv_1day = findViewByID_My(R.id.tv_1day);
        rl_1day = findViewByID_My(R.id.rl_1day);
        rl_1day.setOnClickListener(this);

        tv_1mon = findViewByID_My(R.id.tv_1mon);
        rl_1mon = findViewByID_My(R.id.rl_1mon);
        rl_1mon.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        requestServer();
        requestKLine();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                Log.e("TAG", "没隔1秒执行一次操作");
                count+=2;
                requestKLineUpDate();
//                Request();
            }
        }, 1000, 2 * 1000);
    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();
        if (isAdded()) {
            Request();
        }
    }

    private void Request() {
        params.clear();
        OkhttpUtil.okHttpGet(URLs.Fragment1, params, headerMap, new CallBackUtil<Fragment1Model>() {
            @Override
            public Fragment1Model onParseResponse(Call call, Response response) {
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e, String err) {
                hideProgress();
                myToast(err);
            }

            @Override
            public void onResponse(Fragment1Model response) {
                hideProgress();
                /**
                 * Banner
                 */
                banner.addBannerLifecycleObserver(getActivity())//添加生命周期观察者
                        .setLoopTime(3000)//设置轮播时间
//                .setBannerGalleryEffect(10, 10)//为banner添加画廊效果
                        .setUserInputEnabled(true)//手指可滑动
                        .setAdapter(new CircleImageAdapter(getActivity(), response.getBanner()))
//                        .setAdapter(new MultipleTypesAdapter(UserDetailActivity.this, list_img))
                        .setIndicator(new CircleIndicator(getActivity()))//圆形指示器
//                .setIndicatorGravity(IndicatorConfig.Direction.CENTER)//指示器位置
                        .start();
                //banner点击监听
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(Object data, int position) {

                        /*PhotoShowDialog photoShowDialog = new PhotoShowDialog(UserDetailActivity.this, list_img, position);
                        photoShowDialog.show();*/
                    }
                });
                //banner滑动监听
                banner.addOnPageChangeListener(new OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        endTime = TimeUtils.getNowMills();
        switch (v.getId()) {
            case R.id.rl_1min:
                //1分钟
                interval = "1m";
                startTime = endTime - 60 * 60 * 1000;
                break;
            case R.id.rl_5min:
                //5分钟
                interval = "5m";
                startTime = endTime - 60 * 5 * 60 * 1000;
                break;
            case R.id.rl_30min:
                //30分钟
                interval = "30m";
                startTime = endTime - 60 * 30 * 60 * 1000;
                break;
            case R.id.rl_1h:
                //1小时
                interval = "1h";
                startTime = endTime - 60 * 60 * 60 * 1000;
                break;
            case R.id.rl_1day:
                //1天
                interval = "1d";
                startTime = endTime - 60 * 24 * 60 * 60 * 1000;
                break;
            case R.id.rl_1mon:
                //1月
                interval = "1M";
                startTime = endTime - 60 * 30 * 24 * 60 * 60 * 1000;
                break;
        }
        changeUI();
    }


    @Override
    protected void updateView() {

    }

    private void changeUI() {
        switch (interval) {
            case "1m":
                tv_1min.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "5m":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "30m":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "1h":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "1d":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "1M":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                break;
        }

        requestKLine();
    }

    /**
     * 获取k线图-更新
     */
    private void requestKLine() {
        showProgress(true, getString(R.string.app_loading));
       /* params.clear();
        params.put("symbol",symbol);
        params.put("interval",interval);
        params.put("startTime",startTime+"");
        params.put("endTime",endTime+"");
        OkhttpUtil.okHttpGet(url,params, new CallBackUtil<String>() {
            @Override
            public String onParseResponse(Call call, Response response) {
                MyLogger.i(">>>>>>"+response);
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e, String err) {
                hideProgress();
                MyLogger.i(">>>>>>"+e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                MyLogger.i(">>>>>>"+response);
            }
        });*/
        String string = url + "?symbol=" + symbol
                + "&interval=" + interval
                + "&startTime=" + startTime
                + "&endTime=" + endTime;
        MyLogger.i(">>>>>>" + string);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(string)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                hideProgress();
                byte[] responseInfo = response.body().bytes();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = ConvertUtils.bytes2JSONArray(responseInfo);
                            MyLogger.i(">>>>>>>>>KLine:" + jsonArray.length());
                            if (jsonArray.length() > 0) {
                                datas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray arrays = ConvertUtils.bytes2JSONArray(ConvertUtils.string2Bytes(jsonArray.get(i).toString()));
//                        MyLogger.i(">>>>>>>>>KLineModel:" +arrays.get(0).toString());
                                    kLineEntity = new KLineEntity(
                                            arrays.get(0).toString(),
                                            Float.valueOf(arrays.get(1).toString()),
                                            Float.valueOf(arrays.get(2).toString()),
                                            Float.valueOf(arrays.get(3).toString()),
                                            Float.valueOf(arrays.get(4).toString()),
                                            Float.valueOf(arrays.get(5).toString()),
                                            Float.valueOf(arrays.get(7).toString()), 0, 0, 0, 0, 0, 0,
                                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                            "-1"
                                    );
                                    datas.add(kLineEntity);
                                }
                                mAdapter.changeData(datas);//更新数据
                                mAdapter.notifyDataSetChanged();
                                DataHelper.calculate(datas);//计算MA BOLL RSI KDJ MACD
                                mKChartView.refreshComplete();//加载完成

                                //页面数据
                                open = datas.get(0).Open + "";
                                lastPrice = datas.get(0).Close + "";
                                priceChangePercent = (datas.get(0).Close - datas.get(0).Open) / datas.get(0).Open * 100 + "";
                                //处理完成后给handler发送消息
                                Message msg = new Message();
                                msg.what = COMPLETED;
                                handler.sendMessage(msg);

                            } else {
                                myToast("暂无数据更新");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MyLogger.i(">>>>>>" + e.getMessage());
                hideProgress();
            }
        });
    }

    /**
     * 获取k线图-更多
     */
    private void requestKLineMore() {
//        showProgress(true, getString(R.string.app_loading));
        String string = url + "?symbol=" + symbol
                + "&interval=" + interval
                + "&startTime=" + startTime
                + "&endTime=" + endTime;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(string)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                hideProgress();
                byte[] responseInfo = response.body().bytes();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = ConvertUtils.bytes2JSONArray(responseInfo);
                            MyLogger.i(">>>>>>>>>KLine:" + jsonArray.length());
                            if (jsonArray.length() > 0) {
                                newlist.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray arrays = ConvertUtils.bytes2JSONArray(ConvertUtils.string2Bytes(jsonArray.get(i).toString()));
//                        MyLogger.i(">>>>>>>>>KLineModel:" +arrays.get(0).toString());
                                    kLineEntity = new KLineEntity(
                                            arrays.get(0).toString(),
                                            Float.valueOf(arrays.get(1).toString()),
                                            Float.valueOf(arrays.get(2).toString()),
                                            Float.valueOf(arrays.get(3).toString()),
                                            Float.valueOf(arrays.get(4).toString()),
                                            Float.valueOf(arrays.get(5).toString()),
                                            Float.valueOf(arrays.get(7).toString()), 0, 0, 0, 0, 0, 0,
                                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                            "-1"
                                    );
                                    newlist.add(kLineEntity);
                                }
                                datas.addAll(0, newlist);
                                mAdapter.addFooterData(newlist);//添加尾部数据
//                                mAdapter.changeData(datas);//更新数据
                                mAdapter.notifyDataSetChanged();
                                DataHelper.calculate(datas);//计算MA BOLL RSI KDJ MACD
                                mKChartView.refreshComplete();//加载完成
                            } else {
                                myToast("暂无更多数据");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MyLogger.i(">>>>>>" + e.getMessage());
                hideProgress();
            }
        });
    }

    /**
     * 获取k线图-最新一条数据
     */
    String open = "", lastPrice = "", priceChangePercent = "";

    private void requestKLineUpDate1() {
//        showProgress(true, getString(R.string.app_loading));
        String string = "https://api1.binance.com/api/v3/ticker/24hr" + "?symbol=" + symbol;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(string)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseInfo = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo);

                            kLineEntity = new KLineEntity(
                                    jsonObject.getString("closeTime"),
                                    Float.valueOf(jsonObject.getString("openPrice")),
                                    Float.valueOf(jsonObject.getString("highPrice")),
                                    Float.valueOf(jsonObject.getString("lowPrice")),
                                    Float.valueOf(jsonObject.getString("lastPrice")),
                                    Float.valueOf(jsonObject.getString("count")),
                                    Float.valueOf(jsonObject.getString("volume")), 0, 0, 0, 0, 0, 0,
                                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    "-1"
                            );
                            datas.set(0, kLineEntity);
                            mAdapter.changeItem(0, kLineEntity);//改变某个值
                            mAdapter.notifyDataSetChanged();
                            DataHelper.calculate(datas);//计算MA BOLL RSI KDJ MACD
                            mKChartView.refreshComplete();//加载完成

                            //页面数据
                            open = jsonObject.getString("openPrice");
                            lastPrice = jsonObject.getString("lastPrice");
                            priceChangePercent = jsonObject.getString("priceChangePercent");
                            //处理完成后给handler发送消息
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            handler.sendMessage(msg);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
        });
    }

    double zhangdie = 0;
    private void requestKLineUpDate() {
        String string = url + "?symbol=" + symbol
                + "&interval=" + "1m"
                + "&startTime=" + (TimeUtils.getNowMills() - 60 * 1000)
                + "&endTime=" + TimeUtils.getNowMills();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(string)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                hideProgress();
                byte[] responseInfo = response.body().bytes();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = ConvertUtils.bytes2JSONArray(responseInfo);

                            if (jsonArray.length() > 0) {
                                datas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray arrays = ConvertUtils.bytes2JSONArray(ConvertUtils.string2Bytes(jsonArray.get(i).toString()));
                                    MyLogger.i(">>>>>>>>>KLineModel:" + jsonArray.get(i).toString());
                                    kLineEntity = new KLineEntity(
                                            arrays.get(0).toString(),
                                            Float.valueOf(arrays.get(1).toString()),
                                            Float.valueOf(arrays.get(2).toString()),
                                            Float.valueOf(arrays.get(3).toString()),
                                            Float.valueOf(arrays.get(4).toString()),
                                            Float.valueOf(arrays.get(5).toString()),
                                            Float.valueOf(arrays.get(7).toString()), 0, 0, 0, 0, 0, 0,
                                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                            "-1"
                                    );
                                    datas.add(kLineEntity);
                                }
                                if (count >= 60) {
                                    count = 1;
                                    //添加一条
                                    newlist.clear();
                                    newlist.add(kLineEntity);
                                    datas.add(kLineEntity);
                                    mAdapter.addHeaderData(newlist);//添加头部数据
                                } else {
                                    //修改最新
                                    datas.set(0, kLineEntity);
                                    mAdapter.changeItem(0, kLineEntity);//改变某个值
                                }

                                mAdapter.notifyDataSetChanged();
                                DataHelper.calculate(datas);//计算MA BOLL RSI KDJ MACD
                                mKChartView.refreshComplete();//加载完成

                                //页面数据
                                open = datas.get(0).Open + "";
                                lastPrice = datas.get(0).Close + "";
                                zhangdie = (datas.get(0).Close - datas.get(0).Open) / datas.get(0).Open * 100;
                                //处理完成后给handler发送消息
                                Message msg = new Message();
                                msg.what = COMPLETED;
                                handler.sendMessage(msg);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MyLogger.i(">>>>>>" + e.getMessage());
                hideProgress();
            }
        });
    }
}
