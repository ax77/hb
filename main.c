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

void array_init_6_1024(struct array_1024* __this);
void array_init_8_1024(struct array_1024* __this, int size);
void array_add_10_1024(struct array_1024* __this, char element);
char array_get_12_1024(struct array_1024* __this, int index);
char array_set_14_1024(struct array_1024* __this, int index, char element);
int array_size_16_1024(struct array_1024* __this);
boolean array_is_empty_18_1024(struct array_1024* __this);
void array_deinit_32_1024(struct array_1024* __this);
void string_init_21_(struct string* __this, struct array_1024* buffer);
int string_length_24_(struct string* __this);
char string_get_27_(struct string* __this, int index);
struct array_1024* string_bytes_30_(struct string* __this);
void string_deinit_34_(struct string* __this);
int main_class_main_4_();

struct string
{
struct array_1024* buffer; 

};


static const char t91[] = { 't', 'h', 'e', ' ', 'c', 'h', 'a', 'r', ' ', 'i', 's', ':', ' ', '{', '0', '}', '\0'};
struct array_1024
{
    char* data;
    size_t size;
    size_t alloc;
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

void array_deinit_32_1024(struct array_1024* __this) {
    assert(__this);
}



void string_init_21_(struct string* __this, struct array_1024* buffer)
{
struct string* t37 = __this;
assert(t37);
struct array_1024* t38 = t37->buffer;
struct array_1024* t39 = buffer;
assert(t37);
t37->buffer = t39;


}

int string_length_24_(struct string* __this)
{
struct string* t40 = __this;
assert(t40);
struct array_1024* t41 = t40->buffer;
int t42 = array_size_16_1024(t41);
int t43 = 0;
boolean t44 = t42 > t43;
assert_true(t44);

struct string* t45 = __this;
assert(t45);
struct array_1024* t46 = t45->buffer;
int t47 = array_size_16_1024(t46);
int t48 = 1;
int t49 = t47 - t48;


return t49;

}

char string_get_27_(struct string* __this, int index)
{
int t50 = index;
int t51 = 0;
boolean t52 = t50 >= t51;
assert_true(t52);

int t53 = index;
struct string* t54 = __this;
assert(t54);
struct array_1024* t55 = t54->buffer;
int t56 = array_size_16_1024(t55);
boolean t57 = t53 < t56;
assert_true(t57);

struct string* t58 = __this;
assert(t58);
struct array_1024* t59 = t58->buffer;
int t60 = index;
char t61 = array_get_12_1024(t59, t60);


return t61;

}

struct array_1024* string_bytes_30_(struct string* __this)
{
struct string* t62 = __this;
assert(t62);
struct array_1024* t63 = t62->buffer;


return t63;

}

void string_deinit_34_(struct string* __this)
{
struct string* t64 = __this;
assert(t64);
struct array_1024* t65 = t64->buffer;
array_deinit_32_1024(t65);


}


int main_class_main_4_()
{
struct array_1024* t66 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_6_1024(t66);
struct array_1024* args = t66;

struct array_1024* t67 = args;
char t68 = 'a';
array_add_10_1024(t67, t68);

struct array_1024* t69 = args;
char t70 = 'b';
array_add_10_1024(t69, t70);

struct array_1024* t71 = args;
char t72 = 'c';
array_add_10_1024(t71, t72);

struct array_1024* t73 = args;
char t74 = 'd';
array_add_10_1024(t73, t74);

struct array_1024* t75 = args;
char t76 = 'e';
array_add_10_1024(t75, t76);


{
int t77 = 0;
int i = t77;

for(;;)
{
int t78 = i;
struct array_1024* t79 = args;
int t80 = array_size_16_1024(t79);
boolean t81 = t78 < t80;
boolean t82 = !t81;

if(t82)
{

break;

}

struct array_1024* t87 = args;
int t88 = i;
char t89 = array_get_12_1024(t87, t88);
char c = t89;

struct array_1024* t90 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
int t94 = 17;
array_init_8_1024(t90, t94);
hstrncpy(t90->data, t91, 16);
struct string* t92 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_21_(t92, t90);
char t93 = c;
print(t92, t93);

int t83 = i;
int t84 = i;
int t85 = 1;
int t86 = t84 + t85;
i = t86;


}


}

int t95 = 0;

array_deinit_32_1024(args);

return t95;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_4_();

    printf("%d\n", result);
    return result;

}
