#ifndef HEAP_H_
#define HEAP_H_

#include "headers.h"
#include "mem.h"
#include "vec.h"
#include "map.h"
#include "../generated_types.h"

struct type_descr {
    char *description;
};

struct markable {
    void *ptr;
    size_t size;
    struct type_descr *datatype;
    int is_marked;
    size_t order_of_appearance;
};

void collect_locals();
struct markable *try_to_find_markable_by_ptr(void *from);
void * get_memory(size_t size, struct type_descr *datatype);
char * get_memory_strdup(char *s);
void dump_heap();

void initHeap();
void init_frames();

void open_frame();
void close_frame();
void reg_ptr_in_a_frame(void *ptr);

#endif
