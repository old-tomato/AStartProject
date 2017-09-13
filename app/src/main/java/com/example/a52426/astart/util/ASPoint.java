package com.example.a52426.astart.util;

/**
 * Created by 52426 on 2017/9/13.
 */

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

    public void setData(int x, int y, int h, int g) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ASPoint) {
            return ((ASPoint) obj).x == x && ((ASPoint) obj).y == y;
        }
        return false;
    }
}
