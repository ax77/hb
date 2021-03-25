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

struct stub;
struct none;

int main_class_main_9_();
void stub_init_5_(struct stub* __this);
void stub_deinit_13_(struct stub* __this);
void none_init_2_(struct none* __this);
void none_deinit_15_(struct none* __this);

struct stub
{
int f; 
struct none* n; 

};

struct none
{
int x; 

};


#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 



void stub_init_5_(struct stub* __this)
{
struct stub* t20 = __this;
assert(t20);
struct none* t21 = t20->n;
struct none* t22 = (struct none*) hcalloc( 1u, sizeof(struct none) );
none_init_2_(t22);
assert(t20);
t20->n = t22;

struct stub* t23 = __this;
assert(t23);
int t24 = t23->f;
int t25 = 1;
int t26 = -t25;
assert(t23);
t23->f = t26;


}

void stub_deinit_13_(struct stub* __this)
{
struct stub* t27 = __this;
assert(t27);
struct none* t28 = t27->n;
none_deinit_15_(t28);


}

void none_init_2_(struct none* __this)
{
struct none* t29 = __this;
assert(t29);
int t30 = t29->x;
int t31 = 32;
assert(t29);
t29->x = t31;


}

void none_deinit_15_(struct none* __this)
{

}


int main_class_main_9_()
{
struct stub* t16 = (struct stub*) hcalloc( 1u, sizeof(struct stub) );
stub_init_5_(t16);
struct stub* s1 = t16;

struct stub* t17 = (struct stub*) hcalloc( 1u, sizeof(struct stub) );
stub_init_5_(t17);
struct stub* s2 = t17;


{
struct stub* t18 = (struct stub*) hcalloc( 1u, sizeof(struct stub) );
stub_init_5_(t18);
struct stub* s3 = t18;

stub_deinit_13_(s3);

}

int t19 = 0;

stub_deinit_13_(s2);
stub_deinit_13_(s1);

return t19;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_9_();

    printf("%d\n", result);
    return result;

}
