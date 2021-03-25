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

void array_init_6_1024(struct array_1024* __this);
void array_init_8_1024(struct array_1024* __this, int size);
void array_add_10_1024(struct array_1024* __this, char element);
char array_get_12_1024(struct array_1024* __this, int index);
char array_set_14_1024(struct array_1024* __this, int index, char element);
int array_size_16_1024(struct array_1024* __this);
boolean array_is_empty_18_1024(struct array_1024* __this);
void array_deinit_26_1024(struct array_1024* __this);
void string_init_20_(struct string* __this, const char * const buffer);
int string_length_22_(struct string* __this);
char string_get_24_(struct string* __this, int index);
void string_deinit_28_(struct string* __this);
int main_class_main_4_();


static const char t55[] = { 't', 'h', 'e', ' ', 'c', 'h', 'a', 'r', ' ', 'i', 's', ':', ' ', '{', '0', '}', '\0'};
static const char t58[] = { 'a', '.', 'b', '.', 'c', '\0'};
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

void array_init_6_1024(struct array_1024* __this) {
    assert(__this);
    __this->size = 0;
    __this->alloc = 2;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
}

void array_init_8_1024(struct array_1024* __this, int size) {
    assert(__this);
    assert(size > 0);
    assert(size < INT_MAX);
    __this->size = 0;
    __this->alloc = size;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
}

void array_add_10_1024(struct array_1024* __this, char element) {
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

char array_get_12_1024(struct array_1024* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}

char array_set_14_1024(struct array_1024* __this, int index, char element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    char old =  __this->data[index];
    __this->data[index] = element;
    return old;
}

int array_size_16_1024(struct array_1024* __this) {
    assert(__this);
    return __this->size;
}

boolean array_is_empty_18_1024(struct array_1024* __this) {
    assert(__this);
    return (__this->size == 0);
}

void array_deinit_26_1024(struct array_1024* __this) {
    assert(__this);
}

void string_init_20_(struct string* __this, const char * const buffer) {
    assert(__this);
    assert(buffer);
    __this->buffer = hstrdup(buffer);
    __this->length = strlen(buffer);
}

int string_length_22_(struct string* __this) {
    assert(__this);
    assert(__this->buffer);
    return __this->length;
}

char string_get_24_(struct string* __this, int index) {
    assert(__this);
    assert(__this->buffer);
    assert(__this->length > 0);
    assert(index >= 0);
    assert(index < __this->length);
    return __this->buffer[index];
}

void string_deinit_28_(struct string* __this) {
    assert(__this);
}




int main_class_main_4_()
{
struct array_1024* t31 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_6_1024(t31);
struct array_1024* args = t31;

struct array_1024* t32 = args;
char t33 = 'a';
array_add_10_1024(t32, t33);

struct array_1024* t34 = args;
char t35 = 'b';
array_add_10_1024(t34, t35);

struct array_1024* t36 = args;
char t37 = 'c';
array_add_10_1024(t36, t37);

struct array_1024* t38 = args;
char t39 = 'd';
array_add_10_1024(t38, t39);

struct array_1024* t40 = args;
char t41 = 'e';
array_add_10_1024(t40, t41);


{
int t42 = 0;
int i = t42;

for(;;)
{
int t43 = i;
struct array_1024* t44 = args;
int t45 = array_size_16_1024(t44);
boolean t46 = t43 < t45;
boolean t47 = !t46;

if(t47)
{

break;

}

struct array_1024* t52 = args;
int t53 = i;
char t54 = array_get_12_1024(t52, t53);
char c = t54;

struct string* t56 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_20_(t56, t55);
char t57 = c;
print(t56, t57);

int t48 = i;
int t49 = i;
int t50 = 1;
int t51 = t49 + t50;
i = t51;


}


}

struct string* t59 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_20_(t59, t58);
struct string* s1 = t59;

struct string* t60 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_20_(t60, t58);
struct string* s2 = t60;

int t61 = 0;

string_deinit_28_(s2);
string_deinit_28_(s1);
array_deinit_26_1024(args);

return t61;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_4_();

    printf("%d\n", result);
    return result;

}
