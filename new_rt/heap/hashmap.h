#ifndef SCLIB_HASHMAP_H
#define SCLIB_HASHMAP_H

#include <stddef.h>
#include <stdint.h>

typedef size_t (*hash_fn)(void *key);
typedef int (*equal_fn)(void *a, void *b);
typedef int (*cmp_fn)(void *a, void *b);
typedef void (*free_fn)(void *ptr);

struct map_entry {
    void *key;
    void *val;
    struct map_entry *next;
};

struct hashmap {
    hash_fn hash;
    equal_fn equal;
    size_t size;
    size_t capacity;
    struct map_entry **table;
    size_t threshold;
};

struct hashmap* map_new(hash_fn hash, equal_fn equal);
void map_free(struct hashmap *self);
size_t map_size(struct hashmap *self);
int map_is_empty(struct hashmap *self);
void* map_get(struct hashmap *self, void *key);
void* map_put(struct hashmap *self, void *key, void *value);
void* map_remove(struct hashmap *self, void *key);
void map_clear(struct hashmap *self);
int map_contains(struct hashmap *self, void *key);
size_t hashmap_hash_ptr(void *ptr);
int hashmap_equal_ptr(void *a, void *b);

#endif /* SCLIB_HASHMAP_H */
