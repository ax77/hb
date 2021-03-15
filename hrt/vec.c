#include "vec.h"

struct vec {
    void** data;
    size_t size;
    size_t alloc;
};

static void vec_check(struct vec* vec)
{
    assert(vec);
    assert(vec->data);
}

static void** vec_emptytab(size_t alloc)
{
    if (alloc == 0) {
        alloc = 1;
    }
    void** rv = hmalloc(sizeof(void*) * alloc);
    for (size_t i = 0; i < alloc; i += 1) {
        rv[i] = 0;
    }
    return rv;
}

void vec_add(struct vec *vec, void* ptr)
{
    vec_check(vec);
    if (vec->size >= vec->alloc) {
        vec->alloc += 1;
        vec->alloc *= 2;
        void** oldtb = vec->data;
        void** newtb = vec_emptytab(vec->alloc);
        for (size_t i = 0; i < vec->size; i += 1) {
            newtb[i] = oldtb[i];
        }
        free(vec->data);
        vec->data = newtb;
    }
    vec->data[vec->size] = ptr;
    vec->size += 1;
}

int vec_contains(struct vec *vec, void* ptr)
{
    vec_check(vec);
    for (size_t i = 0; i < vec->size; i += 1) {
        void* cur = vec_get(vec, i);
        if (cur == ptr) {
            return 1;
        }
    }
    return 0;
}

void* vec_get(struct vec *vec, size_t at_index)
{
    vec_check(vec);
    assert(at_index < vec->size);
    return vec->data[at_index];
}

void* vec_set(struct vec *vec, size_t at_index, void* new_value)
{
    vec_check(vec);
    assert(at_index < vec->size);

    void* old_value = vec->data[at_index];
    vec->data[at_index] = new_value;

    return old_value;
}

void* vec_remove_last(struct vec *vec)
{
    vec_check(vec);
    assert(vec->size > 0);
    void* result = vec->data[vec->size - 1];
    vec->size -= 1;
    return result;
}

void* vec_get_last(struct vec *vec)
{
    vec_check(vec);
    assert(vec->size > 0);
    return vec->data[vec->size - 1];
}

int vec_is_empty(struct vec *vec)
{
    vec_check(vec);
    return vec->size == 0;
}

size_t vec_size(struct vec *vec)
{
    vec_check(vec);
    return vec->size;
}

void vec_add_all(struct vec *dst, struct vec *src)
{
    vec_check(dst);
    vec_check(src);
    for (size_t i = 0; i < vec_size(src); i += 1) {
        void* ptr = vec_get(src, i);
        vec_add(dst, ptr);
    }
}

void vec_add_unique(struct vec *vec, void* ptr)
{
    vec_check(vec);
    if (vec_contains(vec, ptr)) {
        return;
    }
    vec_add(vec, ptr);
}

void vec_add_all_unique(struct vec *dst, struct vec *src)
{
    vec_check(dst);
    vec_check(src);
    for (size_t i = 0; i < vec_size(src); i += 1) {
        void* ptr = vec_get(src, i);
        vec_add_unique(dst, ptr);
    }
}

void vec_add_unique_assert_null(struct vec *dst, void *ptr)
{
    assert(dst);
    assert(ptr);
    if (vec_contains(dst, ptr)) {
        return;
    }
    vec_add(dst, ptr);
}

void vec_add_unique_ignore_null(struct vec *dst, void *ptr)
{
    assert(dst);
    if (ptr == NULL) {
        return;
    }
    if (vec_contains(dst, ptr)) {
        return;
    }
    vec_add(dst, ptr);
}

struct vec * vec_new()
{
    struct vec *rv = hmalloc(sizeof(struct vec));
    rv->size = 0;
    rv->alloc = 2;
    rv->data = vec_emptytab(rv->alloc);
    return rv;
}

void **vec_get_table(struct vec *vec)
{
    vec_check(vec);
    return vec->data;
}

void vec_free_self(struct vec *vec)
{
    vec_check(vec);
    free(vec->data);
    vec->data = NULL;
    vec->alloc = 0;
    vec->size = 0;
    free(vec);
}

void vec_sort(struct vec *vec, int (*cmp)(const void *, const void *))
{
    vec_check(vec);
    qsort(vec->data, vec->size, sizeof(void*), cmp);
}

