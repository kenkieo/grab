#include <jni.h>
#include <string>
#include <stdlib.h>

extern "C"
JNIEXPORT jstring

JNICALL Java_zj_test_scrapt_Tweet_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    int a = lrand48();
    char str[32];

    a = arc4random();
    memset(str, 0, sizeof(str));
    sprintf(str, "%d", a);
    return env->NewStringUTF(str);
}
