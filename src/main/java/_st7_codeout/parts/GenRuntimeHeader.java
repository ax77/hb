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
    
    //free
    sb.append("static size_t MIN_HEAP_PTR_ADDR = SIZE_MAX;           \n");
    sb.append("static size_t MAX_HEAP_PTR_ADDR = 0;                  \n");
    
    sb.append("static void *hcalloc(size_t count, size_t elsize) {   \n");
    sb.append("    assert(count);                                    \n");
    sb.append("    assert(elsize);                                   \n");
    sb.append("    void *ret = NULL;                                 \n");
    sb.append("    ret = calloc(count, elsize);                      \n");
    sb.append("    if(ret == NULL) {                                 \n");
    sb.append("        ret = calloc(count, elsize);                  \n");
    sb.append("        if(ret == NULL) {                             \n");
    sb.append("            ret = calloc(count, elsize);              \n");
    sb.append("        }                                             \n");
    sb.append("    }                                                 \n");
    sb.append("    assert(ret);                                      \n");
    sb.append("    size_t iptr = (size_t) ret;                       \n");
    sb.append("    if(iptr < MIN_HEAP_PTR_ADDR) {                    \n");
    sb.append("        MIN_HEAP_PTR_ADDR = iptr;                     \n");
    sb.append("    }                                                 \n");
    sb.append("    if(iptr > MAX_HEAP_PTR_ADDR) {                    \n");
    sb.append("        MAX_HEAP_PTR_ADDR = iptr;                     \n");
    sb.append("    }                                                 \n");
    sb.append("    return ret;                                       \n");
    sb.append("}                                                     \n\n");
    
    sb.append("static void drop_ptr(void **ptr, void *set) {         \n");
    sb.append("    assert(ptr);                                      \n");
    sb.append("    assert(*ptr);                                     \n");
    sb.append("    assert(set);                                      \n");
    sb.append("    // was set already, and possibly was freed too.   \n");
    sb.append("    if(*ptr == set) {                                 \n");
    sb.append("        return;                                       \n");
    sb.append("    }                                                 \n");
    sb.append("    // primitive checking -                           \n");
    sb.append("    // whether the address come from the heap         \n");
    sb.append("    size_t iptr = (size_t) *ptr;                      \n");
    sb.append("    if(iptr < MIN_HEAP_PTR_ADDR) {                    \n");
    sb.append("        return;                                       \n");
    sb.append("    }                                                 \n");
    sb.append("    if(iptr > MAX_HEAP_PTR_ADDR) {                    \n");
    sb.append("        return;                                       \n");
    sb.append("    }                                                 \n");
    sb.append("    // drop                                           \n");
    sb.append("    free(*ptr);                                       \n");
    sb.append("    *ptr = set;                                       \n");
    sb.append("}                                                     \n\n");
    //free
    
    sb.append("char *hstrdup(char *str)                       \n");
    sb.append("{                                              \n");
    sb.append("    assert(str);                               \n");
    sb.append("    size_t len = strlen(str);                  \n");
    sb.append("    char* rv = (char*) hcalloc(1u, len + 1);   \n");
    sb.append("    strcpy(rv, str);                           \n");
    sb.append("    rv[len] = \'\\0\';                         \n");
    sb.append("    return rv;                                 \n");
    sb.append("}                                              \n\n");
    
    // call stack +
    sb.append("struct aux_fcall_stack {                                                 \n");
    sb.append("    const char *func;                                                    \n");
    sb.append("    int line;                                                            \n");
    sb.append("};                                                                       \n\n");
    
    sb.append("#define STACK_SIZE (1024)                                                \n");
    sb.append("struct aux_fcall_stack call_stack[STACK_SIZE];                           \n");
    sb.append("static size_t call_stack_nr = 0;                                         \n");
    sb.append("static inline void push_function(const char *__func, int __line);        \n");
    sb.append("static inline void pop_function();                                       \n");
    sb.append("static void dump_call_stack();                                           \n\n");
    
    sb.append("static inline void push_function(const char *__func, int __line)         \n");
    sb.append("{                                                                        \n");
    sb.append("    if (call_stack_nr >= STACK_SIZE) {                                   \n");
    sb.append("        fprintf(stderr, \"stack overflow: (%s:%d)\\n\", __func, __line); \n");
    sb.append("        dump_call_stack();                                               \n");
    sb.append("        exit(8);                                                         \n");
    sb.append("    }                                                                    \n");
    sb.append("    struct aux_fcall_stack fc = { .func = __func, .line = __line };      \n");
    sb.append("    call_stack[call_stack_nr] = fc;                                      \n");
    sb.append("    call_stack_nr += 1;                                                  \n");
    sb.append("}                                                                        \n\n");
    
    sb.append("static inline void pop_function(const char *__func, int __line)          \n");
    sb.append("{                                                                        \n");
    sb.append("    assert(call_stack_nr > 0);                                           \n");
    sb.append("    call_stack_nr -= 1;                                                  \n");
    sb.append("}                                                                        \n\n");
    
    sb.append("static void dump_call_stack()                                            \n");
    sb.append("{                                                                        \n");
    sb.append("    fprintf(stdout, \"%s\\n\", \"\\nThe call-stack: \");                 \n");
    sb.append("    for (size_t i = 0; i < call_stack_nr; i += 1) {                      \n");
    sb.append("        struct aux_fcall_stack fc = call_stack[i];                       \n");
    sb.append("        fprintf(stdout, \"  %s:%d\\n\", fc.func, fc.line);               \n");
    sb.append("    }                                                                    \n");
    sb.append("}                                                                        \n\n");
    // call stack -
 
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
    sb.append("static inline void assert_true(                                    \n");
    sb.append("  int cnd,                                                         \n");
    sb.append("  struct str *file,                                                \n");
    sb.append("  int line,                                                        \n");
    sb.append("  struct str *expr)                                                \n");
    sb.append("{                                                                  \n");
    sb.append("    assert(file);                                                  \n");
    sb.append("    assert(expr);                                                  \n");
    sb.append("    assert(file->buf);                                             \n");
    sb.append("    assert(expr->buf);                                             \n");
    sb.append("    if (cnd == 0) {                                                \n");
    sb.append("        fprintf(stdout, \"\\nassertion failed: (%s:%d) : [%s]\\n\" \n");
    sb.append("          , file->buf                                              \n");
    sb.append("          , line                                                   \n");
    sb.append("          , expr->buf);                                            \n");
    sb.append("        dump_call_stack();                                         \n");
    sb.append("        exit(7);                                                   \n");
    sb.append("    }                                                              \n");
    sb.append("}                                                                  \n\n");
    return sb.toString();
  }

}
