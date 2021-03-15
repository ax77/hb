#include "mem.h"

void* hmalloc(size_t size)
{
    if (size == 0) {
        size = 1;
    }
    assert(size < INT_MAX);

    void *ptr = NULL;
    ptr = malloc(size);
    if (ptr == NULL) {
        ptr = malloc(size);
        if (ptr == NULL) {
            ptr = malloc(size);
        }
    }
    assert(ptr);
    return ptr;
}

void* hrealloc(void* old, size_t newsize)
{
    void *ptr = NULL;
    ptr = realloc(old, newsize);
    if (ptr == NULL) {
        ptr = realloc(old, newsize);
        if (ptr == NULL) {
            ptr = realloc(old, newsize);
        }
    }
    assert(ptr);
    return ptr;
}

void *hcalloc(size_t count, size_t eltsize)
{
    assert(count);
    assert(eltsize);

    void* ptr = NULL;
    ptr = calloc(count, eltsize);
    if (ptr == NULL) {
        ptr = calloc(count, eltsize);
        if (ptr == NULL) {
            ptr = calloc(count, eltsize);
        }
    }
    assert(ptr);
    return ptr;
}
