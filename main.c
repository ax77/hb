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
    char* rv = (char*) hcalloc(1u, len + 1);   
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

struct array_1024;
struct string;
struct file;

void array_init_58_1024(struct array_1024* __this);
void array_init_60_1024(struct array_1024* __this, int size);
void array_add_62_1024(struct array_1024* __this, char element);
char array_get_64_1024(struct array_1024* __this, int index);
char array_set_66_1024(struct array_1024* __this, int index, char element);
int array_size_68_1024(struct array_1024* __this);
boolean array_is_empty_70_1024(struct array_1024* __this);
void array_deinit_72_1024(struct array_1024* __this);
void string_init_52_(struct string* __this, const char * const buffer);
int string_length_54_(struct string* __this);
char string_get_56_(struct string* __this, int index);
void string_deinit_74_(struct string* __this);
int fd_native_open_46_(struct string* filename, int mode);
int fd_native_close_48_(int fd);
int fd_native_read_50_(int fd, struct array_1024* buffer, int size);
void fd_deinit_76_();
int main_class_main_34_();
void fmt_print_23_(struct array_1024* arr);
void fmt_print_25_(char c);
void fmt_print_27_(int i);
void fmt_print_29_(struct string* s);
void fmt_deinit_80_();
void file_init_6_(struct file* __this, struct string* fullname);
void file_fill_buffer_9_(struct file* __this);
void file_open_12_(struct file* __this);
void file_close_15_(struct file* __this);
int file_read_18_(struct file* __this);
char file_getc_21_(struct file* __this);
void file_deinit_82_(struct file* __this);
struct array_1024* stdio_read_file_3_(struct string* fullname);
void stdio_deinit_84_();

struct file
{
int file_descriptor; 
boolean is_open; 
struct string* fullname; 
struct array_1024* buffer; 

};


static const char t87[] = { 'm', 'a', 'i', 'n', '.', 'c', '\0'};
struct array_1024
{
    char* data;
    size_t size;
    size_t alloc;
};


struct string
{
    char * buffer;
    size_t length;
};


void array_init_58_1024(struct array_1024* __this) {
    assert(__this);
    __this->size = 0;
    __this->alloc = 2;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
}


void array_init_60_1024(struct array_1024* __this, int size) {
    assert(__this);
    assert(size > 0);
    assert(size < INT_MAX);
    __this->size = 0;
    __this->alloc = size;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
}


void array_add_62_1024(struct array_1024* __this, char element) {
    if(__this->size >= __this->alloc) {                
        __this->alloc += 1;                            
        __this->alloc *= 2;                            
        char* ndata = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
        for(size_t i = 0; i < __this->size; i += 1) {  
          ndata[i] = __this->data[i];                  
        }                                              
        free(__this->data);                            
        __this->data = ndata;                          
    }                                                  
    __this->data[__this->size] = element;              
    __this->size += 1;                                 
}


char array_get_64_1024(struct array_1024* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}


char array_set_66_1024(struct array_1024* __this, int index, char element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    char old =  __this->data[index];
    __this->data[index] = element;
    return old;
}


int array_size_68_1024(struct array_1024* __this) {
    assert(__this);
    return __this->size;
}


boolean array_is_empty_70_1024(struct array_1024* __this) {
    assert(__this);
    return (__this->size == 0);
}


void array_deinit_72_1024(struct array_1024* __this) {
    assert(__this);
}


void string_init_52_(struct string* __this, const char * const buffer) {
    assert(__this);
    assert(buffer);
    __this->buffer = hstrdup(buffer);
    __this->length = strlen(buffer);
}


int string_length_54_(struct string* __this) {
    assert(__this);
    assert(__this->buffer);
    return __this->length;
}


char string_get_56_(struct string* __this, int index) {
    assert(__this);
    assert(__this->buffer);
    assert(__this->length > 0);
    assert(index >= 0);
    assert(index < __this->length);
    return __this->buffer[index];
}


void string_deinit_74_(struct string* __this) {
    assert(__this);
}




int fd_native_open_46_(struct string* filename, int mode) {
    assert(filename);
    assert(filename->buffer);
    return open(filename->buffer, O_RDONLY);
}

int fd_native_close_48_(int fd) {
    return close(fd);
}

int fd_native_read_50_(int fd, struct array_1024* buffer, int size) {
    assert(fd != -1);
    assert(buffer);
    assert(buffer->data);
    assert(size > 0);
    return read(fd, buffer->data, size);
}

void fmt_print_23_(struct array_1024* arr) {
    assert(arr);
    assert(arr->data);

    printf("%s\n", arr->data);
}


void fmt_print_25_(char c) {
    printf("%c\n", c);
}


void fmt_print_27_(int i) {
    printf("%d\n", i);
}


void fmt_print_29_(struct string* s) {
    assert(s);
    assert(s->buffer);

    printf("%s\n", s->buffer);
}


void fd_deinit_76_()
{

}

void fmt_deinit_80_()
{

}

void file_init_6_(struct file* __this, struct string* fullname)
{
struct file* t107 = __this;
assert(t107);
int t108 = t107->file_descriptor;
int t109 = 1;
int t110 = -t109;
assert(t107);
t107->file_descriptor = t110;

struct file* t111 = __this;
assert(t111);
boolean t112 = t111->is_open;
boolean t113 = false;
assert(t111);
t111->is_open = t113;

struct file* t114 = __this;
assert(t114);
struct string* t115 = t114->fullname;
struct string* t116 = fullname;
assert(t114);
t114->fullname = t116;

struct file* t117 = __this;
assert(t117);
struct array_1024* t118 = t117->buffer;
int t119 = 2;
struct array_1024* t120 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_60_1024(t120, t119);
assert(t117);
t117->buffer = t120;

struct file* t121 = __this;
file_fill_buffer_9_(t121);


}

void file_fill_buffer_9_(struct file* __this)
{
struct file* t122 = __this;
assert(t122);
struct array_1024* t123 = t122->buffer;
char t124 = '\0';
array_add_62_1024(t123, t124);

struct file* t125 = __this;
assert(t125);
struct array_1024* t126 = t125->buffer;
char t127 = '\0';
array_add_62_1024(t126, t127);


}

void file_open_12_(struct file* __this)
{
struct file* t128 = __this;
assert(t128);
boolean t129 = t128->is_open;
boolean t130 = !t129;
assert_true(t130);

struct file* t131 = __this;
assert(t131);
int t132 = t131->file_descriptor;
struct file* t135 = __this;
assert(t135);
struct string* t136 = t135->fullname;
int t137 = 0;
int t138 = fd_native_open_46_(t136, t137);
assert(t131);
t131->file_descriptor = t138;

struct file* t139 = __this;
assert(t139);
int t140 = t139->file_descriptor;
int t141 = 1;
int t142 = -t141;
boolean t143 = t140 != t142;
assert_true(t143);

struct file* t144 = __this;
assert(t144);
boolean t145 = t144->is_open;
boolean t146 = true;
assert(t144);
t144->is_open = t146;


}

void file_close_15_(struct file* __this)
{
struct file* t147 = __this;
assert(t147);
boolean t148 = t147->is_open;
assert_true(t148);

struct file* t150 = __this;
assert(t150);
int t151 = t150->file_descriptor;
int t152 = fd_native_close_48_(t151);

struct file* t153 = __this;
assert(t153);
boolean t154 = t153->is_open;
boolean t155 = false;
assert(t153);
t153->is_open = t155;


}

int file_read_18_(struct file* __this)
{
struct file* t156 = __this;
assert(t156);
boolean t157 = t156->is_open;
assert_true(t157);

struct file* t159 = __this;
assert(t159);
int t160 = t159->file_descriptor;
struct file* t161 = __this;
assert(t161);
struct array_1024* t162 = t161->buffer;
int t163 = 1;
int t164 = fd_native_read_50_(t160, t162, t163);
int c = t164;

int t165 = c;


return t165;

}

char file_getc_21_(struct file* __this)
{
struct file* t166 = __this;
assert(t166);
boolean t167 = t166->is_open;
assert_true(t167);

struct file* t168 = __this;
assert(t168);
struct array_1024* t169 = t168->buffer;
int t170 = 0;
char t171 = array_get_64_1024(t169, t170);


return t171;

}

void file_deinit_82_(struct file* __this)
{
struct file* t172 = __this;
assert(t172);
struct array_1024* t173 = t172->buffer;
array_deinit_72_1024(t173);

struct file* t174 = __this;
assert(t174);
struct string* t175 = t174->fullname;
string_deinit_74_(t175);


}

struct array_1024* stdio_read_file_3_(struct string* fullname)
{
struct string* t176 = fullname;
struct file* t177 = (struct file*) hcalloc( 1u, sizeof(struct file) );
file_init_6_(t177, t176);
struct file* fp = t177;

struct file* t178 = fp;
file_open_12_(t178);

struct array_1024* t179 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_58_1024(t179);
struct array_1024* rv = t179;

struct file* t180 = fp;
int t181 = file_read_18_(t180);
int sz = t181;

for(;;)
{
int t182 = sz;
int t183 = 0;
boolean t184 = t182 > t183;
boolean t185 = !t184;

if(t185)
{

break;

}

struct file* t186 = fp;
char t187 = file_getc_21_(t186);
char c = t187;

struct array_1024* t188 = rv;
char t189 = c;
array_add_62_1024(t188, t189);

int t190 = sz;
struct file* t191 = fp;
int t192 = file_read_18_(t191);
sz = t192;


}

struct array_1024* t193 = rv;
char t194 = '\0';
array_add_62_1024(t193, t194);

struct file* t195 = fp;
file_close_15_(t195);

struct array_1024* t196 = rv;

array_deinit_72_1024(rv);
file_deinit_82_(fp);

return t196;

}

void stdio_deinit_84_()
{

}


int main_class_main_34_()
{
struct string* t88 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_52_(t88, t87);
struct array_1024* t89 = stdio_read_file_3_(t88);
struct array_1024* args = t89;


{
int t90 = 0;
int i = t90;

for(;;)
{
int t91 = i;
struct array_1024* t92 = args;
int t93 = array_size_68_1024(t92);
boolean t94 = t91 < t93;
boolean t95 = !t94;

if(t95)
{

break;

}

struct array_1024* t100 = args;
int t101 = i;
char t102 = array_get_64_1024(t100, t101);
char c = t102;

char t105 = c;
fmt_print_25_(t105);

int t96 = i;
int t97 = i;
int t98 = 1;
int t99 = t97 + t98;
i = t99;


}


}

int t106 = 0;

array_deinit_72_1024(args);

return t106;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_34_();

    printf("%d\n", result);
    return result;

}
