//
// Created by Administrator on 2017/8/12.
//

#ifndef LOADLIB_HOOK_FUNCITON_H
#define LOADLIB_HOOK_FUNCITON_H

#include <sys/time.h>
#include <time.h>
#ifdef __cplusplus
extern "C"
{
#endif

extern volatile  int miniteFlag;
struct hook_entry {
    const char *func_name;
    void *fn;
    void ** old_fn;
};

struct hook_funs{
    int len;
    struct hook_entry **fun_entry;
};

struct Hook_lib{
    const char *lib_name;
    struct hook_funs *funs_entry;
};

struct Hook_libs{
    int len;
    struct Hook_lib **lib_entry;
};

extern struct Hook_libs hook_libs;

time_t time_hook(time_t *timer);

#ifdef __cplusplus
}
#endif
#endif //LOADLIB_HOOK_FUNCITON_H
