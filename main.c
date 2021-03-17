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
typedef struct array_char * array_char;
typedef struct ptr_char * ptr_char;
typedef struct string * string;

void array_init_17char(array_char __this);
void array_add_23char(array_char __this, char e);
int array_size_26char(array_char __this);
char array_get_29char(array_char __this, int index);
char array_set_32char(array_char __this, int index, char e);
void ptr_init_2char(ptr_char __this, int size);
void ptr_destroy_5char(ptr_char __this);
char ptr_get_8char(ptr_char __this, int at);
char ptr_set_11char(ptr_char __this, int at, char e);
int ptr_size_14char(ptr_char __this);
int main_class_main_46();
void string_init_35(string __this, ptr_char buffer);
int string_length_38(string __this);
char string_get_41(string __this, int index);

struct array_char
{
ptr_char data; 
int size; 
int alloc; 

};

struct ptr_char
{
char_ptr_t raw_data; 
int size; 

};

struct string
{
ptr_char buffer; 

};


#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 

char t198[] = { '%', 'c', '\n', '\0'};


static void std_print_string_char(const string t197, const char t199)
{
assert(t197);

    printf(t197->buffer, t199);
}

char std_mem_set_char_ptr_t_int_char (char *  raw_data, size_t index, char e) 
{                                                                                
  assert(raw_data);                                                              
  char old = raw_data[index];                                              
  raw_data[index] = e;                                                           
  return old;                                                                    
}                                                                                

char std_mem_get_char_ptr_t_int (char *  raw_data, size_t index) 
{                                                                  
  assert(raw_data);                                                
  return raw_data[index];                                          
}                                                                  

void std_mem_free_char_ptr_t(void *p) { 
  free(p);
}
char *  std_mem_malloc_char_ptr_t_int (char *  stub__, size_t size)   
{                                                                  
  char *  ptr = (char * ) hmalloc(size);                       
  return ptr;                                                      
}                                                                  

void array_init_17char(array_char __this)
{
const array_char t47 = __this;
assert(t47);
const int t48 = t47->size;
const int t49 = 0;
assert(t47);
t47->size = t49;

const array_char t50 = __this;
assert(t50);
const int t51 = t50->alloc;
const int t52 = 2;
assert(t50);
t50->alloc = t52;

const array_char t53 = __this;
assert(t53);
const ptr_char t54 = t53->data;
const int t55 = sizeof(char);
const array_char t56 = __this;
assert(t56);
const int t57 = t56->alloc;
const int t58 = t55 * t57;
const ptr_char t59 = hmalloc(sizeof(struct ptr_char));
ptr_init_2char(t59, t58);
assert(t53);
t53->data = t59;


}

void array_add_23char(array_char __this, char e)
{
const array_char t60 = __this;
assert(t60);
const int t61 = t60->size;
const array_char t62 = __this;
assert(t62);
const int t63 = t62->alloc;
const boolean t64 = t61 >= t63;

if(t64)
{
const array_char t65 = __this;
assert(t65);
const int t66 = t65->alloc;
const array_char t67 = __this;
assert(t67);
const int t68 = t67->alloc;
const int t69 = 2;
const int t70 = t68 * t69;
assert(t65);
t65->alloc = t70;

const int t71 = sizeof(char);
const array_char t72 = __this;
assert(t72);
const int t73 = t72->alloc;
const int t74 = t71 * t73;
const ptr_char t75 = hmalloc(sizeof(struct ptr_char));
ptr_init_2char(t75, t74);
ptr_char ndata = t75;


{
const int t76 = 0;
int i = t76;

for(;;)
{
const int t77 = i;
const array_char t78 = __this;
assert(t78);
const int t79 = t78->size;
const boolean t80 = t77 < t79;
const boolean t81 = !t80;

if(t81)
{

break;

}

const ptr_char t86 = ndata;
const int t87 = i;
const array_char t88 = __this;
assert(t88);
const ptr_char t89 = t88->data;
const int t90 = i;
const char t91 = ptr_get_8char(t89, t90);
const char t92 = ptr_set_11char(t86, t87, t91);

const int t82 = i;
const int t83 = i;
const int t84 = 1;
const int t85 = t83 + t84;
i = t85;


}


}

const array_char t93 = __this;
assert(t93);
const ptr_char t94 = t93->data;
ptr_destroy_5char(t94);

const array_char t95 = __this;
assert(t95);
const ptr_char t96 = t95->data;
const ptr_char t97 = ndata;
assert(t95);
t95->data = t97;


}

const array_char t98 = __this;
assert(t98);
const ptr_char t99 = t98->data;
const array_char t100 = __this;
assert(t100);
const int t101 = t100->size;
const char t102 = e;
const char t103 = ptr_set_11char(t99, t101, t102);

const array_char t104 = __this;
assert(t104);
const int t105 = t104->size;
const array_char t106 = __this;
assert(t106);
const int t107 = t106->size;
const int t108 = 1;
const int t109 = t107 + t108;
assert(t104);
t104->size = t109;


}

int array_size_26char(array_char __this)
{
const array_char t110 = __this;
assert(t110);
const int t111 = t110->size;


return t111;

}

char array_get_29char(array_char __this, int index)
{
const int t112 = index;
const int t113 = 0;
const boolean t114 = t112 >= t113;
assert_true(t114);

const int t115 = index;
const array_char t116 = __this;
assert(t116);
const int t117 = t116->size;
const boolean t118 = t115 < t117;
assert_true(t118);

const array_char t119 = __this;
assert(t119);
const ptr_char t120 = t119->data;
const int t121 = index;
const char t122 = ptr_get_8char(t120, t121);


return t122;

}

char array_set_32char(array_char __this, int index, char e)
{
const int t123 = index;
const int t124 = 0;
const boolean t125 = t123 >= t124;
assert_true(t125);

const int t126 = index;
const array_char t127 = __this;
assert(t127);
const int t128 = t127->size;
const boolean t129 = t126 < t128;
assert_true(t129);

const array_char t130 = __this;
assert(t130);
const ptr_char t131 = t130->data;
const int t132 = index;
const char t133 = e;
const char t134 = ptr_set_11char(t131, t132, t133);


return t134;

}

void ptr_init_2char(ptr_char __this, int size)
{
const int t135 = size;
const int t136 = 0;
const boolean t137 = t135 > t136;
assert_true(t137);

const ptr_char t138 = __this;
assert(t138);
const char_ptr_t t139 = t138->raw_data;
const ptr_char t140 = __this;
assert(t140);
const char_ptr_t t141 = t140->raw_data;
const int t142 = size;
const char_ptr_t t143 = std_mem_malloc_char_ptr_t_int(t141, t142);
assert(t138);
t138->raw_data = t143;

const ptr_char t144 = __this;
assert(t144);
const int t145 = t144->size;
const int t146 = size;
assert(t144);
t144->size = t146;


}

void ptr_destroy_5char(ptr_char __this)
{
const ptr_char t147 = __this;
assert(t147);
const char_ptr_t t148 = t147->raw_data;
std_mem_free_char_ptr_t(t148);


}

char ptr_get_8char(ptr_char __this, int at)
{
const int t149 = at;
const ptr_char t150 = __this;
assert(t150);
const int t151 = t150->size;
const boolean t152 = t149 < t151;
assert_true(t152);

const ptr_char t153 = __this;
assert(t153);
const char_ptr_t t154 = t153->raw_data;
const int t155 = at;
const char t156 = std_mem_get_char_ptr_t_int(t154, t155);


return t156;

}

char ptr_set_11char(ptr_char __this, int at, char e)
{
const int t157 = at;
const ptr_char t158 = __this;
assert(t158);
const int t159 = t158->size;
const boolean t160 = t157 < t159;
assert_true(t160);

const ptr_char t161 = __this;
assert(t161);
const char_ptr_t t162 = t161->raw_data;
const int t163 = at;
const char t164 = std_mem_get_char_ptr_t_int(t162, t163);
char old = t164;

const ptr_char t165 = __this;
assert(t165);
const char_ptr_t t166 = t165->raw_data;
const int t167 = at;
const char t168 = e;
const char t169 = std_mem_set_char_ptr_t_int_char(t166, t167, t168);

const char t170 = old;


return t170;

}

int ptr_size_14char(ptr_char __this)
{
const ptr_char t171 = __this;
assert(t171);
const int t172 = t171->size;


return t172;

}

void string_init_35(string __this, ptr_char buffer)
{
const string t201 = __this;
assert(t201);
const ptr_char t202 = t201->buffer;
const ptr_char t203 = buffer;
assert(t201);
t201->buffer = t203;


}

int string_length_38(string __this)
{
const string t204 = __this;
assert(t204);
const ptr_char t205 = t204->buffer;
const int t206 = ptr_size_14char(t205);


return t206;

}

char string_get_41(string __this, int index)
{
const int t207 = index;
const int t208 = 0;
const boolean t209 = t207 >= t208;
assert_true(t209);

const int t210 = index;
const string t211 = __this;
assert(t211);
const ptr_char t212 = t211->buffer;
const int t213 = ptr_size_14char(t212);
const boolean t214 = t210 < t213;
assert_true(t214);

const string t215 = __this;
assert(t215);
const ptr_char t216 = t215->buffer;
const int t217 = index;
const char t218 = ptr_get_8char(t216, t217);


return t218;

}


int main_class_main_46()
{
const array_char t173 = hmalloc(sizeof(struct array_char));
array_init_17char(t173);
array_char x = t173;

const array_char t174 = x;
const char t175 = 'a';
array_add_23char(t174, t175);

const array_char t176 = x;
const char t177 = 'b';
array_add_23char(t176, t177);

const array_char t178 = x;
const char t179 = 'c';
array_add_23char(t178, t179);

const array_char t180 = x;
const char t181 = 'd';
array_add_23char(t180, t181);

const array_char t182 = x;
const char t183 = 'e';
array_add_23char(t182, t183);


{
const int t184 = 0;
int i = t184;

for(;;)
{
const int t185 = i;
const array_char t186 = x;
const int t187 = array_size_26char(t186);
const boolean t188 = t185 < t187;
const boolean t189 = !t188;

if(t189)
{

break;

}

const array_char t194 = x;
const int t195 = i;
const char t196 = array_get_29char(t194, t195);
char c = t196;

const string t197 = hmalloc(sizeof(struct string));
string_init_35(t197, t198);
const char t199 = c;
std_print_string_char(t197, t199);

const int t190 = i;
const int t191 = i;
const int t192 = 1;
const int t193 = t191 + t192;
i = t193;


}


}

const int t200 = 0;


return t200;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_46();

    printf("%d\n", result);
    return result;

}
