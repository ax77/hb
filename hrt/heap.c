#include "heap.h"

static struct hashmap *THE_HEAP = NULL;
static int THE_HEAP_IS_INIT = 0;

static const size_t BYTES_LIMIT_GC = 32;
static size_t BYTES_ALLOCATED = 0;
static size_t BYTES_COLLECTED = 0;

static size_t order_of_appearance = 0;

vec_of("vec of pointers") *FRAMES = NULL;
static int FRAMES_INIT = 0;

/// https://stackoverflow.com/questions/23689687/sorting-an-array-of-struct-pointers-using-qsort
///
/// Your code suggests that you are actually trying sort an array of pointers to struct.
///
/// In this case you are missing a level of indirection. You also have your directions reversed.
///
/// Your compare_data routine would be fine for reverse sorting an array of structs, but you wish to sort an array of pointers based on what they point to.
///
/// int compare_pointed_to_data(const void* a, const void* b) {
///     // a is a pointer into the array of pointers
///     struct access_data *ptr_to_left_struct = *(access_data**)a;
///     struct access_data *ptr_to_right_struct = *(access_data**)b;
///
///     if ( ptr_to_left_struct->sector < ptr_to_right_struct->sector)
///         return -1;
///     else if (ptr_to_left_struct->sector > ptr_to_right_struct->sector)
///         return 1;
///     else
///         return 0;
/// }

int markable_sort(const void *lhs, const void *rhs)
{

    //    struct markable *a = (struct markable *) lhs;
    //    struct markable *b = (struct markable *) rhs;
    //
    //    if (a->order_of_appearance > b->order_of_appearance) {
    //        return -1;
    //    }
    //    if (a->order_of_appearance < b->order_of_appearance) {
    //        return 1;
    //    }
    //    return 0;

    // a is a pointer into the array of pointers
    struct markable *ptr_to_left_struct = *(struct markable**) lhs;
    struct markable *ptr_to_right_struct = *(struct markable**) rhs;

    if (ptr_to_left_struct->order_of_appearance < ptr_to_right_struct->order_of_appearance) {
        return -1;
    }

    if (ptr_to_left_struct->order_of_appearance > ptr_to_right_struct->order_of_appearance) {
        return 1;
    }

    return 0;
}

static size_t hashmap_hash_ptr(void* key)
{
    return (size_t) key;
}

static int hashmap_equal_ptr(void* key1, void* key2)
{
    return key1 == key2;
}

void initHeap()
{
    assert(!THE_HEAP_IS_INIT);
    THE_HEAP_IS_INIT = 1;
    THE_HEAP = map_new(hashmap_hash_ptr, hashmap_equal_ptr);
}

void init_frames()
{
    assert(!FRAMES_INIT);
    FRAMES_INIT = 1;
    FRAMES = vec_new();
}

void open_frame()
{
    assert(FRAMES_INIT == 1);
    vec_add(FRAMES, vec_new());
}

void close_frame()
{
    assert(FRAMES_INIT == 1);
    assert(!vec_is_empty(FRAMES));

    vec_free_self(vec_remove_last(FRAMES));

    if (BYTES_ALLOCATED >= BYTES_LIMIT_GC) {
        collect_locals();
    }

}

static struct vec *cur_frame()
{
    assert(FRAMES_INIT == 1);
    assert(!vec_is_empty(FRAMES));
    return vec_get_last(FRAMES);
}

void reg_ptr_in_a_frame(void *ptr)
{
    assert(FRAMES_INIT == 1);
    assert(ptr);
    struct vec* f = cur_frame();
    vec_add_unique(f, ptr);
}

void * get_memory(size_t size, struct type_descr * descr)
{
    assert(THE_HEAP_IS_INIT == 1);
    assert(descr);

    if (size == 0) {
        size = 1;
    }

    struct markable *e = hmalloc(sizeof(struct markable));
    e->ptr = hmalloc(size);
    e->is_marked = 0;
    e->size = size;
    e->datatype = descr;
    e->order_of_appearance = order_of_appearance;
    order_of_appearance += 1;

    //
    BYTES_ALLOCATED += e->size;
    BYTES_ALLOCATED += sizeof(struct markable);
    //

    map_put(THE_HEAP, e->ptr, e);
    return e->ptr;
}

char * get_memory_strdup(char *s)
{
    assert(THE_HEAP_IS_INIT == 1);
    assert(s);

    size_t len = strlen(s);
    char *rv = (char*) get_memory(len + 1, TD_CHAR_PTR);
    strcpy(rv, s);
    rv[len] = '\0';
    return rv;
}

static void dropMarkable(struct markable *m)
{
    assert(THE_HEAP_IS_INIT == 1);

    assert(m != NULL);
    assert(!m->is_marked);
    assert(m->ptr != NULL);

    //
    BYTES_COLLECTED += m->size;
    BYTES_COLLECTED += sizeof(struct markable);
    //

    free(m->ptr);
    m->ptr = NULL;
    free(m);
}

struct markable *try_to_find_markable_by_ptr(void *from)
{
    assert(THE_HEAP_IS_INIT == 1);
    if (from == NULL) {
        return NULL;
    }
    struct markable* result = map_get(THE_HEAP, from);
    return result;
}

static void dumpTable(struct hashmap *self, char *msg)
{
    assert(self);
    assert(msg);

    printf("\n%s:\n", msg);
    size_t total_bytes = 0;
    size_t overhead = 0;

    struct entry **table = map_table(self);

    for (size_t i = 0; i < map_cap(self); i++) {
        struct entry * e = table[i];
        if (e == NULL) {
            continue;
        }
        for (; e; e = entry_next(e)) {
            struct markable *m = entry_val(e);
            if (m == NULL) {
                continue;
            }
            printf("ptr=%lu, mar=%d, bytes=%8lu, %s\n", (size_t) m->ptr, m->is_marked, m->size,
                    m->datatype->description);

            total_bytes += m->size;
            overhead += sizeof(struct markable);

            if (!m->is_marked) {
            }
        }
    }

    printf("\nBYTES_ALLOCATED=%lu\n", BYTES_ALLOCATED);
    printf("BYTES_COLLECTED=%lu\n", BYTES_COLLECTED);
    printf("total_bytes=%lu\n", total_bytes);
    printf("overhead=%lu\n", overhead);
    printf("map.size=%lu\n", map_size(THE_HEAP));
    printf("map.capacity=%lu\n", map_cap(THE_HEAP));
}

void dump_heap()
{
    dumpTable(THE_HEAP, "HEAP");
}

static void append_deps(struct type_descr *datatype, void *ptr,
vec_of(struct markable*) *inProcessing)
{
    assert(datatype);
    assert(ptr);
    assert(inProcessing);

    if (datatype == TD_ARRAY) {
        struct vec *vec = (struct vec*) ptr;
        for (size_t i = 0; i < vec_size(vec); i += 1) {
            void* array_elem_ptr = (void*) vec_get(vec, i);
            vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(array_elem_ptr));
        }
        vec_add_unique_ignore_null(inProcessing, try_to_find_markable_by_ptr(vec_get_table(vec)));

        return;
    }

#include "../generated_append_deps.txt"

    fprintf(stdout, "unknown datatype desc:%s\n", datatype->description);
    exit(128);
}

static int type_is_compound(struct type_descr *datatype)
{
    assert(datatype);

    if (datatype == TD_ARRAY) {
        return 1;
    }

#include "../generated_is_compound.txt"

    return 0;
}

static void mark_one_root(void *from)
{
    struct markable *first = try_to_find_markable_by_ptr(from);
    if (first == NULL) {
        return;
    }

    vec_of(struct markable*) * in_processing = vec_new();
    vec_add_unique_assert_null(in_processing, first);

    size_t loops = 0;
    while (!vec_is_empty(in_processing)) {
        struct markable *m = vec_remove_last(in_processing);
        assert(m);
        assert(m->datatype);

        loops += 1;

        if (m->is_marked) {
            continue;
        }
        m->is_marked = 1;

        struct type_descr* datatype = m->datatype;
        if (type_is_compound(datatype)) {
            append_deps(datatype, m->ptr, in_processing);
        }
    }

    //printf("loops=%lu\n", loops);
    vec_free_self(in_processing);
}

/// here: if we near the limit of the default heap-size:
/// maybe 32-64-128 MB, we may check the current
/// block, and try to clean the heap, begin with
/// all ROOTS, where each of the root is a local
/// variable defined above, it does not matter what
/// the block is - the function block, or the loop block,
/// it should works also for recursive functions too, and
/// for infinite loops... have to test this...
/// it is a simple and straightforward solution,
/// we should not trace each function, and trace each
/// assignment, we should not update the current stack,
/// so: it is much easier to implement neither the
/// create the array of frames, and apply special
/// form to each assignment...

void collect_locals()
{
    /// mark all stack's roots
    for (size_t i = 0; i < vec_size(FRAMES); i += 1) {
        struct vec *f = vec_get(FRAMES, i);
        for (size_t j = 0; j < vec_size(f); j += 1) {
            void *p = vec_get(f, j);
            mark_one_root(p);
        }
    }

    /// sweep
    vec_of(" void* ") * keys_to_remove = vec_new();

    struct entry **table = map_table(THE_HEAP);

    for (size_t i = 0; i < map_cap(THE_HEAP); i++) {
        struct entry * e = table[i];
        if (e == NULL) {
            continue;
        }
        for (; e; e = entry_next(e)) {
            struct markable *m = entry_val(e);
            if (m == NULL) {
                continue;
            }
            if (m->is_marked) {
                //printf("unmarked:%s, %lu\n", m->datatype->description, (size_t) m->ptr);
                m->is_marked = 0;
                continue;
            }

            //printf("collected:%s, %lu\n", m->datatype->description, (size_t) m->ptr);
            vec_add(keys_to_remove, m);
        }
    }

    /// printf("\nbefore sort: \n");
    /// for (size_t i = 0; i < vec_size(keys_to_remove); i += 1) {
    ///     struct markable *m = (struct markable*) vec_get(keys_to_remove, i);
    ///     printf("%5lu: %s\n", m->order_of_appearance, m->datatype->description);
    /// }

    /// actually - it is not necessary to sort the pointers,
    /// but it is much clean to destroy them in order of
    /// their appearance, because we sure then that all dependencies
    /// will be untouched
    vec_sort(keys_to_remove, markable_sort);

    /// call destructors before anything will be freed
    ///
    for (size_t i = 0; i < vec_size(keys_to_remove); i += 1) {
        struct markable *m = (struct markable*) vec_get(keys_to_remove, i);
#       include "../generated_deinits.txt"
    }

    for (size_t i = 0; i < vec_size(keys_to_remove); i += 1) {
        struct markable *m = (struct markable*) vec_get(keys_to_remove, i);
        void *k = m->ptr;

        map_remove(THE_HEAP, k);
        dropMarkable(m);

        // void *k = vec_get(keys_to_remove, i);
        // map_remove(THE_HEAP, k);
    }
    vec_free_self(keys_to_remove);
}

