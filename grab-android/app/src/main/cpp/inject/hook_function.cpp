//
// Created by Administrator on 2017/8/12.
//
#include "hook_funciton.h"
#include "elfutils.h"
#include<sys/types.h>
#include<dirent.h>

volatile int miniteFlag = 0;



typedef int (*clock_gettime_fun)(int clock_id, timespec *tp);

clock_gettime_fun old_clock_gettime = NULL;

typedef int (*gettimeofday_fun)(struct timeval *tv, struct timezone *tz);

gettimeofday_fun old_gettimeofday = NULL;

typedef time_t (*time_fun)(time_t *timer);

time_fun old_time = NULL;


typedef int (*lrand48_fun)(void);
lrand48_fun old_lrand48 = NULL;

DIR* opendir (const char * path );
typedef DIR* (*opendir_fun)(const char * path);
opendir_fun old_opendir = NULL;

DIR* opendir_hook(const char * path) {
    DIR* t = old_opendir(path);
    DL_ERR("opendir_hook   %s", path);
    return t;
}


time_t time_hook(time_t *timer) {

    time_t t = old_time(timer);
    t += miniteFlag * 60;
//    DL_DEBUG("clock_gettime_hook   %d", t);
    return t;
}

int lrand48_hook() {

    int t = old_lrand48();

    DL_ERR("arc4random_hook   %d", t);
    t = 851401618;
    return t;
}

int clock_gettime_hook(int clock_id, timespec *tp) {
    timespec t;
    int flag;

    flag = old_clock_gettime(clock_id, &t);
    tp->tv_nsec = t.tv_nsec;
    tp->tv_sec = t.tv_sec + (miniteFlag * 60);
//    if (m != tp->tv_sec) {
//        m = tp->tv_sec;
//        if (n++ > 3) {
//            n = 0;
//            DL_DEBUG("clock_gettime_hook   %d", tp->tv_sec);
//        }
//    }
    return flag;
}

int gettimeofday_hook(struct timeval *tv, struct timezone *tz) {
    struct timeval tvn;

    int flag = old_gettimeofday(&tvn, tz);

    tv->tv_sec = tvn.tv_sec + (miniteFlag * 60);
    tv->tv_usec = tvn.tv_usec;

    return flag;
}

//////////////////////////////
struct hook_entry hook_entry11 = {"clock_gettime",
                           (void *) clock_gettime_hook,
                           (void **) &old_clock_gettime};

struct hook_entry hook_entry12 = {"gettimeofday",
                           (void *) gettimeofday_hook,
                           (void **) &old_gettimeofday};

struct hook_entry hook_entry13 = {"time",
                           (void *) time_hook,
                           (void **) &old_time};

struct hook_entry hook_entry14 = {"arc4random",
                           (void *) lrand48_hook,
                           (void **) &old_lrand48};

struct hook_entry hook_entry15 = {"opendir",
                           (void *) opendir_hook,
                           (void **) &old_opendir};

struct hook_entry *hook_entries1[] = {/*&hook_entry11,*/
        &hook_entry12,
        &hook_entry13,
        //&hook_entry15
};

struct hook_funs hook_fun1 = {
        2,//
        hook_entries1
};
struct Hook_lib hook_lib1 = {
        "libs3e_android.so",
        &hook_fun1
};
/////////////////////////////////////////////////////////////
typedef int (*arc4random_fun)(void);

arc4random_fun old_arc4random = NULL;

int arc4random_hook() {

    int t = old_arc4random();

    DL_ERR("arc4random_hook   %d", t);
    t = 851401618;
    return t;
}
struct hook_entry hook_entrya1 = {"arc4random",
                           (void *) arc4random_hook,
                           (void **) &old_arc4random};
struct hook_entry *hook_entries3[] = {/*&hook_entry11,*/
        &hook_entrya1,
};
struct hook_funs hook_fun3 = {
        1,//
        hook_entries3
};
struct Hook_lib hook_lib3 = {
        "libnative-lib.so",
        &hook_fun3
};
/////////////////////////////////////////////////////////////
struct hook_entry hook_entry21 = {"clock_gettime",
                           (void *) clock_gettime_hook,
                           (void **) &old_clock_gettime};

struct hook_entry *hook_entries2[] = {
        &hook_entry21
};
struct hook_funs hook_fun2 = {
        1,
        hook_entries2
};
struct Hook_lib hook_lib2 = {"libs3eOpenAl.so",
                      &hook_fun2
};

//////////////////////////////////////////
struct Hook_lib *hook_entries[] = {
        &hook_lib1,
        &hook_lib2
};

struct Hook_libs hook_libs2 = {
        2,
        hook_entries
};

struct Hook_lib *hook_entriesa[] = {
        &hook_lib3
};

struct Hook_libs hook_libs = {
        1,//2
        hook_entriesa
};