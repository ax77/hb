#include "generated_types.h" 
#include "hrt/heap.h"        

struct type_descr *TD_CHAR_PTR = &(struct type_descr ) { .description = "TD_CHAR_PTR", };       
struct type_descr *TD_ARRAY = &(struct type_descr ) { .description = "TD_ARRAY", };             
struct type_descr *TD_ARRAY_TABLE = &(struct type_descr ) { .description = "TD_ARRAY_TABLE", }; 
struct type_descr *TD_MAIN_CLASS = &(struct type_descr ) { .description = "main_class", };
struct type_descr *TD_OPT = &(struct type_descr ) { .description = "opt", };
struct type_descr *TD_TYPE = &(struct type_descr ) { .description = "type", };
struct type_descr *TD_TOKEN = &(struct type_descr ) { .description = "token", };
struct type_descr *TD_FILE_READER = &(struct type_descr ) { .description = "file_reader", };
struct type_descr *TD_STRING = &(struct type_descr ) { .description = "string", };

char t54[] = { '%', 'd', '\n', '\0'};
char t50[] = { '%', 's', '\n', '\0'};
char t75[] = { 'i', 'n', 't', '\0'};
char t90[] = { 'x', '\0'};

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

static void std_print_string_string(const string t49, const string t52)
{
assert(t49);
assert(t52);

    printf(t49->buffer, t52->buffer);
}

static void std_print_string_int(const string t53, const int t56)
{
assert(t53);

    printf(t53->buffer, t56);
}

void opt_init_29(opt __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const opt t63 = __this;
assert(t63);
const int t64 = t63->sss;
const int t65 = 1;
assert(t63);
t63->sss = t65;

close_frame();

}

void opt_fn_32(opt __this, string s)
{
open_frame();
assert(__this);
assert(s);
reg_ptr_in_a_frame(__this);
reg_ptr_in_a_frame(s);
const string t66 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t66);
string_init(t66, t50);
const string t68 = s;
std_print_string_string(t66, t68);

close_frame();

}

void opt_deinit_39(opt __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
close_frame();

}

void type_init_23(type __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const type t69 = __this;
assert(t69);
const token t70 = t69->pos;
const token t71 = get_memory(sizeof(struct token) , TD_TOKEN);
reg_ptr_in_a_frame(t71);
token_init_20(t71);
assert(t69);
t69->pos = t71;

const type t72 = __this;
assert(t72);
const string t73 = t72->repr;
const string t74 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t74);
string_init(t74, t75);
assert(t72);
t72->repr = t74;

const type t76 = __this;
assert(t76);
const file_reader t77 = t76->fp;
const file_reader t78 = get_memory(sizeof(struct file_reader) , TD_FILE_READER);
reg_ptr_in_a_frame(t78);
file_reader_init_14(t78);
assert(t76);
t76->fp = t78;

close_frame();

}

void type_deinit_26(type __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const type t79 = __this;
assert(t79);
const file_reader t80 = t79->fp;
file_reader_close_17(t80);

const type t81 = __this;
assert(t81);
const file_reader t82 = t81->fp;
file_reader_deinit_43(t82);

const type t83 = __this;
assert(t83);
const string t84 = t83->repr;
string_deinit(t84);

const type t85 = __this;
assert(t85);
const token t86 = t85->pos;
token_deinit_41(t86);

close_frame();

}

void token_init_20(token __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const token t87 = __this;
assert(t87);
const string t88 = t87->value;
const string t89 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t89);
string_init(t89, t90);
assert(t87);
t87->value = t89;

const token t91 = __this;
assert(t91);
const int t92 = t91->f;
const int t93 = 1;
assert(t91);
t91->f = t93;

close_frame();

}

void token_deinit_41(token __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const token t94 = __this;
assert(t94);
const string t95 = t94->value;
string_deinit(t95);

close_frame();

}

void file_reader_init_14(file_reader __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
const file_reader t96 = __this;
assert(t96);
const int t97 = t96->sss;
const int t98 = 1;
assert(t96);
t96->sss = t98;

close_frame();

}

void file_reader_close_17(file_reader __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
close_frame();

}

void file_reader_deinit_43(file_reader __this)
{
open_frame();
assert(__this);
reg_ptr_in_a_frame(__this);
close_frame();

}


int main_class_main_35()
{
open_frame();
const type t46 = get_memory(sizeof(struct type) , TD_TYPE);
reg_ptr_in_a_frame(t46);
type_init_23(t46);
const type tp = t46;

const type t47 = tp;
assert(t47);
const token t48 = t47->pos;
const token tok = t48;

const string t49 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t49);
string_init(t49, t50);
const token t51 = tok;
assert(t51);
const string t52 = t51->value;
std_print_string_string(t49, t52);

const string t53 = get_memory(sizeof(struct string) , TD_STRING);
reg_ptr_in_a_frame(t53);
string_init(t53, t54);
const token t55 = tok;
assert(t55);
const int t56 = t55->f;
std_print_string_int(t53, t56);

const opt t57 = get_memory(sizeof(struct opt) , TD_OPT);
reg_ptr_in_a_frame(t57);
opt_init_29(t57);
const opt ooo = t57;

const opt t58 = ooo;
const token t59 = tok;
assert(t59);
const string t60 = t59->value;
opt_fn_32(t58, t60);

const token t61 = tok;
assert(t61);
const int t62 = t61->f;

opt_deinit_39(ooo);
token_deinit_41(tok);
type_deinit_26(tp);
close_frame();

return t62;

}
int main(int args, char** argv) 
{
    initHeap();   
    init_frames();
    open_frame(); 

    int result = main_class_main_35();

    dump_heap();   
    close_frame(); 
    printf("%d\n", result);
    return result;

}
