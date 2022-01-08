#include <assert.h>
#include <ctype.h>
#include <limits.h>
#include <stdarg.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hashmap.h"

struct hb_ptr {
    void *ptr;
    size_t size;
};

static struct hashmap *MANAGED_HEAP = NULL;
static size_t MIN_HEAP_PTR_ADDR = SIZE_MAX;
static size_t MAX_HEAP_PTR_ADDR = 0;
static size_t MANAGED_HEAP_CURRENT_SIZE = 0;
static size_t MANAGED_HEAP_BYTES_ALLOCATED_TOTAL = 0;
static size_t MANAGED_HEAP_BYTES_DEALLOCATED_TOTAL = 0;

static void* hb_alloc(size_t size) {
    assert(size > 0);
    assert(size < INT_MAX);
    void *ret = NULL;
    ret = calloc(1u, size);
    if (ret == NULL) {
        ret = calloc(1u, size);
        if (ret == NULL) {
            ret = calloc(1u, size);
        }
    }
    assert(ret);

    /// let's save the simple filter for future use.
    size_t iptr = (size_t) ret;
    if (iptr < MIN_HEAP_PTR_ADDR) {
        MIN_HEAP_PTR_ADDR = iptr;
    }
    if (iptr > MAX_HEAP_PTR_ADDR) {
        MAX_HEAP_PTR_ADDR = iptr;
    }

    /// put the pointer into the heap
    struct hb_ptr *mem_chunk = calloc(1u, sizeof(struct hb_ptr));
    assert(mem_chunk);
    mem_chunk->ptr = ret;
    mem_chunk->size = size;
    void *overwritten = map_put(MANAGED_HEAP, ret, mem_chunk);
    assert(overwritten == NULL);
    MANAGED_HEAP_CURRENT_SIZE += size;
    MANAGED_HEAP_BYTES_ALLOCATED_TOTAL += size;

    return ret;
}

static void drop_ptr(void **ptr, void *set) {
    assert(ptr);
    assert(*ptr);
    assert(set);
    // was set already, and possibly was freed too.
    if (*ptr == set) {
        return;
    }
    // primitive checking -
    // whether the address come from the heap
    size_t iptr = (size_t) *ptr;
    if (iptr < MIN_HEAP_PTR_ADDR) {
        return;
    }
    if (iptr > MAX_HEAP_PTR_ADDR) {
        return;
    }
    if (iptr == 0) {
        return;
    }

    // it must be a heap-allocated pointer.
    if (!map_contains(MANAGED_HEAP, *ptr)) {
        return;
    }

    // remove the chunk from the heap.
    struct hb_ptr *mem_chunk = map_remove(MANAGED_HEAP, *ptr);
    assert(mem_chunk);
    assert(mem_chunk->ptr);
    assert(mem_chunk->ptr == *ptr);
    assert(MANAGED_HEAP_CURRENT_SIZE > 0);
    MANAGED_HEAP_CURRENT_SIZE -= mem_chunk->size;
    MANAGED_HEAP_BYTES_DEALLOCATED_TOTAL += mem_chunk->size;

    // drop
    free(mem_chunk->ptr);
    free(mem_chunk);
    *ptr = set;
}

char* hstrdup(char *str) {
    assert(str);
    size_t len = strlen(str);
    char *rv = (char*) hb_alloc(len + 1);
    strcpy(rv, str);
    rv[len] = '\0';
    return rv;
}

int main(int argc, char **argv) {
    MANAGED_HEAP = map_new(hashmap_hash_ptr, hashmap_equal_ptr);

    char sentinel[1];
    char *str = hb_alloc(32);
    printf("%lu\n", MANAGED_HEAP_CURRENT_SIZE);

    drop_ptr((void**) &str, &sentinel);
    printf("%lu\n", MANAGED_HEAP_CURRENT_SIZE);
    assert(str == sentinel);

    printf("\n:ok:\n");
    return 0;
}

