#include "generated_types.h" 
#include "hrt/heap.h"        

#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 



struct type_descr *TD_CHAR_PTR = &(struct type_descr ) { .description = "TD_CHAR_PTR", };       
struct type_descr *TD_ARRAY = &(struct type_descr ) { .description = "TD_ARRAY", };             
struct type_descr *TD_ARRAY_TABLE = &(struct type_descr ) { .description = "TD_ARRAY_TABLE", }; 
struct type_descr *TD_MAIN_CLASS = &(struct type_descr ) { .description = "main_class", };
struct type_descr *TD_STRING = &(struct type_descr ) { .description = "string", };

char t24[] = { 'a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', '\0'};

void string_init(string __this, char *buf)    
{                                             
    assert(__this);                           
    assert(buf);                              
    __this->buffer = get_memory_strdup(buf);  
    __this->len = strlen(buf);                
    __this->buffer[__this->len] = '\0';      
}                                             
char string_get(string __this, size_t index)  
{                                             
    assert(__this);                           
    assert(index < __this->len);              
    return __this->buffer[index];             
}                                             
size_t string_length(string __this)  
{                                             
    assert(__this);                           
    return __this->len;             
}                                             
void string_deinit(string __this)             
{                                             
}                                             
void string_destroy(string __this)            
{                                             
    assert(__this);                           
    free(__this->buffer);                     
    free(__this);                             
}                                             


int main_class_main_18()
{
open_frame();
const string t23 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t23);
string_init(t23, t24);
string s = t23;

const string t25 = s;
const int t26 = 0;
const char t27 = string_get(t25, t26);
const char t28 = 'a';
const boolean t29 = t27 == t28;
assert_true(t29);

const string t30 = s;
const string t31 = s;
const int t32 = string_length(t31);
const int t33 = 1;
const int t34 = t32 - t33;
const char t35 = string_get(t30, t34);
const char t36 = 'F';
const boolean t37 = t35 == t36;
assert_true(t37);

const int t38 = 0;

close_frame();

return t38;

}
int main(int args, char** argv) 
{
    initHeap();   
    init_frames();
    open_frame(); 

    int result = main_class_main_18();

    dump_heap();   
    close_frame(); 
    printf("%d\n", result);
    return result;

}
