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

struct arr_1024;
struct arr_1025;
struct string;

void arr_init_26_1024(struct arr_1024* __this, int size);
char arr_get_28_1024(struct arr_1024* __this, int index);
char arr_set_30_1024(struct arr_1024* __this, int index, char element);
int arr_size_32_1024(struct arr_1024* __this);
void arr_init_26_1025(struct arr_1025* __this, int size);
struct string* arr_get_28_1025(struct arr_1025* __this, int index);
struct string* arr_set_30_1025(struct arr_1025* __this, int index, struct string* element);
int arr_size_32_1025(struct arr_1025* __this);
void assert_is_true_17_(boolean condition);
void assert_panic_20_(struct string* because);
void assert_native_panic_22_(struct string* because);
void assert_native_assert_true_24_(boolean condition);
void string_init_5_(struct string* __this, struct arr_1024* buffer);
int string_length_8_(struct string* __this);
char string_get_11_(struct string* __this, int index);
struct arr_1024* string_bytes_14_(struct string* __this);
int main_class_main_2_();

struct string
{
struct arr_1024* buffer; 

};

struct string string_zero;

#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 

static const char t71[] = { 'q', '.', 'b', '.', 'e', '\0'};
struct arr_1024
{
    char* data;
    size_t size;
};

struct arr_1025
{
    struct string** data;
    size_t size;
};

void arr_init_26_1024(struct arr_1024* __this, int size) {
    assert(__this);
    assert(size > 0);
    __this->size = size;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * size) );
}

char arr_get_28_1024(struct arr_1024* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}

char arr_set_30_1024(struct arr_1024* __this, int index, char element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    char old =  __this->data[index];
    __this->data[index] = element;
    return old;
}

int arr_size_32_1024(struct arr_1024* __this) {
    assert(__this);
    return __this->size;
}

void arr_init_26_1025(struct arr_1025* __this, int size) {
    assert(__this);
    assert(size > 0);
    __this->size = size;
    __this->data = (struct string**) hcalloc( 1u, (sizeof(struct string*) * size) );
}

struct string* arr_get_28_1025(struct arr_1025* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}

struct string* arr_set_30_1025(struct arr_1025* __this, int index, struct string* element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    struct string* old =  __this->data[index];
    __this->data[index] = element;
    return old;
}

int arr_size_32_1025(struct arr_1025* __this) {
    assert(__this);
    return __this->size;
}



void assert_native_panic_22_(struct string* because) {
    assert(because);
}
void assert_native_assert_true_24_(boolean condition) {
    assert_true(condition);
}
void assert_is_true_17_(boolean condition)
{
boolean t34 = condition;
assert_native_assert_true_24_(t34);


}

void assert_panic_20_(struct string* because)
{
struct string* t36 = because;
assert_native_panic_22_(t36);


}

void string_init_5_(struct string* __this, struct arr_1024* buffer)
{
struct string* t37 = __this;
assert(t37);
struct arr_1024* t38 = t37->buffer;
struct arr_1024* t39 = buffer;
assert(t37);
t37->buffer = t39;


}

int string_length_8_(struct string* __this)
{
struct string* t42 = __this;
assert(t42);
struct arr_1024* t43 = t42->buffer;
int t44 = arr_size_32_1024(t43);
int t45 = 0;
boolean t46 = t44 > t45;
assert_is_true_17_(t46);

struct string* t47 = __this;
assert(t47);
struct arr_1024* t48 = t47->buffer;
int t49 = arr_size_32_1024(t48);
int t50 = 1;
int t51 = t49 - t50;


return t51;

}

char string_get_11_(struct string* __this, int index)
{
int t53 = index;
int t54 = 0;
boolean t55 = t53 >= t54;
assert_is_true_17_(t55);

int t57 = index;
struct string* t58 = __this;
assert(t58);
struct arr_1024* t59 = t58->buffer;
int t60 = arr_size_32_1024(t59);
boolean t61 = t57 < t60;
assert_is_true_17_(t61);

struct string* t62 = __this;
assert(t62);
struct arr_1024* t63 = t62->buffer;
int t64 = index;
char t65 = arr_get_28_1024(t63, t64);


return t65;

}

struct arr_1024* string_bytes_14_(struct string* __this)
{
struct string* t66 = __this;
assert(t66);
struct arr_1024* t67 = t66->buffer;


return t67;

}


int main_class_main_2_()
{
int t68 = 16;
struct arr_1025* t69 = (struct arr_1025*) hcalloc(1u, sizeof(struct arr_1025));
arr_init_26_1025(t69, t68);
struct arr_1025* buf = t69;

struct arr_1024* t70 = (struct arr_1024*) hcalloc(1u, sizeof(struct arr_1024));
int t73 = 6;
arr_init_26_1024(t70, t73);
hstrncpy(t70->data, t71, 5);
struct string* t72 = (struct string*) hcalloc(1u, sizeof(struct string));
string_init_5_(t72, t70);
struct string* s = t72;

struct string* t74 = s;
int t75 = string_length_8_(t74);


return t75;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_2_();

    printf("%d\n", result);
    return result;

}
