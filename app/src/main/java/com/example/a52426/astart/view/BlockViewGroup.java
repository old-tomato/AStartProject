package com.example.a52426.astart.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.example.a52426.astart.util.ASPoint;
import com.example.a52426.astart.util.AStartUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 52426 on 2017/9/7.
 */

public class BlockViewGroup extends ViewGroup {

    private static final String TAG = "zkl:" + BlockView.class.getSimpleName();
    private int needHeight;
    private int needWidth;
    private Context context;
    /**
     * 纵向的块数
     */
    private int hPieceCount;
    /**
     * 横向的块数
     */
    private int wPieceCount;
    private int blockSize;

    private BlockView.Position startPosition;
    private BlockView.Position endPosition;
    private BlockView[][] nodePosition;

    private int selectCount = 0;
//    private AStartUtil startUtil;
    private List<ASPoint> pointList;

    /**
     * 是否第一次开始布局
     */
    boolean firstFlag = true;

    public BlockViewGroup(Context context) {
        this(context , null);
    }

    public BlockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public BlockViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWillNotDraw(true);
        wPieceCount = 12;
        hPieceCount = 12;

        createNodeArray(wPieceCount , hPieceCount);
    }

    private void createNodeArray(int wPieceCount, int hPieceCount) {
        nodePosition = new BlockView[hPieceCount][wPieceCount];
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        needWidth = r - l;
        needHeight = b - t;
        blockSize = getChildSize();
        layoutChild(blockSize);
    }

    /**
     * 布局子View
     */
    private void layoutChild(int viewSize) {

        // 注意 这里的X Y 由于最开始的编写失误，导致意思为相反的
        if(firstFlag) {
            if(pointList == null)
            {
                pointList = new ArrayList<>();
            }else{
                pointList.clear();
            }

            for (int x = 0; x < hPieceCount; ++x) {
                for (int y = 0; y < wPieceCount; ++y) {
                    BlockView bv = new BlockView(context);
                    bv.setPosition(y, x);
                    bv.layout(viewSize * y, viewSize * x, viewSize * (y + 1), viewSize * (x + 1));
                    bv.setFlag((x * hPieceCount) + y);

                    // 设置随机的位置
                    double random = Math.random() * 10;
                    if (random < 2) {
                        bv.getPosition().status = BlockView.Position.NOT_ACCESS_POINT;
                        bv.setBackColor(Color.BLACK);
                        ASPoint point = new ASPoint(y, x);
                        Log.d("zkl", "java block point x: " + y + "  y : " + y);
                        pointList.add(point);
                    }

                    nodePosition[x][y] = bv;

                    bv.setOnClickCallBackListener(new BlockView.ClickCallBackListener() {
                        @Override
                        public void onCallBack(BlockView blockView, BlockView.Position position, boolean selectFlag) {
                            if (selectCount < 2) {
                                if (startPosition == null) {
                                    ++selectCount;
                                    startPosition = position;
                                    blockView.getPosition().status = BlockView.Position.START_POINT;
                                } else if (endPosition == null) {
                                    ++selectCount;
                                    endPosition = position;
                                    blockView.getPosition().status = BlockView.Position.END_POINT;
                                }
                            } else {
                                blockView.setBackColor(Color.WHITE);
                            }

                            if (startPosition != null && startPosition.pY == position.pY && startPosition.pX == position.pX) {
                                if (!selectFlag) {
                                    --selectCount;
                                    startPosition = null;
                                    blockView.getPosition().clearStatus();
                                }
                            }
                            if (endPosition != null && endPosition.pY == position.pY && endPosition.pX == position.pX) {
                                if (!selectFlag) {
                                    --selectCount;
                                    endPosition = null;
                                    blockView.getPosition().clearStatus();
                                }
                            }
                        }
                    });
                    addView(bv);
                }
            }
            firstFlag = false;
        }
    }

    private int getChildSize() {
        // 横向的宽度
        int wPiece = needWidth / wPieceCount;
        // 纵向的宽度
        int hPiece = needHeight / hPieceCount;

        // 比较
        return wPiece < hPiece ? wPiece : hPiece;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for(int x =0 ; x < getChildCount() ; ++ x)
        {
            View v = getChildAt(x);
            v.measure(blockSize , blockSize);
        }
        setMeasuredDimension(widthMeasureSpec , heightMeasureSpec);
    }

    public void clear() {
        for(int h = 0 ; h < hPieceCount ; ++ h)
        {
            for(int w = 0 ; w < wPieceCount ; ++ w)
            {
                BlockView blockView = nodePosition[h][w];
                blockView.getPosition().clearStatus();
                blockView.setBackColor(Color.WHITE);
                blockView.setGH(0 , 0);
                startPosition = null;
                endPosition = null;
                selectCount = 0;
            }
        }

        pointList.clear();

        for(int h = 0 ; h < hPieceCount ; ++ h)
        {
            for(int w = 0 ; w < wPieceCount ; ++ w)
            {
                BlockView blockView = nodePosition[h][w];
                double random = Math.random() * 10;
                if(random < 2){
                    blockView.getPosition().status = BlockView.Position.NOT_ACCESS_POINT;
                    blockView.setBackColor(Color.BLACK);
                    pointList.add(new ASPoint(blockView.getPosition().pX , blockView.getPosition().pY));
                }
            }
        }
    }

    public void start() {
        if(startPosition == null || endPosition == null){
            Log.w(TAG , "startPosition or endPosition is null");
            return;
        }
        Log.d(TAG , "start x; " + startPosition.pX + "   y: " + startPosition.pY + "   end x :" + endPosition.pX + "   end y : " + endPosition.pY);

        for(int h = 0 ; h < hPieceCount ; ++ h)
        {
            for(int w = 0 ; w < wPieceCount ; ++ w)
            {
                if(!(nodePosition[h][w].getPosition().status == BlockView.Position.START_POINT ||
                        nodePosition[h][w].getPosition().status == BlockView.Position.END_POINT ||
                        nodePosition[h][w].getPosition().status == BlockView.Position.NOT_ACCESS_POINT)){
                    nodePosition[h][w].setBackColor(Color.WHITE);
                }
            }
        }

        int sx = startPosition.pX;
        int sy = startPosition.pY;

        int ex = endPosition.pX;
        int ey = endPosition.pY;

//        startUtil = new AStartUtil();

        List<ASPoint> way = AStartUtil.getWay(sx, sy, ex, ey, wPieceCount, hPieceCount, pointList);

        Log.d(TAG , "size ==> " + way.size());

        for(ASPoint p : way){
            nodePosition[p.y][p.x].setBackColor(Color.GRAY);
        }

    }
}
