package _st7_codeout.parts;

public abstract class GenRuntimeHeader {

  public static String genIncludesAndRuntimeFuncs() {

    //@formatter:off
    
    StringBuilder sb = new StringBuilder();
    sb.append("#include <assert.h>                            \n");
    sb.append("#include <limits.h>                            \n");
    sb.append("#include <stdarg.h>                            \n");
    sb.append("#include <stddef.h>                            \n");
    sb.append("#include <stdint.h>                            \n");
    sb.append("#include <stdio.h>                             \n");
    sb.append("#include <stdlib.h>                            \n");
    sb.append("#include <string.h>                            \n\n");
    
    sb.append("/// open/close/read/O_RDONLY                   \n");
    sb.append("#include <unistd.h>                            \n");
    sb.append("#include <sys/stat.h>                          \n");
    sb.append("#include <fcntl.h>                             \n\n");
    
    sb.append("typedef int boolean;                           \n");
    sb.append("#define false 0                                \n");
    sb.append("#define true (!(false))                        \n\n");
    
    sb.append("void* hmalloc(size_t size);                    \n");
    sb.append("void* hrealloc(void* old, size_t newsize);     \n");
    sb.append("void *hcalloc(size_t count, size_t eltsize);   \n");
    sb.append("char *hstrdup(char *str);                      \n\n");

    sb.append("void* hmalloc(size_t size)                     \n");
    sb.append("{                                              \n");
    sb.append("    if (size == 0) {                           \n");
    sb.append("        size = 1;                              \n");
    sb.append("    }                                          \n");
    sb.append("    assert(size < INT_MAX);                    \n");
    sb.append("    void *ptr = NULL;                          \n");
    sb.append("    ptr = malloc(size);                        \n");
    sb.append("    if (ptr == NULL) {                         \n");
    sb.append("        ptr = malloc(size);                    \n");
    sb.append("        if (ptr == NULL) {                     \n");
    sb.append("            ptr = malloc(size);                \n");
    sb.append("        }                                      \n");
    sb.append("    }                                          \n");
    sb.append("    assert(ptr);                               \n");
    sb.append("    return ptr;                                \n");
    sb.append("}                                              \n\n");

    sb.append("void* hrealloc(void* old, size_t newsize)      \n");
    sb.append("{                                              \n");
    sb.append("    void *ptr = NULL;                          \n");
    sb.append("    ptr = realloc(old, newsize);               \n");
    sb.append("    if (ptr == NULL) {                         \n");
    sb.append("        ptr = realloc(old, newsize);           \n");
    sb.append("        if (ptr == NULL) {                     \n");
    sb.append("            ptr = realloc(old, newsize);       \n");
    sb.append("        }                                      \n");
    sb.append("    }                                          \n");
    sb.append("    assert(ptr);                               \n");
    sb.append("    return ptr;                                \n");
    sb.append("}                                              \n\n");

    sb.append("void *hcalloc(size_t count, size_t eltsize)    \n");
    sb.append("{                                              \n");
    sb.append("    assert(count);                             \n");
    sb.append("    assert(eltsize);                           \n");
    sb.append("    void* ptr = NULL;                          \n");
    sb.append("    ptr = calloc(count, eltsize);              \n");
    sb.append("    if (ptr == NULL) {                         \n");
    sb.append("        ptr = calloc(count, eltsize);          \n");
    sb.append("        if (ptr == NULL) {                     \n");
    sb.append("            ptr = calloc(count, eltsize);      \n");
    sb.append("        }                                      \n");
    sb.append("    }                                          \n");
    sb.append("    assert(ptr);                               \n");
    sb.append("    return ptr;                                \n");
    sb.append("}                                              \n\n");

    sb.append("char *hstrdup(char *str)                       \n");
    sb.append("{                                              \n");
    sb.append("    assert(str);                               \n");
    sb.append("    size_t len = strlen(str);                  \n");
    sb.append("    char* rv = (char*) hcalloc(1u, len + 1);   \n");
    sb.append("    strcpy(rv, str);                           \n");
    sb.append("    rv[len] = \'\\0\';                         \n");
    sb.append("    return rv;                                 \n");
    sb.append("}                                              \n\n");

    sb.append("char *hstrncpy(char * const to, const char * const from, const size_t count) \n");
    sb.append("{                                              \n");
    sb.append("    assert(to);                                \n");
    sb.append("    assert(from);                              \n");
    sb.append("    assert(to[0] == \'\\0\');                  \n");
    sb.append("    assert(count > 0);                         \n");
    sb.append("    assert(count <= strlen(from));             \n");
    sb.append("    for (size_t i = 0; i < count; i += 1) {    \n");
    sb.append("        const char c = from[i];                \n");
    sb.append("        if (c == \'\\0\') {                    \n");
    sb.append("            break;                             \n");
    sb.append("        }                                      \n");
    sb.append("        to[i] = c;                             \n");
    sb.append("    }                                          \n");
    sb.append("    return to;                                 \n");
    sb.append("}                                              \n\n");

    sb.append("static inline size_t __hash_char_ptr(char* key)      \n");
    sb.append("{                                                    \n");
    sb.append("    assert(key);                                     \n");
    sb.append("    char* str = key;                                 \n");
    sb.append("    size_t hash = 5381;                              \n");
    sb.append("    size_t c = 0;                                    \n");
    sb.append("    while ((c = *str++)) {                           \n");
    sb.append("        hash = ((hash << 5) + hash) + c;             \n");
    sb.append("    }                                                \n");
    sb.append("    return hash;                                     \n");
    sb.append("}                                                    \n\n");

    sb.append("static inline size_t __hash_int(ptrdiff_t i)         \n");
    sb.append("{                                                    \n");
    sb.append("    return (size_t) i;                               \n");
    sb.append("}                                                    \n\n");

    sb.append("static inline size_t __hash_ptr(void * ptr)          \n");
    sb.append("{                                                    \n");
    sb.append("    assert(ptr);                                     \n");
    sb.append("    return (size_t) ptr;                             \n");
    sb.append("}                                                    \n\n");

    //@formatter:on

    return sb.toString();
  }

  public static String genAssertTrueFunction() {
    StringBuilder sb = new StringBuilder();
    sb.append("static inline void assert_true(                                  \n");
    sb.append("  int cnd,                                                       \n");
    sb.append("  struct string *file,                                           \n");
    sb.append("  int line,                                                      \n");
    sb.append("  struct string *expr)                                           \n");
    sb.append("{                                                                \n");
    sb.append("    assert(file);                                                \n");
    sb.append("    assert(file->buffer);                                        \n");
    sb.append("    assert(expr);                                                \n");
    sb.append("    assert(expr->buffer);                                        \n");
    sb.append("    if (cnd == 0) {                                              \n");
    sb.append("        fprintf(stdout, \"assertion failed: (%s:%d) : [%s]\\n\"  \n");
    sb.append("          , file->buffer                                         \n");
    sb.append("          , line                                                 \n");
    sb.append("          , expr->buffer);                                       \n");
    sb.append("        exit(7);                                                 \n");
    sb.append("    }                                                            \n");
    sb.append("}                                                                \n\n");
    return sb.toString();
  }

}
