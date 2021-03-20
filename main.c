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

struct box_1024;
struct vec_1024;
struct string;
struct file;

void box_init_191_1024(struct box_1024* __this, int element_size, int num_of_elements);
int box_element_size_194_1024(struct box_1024* __this);
int box_num_of_elements_197_1024(struct box_1024* __this);
char* box_raw_data_200_1024(struct box_1024* __this);
char* box_malloc_203_1024(struct box_1024* __this);
char* box_calloc_206_1024(struct box_1024* __this);
void box_free_209_1024(struct box_1024* __this);
char box_access_at_212_1024(struct box_1024* __this, int offset);
char box_set_at_215_1024(struct box_1024* __this, int offset, char value);
char* box_memcpy_218_1024(struct box_1024* __this, char* src, int count);
char* box_native_malloc_220_1024(struct box_1024* __this, char* ptr, int size);
char* box_native_calloc_222_1024(struct box_1024* __this, char* ptr, int count, int size);
void box_native_free_224_1024(struct box_1024* __this, char* ptr);
char box_native_ptr_access_at_226_1024(struct box_1024* __this, char* ptr, int offset);
char box_native_ptr_set_at_228_1024(struct box_1024* __this, char* ptr, int offset, char value);
char* box_native_memcpy_230_1024(struct box_1024* __this, char* dst, char* src, int count);
void vec_init_146_1024(struct vec_1024* __this);
void vec_add_152_1024(struct vec_1024* __this, char e);
int vec_size_155_1024(struct vec_1024* __this);
char vec_get_158_1024(struct vec_1024* __this, int index);
char vec_set_161_1024(struct vec_1024* __this, int index, char e);
struct box_1024* vec_get_data_164_1024(struct vec_1024* __this);
void assert_is_true_245_(boolean condition);
void assert_panic_248_(struct string* because);
void assert_native_panic_250_(char* because);
void assert_native_assert_true_252_(boolean condition);
void string_init_233_(struct string* __this, struct box_1024* buffer);
int string_length_236_(struct string* __this);
char string_get_239_(struct string* __this, int index);
struct box_1024* string_get_buffer_242_(struct string* __this);
void file_init_167_(struct file* __this, struct string* fullname);
void file_fill_buffer_170_(struct file* __this);
void file_open_173_(struct file* __this);
void file_close_176_(struct file* __this);
int file_read_179_(struct file* __this);
char file_getc_182_(struct file* __this);
int file_native_open_184_(struct file* __this, char* filename, int mode);
int file_native_close_186_(struct file* __this, int fd);
int file_native_read_188_(struct file* __this, int fd, char* buffer, int size);
struct vec_1024* stdio_read_file_133_(struct string* fullname);
void stdio_printf_136_(struct string* s, char c);
void stdio_printf_139_(struct string* s, int c);
void stdio_native_printf_141_(char* fmt, char c);
void stdio_native_printf_143_(char* fmt, int c);
int main_class_main_129_();

struct box_1024
{
char* raw_data; 
int element_size; 
int num_of_elements; 

};

struct vec_1024
{
struct box_1024* data; 
int size; 
int alloc; 

};

struct string
{
struct box_1024* buffer; 

};

struct file
{
int fd; 
boolean is_open; 
struct string* fullname; 
struct box_1024* buffer; 

};

struct box_1024 box_1024_zero;
struct vec_1024 vec_1024_zero;
struct string string_zero;
struct file file_zero;

#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 

char t592[] = { '%', 'd', '\0'};
char t585[] = { '%', 'c', '\0'};
char t565[] = { 'm', 'a', 'i', 'n', '.', 'c', '\0'};


char* box_native_malloc_220_1024(struct box_1024* __this, char* ptr, int size) {
    return (char*) hmalloc(size);
}
char* box_native_calloc_222_1024(struct box_1024* __this, char* ptr, int count, int size) {
    return (char*) hcalloc(count, size);
}
void box_native_free_224_1024(struct box_1024* __this, char* ptr) {
    free(ptr);
}
char box_native_ptr_access_at_226_1024(struct box_1024* __this, char* ptr, int offset) {
    return ptr[offset];
}
char box_native_ptr_set_at_228_1024(struct box_1024* __this, char* ptr, int offset, char value) {
    char old = ptr[offset];
    ptr[offset] = value;
    return old;
}
char* box_native_memcpy_230_1024(struct box_1024* __this, char* dst, char* src, int count) {
    return (char*) memcpy(dst, src, count);
}
void assert_native_panic_250_(char* because) {
    assert(because);
}
void assert_native_assert_true_252_(boolean condition) {
    assert_true(condition);
}
int file_native_open_184_(struct file* __this, char* filename, int mode) {
    return open(filename, O_RDONLY);
}
int file_native_close_186_(struct file* __this, int fd) {
    return close(fd);
}
int file_native_read_188_(struct file* __this, int fd, char* buffer, int size) {
    assert(fd != -1);
    assert(buffer);
    assert(size > 0);
    return read(fd, buffer, size);
}
void stdio_native_printf_141_(char* fmt, char c) {
    printf(fmt, c);
}
void stdio_native_printf_143_(char* fmt, int c) {
    printf(fmt, c);
}
void box_init_191_1024(struct box_1024* __this, int element_size, int num_of_elements)
{
int t255 = element_size;
int t256 = 0;
boolean t257 = t255 > t256;
assert_is_true_245_(t257);

int t259 = num_of_elements;
int t260 = 0;
boolean t261 = t259 > t260;
assert_is_true_245_(t261);

struct box_1024* t262 = __this;
assert(t262);
int t263 = t262->element_size;
int t264 = element_size;
assert(t262);
t262->element_size = t264;

struct box_1024* t265 = __this;
assert(t265);
int t266 = t265->num_of_elements;
int t267 = num_of_elements;
assert(t265);
t265->num_of_elements = t267;

struct box_1024* t268 = __this;
assert(t268);
char* t269 = t268->raw_data;
struct box_1024* t270 = __this;
char* t271 = box_malloc_203_1024(t270);
assert(t268);
t268->raw_data = t271;


}

int box_element_size_194_1024(struct box_1024* __this)
{
struct box_1024* t272 = __this;
assert(t272);
int t273 = t272->element_size;


return t273;

}

int box_num_of_elements_197_1024(struct box_1024* __this)
{
struct box_1024* t274 = __this;
assert(t274);
int t275 = t274->num_of_elements;


return t275;

}

char* box_raw_data_200_1024(struct box_1024* __this)
{
struct box_1024* t276 = __this;
assert(t276);
char* t277 = t276->raw_data;


return t277;

}

char* box_malloc_203_1024(struct box_1024* __this)
{
struct box_1024* t278 = __this;
struct box_1024* t279 = __this;
assert(t279);
char* t280 = t279->raw_data;
struct box_1024* t281 = __this;
assert(t281);
int t282 = t281->element_size;
struct box_1024* t283 = __this;
assert(t283);
int t284 = t283->num_of_elements;
int t285 = t282 * t284;
char* t286 = box_native_malloc_220_1024(t278, t280, t285);


return t286;

}

char* box_calloc_206_1024(struct box_1024* __this)
{
struct box_1024* t287 = __this;
struct box_1024* t288 = __this;
assert(t288);
char* t289 = t288->raw_data;
struct box_1024* t290 = __this;
assert(t290);
int t291 = t290->num_of_elements;
struct box_1024* t292 = __this;
assert(t292);
int t293 = t292->element_size;
char* t294 = box_native_calloc_222_1024(t287, t289, t291, t293);


return t294;

}

void box_free_209_1024(struct box_1024* __this)
{
struct box_1024* t295 = __this;
struct box_1024* t296 = __this;
assert(t296);
char* t297 = t296->raw_data;
box_native_free_224_1024(t295, t297);


}

char box_access_at_212_1024(struct box_1024* __this, int offset)
{
int t299 = offset;
struct box_1024* t300 = __this;
assert(t300);
int t301 = t300->num_of_elements;
boolean t302 = t299 < t301;
assert_is_true_245_(t302);

struct box_1024* t303 = __this;
struct box_1024* t304 = __this;
assert(t304);
char* t305 = t304->raw_data;
int t306 = offset;
char t307 = box_native_ptr_access_at_226_1024(t303, t305, t306);


return t307;

}

char box_set_at_215_1024(struct box_1024* __this, int offset, char value)
{
int t309 = offset;
struct box_1024* t310 = __this;
assert(t310);
int t311 = t310->num_of_elements;
boolean t312 = t309 < t311;
assert_is_true_245_(t312);

struct box_1024* t313 = __this;
struct box_1024* t314 = __this;
assert(t314);
char* t315 = t314->raw_data;
int t316 = offset;
char t317 = value;
char t318 = box_native_ptr_set_at_228_1024(t313, t315, t316, t317);


return t318;

}

char* box_memcpy_218_1024(struct box_1024* __this, char* src, int count)
{
int t320 = count;
int t321 = 0;
boolean t322 = t320 > t321;
assert_is_true_245_(t322);

int t324 = count;
struct box_1024* t325 = __this;
assert(t325);
int t326 = t325->num_of_elements;
boolean t327 = t324 <= t326;
assert_is_true_245_(t327);

struct box_1024* t328 = __this;
struct box_1024* t329 = __this;
assert(t329);
char* t330 = t329->raw_data;
char* t331 = src;
int t332 = count;
char* t333 = box_native_memcpy_230_1024(t328, t330, t331, t332);


return t333;

}

void vec_init_146_1024(struct vec_1024* __this)
{
struct vec_1024* t334 = __this;
assert(t334);
int t335 = t334->size;
int t336 = 0;
assert(t334);
t334->size = t336;

struct vec_1024* t337 = __this;
assert(t337);
int t338 = t337->alloc;
int t339 = 2;
assert(t337);
t337->alloc = t339;

struct vec_1024* t340 = __this;
assert(t340);
struct box_1024* t341 = t340->data;
int t342 = sizeof(char);
struct vec_1024* t343 = __this;
assert(t343);
int t344 = t343->alloc;
struct box_1024* t345 = hmalloc(sizeof(struct box_1024));
box_init_191_1024(t345, t342, t344);
assert(t340);
t340->data = t345;


}

void vec_add_152_1024(struct vec_1024* __this, char e)
{
struct vec_1024* t346 = __this;
assert(t346);
int t347 = t346->size;
struct vec_1024* t348 = __this;
assert(t348);
int t349 = t348->alloc;
boolean t350 = t347 >= t349;

if(t350)
{
struct vec_1024* t351 = __this;
assert(t351);
int t352 = t351->alloc;
struct vec_1024* t353 = __this;
assert(t353);
int t354 = t353->alloc;
int t355 = 2;
int t356 = t354 * t355;
assert(t351);
t351->alloc = t356;

int t357 = sizeof(char);
struct vec_1024* t358 = __this;
assert(t358);
int t359 = t358->alloc;
struct box_1024* t360 = hmalloc(sizeof(struct box_1024));
box_init_191_1024(t360, t357, t359);
struct box_1024* ndata = t360;


{
int t361 = 0;
int i = t361;

for(;;)
{
int t362 = i;
struct vec_1024* t363 = __this;
assert(t363);
int t364 = t363->size;
boolean t365 = t362 < t364;
boolean t366 = !t365;

if(t366)
{

break;

}

struct box_1024* t371 = ndata;
int t372 = i;
struct vec_1024* t373 = __this;
assert(t373);
struct box_1024* t374 = t373->data;
int t375 = i;
char t376 = box_access_at_212_1024(t374, t375);
char t377 = box_set_at_215_1024(t371, t372, t376);

int t367 = i;
int t368 = i;
int t369 = 1;
int t370 = t368 + t369;
i = t370;


}


}

struct vec_1024* t378 = __this;
assert(t378);
struct box_1024* t379 = t378->data;
box_free_209_1024(t379);

struct vec_1024* t380 = __this;
assert(t380);
struct box_1024* t381 = t380->data;
struct box_1024* t382 = ndata;
assert(t380);
t380->data = t382;


}

struct vec_1024* t383 = __this;
assert(t383);
struct box_1024* t384 = t383->data;
struct vec_1024* t385 = __this;
assert(t385);
int t386 = t385->size;
char t387 = e;
char t388 = box_set_at_215_1024(t384, t386, t387);

struct vec_1024* t389 = __this;
assert(t389);
int t390 = t389->size;
struct vec_1024* t391 = __this;
assert(t391);
int t392 = t391->size;
int t393 = 1;
int t394 = t392 + t393;
assert(t389);
t389->size = t394;


}

int vec_size_155_1024(struct vec_1024* __this)
{
struct vec_1024* t395 = __this;
assert(t395);
int t396 = t395->size;


return t396;

}

char vec_get_158_1024(struct vec_1024* __this, int index)
{
int t398 = index;
int t399 = 0;
boolean t400 = t398 >= t399;
assert_is_true_245_(t400);

int t402 = index;
struct vec_1024* t403 = __this;
assert(t403);
int t404 = t403->size;
boolean t405 = t402 < t404;
assert_is_true_245_(t405);

struct vec_1024* t406 = __this;
assert(t406);
struct box_1024* t407 = t406->data;
int t408 = index;
char t409 = box_access_at_212_1024(t407, t408);


return t409;

}

char vec_set_161_1024(struct vec_1024* __this, int index, char e)
{
int t411 = index;
int t412 = 0;
boolean t413 = t411 >= t412;
assert_is_true_245_(t413);

int t415 = index;
struct vec_1024* t416 = __this;
assert(t416);
int t417 = t416->size;
boolean t418 = t415 < t417;
assert_is_true_245_(t418);

struct vec_1024* t419 = __this;
assert(t419);
struct box_1024* t420 = t419->data;
int t421 = index;
char t422 = e;
char t423 = box_set_at_215_1024(t420, t421, t422);


return t423;

}

struct box_1024* vec_get_data_164_1024(struct vec_1024* __this)
{
struct vec_1024* t424 = __this;
assert(t424);
struct box_1024* t425 = t424->data;


return t425;

}

void assert_is_true_245_(boolean condition)
{
boolean t427 = condition;
assert_native_assert_true_252_(t427);


}

void assert_panic_248_(struct string* because)
{
struct string* t429 = because;
struct box_1024* t430 = string_get_buffer_242_(t429);
char* t431 = box_raw_data_200_1024(t430);
assert_native_panic_250_(t431);


}

void string_init_233_(struct string* __this, struct box_1024* buffer)
{
struct string* t432 = __this;
assert(t432);
struct box_1024* t433 = t432->buffer;
struct box_1024* t434 = buffer;
assert(t432);
t432->buffer = t434;


}

int string_length_236_(struct string* __this)
{
struct string* t435 = __this;
assert(t435);
struct box_1024* t436 = t435->buffer;
int t437 = box_num_of_elements_197_1024(t436);


return t437;

}

char string_get_239_(struct string* __this, int index)
{
int t439 = index;
int t440 = 0;
boolean t441 = t439 >= t440;
assert_is_true_245_(t441);

int t443 = index;
struct string* t444 = __this;
assert(t444);
struct box_1024* t445 = t444->buffer;
int t446 = box_num_of_elements_197_1024(t445);
boolean t447 = t443 < t446;
assert_is_true_245_(t447);

struct string* t448 = __this;
assert(t448);
struct box_1024* t449 = t448->buffer;
int t450 = index;
char t451 = box_access_at_212_1024(t449, t450);


return t451;

}

struct box_1024* string_get_buffer_242_(struct string* __this)
{
struct string* t452 = __this;
assert(t452);
struct box_1024* t453 = t452->buffer;


return t453;

}

void file_init_167_(struct file* __this, struct string* fullname)
{
struct file* t454 = __this;
assert(t454);
int t455 = t454->fd;
int t456 = 1;
int t457 = -t456;
assert(t454);
t454->fd = t457;

struct file* t458 = __this;
assert(t458);
boolean t459 = t458->is_open;
boolean t460 = false;
assert(t458);
t458->is_open = t460;

struct file* t461 = __this;
assert(t461);
struct string* t462 = t461->fullname;
struct string* t463 = fullname;
assert(t461);
t461->fullname = t463;

struct file* t464 = __this;
assert(t464);
struct box_1024* t465 = t464->buffer;
int t466 = sizeof(char);
int t467 = 2;
struct box_1024* t468 = hmalloc(sizeof(struct box_1024));
box_init_191_1024(t468, t466, t467);
assert(t464);
t464->buffer = t468;

struct file* t469 = __this;
file_fill_buffer_170_(t469);


}

void file_fill_buffer_170_(struct file* __this)
{
struct file* t470 = __this;
assert(t470);
struct box_1024* t471 = t470->buffer;
int t472 = 0;
char t473 = '\0';
char t474 = box_set_at_215_1024(t471, t472, t473);

struct file* t475 = __this;
assert(t475);
struct box_1024* t476 = t475->buffer;
int t477 = 1;
char t478 = '\0';
char t479 = box_set_at_215_1024(t476, t477, t478);


}

void file_open_173_(struct file* __this)
{
struct file* t481 = __this;
assert(t481);
boolean t482 = t481->is_open;
boolean t483 = !t482;
assert_is_true_245_(t483);

struct file* t484 = __this;
assert(t484);
int t485 = t484->fd;
struct file* t486 = __this;
struct file* t487 = __this;
assert(t487);
struct string* t488 = t487->fullname;
struct box_1024* t489 = string_get_buffer_242_(t488);
char* t490 = box_raw_data_200_1024(t489);
int t491 = 0;
int t492 = file_native_open_184_(t486, t490, t491);
assert(t484);
t484->fd = t492;

struct file* t494 = __this;
assert(t494);
int t495 = t494->fd;
int t496 = 1;
int t497 = -t496;
boolean t498 = t495 != t497;
assert_is_true_245_(t498);

struct file* t499 = __this;
assert(t499);
boolean t500 = t499->is_open;
boolean t501 = true;
assert(t499);
t499->is_open = t501;


}

void file_close_176_(struct file* __this)
{
struct file* t503 = __this;
assert(t503);
boolean t504 = t503->is_open;
assert_is_true_245_(t504);

struct file* t505 = __this;
struct file* t506 = __this;
assert(t506);
int t507 = t506->fd;
int t508 = file_native_close_186_(t505, t507);

struct file* t509 = __this;
assert(t509);
boolean t510 = t509->is_open;
boolean t511 = false;
assert(t509);
t509->is_open = t511;


}

int file_read_179_(struct file* __this)
{
struct file* t513 = __this;
assert(t513);
boolean t514 = t513->is_open;
assert_is_true_245_(t514);

struct file* t515 = __this;
struct file* t516 = __this;
assert(t516);
int t517 = t516->fd;
struct file* t518 = __this;
assert(t518);
struct box_1024* t519 = t518->buffer;
char* t520 = box_raw_data_200_1024(t519);
int t521 = 1;
int t522 = file_native_read_188_(t515, t517, t520, t521);
int c = t522;

int t523 = c;


return t523;

}

char file_getc_182_(struct file* __this)
{
struct file* t525 = __this;
assert(t525);
boolean t526 = t525->is_open;
assert_is_true_245_(t526);

struct file* t527 = __this;
assert(t527);
struct box_1024* t528 = t527->buffer;
int t529 = 0;
char t530 = box_access_at_212_1024(t528, t529);


return t530;

}

struct vec_1024* stdio_read_file_133_(struct string* fullname)
{
struct string* t531 = fullname;
struct file* t532 = hmalloc(sizeof(struct file));
file_init_167_(t532, t531);
struct file* fp = t532;

struct file* t533 = fp;
file_open_173_(t533);

struct vec_1024* t534 = hmalloc(sizeof(struct vec_1024));
vec_init_146_1024(t534);
struct vec_1024* rv = t534;

struct file* t535 = fp;
int t536 = file_read_179_(t535);
int sz = t536;

for(;;)
{
int t537 = sz;
int t538 = 0;
boolean t539 = t537 > t538;
boolean t540 = !t539;

if(t540)
{

break;

}

struct file* t541 = fp;
char t542 = file_getc_182_(t541);
char c = t542;

struct vec_1024* t543 = rv;
char t544 = c;
vec_add_152_1024(t543, t544);

int t545 = sz;
struct file* t546 = fp;
int t547 = file_read_179_(t546);
sz = t547;


}

struct vec_1024* t548 = rv;
char t549 = '\0';
vec_add_152_1024(t548, t549);

struct file* t550 = fp;
file_close_176_(t550);

struct vec_1024* t551 = rv;


return t551;

}

void stdio_printf_136_(struct string* s, char c)
{
struct string* t553 = s;
struct box_1024* t554 = string_get_buffer_242_(t553);
char* t555 = box_raw_data_200_1024(t554);
char t556 = c;
stdio_native_printf_141_(t555, t556);


}

void stdio_printf_139_(struct string* s, int c)
{
struct string* t558 = s;
struct box_1024* t559 = string_get_buffer_242_(t558);
char* t560 = box_raw_data_200_1024(t559);
int t561 = c;
stdio_native_printf_143_(t560, t561);


}


int main_class_main_129_()
{
struct box_1024* t564 = hmalloc(sizeof(struct box_1024));
int t568 = 7;
int t569 = sizeof(char);
box_init_191_1024(t564, t569, t568);
box_memcpy_218_1024(t564, t565, t568);
struct string* t566 = hmalloc(sizeof(struct string));
string_init_233_(t566, t564);
struct vec_1024* t567 = stdio_read_file_133_(t566);
struct vec_1024* buf = t567;


{
int t570 = 0;
int i = t570;

for(;;)
{
int t571 = i;
struct vec_1024* t572 = buf;
int t573 = vec_size_155_1024(t572);
boolean t574 = t571 < t573;
boolean t575 = !t574;

if(t575)
{

break;

}

struct vec_1024* t580 = buf;
int t581 = i;
char t582 = vec_get_158_1024(t580, t581);
char c = t582;

struct box_1024* t584 = hmalloc(sizeof(struct box_1024));
int t588 = 3;
int t589 = sizeof(char);
box_init_191_1024(t584, t589, t588);
box_memcpy_218_1024(t584, t585, t588);
struct string* t586 = hmalloc(sizeof(struct string));
string_init_233_(t586, t584);
char t587 = c;
stdio_printf_136_(t586, t587);

int t576 = i;
int t577 = i;
int t578 = 1;
int t579 = t577 + t578;
i = t579;


}


}

struct box_1024* t591 = hmalloc(sizeof(struct box_1024));
int t595 = 3;
int t596 = sizeof(char);
box_init_191_1024(t591, t596, t595);
box_memcpy_218_1024(t591, t592, t595);
struct string* t593 = hmalloc(sizeof(struct string));
string_init_233_(t593, t591);
int t594 = 32768;
stdio_printf_139_(t593, t594);

int t597 = 0;


return t597;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_129_();

    printf("%d\n", result);
    return result;

}
