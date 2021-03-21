#include <assert.h>                            
#include <limits.h>                            
#include <stdarg.h>                            
#include <stddef.h>                            
#include <stdint.h>                            
#include <stdio.h>                             
#include <stdlib.h>                            
#include <string.h>                            

/// open/close/read/O_RDONLY                   
#include <unistd.h>                            
#include <sys/stat.h>                          
#include <fcntl.h>                             

typedef int boolean;                           

#define false 0                                

#define true (!(false))                        

void* hmalloc(size_t size);                    
void* hrealloc(void* old, size_t newsize);     
void *hcalloc(size_t count, size_t eltsize);   
char *hstrdup(char *str);                      

void* hmalloc(size_t size)                     
{                                              
    if (size == 0) {                           
        size = 1;                              
    }                                          
    assert(size < INT_MAX);                    
    void *ptr = NULL;                          
    ptr = malloc(size);                        
    if (ptr == NULL) {                         
        ptr = malloc(size);                    
        if (ptr == NULL) {                     
            ptr = malloc(size);                
        }                                      
    }                                          
    assert(ptr);                               
    return ptr;                                
}                                              

void* hrealloc(void* old, size_t newsize)      
{                                              
    void *ptr = NULL;                          
    ptr = realloc(old, newsize);               
    if (ptr == NULL) {                         
        ptr = realloc(old, newsize);           
        if (ptr == NULL) {                     
            ptr = realloc(old, newsize);       
        }                                      
    }                                          
    assert(ptr);                               
    return ptr;                                
}                                              

void *hcalloc(size_t count, size_t eltsize)    
{                                              
    assert(count);                             
    assert(eltsize);                           
    void* ptr = NULL;                          
    ptr = calloc(count, eltsize);              
    if (ptr == NULL) {                         
        ptr = calloc(count, eltsize);          
        if (ptr == NULL) {                     
            ptr = calloc(count, eltsize);      
        }                                      
    }                                          
    assert(ptr);                               
    return ptr;                                
}                                              

char *hstrdup(char *str)                       
{                                              
    assert(str);                               
    size_t len = strlen(str);                  
    char* rv = (char*) hmalloc(len + 1);       
    strcpy(rv, str);                           
    rv[len] = '\0';                         
    return rv;                                 
}                                              

char *hstrncpy(char * const to, const char * const from, const size_t count) 
{                                              
    assert(to);                                
    assert(from);                              
    assert(to[0] == '\0');                  
    assert(count > 0);                         
    assert(count <= strlen(from));             
    for (size_t i = 0; i < count; i += 1) {    
        const char c = from[i];                
        if (c == '\0') {                    
            break;                             
        }                                      
        to[i] = c;                             
    }                                          
    return to;                                 
}                                              

struct array_1024;
struct string;

void array_init_34_1024(struct array_1024* __this, int size);
char array_get_36_1024(struct array_1024* __this, int index);
char array_set_38_1024(struct array_1024* __this, int index, char element);
int array_size_40_1024(struct array_1024* __this);
void assert_is_true_25_(boolean condition);
void assert_panic_28_(struct string* because);
void assert_native_panic_30_(struct string* because);
void assert_native_assert_true_32_(boolean condition);
void string_init_13_(struct string* __this, struct array_1024* buffer);
int string_length_16_(struct string* __this);
char string_get_19_(struct string* __this, int index);
struct array_1024* string_bytes_22_(struct string* __this);
void fmt_print_4_(struct array_1024* arr);
void fmt_print_6_(char c);
void fmt_print_8_(int i);
void fmt_print_10_(struct string* s);
int main_class_main_2_();

struct string
{
struct array_1024* buffer; 

};

struct string string_zero;

#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 

static const char t102[] = { 'a', '.', 'b', '.', 'c', '.', 'd', '.', 'e', '.', 'f', '\0'};
struct array_1024
{
    char* data;
    size_t size;
};

void array_init_34_1024(struct array_1024* __this, int size) {
    assert(__this);
    assert(size > 0);
    __this->size = size;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * size) );
}

char array_get_36_1024(struct array_1024* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}

char array_set_38_1024(struct array_1024* __this, int index, char element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    char old =  __this->data[index];
    __this->data[index] = element;
    return old;
}

int array_size_40_1024(struct array_1024* __this) {
    assert(__this);
    return __this->size;
}



void assert_native_panic_30_(struct string* because) {
    assert(because);
}
void assert_native_assert_true_32_(boolean condition) {
    assert_true(condition);
}
void fmt_print_4_(struct array_1024* arr) {
    assert(arr);
    assert(arr->data);

    printf("%s\n", arr->data);
}

void fmt_print_6_(char c) {
    printf("%c\n", c);
}

void fmt_print_8_(int i) {
    printf("%d\n", i);
}

void fmt_print_10_(struct string* s) {
    assert(s);
    assert(s->buffer);
    assert(s->buffer->data);

    printf("%s\n", s->buffer->data);
}

void assert_is_true_25_(boolean condition)
{
boolean t42 = condition;
assert_native_assert_true_32_(t42);


}

void assert_panic_28_(struct string* because)
{
struct string* t44 = because;
assert_native_panic_30_(t44);


}

void string_init_13_(struct string* __this, struct array_1024* buffer)
{
struct string* t45 = __this;
assert(t45);
struct array_1024* t46 = t45->buffer;
struct array_1024* t47 = buffer;
assert(t45);
t45->buffer = t47;


}

int string_length_16_(struct string* __this)
{
struct string* t50 = __this;
assert(t50);
struct array_1024* t51 = t50->buffer;
int t52 = array_size_40_1024(t51);
int t53 = 0;
boolean t54 = t52 > t53;
assert_is_true_25_(t54);

struct string* t55 = __this;
assert(t55);
struct array_1024* t56 = t55->buffer;
int t57 = array_size_40_1024(t56);
int t58 = 1;
int t59 = t57 - t58;


return t59;

}

char string_get_19_(struct string* __this, int index)
{
int t61 = index;
int t62 = 0;
boolean t63 = t61 >= t62;
assert_is_true_25_(t63);

int t65 = index;
struct string* t66 = __this;
assert(t66);
struct array_1024* t67 = t66->buffer;
int t68 = array_size_40_1024(t67);
boolean t69 = t65 < t68;
assert_is_true_25_(t69);

struct string* t70 = __this;
assert(t70);
struct array_1024* t71 = t70->buffer;
int t72 = index;
char t73 = array_get_36_1024(t71, t72);


return t73;

}

struct array_1024* string_bytes_22_(struct string* __this)
{
struct string* t74 = __this;
assert(t74);
struct array_1024* t75 = t74->buffer;


return t75;

}


int main_class_main_2_()
{
int t76 = 4;
struct array_1024* t77 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_34_1024(t77, t76);
struct array_1024* buf = t77;

struct array_1024* t78 = buf;
int t79 = 0;
char t80 = 'a';
char t81 = array_set_38_1024(t78, t79, t80);

struct array_1024* t82 = buf;
int t83 = 1;
char t84 = 'b';
char t85 = array_set_38_1024(t82, t83, t84);

struct array_1024* t86 = buf;
int t87 = 2;
char t88 = 'c';
char t89 = array_set_38_1024(t86, t87, t88);

struct array_1024* t90 = buf;
int t91 = 3;
char t92 = '\0';
char t93 = array_set_38_1024(t90, t91, t92);

struct array_1024* t96 = buf;
fmt_print_4_(t96);

int t98 = 32768;
fmt_print_8_(t98);

char t100 = '_';
fmt_print_6_(t100);

struct array_1024* t101 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
int t104 = 12;
array_init_34_1024(t101, t104);
hstrncpy(t101->data, t102, 11);
struct string* t103 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_13_(t103, t101);
struct string* s = t103;

struct string* t106 = s;
fmt_print_10_(t106);

struct array_1024* t108 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
int t111 = 12;
array_init_34_1024(t108, t111);
hstrncpy(t108->data, t102, 11);
struct string* t110 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_13_(t110, t108);
fmt_print_10_(t110);

int t112 = 32;


return t112;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_2_();

    printf("%d\n", result);
    return result;

}
