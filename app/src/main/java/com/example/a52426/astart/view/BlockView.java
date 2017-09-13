package com.example.a52426.astart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 52426 on 2017/9/7.
 */

public class BlockView extends View implements View.OnClickListener {

    private static final String TAG = "zkl:" + BlockView.class.getSimpleName();
    private Paint edgingPaint;
    private int needHeight;
    private int needWidth;
    private Paint backPaint;
    private int flag;
    private TextPaint tp;
    private float fontSize;
    private static final int EDGING_PAINT_WIDTH = 20;
    private static final int FONT_SIZE = 40;
    private Position position;

    private boolean selectFlag = false;

    /**
     * 从起点到我的距离
     */
    private int g;
    /**
     * 从我到目标的距离
     */
    private int h;
    /**
     * 总的需要消耗的时间
     */
    private int f;

    private ClickCallBackListener clickCallBackListener;

    public BlockView(Context context) {
        this(context , null);
    }

    public BlockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        position = new Position();
        setClickable(true);
        initPaint();
        initListener();
    }

    private void initListener() {
        setOnClickListener(this);
    }

    public interface ClickCallBackListener{
        void onCallBack(BlockView view, Position position, boolean selectFlag);
    }

    public void setOnClickCallBackListener(ClickCallBackListener callBackListener) {
        clickCallBackListener = callBackListener;
    }

    @Override
    public void onClick(View v) {
        if(position.status == Position.NOT_ACCESS_POINT){
            // DO NOTHING
            return;
        }
        if(!selectFlag){
            selectFlag = true;
            setBackColor(Color.GREEN);
        }else{
            selectFlag = false;
            setBackColor(Color.WHITE);
        }

        postInvalidate();

        if(clickCallBackListener != null){
            clickCallBackListener.onCallBack(this ,position , selectFlag);
        }
    }

    public void setFlag(int flag){
        this.flag = flag;
    }

    public void setBackColor(int color)
    {
        backPaint.setColor(color);
        postInvalidate();
    }

    public void setPosition(int x , int y)
    {
        position.pX = x;
        position.pY = y;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setGH(int h , int g)
    {
        this.h = h;
        this.g = g;
        f = h + g;
        postInvalidate();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        edgingPaint = new Paint();
        edgingPaint.setColor(Color.RED);
        edgingPaint.setAntiAlias(true);
        edgingPaint.setStrokeWidth(EDGING_PAINT_WIDTH);
        edgingPaint.setStyle(Paint.Style.STROKE);

        backPaint = new Paint();
        backPaint.setColor(Color.WHITE);
        backPaint.setAntiAlias(true);

        tp = new TextPaint();
        tp.setColor(Color.BLACK);
        tp.setAntiAlias(true);
        tp.setTextSize(FONT_SIZE);

        fontSize = tp.measureText("a");

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Log.d(TAG , "left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
        needWidth = right - left;
        needHeight = bottom - top;
        //Log.d(TAG , "needWidth: " + needWidth + "  needHeight: " + needHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 通过布局时候的宽度和高度设定当前的内容，如果没有，使用配置的属性信息
        if(needHeight != 0 && needWidth != 0) {
            int mHeight = MeasureSpec.makeMeasureSpec(needHeight , MeasureSpec.EXACTLY);
            int mWidth = MeasureSpec.makeMeasureSpec(needWidth , MeasureSpec.EXACTLY);
            setMeasuredDimension(mWidth , mHeight);
        }else {
            // 使用默认的信息
            super.onMeasure(widthMeasureSpec , heightMeasureSpec);
            needWidth = MeasureSpec.getSize(widthMeasureSpec);
            needHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 首先绘制背景
        RectF backRect = new RectF(0 , 0 , needWidth , needHeight);
        canvas.drawRect(backRect , backPaint);

        RectF edgingRect = new RectF(0 , 0 , needWidth , needHeight);
        canvas.drawRect(edgingRect , edgingPaint);

        canvas.drawText("x: " + position.pX , EDGING_PAINT_WIDTH , fontSize + EDGING_PAINT_WIDTH , tp);
        canvas.drawText("y: " + position.pY , EDGING_PAINT_WIDTH , needHeight - EDGING_PAINT_WIDTH , tp);
    }

    public class Position {
        public int pX;
        public int pY;

        public static final int START_POINT = 1;
        public static final int END_POINT = 2;
        /**
         * 阻碍状态
         */
        public static final int NOT_ACCESS_POINT = 3;

        public int status;

        public void clearStatus() {
            status = 0;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Position) {
                return (pX == ((Position) obj).pX && pY == ((Position) obj).pY);
            }else{
                return false;
            }
        }
    }
}
