package com.example.a52426.astart.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 52426 on 2017/9/7.
 */

public class AStartUtil {

    private static final String TAG = "zkl:" + AStartUtil.class.getSimpleName();

    private int xLength;
    private int yLength;
    private final ASPoint[][] ASPoints;
    private ASPoint startASPoint;
    private ASPoint endPoint;
    /**
     * 不在进行判断的节点
     */
    private final List<ASPoint> closeList;
    /**
     * 将要进行判断的节点
     */
    private final List<ASPoint> openList;
    /**
     * 确定的节点
     */
    private final List<ASPoint> pathList;
    private int nextX;
    private int nextY;
    private OnBlockKindCheckListener onBlockKindCheckListener;

    /**
     * DEBUG用的模式，如果为真，就表示需要调用next方法查看下一个动作
     */
    private boolean debugMode = false;

    public class ASPoint {
        public int x;
        public int y;
        /**
         * 该点到目标的距离
         */
        public int h;
        /**
         * 该点到起点的距离
         */
        public int g;


        public ASPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public ASPoint() {
        }

        public void setData(int x , int y , int h , int g){
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ASPoint){
                return ((ASPoint) obj).x == x && ((ASPoint) obj).y == y;
            }
            return false;
        }
    }

    public AStartUtil(int x, int y) {
        this.xLength = x;
        this.yLength = y;
        ASPoints = new ASPoint[yLength][xLength];
        closeList = new ArrayList<>();
        openList = new ArrayList<>();
        pathList = new ArrayList<>();
    }

    public void setStartPoint(int x , int y){
        startASPoint = new ASPoint(x , y);
    }

    public void setEndPoint(int x , int y){
        endPoint = new ASPoint(x , y);
    }

    public  List<ASPoint> findWay(){
        searchAround(startASPoint.x , startASPoint.y);
        return pathList;
    }

    public interface OnBlockKindCheckListener{
        boolean canAccess(int x, int y);
    }

    public void seteOnBlockKindCheckListener(OnBlockKindCheckListener listener){
        onBlockKindCheckListener = listener;
    }

    public void setDebugMode(boolean debugMode){
        this.debugMode = debugMode;
    }

    private void searchAround(int x, int y) {
        Log.d(TAG , "search x:" + x + "   y: " + y);
        // 清空之前的开放列表
        openList.clear();
        // 将自身放入到关闭节点中
        closeList.add(new ASPoint(x , y));

        if(x == endPoint.x && y == endPoint.y){
            // 全部完成了
            pathList.add(new ASPoint(x , y));
            return;
        }

        ASPoint tempPoint = new ASPoint();
        for(int h = -1 ; h <= 1 ; ++ h ) {
            for(int w = -1 ; w <= 1 ; ++ w) {
                int newX = x + w;
                int newY = y + h;

                tempPoint.x = newX;
                tempPoint.y = newY;

                if((newX >= 0 && newY >= 0) && (newX < xLength && newY < yLength)){
                    // 判断是否在关闭节点中
                    if(!closeList.contains(tempPoint)){
                        // 首先检查是否不能够通过
                        if(onBlockKindCheckListener != null){
                            boolean canAccess = onBlockKindCheckListener.canAccess(newX , newY);
                            if(canAccess){
                                // 将节点放入到开放列表中
                                openList.add(new ASPoint(newX , newY));
                            }else{
                                closeList.add(new ASPoint(newX , newY));
                            }
                        }else{
                            // 将节点放入到开放列表中
                            openList.add(new ASPoint(newX , newY));
                        }
                    }
                }
            }
        }

        if(openList.size() == 0){
            // 不可达状态，可能存在问题
            return;
        }

        ASPoint nextPoint = getNextPoint(openList);
        pathList.add(nextPoint);
        closeList.add(nextPoint);

        nextX = nextPoint.x;
        nextY = nextPoint.y;
        if(!debugMode){
            Log.d(TAG , "next x: " + nextPoint.x + "   y: " + nextPoint.y);
            searchAround(nextPoint.x , nextPoint.y);
        }
    }

    public List<ASPoint> next(){
        searchAround(nextX , nextY);
        return pathList;
    }

    private ASPoint getNextPoint(List<ASPoint> openList) {

        int startX = startASPoint.x;
        int startY = startASPoint.y;
        int endX = endPoint.x;
        int endY = endPoint.y;

        ASPoint minPoint = new ASPoint();
        int minF = -1;

        for(ASPoint point : openList){
            int g = Math.abs(point.x - startX) + Math.abs(point.y - startY);
            int h = Math.abs(endX - point.x) + Math.abs(endY - point.y);
            int f = g + h;

            //Log.d(TAG , "x: " + point.x + "  y: " + point.y + " g: " + g + "  h: " + h + "  f: " + f);

            if(minF == -1){
                minPoint.setData(point.x , point.y , h , g);
                minF = f;
            }else if(minF > f){
                minPoint.setData(point.x , point.y , h , g);
                minF = f;
            }else if((minF == f) && (minPoint.h > h)){
                minPoint.setData(point.x , point.y , h , g);
                minF = f;
            }

        }
        return minPoint;
    }

}
