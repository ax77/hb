#include "generated_types.h" 
#include "hrt/heap.h"        

struct type_descr *TD_CHAR_PTR = &(struct type_descr ) { .description = "TD_CHAR_PTR", };       
struct type_descr *TD_ARRAY = &(struct type_descr ) { .description = "TD_ARRAY", };             
struct type_descr *TD_ARRAY_TABLE = &(struct type_descr ) { .description = "TD_ARRAY_TABLE", }; 
struct type_descr *TD_MAIN_CLASS = &(struct type_descr ) { .description = "main_class", };
struct type_descr *TD_STRING = &(struct type_descr ) { .description = "string", };

char t26[] = { 'a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', '\0'};
char t39[] = { '1', '.', '2', '.', '3', '\0'};
char t44[] = { '%', 'c', '\n', '\0'};

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

static void std_print_string_char(const string t43, const char t45)
{
assert(t43);

    printf(t43->buffer, t45);
}


int main_class_main_20()
{
open_frame();
const string t25 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t25);
string_init(t25, t26);
string s = t25;

const int t27 = 0;
int cnt = t27;


{
const int t28 = 0;
int i = t28;

for(;;)
{
const int t29 = i;
const string t30 = s;
const int t31 = string_length(t30);
const boolean t32 = t29 < t31;
const boolean t33 = !t32;

if(t33)
{

break;

}

const string t38 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t38);
string_init(t38, t39);
string local_check_destroy = t38;

const string t40 = s;
const int t41 = i;
const char t42 = string_get(t40, t41);
char c = t42;

const string t43 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t43);
string_init(t43, t44);
const char t45 = c;
std_print_string_char(t43, t45);

const int t46 = cnt;
const int t47 = cnt;
const int t48 = 1;
const int t49 = t47 + t48;
cnt = t49;

const int t34 = i;
const int t35 = i;
const int t36 = 1;
const int t37 = t35 + t36;
i = t37;


}


}

const int t50 = cnt;

close_frame();

return t50;

}
int main(int args, char** argv) 
{
    initHeap();   
    init_frames();
    open_frame(); 

    int result = main_class_main_20();

    dump_heap();   
    close_frame(); 
    printf("%d\n", result);
    return result;

}
