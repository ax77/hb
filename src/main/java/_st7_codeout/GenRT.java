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
    sb.append("#include <string.h>                            \n");
    sb.append("void* hmalloc(size_t size);                    \n");
    sb.append("void* hrealloc(void* old, size_t newsize);     \n");
    sb.append("void *hcalloc(size_t count, size_t eltsize);   \n");
    sb.append("char *hstrdup(char *str);                      \n");
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
    sb.append("}                                              \n");
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
    sb.append("}                                              \n");
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
    sb.append("}                                              \n");
    sb.append("char *hstrdup(char *str)                       \n");
    sb.append("{                                              \n");
    sb.append("    assert(str);                               \n");
    sb.append("    size_t len = strlen(str);                  \n");
    sb.append("    char* rv = (char*) hmalloc(len + 1);       \n");
    sb.append("    strcpy(rv, str);                           \n");
    sb.append("    rv[len] = \'\\0\';                         \n");
    sb.append("    return rv;                                 \n");
    sb.append("}                                              \n");
    //@formatter:on

    return sb.toString();
  }

}
