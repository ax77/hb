#include "map.h"

struct entry {
    void* key;
    void* val;
    struct entry * next;
};

struct hashmap {
    hash_f hash;
    equal_f equal;
    size_t size;
    size_t capacity;
    struct entry ** table;
    int threshold;
};

static void* entry_new(void* key, void* val, struct entry * next)
{
    struct entry * entry = (struct entry *) hmalloc(sizeof(struct entry));
    entry->key = key;
    entry->val = val;
    entry->next = next;
    return entry;
}

struct entry **empty_table(size_t capacity)
{
    assert(capacity);
    struct entry ** table = (struct entry **) hmalloc(sizeof(struct entry *) * capacity);
    for (size_t i = 0; i < capacity; i++) {
        table[i] = NULL;
    }
    return table;
}

struct hashmap* map_new(hash_f hash, equal_f equal)
{
    assert(hash);
    assert(equal);
    struct hashmap* map = (struct hashmap*) hmalloc(sizeof(struct hashmap));
    map->hash = hash;
    map->equal = equal;
    map->size = 0;
    map->capacity = 11;
    map->table = empty_table(map->capacity);
    map->threshold = map->capacity * (0.75f);
    return map;
}

static size_t hashmap_index(struct hashmap* self, void* key, size_t capacity)
{
    assert(self);
    assert(key);
    assert(capacity);
    return self->hash(key) % capacity;
}

void* map_get(struct hashmap* self, void* key)
{
    assert(self);
    assert(key);

    size_t index = hashmap_index(self, key, self->capacity);
    for (struct entry * e = self->table[index]; e; e = e->next) {
        if (self->equal(e->key, key)) {
            return e->val;
        }
    }
    return NULL;
}

void* map_put(struct hashmap* self, void* key, void* val)
{
    assert(self);
    assert(key);
    assert(val);

    size_t index = hashmap_index(self, key, self->capacity);

    // overwrite
    for (struct entry * e = self->table[index]; e; e = e->next) {
        if (self->equal(key, e->key)) {
            assert(0);
            e->val = val;
            return val;
        }
    }

    // resize
    if (self->size > self->threshold) {

        /// resize
        size_t new_capacity = self->capacity * 2 + 1;
        struct entry ** new_table = empty_table(new_capacity);
        for (size_t i = 0; i < self->capacity; i++) {
            struct entry * next;
            for (struct entry * e = self->table[i]; e; e = next) {
                next = e->next;
                size_t index = hashmap_index(self, e->key, new_capacity);
                e->next = new_table[index];
                new_table[index] = e;
            }
        }
        free(self->table);
        self->table = new_table;
        self->capacity = new_capacity;
        self->threshold = new_capacity * (0.75f);
        ///

        index = hashmap_index(self, key, self->capacity);
    }

    // add
    struct entry * new_entry = entry_new(key, val, self->table[index]);
    self->table[index] = new_entry;
    self->size++;
    return val;
}

size_t map_cap(struct hashmap* self)
{
    assert(self);
    return self->capacity;
}

struct entry ** map_table(struct hashmap* self)
{
    assert(self);
    assert(self->table);
    return self->table;
}

struct entry * entry_next(struct entry *e)
{
    assert(e);
    return e->next;
}

void * entry_val(struct entry *e)
{
    assert(e);
    return e->val;
}

void * entry_key(struct entry *e)
{
    assert(e);
    return e->key;
}

size_t map_size(struct hashmap* self)
{
    assert(self);
    return self->size;
}

void* map_remove(struct hashmap* self, void* key)
{
    assert(self);
    assert(key);

    int index = hashmap_index(self, key, self->capacity);
    struct entry* e = self->table[index];
    if (e == NULL) {
        return NULL;
    }
    struct entry* prev = NULL;
    struct entry* next;
    for (; e; prev = e, e = next) {
        next = e->next;
        if (self->equal(key, e->key)) {
            void* val = e->val;
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
