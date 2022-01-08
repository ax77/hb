#include "hashmap.h"

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

static const size_t kDefaultCapacity = 11;
static const float kLoadFactor = 0.75;

static void* cc_malloc(size_t size) {
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
    return ret;
}

static void* entry_new(void *key, void *val, struct map_entry *next) {
    struct map_entry *entry = (struct map_entry*) cc_malloc(
            sizeof(struct map_entry));
    entry->key = key;
    entry->val = val;
    entry->next = next;
    return entry;
}

static struct map_entry** empty_table(size_t capacity) {
    assert(capacity);

    struct map_entry **table = (struct map_entry**) cc_malloc(
            sizeof(struct map_entry*) * capacity);
    for (size_t i = 0; i < capacity; i++) {
        table[i] = NULL;
    }
    return table;
}

struct hashmap* map_new(hash_fn hash, equal_fn equal) {
    assert(hash);
    assert(equal);

    struct hashmap *hashmap = (struct hashmap*) cc_malloc(
            sizeof(struct hashmap));
    hashmap->hash = hash;
    hashmap->equal = equal;
    hashmap->size = 0;
    hashmap->capacity = kDefaultCapacity;
    hashmap->table = empty_table(hashmap->capacity);
    hashmap->threshold = hashmap->capacity * kLoadFactor;
    return hashmap;
}

void map_free(struct hashmap *self) {
    assert(self);

    map_clear(self);
    free(self->table);
    free(self);
}

size_t map_size(struct hashmap *self) {
    assert(self);
    return self->size;
}

int map_is_empty(struct hashmap *self) {
    assert(self);
    return self->size == 0;
}

static size_t hashmap_index(struct hashmap *self, void *key, size_t capacity) {
    return self->hash(key) % capacity;
}

void* map_get(struct hashmap *self, void *key) {
    assert(self);
    assert(key);

    size_t index = hashmap_index(self, key, self->capacity);
    for (struct map_entry *e = self->table[index]; e; e = e->next) {
        if (self->equal(e->key, key)) {
            return e->val;
        }
    }
    return NULL;
}

int map_contains(struct hashmap *self, void *key) {
    assert(self);
    assert(key);
    return map_get(self, key) != NULL;
}

void* map_put(struct hashmap *self, void *key, void *val) {
    assert(self);
    assert(key);

    size_t index = hashmap_index(self, key, self->capacity);

    // overwrite
    for (struct map_entry *e = self->table[index]; e; e = e->next) {
        if (self->equal(key, e->key)) {
            void *oldval = e->val;
            e->val = val;
            return oldval;
        }
    }

    // resize
    if (self->size > self->threshold) {

        //hashmap_resize(self);
        size_t new_capacity = self->capacity * 2 + 1;
        struct map_entry **new_table = empty_table(new_capacity);

        //hashmap_rehash(self, new_table, new_capacity);
        for (size_t i = 0; i < self->capacity; i++) {
            struct map_entry *next = NULL;
            for (struct map_entry *e = self->table[i]; e; e = next) {
                next = e->next;
                size_t index = hashmap_index(self, e->key, new_capacity);
                e->next = new_table[index];
                new_table[index] = e;
            }
        }

        free(self->table);
        self->table = new_table;
        self->capacity = new_capacity;
        //rehash-

        self->threshold = new_capacity * kLoadFactor;
        //resize-

        index = hashmap_index(self, key, self->capacity);
    }

    // add
    struct map_entry *new_entry = entry_new(key, val, self->table[index]);
    self->table[index] = new_entry;
    self->size++;
    return NULL;
}

void* map_remove(struct hashmap *self, void *key) {
    assert(self);
    assert(key);

    size_t index = hashmap_index(self, key, self->capacity);
    struct map_entry *e = self->table[index];
    if (e == NULL) {
        return NULL;
    }

    struct map_entry *prev = NULL;
    struct map_entry *next = NULL;
    for (; e; prev = e, e = next) {
        next = e->next;
        if (self->equal(key, e->key)) {
            void *val = e->val;
            if (prev == NULL) {
                self->table[index] = next;
            } else {
                prev->next = next;
            }
            self->size--;
            free(e);
            return val;
        }
    }
    return NULL;
}

void map_clear(struct hashmap *self) {
    assert(self);

    for (size_t i = 0; i < self->capacity; i++) {
        struct map_entry *e = self->table[i];
        if (e == NULL) {
            continue;
        }
        struct map_entry *next;
        for (; e; e = next) {
            next = e->next;
            free(e);
        }
        self->table[i] = NULL;
    }
    self->size = 0;
}

size_t hashmap_hash_ptr(void *ptr) {
    return (size_t) ptr;
}

int hashmap_equal_ptr(void *a, void *b) {
    return a == b;
}

