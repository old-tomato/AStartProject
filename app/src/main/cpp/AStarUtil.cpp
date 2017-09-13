//
// Created by 52426 on 2017/9/10.
//

#include "AStarUtil.h"

#ifdef __cplusplus
// 要求使用c编译器进行编译
extern "C" {
#endif

AStarUtil::AStarUtil(int startX , int startY , int endX , int endY):startX(startX),startY(startY),endX(endX),endY(endY)
{
    blockPointV = new vector<Point>();
    openPointV = new vector<Point>();
    closePointV = new vector<Point>();
    pathPointV = new vector<Point>();
};

void AStarUtil::setBlockPoint(vector<Point> & blockPoint)
{
    this->blockPointV->resize(blockPoint.size());
    copy(blockPoint.begin() , blockPoint.end() , this->blockPointV->begin());
}

vector<Point> * AStarUtil::find()
{
    findWay(startX , startY);
    return pathPointV;
}

void AStarUtil::setWHCount(int wCount , int hCount)
{
    this->wCount = wCount;
    this->hCount = hCount;
}

void AStarUtil::findWay(int x , int y)
{
//    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "findWay [%d , %d]" , x , y);
    // 清空开放列表，准备下次使用
    this->openPointV->clear();

    Point currentPoint(x , y);
    this->closePointV->push_back(currentPoint);

    if(x == endX && y == endY)
    {
        this->pathPointV->push_back(Point(x , y));
        return;
    }

    // 找寻当前边缘的点
    for(int w = -1 ; w <= 1 ; ++ w)
    {
        for(int h = -1 ; h <= 1 ; ++ h)
        {
            int newX = w + x;
            int newY = h + y;
//            __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "newX newY [%d , %d]" , newX , newY);
            // 剔除不合格的内容
            if((newX >= 0 && newX < wCount) && (newY >= 0 && newY < hCount))
            {
                Point testPoint(newX , newY);
                const vector<Point>::iterator &f = std::find(closePointV->begin() , closePointV->end() , testPoint);
                // 当在关闭节点中没有找到的时候，说明当前的点可以进行判断
                if(f == closePointV->end())
                {
                    if(!isPointBlock(testPoint))
                    {
                        this->openPointV->push_back(testPoint);
                    }
                }
            }
        }
    }

//    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "===============!!!!===============");

    if(openPointV->size() == 0)
    {
        // 没有找到任何可以走的道路
        return ;
    }

//    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "openPointV count %d" , openPointV->size());
    vector<Point>::iterator it = openPointV->begin();

    for(it ; it != openPointV->end() ; ++ it)
    {
        // 打印内容
//        __android_log_print(ANDROID_LOG_DEBUG , "JNI open point" , "%d , %d" , (*it).x , (*it).y);
    }

//    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "==============================");

    Point nextPoint = searchAround(this->openPointV);
    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "next point [%d , %d]" , nextPoint.x , nextPoint.y);
    this->pathPointV->push_back(nextPoint);
    findWay(nextPoint.x , nextPoint.y);
}

Point AStarUtil::searchAround(vector<Point> * points)
{
//    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "searchAround");
    Point minPoint;
    minPoint.g = -1;
    minPoint.h = -1;
    int eX = endX;
    int eY = endY;

    int minF = -1;

    vector<Point>::iterator it = points->begin();
    for(it ; it != points->end() ; ++ it)
    {
//        __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "find minF");
        // 到起点的距离
        int g = abs((*it).x - startX) +  abs((*it).y - startY);
        // 到终点的距离
        int h = abs(eX - (*it).x) +  abs(eY - (*it).y);
        int f = g+ h;
//        __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "x: %d , y %d , g %d , h %d , f %d" , it->x , it->y , g , h , f);
        // 计算最短的点
        if(minF == -1)
        {
            minPoint.setData((*it).x , (*it).y , h , g);
            minF = f;
        }
        if(f < minF)
        {
            minPoint.setData((*it).x , (*it).y , h , g);
            minF = f;
        }
        if(minF == f)
        {
            if(minPoint.h > h)
            {
                minPoint.setData((*it).x , (*it).y , h , g);
            }
        }
    }
    return minPoint;
}

AStarUtil::~AStarUtil()
{
    // TODO 还需要释放资源

}

bool AStarUtil::isPointBlock(Point & point) {
    // 注意： 这里的find与本类中的find名称重复，需要通过命名空间区分开来
    const vector<Point>::iterator &fit = std::find(blockPointV->begin() , blockPointV->end() , point);
    return !(fit == blockPointV->end());
}

#ifdef __cplusplus
}
#endif