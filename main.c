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
struct cbuf;
struct file;

void array_init_99_1024(struct array_1024* __this);
void array_init_101_1024(struct array_1024* __this, int size);
void array_add_103_1024(struct array_1024* __this, char element);
char array_get_105_1024(struct array_1024* __this, int index);
char array_set_107_1024(struct array_1024* __this, int index, char element);
int array_size_109_1024(struct array_1024* __this);
boolean array_is_empty_111_1024(struct array_1024* __this);
void array_test_113_1024();
void array_deinit_115_1024(struct array_1024* __this);
void string_init_91(struct string* __this, const char * const buffer);
int string_length_93(struct string* __this);
char string_get_95(struct string* __this, int index);
void string_test_97();
void string_deinit_117(struct string* __this);
int fd_native_open_85(struct string* filename, int mode);
int fd_native_close_87(int fd);
int fd_native_read_89(int fd, struct array_1024* buffer, int size);
void fd_deinit_119();
int main_class_main_73();
void fmt_print_64(struct array_1024* arr);
void fmt_print_66(char c);
void fmt_print_68(int i);
void fmt_print_70(struct string* s);
void fmt_deinit_123();
void cbuf_init_24(struct cbuf* __this, struct string* input);
void cbuf_fill_buffer_31(struct cbuf* __this, struct string* input);
boolean cbuf_isEof_34(struct cbuf* __this);
char cbuf_peekc_37(struct cbuf* __this);
struct array_1024* cbuf_peekc3_40(struct cbuf* __this);
char cbuf_nextc_58(struct cbuf* __this);
void cbuf_test_60();
void cbuf_test_62();
void cbuf_deinit_125(struct cbuf* __this);
void file_init_6(struct file* __this, struct string* fullname);
void file_fill_buffer_9(struct file* __this);
void file_open_12(struct file* __this);
void file_close_15(struct file* __this);
int file_read_18(struct file* __this);
char file_getc_21(struct file* __this);
void file_deinit_127(struct file* __this);
struct array_1024* stdio_read_file_3(struct string* fullname);
void stdio_deinit_129();

struct cbuf
{
struct array_1024* buffer; 
int size; 
int offset; 
int line; 
int column; 
char prevc; 
int eofs; 

};

struct file
{
int file_descriptor; 
boolean is_open; 
struct string* fullname; 
struct array_1024* buffer; 

};


static const char t146[] = { 'a', '.', 'b', '.', 'c', '\0'};
static const char t444[] = { '/', '\\', '\n', '/', 'a', '\0'};
static const char t433[] = { 'a', '\\', '\n', 'b', '\0'};
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


void array_init_99_1024(struct array_1024* __this) {
    assert(__this);
    __this->size = 0;
    __this->alloc = 2;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
}


void array_init_101_1024(struct array_1024* __this, int size) {
    assert(__this);
    assert(size > 0);
    assert(size < INT_MAX);
    __this->size = 0;
    __this->alloc = size;
    __this->data = (char*) hcalloc( 1u, (sizeof(char) * __this->alloc) );
}


void array_add_103_1024(struct array_1024* __this, char element) {
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


char array_get_105_1024(struct array_1024* __this, int index) {
    assert(__this);
    assert(__this->data);
    assert(__this->size > 0);
    assert(index >= 0);
    assert(index < __this->size);
    return __this->data[index];
}


char array_set_107_1024(struct array_1024* __this, int index, char element) {
    assert(__this);
    assert(__this->data);
    assert(index >= 0);
    assert(index < __this->size);
    char old =  __this->data[index];
    __this->data[index] = element;
    return old;
}


int array_size_109_1024(struct array_1024* __this) {
    assert(__this);
    return __this->size;
}


boolean array_is_empty_111_1024(struct array_1024* __this) {
    assert(__this);
    return (__this->size == 0);
}


void array_test_113_1024() {

{
struct array_1024* t130 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_99_1024(t130);
struct array_1024* arr = t130;

struct array_1024* t131 = arr;
char t132 = '1';
array_add_103_1024(t131, t132);

struct array_1024* t133 = arr;
char t134 = '2';
array_add_103_1024(t133, t134);

struct array_1024* t135 = arr;
char t136 = '3';
array_add_103_1024(t135, t136);

struct array_1024* t137 = arr;
int t138 = 0;
char t139 = array_get_105_1024(t137, t138);
char t140 = '1';
boolean t141 = t139 == t140;
assert_true(t141);

struct array_1024* t142 = arr;
int t143 = array_size_109_1024(t142);
int t144 = 3;
boolean t145 = t143 == t144;
assert_true(t145);

array_deinit_115_1024(arr);

}

}


void array_deinit_115_1024(struct array_1024* __this) {
    assert(__this);
}


void string_init_91(struct string* __this, const char * const buffer) {
    assert(__this);
    assert(buffer);
    __this->buffer = hstrdup(buffer);
    __this->length = strlen(buffer);
}


int string_length_93(struct string* __this) {
    assert(__this);
    assert(__this->buffer);
    return __this->length;
}


char string_get_95(struct string* __this, int index) {
    assert(__this);
    assert(__this->buffer);
    assert(__this->length > 0);
    assert(index >= 0);
    assert(index < __this->length);
    return __this->buffer[index];
}


void string_test_97() {

{
struct string* t147 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_91(t147, t146);
struct string* s = t147;

struct string* t148 = s;
int t149 = 0;
char t150 = string_get_95(t148, t149);
char c = t150;

char t151 = c;
char t152 = 'a';
boolean t153 = t151 == t152;
assert_true(t153);

string_deinit_117(s);

}

}


void string_deinit_117(struct string* __this) {
    assert(__this);
}




int fd_native_open_85(struct string* filename, int mode) {
    assert(filename);
    assert(filename->buffer);
    return open(filename->buffer, O_RDONLY);
}

int fd_native_close_87(int fd) {
    return close(fd);
}

int fd_native_read_89(int fd, struct array_1024* buffer, int size) {
    assert(fd != -1);
    assert(buffer);
    assert(buffer->data);
    assert(size > 0);
    return read(fd, buffer->data, size);
}

void fmt_print_64(struct array_1024* arr) {
    assert(arr);
    assert(arr->data);

    printf("%s\n", arr->data);
}


void fmt_print_66(char c) {
    printf("%c\n", c);
}


void fmt_print_68(int i) {
    printf("%d\n", i);
}


void fmt_print_70(struct string* s) {
    assert(s);
    assert(s->buffer);

    printf("%s\n", s->buffer);
}


void fd_deinit_119()
{

}

void fmt_deinit_123()
{

}

void cbuf_init_24(struct cbuf* __this, struct string* input)
{
struct cbuf* t155 = __this;
assert(t155);
struct array_1024* t156 = t155->buffer;
struct array_1024* t157 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_99_1024(t157);
assert(t155);
t155->buffer = t157;

struct cbuf* t158 = __this;
assert(t158);
int t159 = t158->size;
struct string* t160 = input;
int t161 = string_length_93(t160);
int t162 = 8;
int t163 = t161 + t162;
assert(t158);
t158->size = t163;

struct cbuf* t164 = __this;
assert(t164);
int t165 = t164->offset;
int t166 = 0;
assert(t164);
t164->offset = t166;

struct cbuf* t167 = __this;
assert(t167);
int t168 = t167->line;
int t169 = 1;
assert(t167);
t167->line = t169;

struct cbuf* t170 = __this;
assert(t170);
int t171 = t170->column;
int t172 = 0;
assert(t170);
t170->column = t172;

struct cbuf* t173 = __this;
assert(t173);
char t174 = t173->prevc;
char t175 = '\0';
assert(t173);
t173->prevc = t175;

struct cbuf* t176 = __this;
assert(t176);
int t177 = t176->eofs;
int t178 = 1;
int t179 = -t178;
assert(t176);
t176->eofs = t179;

struct cbuf* t180 = __this;
struct string* t181 = input;
cbuf_fill_buffer_31(t180, t181);


}

void cbuf_fill_buffer_31(struct cbuf* __this, struct string* input)
{

{
int t182 = 0;
int i = t182;

for(;;)
{
int t183 = i;
struct string* t184 = input;
int t185 = string_length_93(t184);
boolean t186 = t183 < t185;
boolean t187 = !t186;

if(t187)
{

break;

}

struct string* t192 = input;
int t193 = i;
char t194 = string_get_95(t192, t193);
char c = t194;

struct cbuf* t195 = __this;
assert(t195);
struct array_1024* t196 = t195->buffer;
char t197 = c;
array_add_103_1024(t196, t197);

int t188 = i;
int t189 = i;
int t190 = 1;
int t191 = t189 + t190;
i = t191;


}


}


{
int t198 = 0;
int i = t198;

for(;;)
{
int t199 = i;
int t200 = 8;
boolean t201 = t199 < t200;
boolean t202 = !t201;

if(t202)
{

break;

}

struct cbuf* t207 = __this;
assert(t207);
struct array_1024* t208 = t207->buffer;
char t209 = '\0';
array_add_103_1024(t208, t209);

int t203 = i;
int t204 = i;
int t205 = 1;
int t206 = t204 + t205;
i = t206;


}


}


}

boolean cbuf_isEof_34(struct cbuf* __this)
{
struct cbuf* t210 = __this;
assert(t210);
int t211 = t210->eofs;
int t212 = 8;
boolean t213 = t211 >= t212;


return t213;

}

char cbuf_peekc_37(struct cbuf* __this)
{
struct cbuf* t214 = __this;
assert(t214);
int t215 = t214->offset;
int save_offset = t215;

struct cbuf* t216 = __this;
assert(t216);
int t217 = t216->line;
int save_line = t217;

struct cbuf* t218 = __this;
assert(t218);
int t219 = t218->column;
int save_column = t219;

struct cbuf* t220 = __this;
assert(t220);
char t221 = t220->prevc;
char save_prevc = t221;

struct cbuf* t222 = __this;
assert(t222);
int t223 = t222->eofs;
int save_eofs = t223;

struct cbuf* t224 = __this;
char t225 = cbuf_nextc_58(t224);
char c = t225;

struct cbuf* t226 = __this;
assert(t226);
int t227 = t226->offset;
int t228 = save_offset;
assert(t226);
t226->offset = t228;

struct cbuf* t229 = __this;
assert(t229);
int t230 = t229->line;
int t231 = save_line;
assert(t229);
t229->line = t231;

struct cbuf* t232 = __this;
assert(t232);
int t233 = t232->column;
int t234 = save_column;
assert(t232);
t232->column = t234;

struct cbuf* t235 = __this;
assert(t235);
char t236 = t235->prevc;
char t237 = save_prevc;
assert(t235);
t235->prevc = t237;

struct cbuf* t238 = __this;
assert(t238);
int t239 = t238->eofs;
int t240 = save_eofs;
assert(t238);
t238->eofs = t240;

char t241 = c;


return t241;

}

struct array_1024* cbuf_peekc3_40(struct cbuf* __this)
{
struct array_1024* t242 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_99_1024(t242);
struct array_1024* res = t242;

struct cbuf* t243 = __this;
assert(t243);
int t244 = t243->offset;
int save_offset = t244;

struct cbuf* t245 = __this;
assert(t245);
int t246 = t245->line;
int save_line = t246;

struct cbuf* t247 = __this;
assert(t247);
int t248 = t247->column;
int save_column = t248;

struct cbuf* t249 = __this;
assert(t249);
char t250 = t249->prevc;
char save_prevc = t250;

struct cbuf* t251 = __this;
assert(t251);
int t252 = t251->eofs;
int save_eofs = t252;

struct array_1024* t253 = res;
struct cbuf* t254 = __this;
char t255 = cbuf_nextc_58(t254);
array_add_103_1024(t253, t255);

struct array_1024* t256 = res;
struct cbuf* t257 = __this;
char t258 = cbuf_nextc_58(t257);
array_add_103_1024(t256, t258);

struct array_1024* t259 = res;
struct cbuf* t260 = __this;
char t261 = cbuf_nextc_58(t260);
array_add_103_1024(t259, t261);

struct cbuf* t262 = __this;
assert(t262);
int t263 = t262->offset;
int t264 = save_offset;
assert(t262);
t262->offset = t264;

struct cbuf* t265 = __this;
assert(t265);
int t266 = t265->line;
int t267 = save_line;
assert(t265);
t265->line = t267;

struct cbuf* t268 = __this;
assert(t268);
int t269 = t268->column;
int t270 = save_column;
assert(t268);
t268->column = t270;

struct cbuf* t271 = __this;
assert(t271);
char t272 = t271->prevc;
char t273 = save_prevc;
assert(t271);
t271->prevc = t273;

struct cbuf* t274 = __this;
assert(t274);
int t275 = t274->eofs;
int t276 = save_eofs;
assert(t274);
t274->eofs = t276;

struct array_1024* t277 = res;

array_deinit_115_1024(res);

return t277;

}

char cbuf_nextc_58(struct cbuf* __this)
{
for(;;)
{
struct cbuf* t278 = __this;
boolean t279 = cbuf_isEof_34(t278);
boolean t280 = !t279;
boolean t281 = !t280;

if(t281)
{

break;

}

struct cbuf* t282 = __this;
assert(t282);
int t283 = t282->eofs;
int t284 = 8;
boolean t285 = t283 > t284;

if(t285)
{

}

struct cbuf* t286 = __this;
assert(t286);
char t287 = t286->prevc;
char t288 = '\n';
boolean t289 = t287 == t288;

if(t289)
{
struct cbuf* t290 = __this;
assert(t290);
int t291 = t290->line;
struct cbuf* t292 = __this;
assert(t292);
int t293 = t292->line;
int t294 = 1;
int t295 = t293 + t294;
assert(t290);
t290->line = t295;

struct cbuf* t296 = __this;
assert(t296);
int t297 = t296->column;
int t298 = 0;
assert(t296);
t296->column = t298;


}

struct cbuf* t299 = __this;
assert(t299);
struct array_1024* t300 = t299->buffer;
struct cbuf* t301 = __this;
assert(t301);
int t302 = t301->offset;
char t303 = array_get_105_1024(t300, t302);
char t304 = '\\';
boolean t305 = t303 == t304;

if(t305)
{
struct cbuf* t306 = __this;
assert(t306);
struct array_1024* t307 = t306->buffer;
struct cbuf* t308 = __this;
assert(t308);
int t309 = t308->offset;
int t310 = 1;
int t311 = t309 + t310;
char t312 = array_get_105_1024(t307, t311);
char t313 = '\r';
boolean t314 = t312 == t313;

if(t314)
{
struct cbuf* t315 = __this;
assert(t315);
struct array_1024* t316 = t315->buffer;
struct cbuf* t317 = __this;
assert(t317);
int t318 = t317->offset;
int t319 = 2;
int t320 = t318 + t319;
char t321 = array_get_105_1024(t316, t320);
char t322 = '\n';
boolean t323 = t321 == t322;

if(t323)
{
struct cbuf* t324 = __this;
assert(t324);
int t325 = t324->offset;
struct cbuf* t326 = __this;
assert(t326);
int t327 = t326->offset;
int t328 = 3;
int t329 = t327 + t328;
assert(t324);
t324->offset = t329;


}
else 
{
struct cbuf* t330 = __this;
assert(t330);
int t331 = t330->offset;
struct cbuf* t332 = __this;
assert(t332);
int t333 = t332->offset;
int t334 = 2;
int t335 = t333 + t334;
assert(t330);
t330->offset = t335;


}

struct cbuf* t336 = __this;
assert(t336);
char t337 = t336->prevc;
char t338 = '\n';
assert(t336);
t336->prevc = t338;


continue;

}

struct cbuf* t339 = __this;
assert(t339);
struct array_1024* t340 = t339->buffer;
struct cbuf* t341 = __this;
assert(t341);
int t342 = t341->offset;
int t343 = 1;
int t344 = t342 + t343;
char t345 = array_get_105_1024(t340, t344);
char t346 = '\n';
boolean t347 = t345 == t346;

if(t347)
{
struct cbuf* t348 = __this;
assert(t348);
int t349 = t348->offset;
struct cbuf* t350 = __this;
assert(t350);
int t351 = t350->offset;
int t352 = 2;
int t353 = t351 + t352;
assert(t348);
t348->offset = t353;

struct cbuf* t354 = __this;
assert(t354);
char t355 = t354->prevc;
char t356 = '\n';
assert(t354);
t354->prevc = t356;


continue;

}


}

struct cbuf* t357 = __this;
assert(t357);
struct array_1024* t358 = t357->buffer;
struct cbuf* t359 = __this;
assert(t359);
int t360 = t359->offset;
char t361 = array_get_105_1024(t358, t360);
char t362 = '\r';
boolean t363 = t361 == t362;

if(t363)
{
struct cbuf* t364 = __this;
assert(t364);
struct array_1024* t365 = t364->buffer;
struct cbuf* t366 = __this;
assert(t366);
int t367 = t366->offset;
int t368 = 1;
int t369 = t367 + t368;
char t370 = array_get_105_1024(t365, t369);
char t371 = '\n';
boolean t372 = t370 == t371;

if(t372)
{
struct cbuf* t373 = __this;
assert(t373);
int t374 = t373->offset;
struct cbuf* t375 = __this;
assert(t375);
int t376 = t375->offset;
int t377 = 2;
int t378 = t376 + t377;
assert(t373);
t373->offset = t378;


}
else 
{
struct cbuf* t379 = __this;
assert(t379);
int t380 = t379->offset;
struct cbuf* t381 = __this;
assert(t381);
int t382 = t381->offset;
int t383 = 1;
int t384 = t382 + t383;
assert(t379);
t379->offset = t384;


}

struct cbuf* t385 = __this;
assert(t385);
char t386 = t385->prevc;
char t387 = '\n';
assert(t385);
t385->prevc = t387;

char t388 = '\n';


return t388;

}

struct cbuf* t389 = __this;
assert(t389);
int t390 = t389->offset;
struct cbuf* t391 = __this;
assert(t391);
int t392 = t391->size;
boolean t393 = t390 == t392;

if(t393)
{
struct cbuf* t394 = __this;
assert(t394);
int t395 = t394->eofs;
struct cbuf* t396 = __this;
assert(t396);
int t397 = t396->eofs;
int t398 = 1;
int t399 = t397 + t398;
assert(t394);
t394->eofs = t399;

char t400 = '\0';


return t400;

}

struct cbuf* t401 = __this;
assert(t401);
struct array_1024* t402 = t401->buffer;
struct cbuf* t403 = __this;
assert(t403);
int t404 = t403->offset;
char t405 = array_get_105_1024(t402, t404);
char next = t405;

struct cbuf* t406 = __this;
assert(t406);
int t407 = t406->offset;
struct cbuf* t408 = __this;
assert(t408);
int t409 = t408->offset;
int t410 = 1;
int t411 = t409 + t410;
assert(t406);
t406->offset = t411;

struct cbuf* t412 = __this;
assert(t412);
int t413 = t412->column;
struct cbuf* t414 = __this;
assert(t414);
int t415 = t414->column;
int t416 = 1;
int t417 = t415 + t416;
assert(t412);
t412->column = t417;

struct cbuf* t418 = __this;
assert(t418);
char t419 = t418->prevc;
char t420 = next;
assert(t418);
t418->prevc = t420;

char t421 = next;
char t422 = '\0';
boolean t423 = t421 == t422;

if(t423)
{
struct cbuf* t424 = __this;
assert(t424);
int t425 = t424->eofs;
struct cbuf* t426 = __this;
assert(t426);
int t427 = t426->eofs;
int t428 = 1;
int t429 = t427 + t428;
assert(t424);
t424->eofs = t429;

char t430 = '\0';


return t430;

}

char t431 = next;


return t431;

}

char t432 = '\0';


return t432;

}

void cbuf_test_60()
{
struct string* t434 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_91(t434, t433);
struct cbuf* t435 = (struct cbuf*) hcalloc( 1u, sizeof(struct cbuf) );
cbuf_init_24(t435, t434);
struct cbuf* buf = t435;

struct cbuf* t436 = buf;
char t437 = cbuf_nextc_58(t436);
char t438 = 'a';
boolean t439 = t437 == t438;
assert_true(t439);

struct cbuf* t440 = buf;
char t441 = cbuf_nextc_58(t440);
char t442 = 'b';
boolean t443 = t441 == t442;
assert_true(t443);

cbuf_deinit_125(buf);

}

void cbuf_test_62()
{
struct string* t445 = (struct string*) hcalloc( 1u, sizeof(struct string) );
string_init_91(t445, t444);
struct cbuf* t446 = (struct cbuf*) hcalloc( 1u, sizeof(struct cbuf) );
cbuf_init_24(t446, t445);
struct cbuf* buf = t446;

struct cbuf* t447 = buf;
char t448 = cbuf_nextc_58(t447);
char t449 = '/';
boolean t450 = t448 == t449;
assert_true(t450);

struct cbuf* t451 = buf;
char t452 = cbuf_nextc_58(t451);
char t453 = '/';
boolean t454 = t452 == t453;
assert_true(t454);

struct cbuf* t455 = buf;
char t456 = cbuf_nextc_58(t455);
char t457 = 'a';
boolean t458 = t456 == t457;
assert_true(t458);

cbuf_deinit_125(buf);

}

void cbuf_deinit_125(struct cbuf* __this)
{
struct cbuf* t459 = __this;
assert(t459);
struct array_1024* t460 = t459->buffer;
array_deinit_115_1024(t460);


}

void file_init_6(struct file* __this, struct string* fullname)
{
struct file* t461 = __this;
assert(t461);
int t462 = t461->file_descriptor;
int t463 = 1;
int t464 = -t463;
assert(t461);
t461->file_descriptor = t464;

struct file* t465 = __this;
assert(t465);
boolean t466 = t465->is_open;
boolean t467 = false;
assert(t465);
t465->is_open = t467;

struct file* t468 = __this;
assert(t468);
struct string* t469 = t468->fullname;
struct string* t470 = fullname;
assert(t468);
t468->fullname = t470;

struct file* t471 = __this;
assert(t471);
struct array_1024* t472 = t471->buffer;
int t473 = 2;
struct array_1024* t474 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_101_1024(t474, t473);
assert(t471);
t471->buffer = t474;

struct file* t475 = __this;
file_fill_buffer_9(t475);


}

void file_fill_buffer_9(struct file* __this)
{
struct file* t476 = __this;
assert(t476);
struct array_1024* t477 = t476->buffer;
char t478 = '\0';
array_add_103_1024(t477, t478);

struct file* t479 = __this;
assert(t479);
struct array_1024* t480 = t479->buffer;
char t481 = '\0';
array_add_103_1024(t480, t481);


}

void file_open_12(struct file* __this)
{
struct file* t482 = __this;
assert(t482);
boolean t483 = t482->is_open;
boolean t484 = !t483;
assert_true(t484);

struct file* t485 = __this;
assert(t485);
int t486 = t485->file_descriptor;
struct file* t489 = __this;
assert(t489);
struct string* t490 = t489->fullname;
int t491 = 0;
int t492 = fd_native_open_85(t490, t491);
assert(t485);
t485->file_descriptor = t492;

struct file* t493 = __this;
assert(t493);
int t494 = t493->file_descriptor;
int t495 = 1;
int t496 = -t495;
boolean t497 = t494 != t496;
assert_true(t497);

struct file* t498 = __this;
assert(t498);
boolean t499 = t498->is_open;
boolean t500 = true;
assert(t498);
t498->is_open = t500;


}

void file_close_15(struct file* __this)
{
struct file* t501 = __this;
assert(t501);
boolean t502 = t501->is_open;
assert_true(t502);

struct file* t504 = __this;
assert(t504);
int t505 = t504->file_descriptor;
int t506 = fd_native_close_87(t505);

struct file* t507 = __this;
assert(t507);
boolean t508 = t507->is_open;
boolean t509 = false;
assert(t507);
t507->is_open = t509;


}

int file_read_18(struct file* __this)
{
struct file* t510 = __this;
assert(t510);
boolean t511 = t510->is_open;
assert_true(t511);

struct file* t513 = __this;
assert(t513);
int t514 = t513->file_descriptor;
struct file* t515 = __this;
assert(t515);
struct array_1024* t516 = t515->buffer;
int t517 = 1;
int t518 = fd_native_read_89(t514, t516, t517);
int c = t518;

int t519 = c;


return t519;

}

char file_getc_21(struct file* __this)
{
struct file* t520 = __this;
assert(t520);
boolean t521 = t520->is_open;
assert_true(t521);

struct file* t522 = __this;
assert(t522);
struct array_1024* t523 = t522->buffer;
int t524 = 0;
char t525 = array_get_105_1024(t523, t524);


return t525;

}

void file_deinit_127(struct file* __this)
{
struct file* t526 = __this;
assert(t526);
struct array_1024* t527 = t526->buffer;
array_deinit_115_1024(t527);

struct file* t528 = __this;
assert(t528);
struct string* t529 = t528->fullname;
string_deinit_117(t529);


}

struct array_1024* stdio_read_file_3(struct string* fullname)
{
struct string* t530 = fullname;
struct file* t531 = (struct file*) hcalloc( 1u, sizeof(struct file) );
file_init_6(t531, t530);
struct file* fp = t531;

struct file* t532 = fp;
file_open_12(t532);

struct array_1024* t533 = (struct array_1024*) hcalloc( 1u, sizeof(struct array_1024) );
array_init_99_1024(t533);
struct array_1024* rv = t533;

struct file* t534 = fp;
int t535 = file_read_18(t534);
int sz = t535;

for(;;)
{
int t536 = sz;
int t537 = 0;
boolean t538 = t536 > t537;
boolean t539 = !t538;

if(t539)
{

break;

}

struct file* t540 = fp;
char t541 = file_getc_21(t540);
char c = t541;

struct array_1024* t542 = rv;
char t543 = c;
array_add_103_1024(t542, t543);

int t544 = sz;
struct file* t545 = fp;
int t546 = file_read_18(t545);
sz = t546;


}

struct array_1024* t547 = rv;
char t548 = '\0';
array_add_103_1024(t547, t548);

struct file* t549 = fp;
file_close_15(t549);

struct array_1024* t550 = rv;

array_deinit_115_1024(rv);
file_deinit_127(fp);

return t550;

}

void stdio_deinit_129()
{

}


void __run_all_tests__() 
{
printf("test: %s\n", "array :: get");
array_test_113_1024();
printf("test: %s\n", "string :: get first char");
string_test_97();
printf("test: %s\n", "cbuf :: backslash-newline handling");
cbuf_test_60();
printf("test: %s\n", "cbuf :: backslash-newline with comments handling");
cbuf_test_62();

}
int main_class_main_73()
{
int t154 = 0;


return t154;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_73();

    printf("%d\n", result);
    return result;

}
