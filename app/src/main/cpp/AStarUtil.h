//
// Created by 52426 on 2017/9/10.
//

#ifndef ASTART_ASTARUTIL_H
#define ASTART_ASTARUTIL_H

#include <vector>
#include <functional>
#include <algorithm>
#include <android/log.h>
#include <cmath>

using namespace std;

/**
 * 这是一个使用C++实现的简单的A*寻路算法
 */

struct Point
{
    int x;
    int y;
    // 到终点的距离
    int h;
    // 到起点的距离
    int g;
    Point(){}
    Point(int x , int y):x(x),y(y){}
    bool operator==(const Point & p)
    {
        return p.x == x && p.y == y;
    }
    void setData(int x , int y , int h , int g)
    {
        this->x = x;
        this->y = y;
        this->h = h;
        this->g = g;

    }
};

class AStarUtil {

private:
    /**
     * 横向的块数
     */
    int wCount;
    /**
     * 纵向的块数
     */
    int hCount;
    /**
     * 开始的x
     */
    int startX;
    /**
     * 开始的y
     */
    int startY;
    /**
     * 结束的x
     */
    int endX;
    /**
     * 结束的y
     */
    int endY;

    /**
     * 存放障碍物节点
     */
    vector<Point> * blockPointV;
    /**
     * 存放当前的打开节点
     */
    vector<Point> * openPointV;
    /**
     * 存放已经测试过的无效节点
     */
    vector<Point> * closePointV;
    /**
     * 最后的结尾
     */
    vector<Point> * pathPointV;

private:
    /**
     * 正式开始进行搜索
     * @param x
     * @param y
     */
    void findWay(int x , int y);
    /**
     * 搜索列表中各个点到达终点的距离
     * @param points
     */
    Point searchAround(vector<Point> * points);
    /**
     * 判断当前的点是否是障碍点
     * @param point
     * @return
     */
    bool isPointBlock(Point & point);
public:
    AStarUtil(int startX , int startY , int endX , int endY);

    /**
     * 设置当前的障碍位置
     * @param blockPoint
     */
    void setBlockPoint(vector<Point> & blockPoint);

    /**
     * 设置宽和高的总的块数
     * @param wCount
     * @param hCount
     */
    void setWHCount(int wCount , int hCount);

    /**
     * 开始找寻当前的位置
     */
    vector<Point> * find();

    /**
     * 析构函数
     */
    ~AStarUtil();
};


#endif //ASTART_ASTARUTIL_H
