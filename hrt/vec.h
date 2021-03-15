#ifndef VEC_H_
#define VEC_H_

#include "headers.h"
#include "mem.h"

struct vec;
#define vec_of(T) struct vec

void vec_add(struct vec *vec, void* ptr);
int vec_contains(struct vec *vec, void* ptr);
void* vec_get(struct vec *vec, size_t at_index);
void* vec_remove_last(struct vec *vec);
void* vec_get_last(struct vec *vec);
int vec_is_empty(struct vec *vec);
size_t vec_size(struct vec *vec);
void vec_add_all(struct vec *dst, struct vec *src);
void vec_add_unique(struct vec *vec, void* ptr);
void vec_add_all_unique(struct vec *dst, struct vec *src);
void* vec_set(struct vec *vec, size_t at_index, void* new_value);
void vec_add_unique_assert_null(struct vec *dst, void *ptr);
void vec_add_unique_ignore_null(struct vec *dst, void *ptr);
struct vec *vec_new();
void vec_free_self(struct vec *vec);
void **vec_get_table(struct vec *vec);
void vec_sort(struct vec *vec, int (*cmp)(const void *, const void *));

#endif /* VEC_H_ */
