#ifndef MAP_H_
#define MAP_H_

#include "headers.h"
#include "mem.h"

struct entry;
struct hashmap;

typedef size_t (*hash_f)(void* key);
typedef int (*equal_f)(void* key1, void* key2);

struct hashmap* map_new(hash_f hash, equal_f equal);
void * map_get(struct hashmap* self, void* key);
void * map_put(struct hashmap* self, void* key, void* value);
void* map_remove(struct hashmap* self, void* key);
size_t map_cap(struct hashmap* self);
struct entry ** map_table(struct hashmap* self);
struct entry * entry_next(struct entry *e);
void * entry_val(struct entry *e);
void * entry_key(struct entry *e);
size_t map_size(struct hashmap* self);

#endif
