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

#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 

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
    char* rv = (char*) hcalloc(1u, len + 1);   
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

void array_init_14_1024(struct array_1024* __this);
void array_init_16_1024(struct array_1024* __this, int size);
void array_add_18_1024(struct array_1024* __this, char element);
char array_get_20_1024(struct array_1024* __this, int index);
char array_set_22_1024(struct array_1024* __this, int index, char element);
int array_size_24_1024(struct array_1024* __this);
boolean array_is_empty_26_1024(struct array_1024* __this);
void array_deinit_34_1024(struct array_1024* __this);
void string_init_28_(struct string* __this, const char * const buffer);
int string_length_30_(struct string* __this);
char string_get_32_(struct string* __this, int index);
void string_deinit_36_(struct string* __this);
void fmt_print_6_(struct array_1024* arr);
void fmt_print_8_(char c);
void fmt_print_10_(int i);
void fmt_print_12_(struct string* s);
void fmt_deinit_38_();
int main_class_main_4_();


static const char t68[] = { 'a', '.', 'b', '.', 'c', '\0'};
struct array_1024
{
    char* data;
    size_t size;
    size_t alloc;
};


struct string
{
    char * buffer;
    size_t length;
};


void array_init_14_1024(struct array_1024* __this) {
    assert(__this);
    __this->size = 0;
    __this->alloc = 2;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
}


void array_init_16_1024(struct array_1024* __this, int size) {
    assert(__this);
    assert(size > 0);
    assert(size < INT_MAX);
    __this->size = 0;
    __this->alloc = size;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
}


void array_add_18_1024(struct array_1024* __this, char element) {
    if(__this->size >= __this->alloc) {                
        __this->alloc += 1;                            
        __this->alloc *= 2;                            
        char* ndata = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
        for(size_t i = 0; i < __this->size; i += 1) {  
          ndata[i] = __this->data[i];                  
        }                                              
        free(__this->data);                            
        __this->data = ndata;                          
    }                                                  
    __this->data[__this->size] = element;              
    __this->size += 1;                                 
}


char array_get_20_1024(struct array_1024* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}


char array_set_22_1024(struct array_1024* __this, int index, char element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    char old =  __this->data[index];
    __this->data[index] = element;
    return old;
}


int array_size_24_1024(struct array_1024* __this) {
    assert(__this);
    return __this->size;
}


boolean array_is_empty_26_1024(struct array_1024* __this) {
    assert(__this);
    return (__this->size == 0);
}


void array_deinit_34_1024(struct array_1024* __this) {
    assert(__this);
}


void string_init_28_(struct string* __this, const char * const buffer) {
    assert(__this);
    assert(buffer);
    __this->buffer = hstrdup(buffer);
    __this->length = strlen(buffer);
}


int string_length_30_(struct string* __this) {
    assert(__this);
    assert(__this->buffer);
    return __this->length;
}


char string_get_32_(struct string* __this, int index) {
    assert(__this);
    assert(__this->buffer);
    assert(__this->length > 0);
    assert(index >= 0);
    assert(index < __this->length);
    return __this->buffer[index];
}


void string_deinit_36_(struct string* __this) {
    assert(__this);
}




void fmt_print_6_(struct array_1024* arr) {
    assert(arr);
    assert(arr->data);

    printf("%s\n", arr->data);
}


void fmt_print_8_(char c) {
    printf("%c\n", c);
}


void fmt_print_10_(int i) {
    printf("%d\n", i);
}


void fmt_print_12_(struct string* s) {
    assert(s);
    assert(s->buffer);

    printf("%s\n", s->buffer);
}


void fmt_deinit_38_()
{

}


int main_class_main_4_()
{
struct array_1024* t41 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_14_1024(t41);
struct array_1024* args = t41;

struct array_1024* t42 = args;
char t43 = 'a';
array_add_18_1024(t42, t43);

struct array_1024* t44 = args;
char t45 = 'b';
array_add_18_1024(t44, t45);

struct array_1024* t46 = args;
char t47 = 'c';
array_add_18_1024(t46, t47);

struct array_1024* t48 = args;
char t49 = 'd';
array_add_18_1024(t48, t49);

struct array_1024* t50 = args;
char t51 = 'e';
array_add_18_1024(t50, t51);


{
int t52 = 0;
int i = t52;

for(;;)
{
int t53 = i;
struct array_1024* t54 = args;
int t55 = array_size_24_1024(t54);
boolean t56 = t53 < t55;
boolean t57 = !t56;

if(t57)
{

break;

}

struct array_1024* t62 = args;
int t63 = i;
char t64 = array_get_20_1024(t62, t63);
char c = t64;

char t67 = c;
fmt_print_8_(t67);

int t58 = i;
int t59 = i;
int t60 = 1;
int t61 = t59 + t60;
i = t61;


}


}

struct string* t69 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_28_(t69, t68);
struct string* s1 = t69;

struct string* t70 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_28_(t70, t68);
struct string* s2 = t70;

int t71 = 0;

string_deinit_36_(s2);
string_deinit_36_(s1);
array_deinit_34_1024(args);

return t71;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_4_();

    printf("%d\n", result);
    return result;

}
