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




void stub_init_5_(struct stub* __this)
{
struct none* t23 = (struct none*) hcalloc( 1u, sizeof(struct none) );
none_init_2_(t23);
struct none* tmp = t23;

struct stub* t24 = __this;
assert(t24);
struct none* t25 = t24->n;
struct none* t26 = (struct none*) hcalloc( 1u, sizeof(struct none) );
none_init_2_(t26);
assert(t24);
t24->n = t26;

struct stub* t27 = __this;
assert(t27);
int t28 = t27->f;
int t29 = 1;
int t30 = -t29;
assert(t27);
t27->f = t30;

none_deinit_15_(tmp);

}

void stub_deinit_13_(struct stub* __this)
{
struct stub* t31 = __this;
assert(t31);
struct none* t32 = t31->n;
none_deinit_15_(t32);


}

void none_init_2_(struct none* __this)
{
struct none* t33 = __this;
assert(t33);
int t34 = t33->x;
int t35 = 32;
assert(t33);
t33->x = t35;


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

int t19 = 1;
int t20 = 1;
boolean t21 = t19 == t20;
assert_true(t21);

int t22 = 0;

stub_deinit_13_(s2);
stub_deinit_13_(s1);

return t22;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_9_();

    printf("%d\n", result);
    return result;

}
