#include <assert.h>                            
#include <limits.h>                            
#include <stdarg.h>                            
#include <stddef.h>                            
#include <stdint.h>                            
#include <stdio.h>                             
#include <stdlib.h>                            
#include <string.h>                            
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
typedef char * char_ptr_t;
typedef int boolean;                        
typedef struct string * string;             

struct string                               
{                                           
    char *buffer;                           
    size_t len;                             
};                                          

void string_init(string __this, char *buf); 
typedef struct ptr_char * ptr_char;
typedef struct vec_char * vec_char;

void ptr_init_2char(ptr_char __this, int size);
void ptr_destroy_5char(ptr_char __this);
char ptr_get_8char(ptr_char __this, int at);
char ptr_set_11char(ptr_char __this, int at, char e);
void vec_init_14char(vec_char __this);
void vec_add_20char(vec_char __this, char e);
int vec_size_23char(vec_char __this);
char vec_get_26char(vec_char __this, int index);
char vec_set_29char(vec_char __this, int index, char e);
int main_class_main_34();

struct ptr_char
{
char_ptr_t raw_data; 
int size; 

};

struct vec_char
{
ptr_char data; 
int size; 
int alloc; 

};


#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 


void string_init(string __this, char *buf)    
{                                             
    assert(__this);                           
    assert(buf);                              
    __this->buffer = hstrdup(buf);            
    __this->len = strlen(buf);                
    __this->buffer[__this->len] = '\0';      
}                                             
char string_get(string __this, size_t index)  
{                                             
    assert(__this);                           
    assert(__this->buffer);                   
    assert(index < __this->len);              
    return __this->buffer[index];             
}                                             
size_t string_length(string __this)  
{                                             
    assert(__this);                           
    return __this->len;                       
}                                             

char *  std_mem_malloc_char_ptr_t_int (char *  stub__, size_t size)   
{                                                                  
  char *  ptr = (char * ) hmalloc(size);                       
  return ptr;                                                      
}                                                                  

char std_mem_get_char_ptr_t_int (char *  raw_data, size_t index) 
{                                                                  
  assert(raw_data);                                                
  return raw_data[index];                                          
}                                                                  

char std_mem_set_char_ptr_t_int_char (char *  raw_data, size_t index, char e) 
{                                                                                
  assert(raw_data);                                                              
  char old = raw_data[index];                                              
  raw_data[index] = e;                                                           
  return old;                                                                    
}                                                                                

void std_mem_free_char_ptr_t(void *p) { 
  free(p);
}
void ptr_init_2char(ptr_char __this, int size)
{
const ptr_char t35 = __this;
assert(t35);
const char_ptr_t t36 = t35->raw_data;
const ptr_char t37 = __this;
assert(t37);
const char_ptr_t t38 = t37->raw_data;
const int t39 = size;
const char_ptr_t t40 = std_mem_malloc_char_ptr_t_int(t38, t39);
assert(t35);
t35->raw_data = t40;

const ptr_char t41 = __this;
assert(t41);
const int t42 = t41->size;
const int t43 = size;
assert(t41);
t41->size = t43;


}

void ptr_destroy_5char(ptr_char __this)
{
const ptr_char t44 = __this;
assert(t44);
const char_ptr_t t45 = t44->raw_data;
std_mem_free_char_ptr_t(t45);


}

char ptr_get_8char(ptr_char __this, int at)
{
const int t46 = at;
const ptr_char t47 = __this;
assert(t47);
const int t48 = t47->size;
const boolean t49 = t46 < t48;
assert_true(t49);

const ptr_char t50 = __this;
assert(t50);
const char_ptr_t t51 = t50->raw_data;
const int t52 = at;
const char t53 = std_mem_get_char_ptr_t_int(t51, t52);


return t53;

}

char ptr_set_11char(ptr_char __this, int at, char e)
{
const int t54 = at;
const ptr_char t55 = __this;
assert(t55);
const int t56 = t55->size;
const boolean t57 = t54 < t56;
assert_true(t57);

const ptr_char t58 = __this;
assert(t58);
const char_ptr_t t59 = t58->raw_data;
const int t60 = at;
const char t61 = std_mem_get_char_ptr_t_int(t59, t60);
char old = t61;

const ptr_char t62 = __this;
assert(t62);
const char_ptr_t t63 = t62->raw_data;
const int t64 = at;
const char t65 = e;
const char t66 = std_mem_set_char_ptr_t_int_char(t63, t64, t65);

const char t67 = old;


return t67;

}

void vec_init_14char(vec_char __this)
{
const vec_char t68 = __this;
assert(t68);
const int t69 = t68->size;
const int t70 = 0;
assert(t68);
t68->size = t70;

const vec_char t71 = __this;
assert(t71);
const int t72 = t71->alloc;
const int t73 = 2;
assert(t71);
t71->alloc = t73;

const vec_char t74 = __this;
assert(t74);
const ptr_char t75 = t74->data;
const int t76 = sizeof(char);
const vec_char t77 = __this;
assert(t77);
const int t78 = t77->alloc;
const int t79 = t76 * t78;
const ptr_char t80 = hmalloc(sizeof(struct ptr_char));
ptr_init_2char(t80, t79);
assert(t74);
t74->data = t80;


}

void vec_add_20char(vec_char __this, char e)
{
const vec_char t81 = __this;
assert(t81);
const int t82 = t81->size;
const vec_char t83 = __this;
assert(t83);
const int t84 = t83->alloc;
const boolean t85 = t82 >= t84;

if(t85)
{
const vec_char t86 = __this;
assert(t86);
const int t87 = t86->alloc;
const vec_char t88 = __this;
assert(t88);
const int t89 = t88->alloc;
const int t90 = 2;
const int t91 = t89 * t90;
assert(t86);
t86->alloc = t91;

const int t92 = sizeof(char);
const vec_char t93 = __this;
assert(t93);
const int t94 = t93->alloc;
const int t95 = t92 * t94;
const ptr_char t96 = hmalloc(sizeof(struct ptr_char));
ptr_init_2char(t96, t95);
ptr_char ndata = t96;


{
const int t97 = 0;
int i = t97;

for(;;)
{
const int t98 = i;
const vec_char t99 = __this;
assert(t99);
const int t100 = t99->size;
const boolean t101 = t98 < t100;
const boolean t102 = !t101;

if(t102)
{

break;

}

const ptr_char t107 = ndata;
const int t108 = i;
const vec_char t109 = __this;
assert(t109);
const ptr_char t110 = t109->data;
const int t111 = i;
const char t112 = ptr_get_8char(t110, t111);
const char t113 = ptr_set_11char(t107, t108, t112);

const int t103 = i;
const int t104 = i;
const int t105 = 1;
const int t106 = t104 + t105;
i = t106;


}


}

const vec_char t114 = __this;
assert(t114);
const ptr_char t115 = t114->data;
ptr_destroy_5char(t115);

const vec_char t116 = __this;
assert(t116);
const ptr_char t117 = t116->data;
const ptr_char t118 = ndata;
assert(t116);
t116->data = t118;


}

const vec_char t119 = __this;
assert(t119);
const ptr_char t120 = t119->data;
const vec_char t121 = __this;
assert(t121);
const int t122 = t121->size;
const char t123 = e;
const char t124 = ptr_set_11char(t120, t122, t123);

const vec_char t125 = __this;
assert(t125);
const int t126 = t125->size;
const vec_char t127 = __this;
assert(t127);
const int t128 = t127->size;
const int t129 = 1;
const int t130 = t128 + t129;
assert(t125);
t125->size = t130;


}

int vec_size_23char(vec_char __this)
{
const vec_char t131 = __this;
assert(t131);
const int t132 = t131->size;


return t132;

}

char vec_get_26char(vec_char __this, int index)
{
const int t133 = index;
const vec_char t134 = __this;
assert(t134);
const int t135 = t134->size;
const boolean t136 = t133 < t135;
assert_true(t136);

const vec_char t137 = __this;
assert(t137);
const ptr_char t138 = t137->data;
const int t139 = index;
const char t140 = ptr_get_8char(t138, t139);


return t140;

}

char vec_set_29char(vec_char __this, int index, char e)
{
const int t141 = index;
const vec_char t142 = __this;
assert(t142);
const int t143 = t142->size;
const boolean t144 = t141 < t143;
assert_true(t144);

const vec_char t145 = __this;
assert(t145);
const ptr_char t146 = t145->data;
const int t147 = index;
const char t148 = e;
const char t149 = ptr_set_11char(t146, t147, t148);


return t149;

}


int main_class_main_34()
{
const vec_char t150 = hmalloc(sizeof(struct vec_char));
vec_init_14char(t150);
vec_char x = t150;

const vec_char t151 = x;
const char t152 = 'a';
vec_add_20char(t151, t152);

const vec_char t153 = x;
const char t154 = 'b';
vec_add_20char(t153, t154);

const vec_char t155 = x;
const char t156 = 'c';
vec_add_20char(t155, t156);

const vec_char t157 = x;
const char t158 = 'd';
vec_add_20char(t157, t158);

const vec_char t159 = x;
const char t160 = 'e';
vec_add_20char(t159, t160);


{
const int t161 = 0;
int i = t161;

for(;;)
{
const int t162 = i;
const vec_char t163 = x;
const int t164 = vec_size_23char(t163);
const boolean t165 = t162 < t164;
const boolean t166 = !t165;

if(t166)
{

break;

}

const vec_char t171 = x;
const int t172 = i;
const char t173 = vec_get_26char(t171, t172);
char c = t173;

const int t167 = i;
const int t168 = i;
const int t169 = 1;
const int t170 = t168 + t169;
i = t170;


}


}

const int t174 = 0;


return t174;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_34();

    printf("%d\n", result);
    return result;

}
