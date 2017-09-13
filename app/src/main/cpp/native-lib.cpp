#include <jni.h>
#include <string>
#include <android/log.h>
#include <vector>
#include "AStarUtil.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jobject JNICALL
Java_com_example_a52426_astart_util_AStartUtil_getWay(JNIEnv *env, jobject instance, jint startX,
                                                      jint startY, jint endX, jint endY, jint wCount , jint hCount,
                                                      jobject blockList) {

    // 初始化工具类
    AStarUtil util(startX , startY , endX , endY);
    util.setWHCount(wCount , hCount);

    jclass blockListClazz = env->GetObjectClass(blockList);
    jmethodID sizeMethodId = env->GetMethodID(blockListClazz , "size" , "()I");
    // 获得block点的数量
    jint size = env->CallIntMethod(blockList , sizeMethodId);
    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "astart get size %d" , size);

    jmethodID getMethodId = env->GetMethodID(blockListClazz , "get" , "(I)Ljava/lang/Object;");

    // 获得ASPoint中的查找x，y的方法
    jclass aspointClazz = env->FindClass("com/example/a52426/astart/util/ASPoint");
    jfieldID xFieldId = env->GetFieldID(aspointClazz , "x" , "I");
    jfieldID yFieldId = env->GetFieldID(aspointClazz , "y" , "I");

    vector<Point> points;

    for(int i = 0 ; i < size ; ++ i)
    {
        // 找到需要的参数内容
        jobject po = env->CallObjectMethod(blockList , getMethodId , i);
        jint x = env->GetIntField(po , xFieldId);
        jint y = env->GetIntField(po , yFieldId);
        __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "%d block point x: %d , y %d" , i , x , y);

        // 准备组装成C++中的点类
        Point point(x , y);
        points.push_back(point);
    }

    util.setBlockPoint(points);
    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "before find");
    vector<Point> *pVector = util.find();
    __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "after find  size is : %d" , pVector->size());

    vector<Point>::iterator it = pVector->begin();
    int waySize = pVector->size();
    for(it ; it != pVector->end() ; ++ it)
    {
        __android_log_print(ANDROID_LOG_DEBUG , "JNI" , "find way [%d , %d]" , it->x , it->y);
    }


    jclass arrayListClazz = env->FindClass("java/util/ArrayList");
    jmethodID methodId = env->GetMethodID(arrayListClazz , "<init>" , "()V");
    jmethodID addMethoId = env->GetMethodID(arrayListClazz , "add" , "(Ljava/lang/Object;)Z");
    jobject returnList = env->NewObject(arrayListClazz , methodId);

    jmethodID pointMethodId = env->GetMethodID(aspointClazz , "<init>" , "(II)V");

    it = pVector->begin();
    for(it ; it != pVector->end() ; ++ it)
    {
        jobject aspointObj = env->NewObject(aspointClazz , pointMethodId , it->x , it->y);

        env->CallBooleanMethod(returnList , addMethoId , aspointObj);
    }
    
    return returnList;
}

JNIEXPORT jstring JNICALL
Java_com_example_a52426_astart_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

#ifdef __cplusplus
}
#endif