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
struct type_descr *TD_OPT = &(struct type_descr ) { .description = "opt", };
struct type_descr *TD_STRING = &(struct type_descr ) { .description = "string", };

char t50[] = { 'i', 'n', ' ', 'a', ' ', 'l', 'o', 'o', 'p', ' ', 't', 'h', 'i', 's', ' ', 's', 't', 'r', 'i', 'n', 'g', ' ', 'w', 'a', 's', ' ', 'd', 'e', 'f', 'i', 'n', 'e', 'd', '\0'};
char t66[] = { '[', '%', 'd', ']', '\n', '\0'};
char t37[] = { '[', '%', 's', ']', '\n', '\0'};
char t35[] = { '1', '.', '2', '.', '3', '\0'};
char t64[] = { '-', '1', '\0'};

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

static void std_print_string_string(const string t36, const string t38)
{
assert(t36);
assert(t38);

    printf(t36->buffer, t38->buffer);
}

static void std_print_string_int(const string t65, const int t68)
{
assert(t65);

    printf(t65->buffer, t68);
}

void opt_init_18(opt __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const opt t57 = __this;
assert(t57);
const int t58 = t57->flag;
const int t59 = 1;
const int t60 = -t59;
assert(t57);
t57->flag = t60;

const opt t61 = __this;
assert(t61);
const string t62 = t61->value;
const string t63 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t63);
string_init(t63, t64);
assert(t61);
t61->value = t63;

close_frame();

}

void opt_fn_21(opt __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const string t65 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t65);
string_init(t65, t66);
const opt t67 = __this;
assert(t67);
const int t68 = t67->flag;
std_print_string_int(t65, t68);

const string t69 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t69);
string_init(t69, t37);
const opt t71 = __this;
assert(t71);
const string t72 = t71->value;
std_print_string_string(t69, t72);

close_frame();

}

void opt_deinit_30(opt __this)
{
assert(__this);
const opt t73 = __this;
assert(t73);
const string t74 = t73->value;
string_deinit(t74);


}


int main_class_main_26()
{
open_frame();
const opt t33 = get_memory(sizeof(struct opt), TD_OPT);
reg_ptr_in_a_frame(t33);
opt_init_18(t33);
opt o = t33;

const string t34 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t34);
string_init(t34, t35);
string s = t34;

const string t36 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t36);
string_init(t36, t37);
const string t38 = s;
std_print_string_string(t36, t38);

const int t39 = 0;
int cnt = t39;


{
const int t40 = 0;
int i = t40;

for(;;)
{
const int t41 = i;
const int t42 = 8;
const boolean t43 = t41 < t42;
const boolean t44 = !t43;

if(t44)
{

break;

}

const string t49 = get_memory(sizeof(struct string), TD_STRING);
reg_ptr_in_a_frame(t49);
string_init(t49, t50);
string in_a_loop = t49;

const int t51 = cnt;
const int t52 = cnt;
const int t53 = 1;
const int t54 = t52 + t53;
cnt = t54;

const int t45 = i;
const int t46 = i;
const int t47 = 1;
const int t48 = t46 + t47;
i = t48;


}


}

const opt t55 = o;
opt_fn_21(t55);

const int t56 = cnt;

close_frame();

return t56;

}
int main(int args, char** argv) 
{
    initHeap();   
    init_frames();
    open_frame(); 

    int result = main_class_main_26();

    dump_heap();   
    close_frame(); 
    printf("%d\n", result);
    return result;

}
