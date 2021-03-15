#include "generated_types.h" 
#include "hrt/heap.h"        
struct type_descr *TD_CHAR_PTR = &(struct type_descr ) { .description = "TD_CHAR_PTR", };       
struct type_descr *TD_ARRAY = &(struct type_descr ) { .description = "TD_ARRAY", };             
struct type_descr *TD_ARRAY_TABLE = &(struct type_descr ) { .description = "TD_ARRAY_TABLE", }; 
struct type_descr *TD_MAIN_CLASS = &(struct type_descr ) { .description = "main_class", };
struct type_descr *TD_TYPE = &(struct type_descr ) { .description = "type", };
struct type_descr *TD_TOKEN = &(struct type_descr ) { .description = "token", };
struct type_descr *TD_FILE_READER = &(struct type_descr ) { .description = "file_reader", };
struct type_descr *TD_STRING = &(struct type_descr ) { .description = "string", };
char t46[] = { '%', 'd', '\n', '\0'};
char t42[] = { '%', 's', '\n', '\0'};
char t57[] = { 'i', 'n', 't', '\0'};
char t72[] = { 'x', '\0'};
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
void string_deinit(string __this)             
{                                             
}                                             
void string_destroy(string __this)            
{                                             
    assert(__this);                           
    free(__this->buffer);                     
    free(__this);                             
}                                             

void type_init_23(type __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const type t51 = __this;
assert(t51);
const token t52 = t51->pos;
const token t53 = get_memory(sizeof(struct token) , TD_TOKEN);
reg_ptr_in_a_frame(t53);
token_init_20(t53);
assert(t51);
t51->pos = t53;

const type t54 = __this;
assert(t54);
const string t55 = t54->repr;
const string t56 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t56);
string_init(t56, t57);
assert(t54);
t54->repr = t56;

const type t58 = __this;
assert(t58);
const file_reader t59 = t58->fp;
const file_reader t60 = get_memory(sizeof(struct file_reader) , TD_FILE_READER);
reg_ptr_in_a_frame(t60);
file_reader_init_14(t60);
assert(t58);
t58->fp = t60;

close_frame();

}
void type_deinit_26(type __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const type t61 = __this;
assert(t61);
const file_reader t62 = t61->fp;
file_reader_close_17(t62);

const type t63 = __this;
assert(t63);
const file_reader t64 = t63->fp;
file_reader_deinit_35(t64);

const type t65 = __this;
assert(t65);
const string t66 = t65->repr;
string_deinit(t66);

const type t67 = __this;
assert(t67);
const token t68 = t67->pos;
token_deinit_33(t68);

close_frame();

}
void token_init_20(token __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const token t69 = __this;
assert(t69);
const string t70 = t69->value;
const string t71 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t71);
string_init(t71, t72);
assert(t69);
t69->value = t71;

const token t73 = __this;
assert(t73);
const int t74 = t73->f;
const int t75 = 1;
assert(t73);
t73->f = t75;

close_frame();

}
void token_deinit_33(token __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const token t76 = __this;
assert(t76);
const string t77 = t76->value;
string_deinit(t77);

close_frame();

}
void file_reader_init_14(file_reader __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const file_reader t78 = __this;
assert(t78);
const int t79 = t78->fp;
const int t80 = 1;
const int t81 = -t80;
assert(t78);
t78->fp = t81;

close_frame();

}
void file_reader_close_17(file_reader __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
close_frame();

}
void file_reader_deinit_35(file_reader __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
close_frame();

}
static void std_print_string_string(const string t41, const string t44)
{
assert(t41);
assert(t44);

    printf(t41->buffer, t44->buffer);
}
static void std_print_string_int(const string t45, const int t48)
{
assert(t45);

    printf(t45->buffer, t48);
}

int main_class_main_29()
{
open_frame();
const type t38 = get_memory(sizeof(struct type) , TD_TYPE);
reg_ptr_in_a_frame(t38);
type_init_23(t38);
const type tp = t38;

const type t39 = tp;
assert(t39);
const token t40 = t39->pos;
const token tok = t40;

const string t41 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t41);
string_init(t41, t42);
const token t43 = tok;
assert(t43);
const string t44 = t43->value;
std_print_string_string(t41, t44);

const string t45 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t45);
string_init(t45, t46);
const token t47 = tok;
assert(t47);
const int t48 = t47->f;
std_print_string_int(t45, t48);

const token t49 = tok;
assert(t49);
const int t50 = t49->f;

token_deinit_33(tok);
type_deinit_26(tp);
close_frame();

return t50;

}
int main(int args, char** argv) 
{
    initHeap();   
    init_frames();
    open_frame(); 

    int result = main_class_main_29();

    dump_heap();   
    close_frame(); 
    printf("%d\n", result);
    return result;

}
