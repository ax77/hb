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

struct arr_1024;
struct arr_1025;
struct string;

void arr_init_16_1024(struct arr_1024* __this, int size);
char arr_get_18_1024(struct arr_1024* __this, int index);
char arr_set_20_1024(struct arr_1024* __this, int index, char element);
int arr_size_22_1024(struct arr_1024* __this);
void arr_init_16_1025(struct arr_1025* __this, int size);
struct string* arr_get_18_1025(struct arr_1025* __this, int index);
struct string* arr_set_20_1025(struct arr_1025* __this, int index, struct string* element);
int arr_size_22_1025(struct arr_1025* __this);
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

char t38[] = { 'q', '.', 'b', '.', 'e', '\0'};
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

void arr_init_16_1024(struct arr_1024* __this, int size) {
    assert(__this);
    assert(size > 0);
    __this->size = size;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * size) );
}

char arr_get_18_1024(struct arr_1024* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}

char arr_set_20_1024(struct arr_1024* __this, int index, char element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    char old =  __this->data[index];
    __this->data[index] = element;
    return old;
}

int arr_size_22_1024(struct arr_1024* __this) {
    assert(__this);
    return __this->size;
}

void arr_init_16_1025(struct arr_1025* __this, int size) {
    assert(__this);
    assert(size > 0);
    __this->size = size;
    __this->data = (struct string**) hcalloc( 1u, (sizeof(struct string*) * size) );
}

struct string* arr_get_18_1025(struct arr_1025* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}

struct string* arr_set_20_1025(struct arr_1025* __this, int index, struct string* element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    struct string* old =  __this->data[index];
    __this->data[index] = element;
    return old;
}

int arr_size_22_1025(struct arr_1025* __this) {
    assert(__this);
    return __this->size;
}



void string_init_5_(struct string* __this, struct arr_1024* buffer)
{
struct string* t23 = __this;
assert(t23);
struct arr_1024* t24 = t23->buffer;
struct arr_1024* t25 = buffer;
assert(t23);
t23->buffer = t25;


}

int string_length_8_(struct string* __this)
{
struct string* t26 = __this;
assert(t26);
struct arr_1024* t27 = t26->buffer;
int t28 = arr_size_22_1024(t27);


return t28;

}

char string_get_11_(struct string* __this, int index)
{
struct string* t29 = __this;
assert(t29);
struct arr_1024* t30 = t29->buffer;
int t31 = index;
char t32 = arr_get_18_1024(t30, t31);


return t32;

}

struct arr_1024* string_bytes_14_(struct string* __this)
{
struct string* t33 = __this;
assert(t33);
struct arr_1024* t34 = t33->buffer;


return t34;

}


int main_class_main_2_()
{
int t35 = 16;
struct arr_1025* t36 = (struct arr_1025*) hcalloc(1u, sizeof(struct arr_1025));
arr_init_16_1025(t36, t35);
struct arr_1025* buf = t36;

struct arr_1024* t37 = (struct arr_1024*) hcalloc(1u, sizeof(struct arr_1024));
int t40 = 6;
arr_init_16_1024(t37, t40);
strcpy(t37->data, t38);
struct string* t39 = (struct string*) hcalloc(1u, sizeof(struct string));
string_init_5_(t39, t37);
struct string* s = t39;

struct string* t41 = s;
int t42 = string_length_8_(t41);


return t42;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_2_();

    printf("%d\n", result);
    return result;

}
