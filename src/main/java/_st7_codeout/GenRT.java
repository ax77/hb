package _st7_codeout;

public abstract class GenRT {

  public static String prebuf() {

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
    
    sb.append("#define assert_true(expr) do {                               \\\n");
    sb.append("  if( !(expr) ) {                                            \\\n");
    sb.append("    fprintf(stderr, \"assert fail: (%s:%s():%d) : [%s]\\n\"     \\\n");
    sb.append("    , __FILE__, __func__, __LINE__, #expr);                  \\\n");
    sb.append("    exit(128);                                               \\\n");
    sb.append("  }                                                          \\\n");
    sb.append("} while(0) \n\n");
    
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
    sb.append("    char* rv = (char*) hmalloc(len + 1);       \n");
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

    //@formatter:on

    return sb.toString();
  }

}
