package com.app.hnt.fragment;

import android.os.Bundle;
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
import com.app.hnt.model.WebSocketModel;
import com.app.hnt.model.WebSocket_ListModel;
import com.app.hnt.net.URLs;
import com.app.hnt.okhttp.CallBackUtil;
import com.app.hnt.okhttp.OkhttpUtil;
import com.app.hnt.okhttp.websocket.IReceiveMessage;
import com.app.hnt.okhttp.websocket.WebSocketManager;
import com.app.hnt.utils.MyLogger;
import com.app.hnt.view.chart.DataHelper;
import com.app.hnt.view.chart.KChartAdapter;
import com.app.hnt.view.chart.KLineEntity;
import com.blankj.utilcode.util.BarUtils;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.google.gson.Gson;
import com.liaoinstan.springview.widget.SpringView;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by fafukeji01 on 2016/1/6.
 * 首页
 */
public class Fragment1_btc extends BaseFragment {
    Banner banner;
    TextView textView1, textView2, textView3, textView4, textView5;

    //走势图
    RelativeLayout rl_1min, rl_5min, rl_30min, rl_1h, rl_1day, rl_1mon;
    TextView tv_1min, tv_5min, tv_30min, tv_1h, tv_1day, tv_1mon;
    boolean isShowOver = false, isNew = true;
    //wss://api.hadax.com/ws
    //wss://api.huobi.pro/ws
    //wss://api-aws.huobi.pro/ws
    String url = "wss://api.hadax.com/ws",
            fenshi = "1min", id = "btcusdt",
            sub = "market." + id + ".kline." + fenshi;

    long tempTime = 0, from = 0, to = 0, num = 720, time = 60 * num;
    KChartView mKChartView;
    private KChartAdapter mAdapter;
    List<KLineEntity> datas = new ArrayList<>();
    List<KLineEntity> newlist = new ArrayList<>();

    KLineEntity kLineEntity;
    Gson mGson = new Gson();

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
                if (isShowOver) {
                    MyLogger.i(">>>加载更多");
                    //获取历史数据
                    isShowOver = false;
                    isNew = false;
                    to = from;
                    from = (Long) (to - time);
                    showHistory(from, to);
                }
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
        requestWebSocket();
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
        switch (v.getId()) {
            case R.id.rl_1min:
                //1分钟
                cancelDingYue();//先取消订阅-再订阅最新
                fenshi = "1min";
                changeUI();
                sub = "market." + id + ".kline." + fenshi;
                time = 60 * num;//60*12条
                changeKChart();
                break;
            case R.id.rl_5min:
                //5分钟
                cancelDingYue();//先取消订阅-再订阅最新
                fenshi = "5min";
                changeUI();
                sub = "market." + id + ".kline." + fenshi;
                time = 60 * 5 * num;//60*12条
                changeKChart();
                break;
            case R.id.rl_30min:
                //30分钟
                cancelDingYue();//先取消订阅-再订阅最新
                fenshi = "30min";
                changeUI();
                sub = "market." + id + ".kline." + fenshi;
                time = 60 * 30 * num;//60*12条
                changeKChart();
                break;
            case R.id.rl_1h:
                //1小时
                cancelDingYue();//先取消订阅-再订阅最新
                fenshi = "60min";
                changeUI();
                sub = "market." + id + ".kline." + fenshi;
                time = 60 * 60 * num;//60*12条
                changeKChart();
                break;
            case R.id.rl_1day:
                //1天
                cancelDingYue();//先取消订阅-再订阅最新
                fenshi = "1day";
                changeUI();
                sub = "market." + id + ".kline." + fenshi;
                time = 60 * 60 * 24 * num;//60*12条
                changeKChart();
                break;
            case R.id.rl_1mon:
                //1月
                cancelDingYue();//先取消订阅-再订阅最新
                fenshi = "1mon";
                changeUI();
                sub = "market." + id + ".kline." + fenshi;
                time = 60 * 60 * 24 * 30 * 60;//60条
                changeKChart();
                break;
        }
    }

    /**
     * 连接websocket、解析、展示
     */
    private void requestWebSocket() {
        isNew = true;
        MyLogger.i(">>>是否连接" + WebSocketManager.getInstance().isConnect());
        if (!WebSocketManager.getInstance().isConnect()) {//是否连接
            //没有连接时，开始连接
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*try {
                        synchronized (this) {
                            wait(2000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    WebSocketManager.getInstance().init(url, new IReceiveMessage() {
                        @Override
                        public void onConnectSuccess() {
                            MyLogger.i(">>>>>>连接成功");
                            changeKChart();
                        }

                        @Override
                        public void onConnectFailed() {
                            isShowOver = false;
                            MyLogger.i(">>>>>>连接失败");
                            WebSocketManager.getInstance().reconnect();//重连
                        }

                        @Override
                        public void onClose() {
                            isShowOver = false;
                            MyLogger.i(">>>>>>关闭成功");
                        }

                        @Override
                        public void onMessage(String text) {
//                        MyLogger.i("接收消息", text);
                            //得到心跳 {"ping":1592998031971}，发送心跳{"pong":1592998031971}
                            JSONObject jObj;
                            String ping = "";
                            try {
                                //解析数据
                                if (text.indexOf("ping") != -1) {
                                    //TODO 判断有无ping数据
                                    jObj = new JSONObject(text);
                                    ping = jObj.getString("ping");

                                    JSONObject jObj_pong = new JSONObject();
                                    //发送心跳
                                    jObj_pong.put("pong", ping);
                                    WebSocketManager.getInstance().sendMessage(jObj_pong.toString());
                                } else if (text.indexOf("data") != -1) {
                                    //TODO 判断有无data数据
//                                MyLogger.i("接收消息-历史记录", text);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //解析数据
                                            WebSocket_ListModel model = mGson.fromJson(text, WebSocket_ListModel.class);
                                            newlist.clear();
                                            for (WebSocket_ListModel.DataBean bean : model.getData()) {
                                                if (bean != null) {
                                                    kLineEntity = new KLineEntity(
                                                            bean.getId() + "",
                                                            (float) bean.getOpen(),
                                                            (float) bean.getHigh(),
                                                            (float) bean.getLow(),
                                                            (float) bean.getClose(),
                                                            (float) bean.getVol(),
                                                            (float) bean.getAmount(), 0, 0, 0, 0, 0, 0,
                                                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                                            "-1"
                                                    );
                                                    newlist.add(kLineEntity);
                                                }
                                            }
                                            datas.addAll(0, newlist);
                                            if (isNew) {//是新数据
//                                            MyLogger.i(">>>>>>历史新数据");
                                                tempTime = model.getData().get(newlist.size() - 1).getId();
//                                            mKChartView.setAdapter(mAdapter);
                                                mAdapter.changeData(datas);//更新数据
                                                isNew = false;
                                            } else {//不是新数据
//                                            MyLogger.i(">>>>>>历史更多数据");
                                                mAdapter.addFooterData(newlist);//添加尾部数据
                                            }
                                            mAdapter.notifyDataSetChanged();
                                            DataHelper.calculate(datas);//计算MA BOLL RSI KDJ MACD
                                            mKChartView.refreshComplete();//加载完成
                                            isShowOver = true;
//                                        MyLogger.i(">>>>>>"+mKChartView.getWidth());
                                        }
                                    });

                                } else {
//                                    MyLogger.i("接收消息-订阅数据", text);
                                    if (isNew == false) {//有了历史数据后展示最新数据
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //解析数据
                                                WebSocketModel model = mGson.fromJson(text, WebSocketModel.class);
                                                if (model != null && model.getTick() != null) {
//                                            MyLogger.i(">>>>>" + CommonUtil.timedate(model.getTick().getId() + ""));
                                                    kLineEntity = new KLineEntity(
                                                            model.getTick().getId() + "",
                                                            (float) model.getTick().getOpen(),
                                                            (float) model.getTick().getHigh(),
                                                            (float) model.getTick().getLow(),
                                                            (float) model.getTick().getClose(),
                                                            (float) model.getTick().getVol(),
                                                            (float) model.getTick().getAmount(), 0, 0, 0, 0, 0, 0,
                                                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                                            "-1"
                                                    );
                                                    newlist.clear();
                                                    newlist.add(kLineEntity);
                                                    if (tempTime != model.getTick().getId()) {
                                                        tempTime = model.getTick().getId();
                                                        datas.add(kLineEntity);
                                                        mAdapter.addHeaderData(newlist);//添加头部数据
                                                    } else {
                                                        if (datas.size() > 0) {
                                                            datas.set(datas.size() - 1, kLineEntity);
                                                            mAdapter.changeItem(datas.size() - 1, kLineEntity);//改变某个值
                                                        }
                                                    }
//                                            MyLogger.i(">>>>>"+datas.size());
//                                            mAdapter.notifyDataSetChanged();

                                                    DataHelper.calculate(datas);//计算MA BOLL RSI KDJ MACD
                                                    mKChartView.refreshComplete();//加载完成

                                                    //页面数据
                                                    String close = String.valueOf(model.getTick().getOpen());
                                                    String[] str = close.split("\\.");
                                                    textView1.setText(str[0]);
                                                    textView2.setText("." + str[1]);
                                                    textView3.setText("$" + model.getTick().getClose() + "");

                                                    double zd = (model.getTick().getClose() - model.getTick().getOpen()) / model.getTick().getOpen() * 100;

                                                    if (zd > 0) {
                                                        textView4.setText("+" + String.format("%.2f", zd) + "%");
                                                        textView4.setTextColor(getResources().getColor(R.color.green));
                                                    } else {
                                                        textView4.setText(String.format("%.2f", zd) + "%");
                                                        textView4.setTextColor(getResources().getColor(R.color.red));
                                                    }

                                                }

                                            }
                                        });
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();
        } else {
            //有连接-加载历史
            cancelDingYue();//先取消订阅-再订阅最新
            changeKChart();
        }
    }

    /**
     * 选择分时，更改k线图
     */
    private void changeKChart() {

        datas.clear();
        isShowOver = false;
        isNew = true;
        mKChartView.showLoading();//这里有调用onLoadMoreBegin，会加载一次数据
        from = (Long) (System.currentTimeMillis() / 1000 - time);
        to = (Long) System.currentTimeMillis() / 1000;

        showHistory(from, to);
    }

    /**
     * 取消订阅
     */
    private void cancelDingYue() {
        try {
            JSONObject jObj_dingyue = new JSONObject();
            jObj_dingyue.put("unsub", sub);
            jObj_dingyue.put("id", id);
            WebSocketManager.getInstance().sendMessage(jObj_dingyue.toString());
            MyLogger.i(">>>>>>>取消订阅：" + jObj_dingyue.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看历史数据
     */
    private void showHistory(Long from, Long to) {
        isShowOver = false;
        mKChartView.showLoading();//这里有调用onLoadMoreBegin，会加载一次数据
        try {
            JSONObject jObj_lishi = new JSONObject();
            jObj_lishi.put("req", sub);
            jObj_lishi.put("id", id);
            jObj_lishi.put("from", from);
            jObj_lishi.put("to", to);
            WebSocketManager.getInstance().sendMessage(jObj_lishi.toString());
            MyLogger.i(">>>>>>>历史数据提交：" + jObj_lishi.toString());

            //订阅前需要先取消订阅
            JSONObject jObj_dingyue = new JSONObject();
            jObj_dingyue.put("sub", sub);
            jObj_dingyue.put("id", id);
            WebSocketManager.getInstance().sendMessage(jObj_dingyue.toString());
            MyLogger.i(">>>>>>>订阅提交：" + jObj_dingyue.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateView() {

    }
    private void changeUI() {
        switch (fenshi) {
            case "1min":
                tv_1min.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "5min":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "30min":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "60min":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "1day":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                tv_1mon.setBackgroundResource(R.color.transparent);
                break;
            case "1mon":
                tv_1min.setBackgroundResource(R.color.transparent);
                tv_5min.setBackgroundResource(R.color.transparent);
                tv_30min.setBackgroundResource(R.color.transparent);
                tv_1h.setBackgroundResource(R.color.transparent);
                tv_1day.setBackgroundResource(R.color.transparent);
                tv_1mon.setBackgroundResource(R.drawable.yuanjiao_10_heise);
                break;
        }
    }

}
