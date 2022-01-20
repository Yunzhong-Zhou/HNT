package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.entity.ICandle;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;
import com.github.tifezh.kchartlib.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 主图的实现类
 * Created by tifezh on 2016/6/14.
 */

public class MainDraw implements IChartDraw<ICandle> {

    private float mCandleWidth = 0;
    private float mCandleLineWidth = 0;
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma20Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context mContext;

    private boolean mCandleSolid = true;//蜡烛是否实心

    public MainDraw(BaseKChartView view) {
        Context context = view.getContext();
        mContext = context;
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));
    }

    @Override
    public void drawTranslated(@Nullable ICandle lastPoint, @NonNull ICandle curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
        drawCandle(view, canvas, curX, curPoint.getHighPrice(), curPoint.getLowPrice(), curPoint.getOpenPrice(), curPoint.getClosePrice(), curPoint.getType());
        /*//画ma5
        if (lastPoint.getMA5Price() != 0) {
            view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getMA5Price(), curX, curPoint.getMA5Price());
        }
        //画ma10
        if (lastPoint.getMA10Price() != 0) {
            view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getMA10Price(), curX, curPoint.getMA10Price());
        }
        //画ma20
        if (lastPoint.getMA20Price() != 0) {
            view.drawMainLine(canvas, ma20Paint, lastX, lastPoint.getMA20Price(), curX, curPoint.getMA20Price());
        }*/
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        /*ICandle point = (IKLine) view.getItem(position);
        String text = "MA5:" + view.formatValue(point.getMA5Price()) + " ";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma5Paint.measureText(text);
        text = "MA10:" + view.formatValue(point.getMA10Price()) + " ";
        canvas.drawText(text, x, y, ma10Paint);
        x += ma10Paint.measureText(text);
        text = "MA20:" + view.formatValue(point.getMA20Price()) + " ";
        canvas.drawText(text, x, y, ma20Paint);*/

        //长按显示
        if (view.isLongPress()) {
            drawSelector(view, canvas);
        }


    }

    @Override
    public float getMaxValue(ICandle point) {
        return Math.max(point.getHighPrice(), point.getMA20Price());
    }

    @Override
    public float getMinValue(ICandle point) {
        return Math.min(point.getMA20Price(), point.getLowPrice());
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 画蜡烛
     *
     * @param canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
    private void drawCandle(BaseKChartView view, Canvas canvas, float x, float high, float low, float open, float close, String type) {
        Bitmap mBitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_b);
        Bitmap mBitmap0 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_s);

        int w1 = mBitmap1.getWidth();
        int h1 = mBitmap1.getHeight();

        int w0 = mBitmap0.getWidth();
        int h0 = mBitmap0.getHeight();

        high = view.getMainY(high);
        low = view.getMainY(low);
        open = view.getMainY(open);
        close = view.getMainY(close);
        float r = mCandleWidth / 2;
        float lineR = mCandleLineWidth / 2;
        if (open > close) {
            //实心
            if (mCandleSolid) {
                //画矩形
                canvas.drawRect(x - r, close, x + r, open, mRedPaint);
                canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint);

                if (type.equals("1")) {
//                    System.out.println(">>>>>>>>>>>" + type);
                    //画买入 b点
                    drawImage(canvas, mBitmap1, (int) (x - w1 / 2), (int) open, w1, h1, 0, 0);
                }
                if (type.equals("0")) {
//                    System.out.println(">>>>>>>>>>>" + type);
                    //画卖出 s点
                    drawImage(canvas, mBitmap0, (int) (x - w0 / 2), (int) close - h0, w0, h0, 0, 0);
                }
            } else {
                //画线
                mRedPaint.setStrokeWidth(mCandleLineWidth);
                canvas.drawLine(x, high, x, close, mRedPaint);
                canvas.drawLine(x, open, x, low, mRedPaint);
                canvas.drawLine(x - r + lineR, open, x - r + lineR, close, mRedPaint);
                canvas.drawLine(x + r - lineR, open, x + r - lineR, close, mRedPaint);
                mRedPaint.setStrokeWidth(mCandleLineWidth * view.getScaleX());
                canvas.drawLine(x - r, open, x + r, open, mRedPaint);
                canvas.drawLine(x - r, close, x + r, close, mRedPaint);
            }

        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, mGreenPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, mGreenPaint);

            if (type.equals("1")) {
//                System.out.println(">>>>>>>>>>>" + type);
                //画买入 b点
                drawImage(canvas, mBitmap1, (int) (x - w1 / 2), (int) close, w1, h1, 0, 0);
            }
            if (type.equals("0")) {
//                System.out.println(">>>>>>>>>>>" + type);
                //画卖出 s点
                drawImage(canvas, mBitmap0, (int) (x - w0 / 2), (int) open - h0, w0, h0, 0, 0);
            }

        } else {
            canvas.drawRect(x - r, open, x + r, close + 1, mRedPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint);

            if (type.equals("1")) {
//                System.out.println(">>>>>>>>>>>" + type);
                //画买入 b点
                drawImage(canvas, mBitmap1, (int) (x - w1 / 2), (int) open, w1, h1, 0, 0);
            }
            if (type.equals("0")) {
//                System.out.println(">>>>>>>>>>>" + type);
                //画卖出 s点
                drawImage(canvas, mBitmap0, (int) (x - w0 / 2), (int) open - h0, w0, h0, 0, 0);
            }

        }

    }

    /**
     * draw选择器
     *
     * @param view
     * @param canvas
     */
    private void drawSelector(BaseKChartView view, Canvas canvas) {
        Paint.FontMetrics metrics = mSelectorTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        int index = view.getSelectedIndex();
        float padding = ViewUtil.Dp2Px(mContext, 5);
        float margin = ViewUtil.Dp2Px(mContext, 5);
        float width = 0;
        float left;
        float top = margin + view.getTopPadding();
        float height = padding * 8 + textHeight * 9;

        ICandle point = (ICandle) view.getItem(index);
        List<String> strings = new ArrayList<>();
        strings.add(view.formatDateTime(view.getAdapter().getDate(index)));
        strings.add(mContext.getString(R.string.kchart_string1) + point.getHighPrice());
        strings.add(mContext.getString(R.string.kchart_string2) + point.getLowPrice());
        strings.add(mContext.getString(R.string.kchart_string3) + point.getOpenPrice());
        strings.add(mContext.getString(R.string.kchart_string4)+ point.getClosePrice());
        strings.add(mContext.getString(R.string.kchart_string5) + String.format("%.2f", (point.getClosePrice() - point.getOpenPrice())));
        strings.add(mContext.getString(R.string.kchart_string6) + String.format("%.2f", (point.getClosePrice() - point.getOpenPrice()) / point.getOpenPrice() * 100) + "%");
        strings.add(mContext.getString(R.string.kchart_string7) + (int) point.getAmount());

        for (String s : strings) {
            width = Math.max(width, mSelectorTextPaint.measureText(s));
        }
        width += padding * 2;

        float x = view.translateXtoX(view.getX(index));
        if (x > view.getChartWidth() / 2) {
            left = margin;//显示在左边
        } else {
            left = view.getChartWidth() - width - margin;//显示在右边
        }

        RectF r = new RectF(left, top, left + width, top + height);
        canvas.drawRoundRect(r, padding, padding, mSelectorBackgroundPaint);//画背景
        float y = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2;

        /*for (String s : strings) {
            canvas.drawText(s, left + padding, y, mSelectorTextPaint);//画文字
            y += textHeight + padding;
        }*/
        for (int i = 0; i < strings.size(); i++) {
            if (i == 5 || i == 6) {
                if ((point.getClosePrice() - point.getOpenPrice()) >= 0) {
                    setSelectorTextColor(ContextCompat.getColor(mContext, R.color.chart_red));
                    canvas.drawText(strings.get(i), left + padding, y, mSelectorTextPaint);//画文字-涨跌额
                } else {
                    setSelectorTextColor(ContextCompat.getColor(mContext, R.color.chart_green));
                    canvas.drawText(strings.get(i), left + padding, y, mSelectorTextPaint);//画文字-涨跌幅
                }
            } else {
                setSelectorTextColor(ContextCompat.getColor(mContext, R.color.chart_text));
                canvas.drawText(strings.get(i), left + padding, y, mSelectorTextPaint);//画文字
            }
            y += textHeight + padding;
        }

    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth
     */
    public void setCandleWidth(float candleWidth) {
        mCandleWidth = candleWidth;
    }

    /**
     * 设置蜡烛线宽度
     *
     * @param candleLineWidth
     */
    public void setCandleLineWidth(float candleLineWidth) {
        mCandleLineWidth = candleLineWidth;
    }

    /**
     * 设置ma5颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    /**
     * 设置ma20颜色
     *
     * @param color
     */
    public void setMa20Color(int color) {
        this.ma20Paint.setColor(color);
    }

    /**
     * 设置选择器文字颜色
     *
     * @param color
     */
    public void setSelectorTextColor(int color) {
        mSelectorTextPaint.setColor(color);
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize
     */
    public void setSelectorTextSize(float textSize) {
        mSelectorTextPaint.setTextSize(textSize);
    }

    /**
     * 设置选择器背景
     *
     * @param color
     */
    public void setSelectorBackgroundColor(int color) {
        mSelectorBackgroundPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        ma20Paint.setStrokeWidth(width);
        ma10Paint.setStrokeWidth(width);
        ma5Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        ma20Paint.setTextSize(textSize);
        ma10Paint.setTextSize(textSize);
        ma5Paint.setTextSize(textSize);
    }

    /**
     * 蜡烛是否实心
     */
    public void setCandleSolid(boolean candleSolid) {
        mCandleSolid = candleSolid;
    }


    /*---------------------------------
     * 绘制图片
     * @param       x屏幕上的x坐标
     * @param       y屏幕上的y坐标
     * @param       w要绘制的图片的宽度
     * @param       h要绘制的图片的高度
     * @param       bx图片上的x坐标
     * @param       by图片上的y坐标
     *
     * @return      null
     ------------------------------------*/

    public static void drawImage(Canvas canvas, Bitmap blt, int x, int y,
                                 int w, int h, int bx, int by) {
        Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形

        src.left = bx;
        src.top = by;
        src.right = bx + w;
        src.bottom = by + h;

        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, null, dst, null);
        src = null;
        dst = null;
    }
}
