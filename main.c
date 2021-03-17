#include <assert.h>                            
#include <limits.h>                            
#include <stdarg.h>                            
#include <stddef.h>                            
#include <stdint.h>                            
#include <stdio.h>                             
#include <stdlib.h>                            
#include <string.h>                            

typedef int boolean;                           

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

struct array_1024;
struct ptr_1027;
struct array_1027;
struct ptr_1024;
struct string;

void array_init_17_1024(struct array_1024* __this);
void array_add_23_1024(struct array_1024* __this, char e);
int array_size_26_1024(struct array_1024* __this);
char array_get_29_1024(struct array_1024* __this, int index);
char array_set_32_1024(struct array_1024* __this, int index, char e);
void ptr_init_2_1027(struct ptr_1027* __this, int size);
void ptr_destroy_5_1027(struct ptr_1027* __this);
struct string* ptr_get_8_1027(struct ptr_1027* __this, int at);
struct string* ptr_set_11_1027(struct ptr_1027* __this, int at, struct string* e);
int ptr_size_14_1027(struct ptr_1027* __this);
void array_init_17_1027(struct array_1027* __this);
void array_add_23_1027(struct array_1027* __this, struct string* e);
int array_size_26_1027(struct array_1027* __this);
struct string* array_get_29_1027(struct array_1027* __this, int index);
struct string* array_set_32_1027(struct array_1027* __this, int index, struct string* e);
void ptr_init_2_1024(struct ptr_1024* __this, int size);
void ptr_destroy_5_1024(struct ptr_1024* __this);
char ptr_get_8_1024(struct ptr_1024* __this, int at);
char ptr_set_11_1024(struct ptr_1024* __this, int at, char e);
int ptr_size_14_1024(struct ptr_1024* __this);
int main_class_main_46_();
void string_init_35_(struct string* __this, struct ptr_1024* buffer);
int string_length_38_(struct string* __this);
char string_get_41_(struct string* __this, int index);

struct array_1024
{
struct ptr_1024* data; 
int size; 
int alloc; 

};

struct ptr_1027
{
struct string** raw_data; 
int size; 

};

struct array_1027
{
struct ptr_1027* data; 
int size; 
int alloc; 

};

struct ptr_1024
{
char* raw_data; 
int size; 

};

struct string
{
struct ptr_1024* buffer; 

};


#define assert_true(expr) do {                               \
  if( !(expr) ) {                                            \
    fprintf(stderr, "assert fail: (%s:%s():%d) : [%s]\n"     \
    , __FILE__, __func__, __LINE__, #expr);                  \
    exit(128);                                               \
  }                                                          \
} while(0) 

char t312[] = { 'd', '\0'};
char t309[] = { 'c', '\0'};
char t306[] = { 'b', '\0'};
char t303[] = { 'a', '\0'};
char t330[] = { '%', 's', '\n', '\0'};
char t315[] = { 'e', '\0'};


struct string* std_mem_get_1025_1026 (struct string* *  raw_data, size_t index) 
{                                                                  
  assert(raw_data);                                                
  return raw_data[index];                                          
}                                                                  

struct string* *  std_mem_malloc_1025_1026 (struct string* *  stub__, size_t size)   
{                                                                  
  struct string* *  ptr = (struct string* * ) hmalloc(size);                       
  return ptr;                                                      
}                                                                  

void std_mem_free_1028(void *p) { 
  free(p);
}
struct string* std_mem_set_1025_1026_1027 (struct string* *  raw_data, size_t index, struct string* e) 
{                                                                                
  assert(raw_data);                                                              
  struct string* old = raw_data[index];                                              
  raw_data[index] = e;                                                           
  return old;                                                                    
}                                                                                

void std_mem_free_1025(void *p) { 
  free(p);
}
char std_mem_get_1028_1026 (char *  raw_data, size_t index) 
{                                                                  
  assert(raw_data);                                                
  return raw_data[index];                                          
}                                                                  

char std_mem_set_1028_1026_1024 (char *  raw_data, size_t index, char e) 
{                                                                                
  assert(raw_data);                                                              
  char old = raw_data[index];                                              
  raw_data[index] = e;                                                           
  return old;                                                                    
}                                                                                

char *  std_mem_malloc_1028_1026 (char *  stub__, size_t size)   
{                                                                  
  char *  ptr = (char * ) hmalloc(size);                       
  return ptr;                                                      
}                                                                  

static void std_print_1027_1027(struct string* t329, struct string* t331)
{
assert(t329);
assert(t331);

    printf(t329->buffer, t331->buffer);
}

void array_init_17_1024(struct array_1024* __this)
{
struct array_1024* t47 = __this;
assert(t47);
int t48 = t47->size;
int t49 = 0;
assert(t47);
t47->size = t49;

struct array_1024* t50 = __this;
assert(t50);
int t51 = t50->alloc;
int t52 = 2;
assert(t50);
t50->alloc = t52;

struct array_1024* t53 = __this;
assert(t53);
struct ptr_1024* t54 = t53->data;
int t55 = sizeof(char);
struct array_1024* t56 = __this;
assert(t56);
int t57 = t56->alloc;
int t58 = t55 * t57;
struct ptr_1024* t59 = hmalloc(sizeof(struct ptr_1024));
ptr_init_2_1024(t59, t58);
assert(t53);
t53->data = t59;


}

void array_add_23_1024(struct array_1024* __this, char e)
{
struct array_1024* t60 = __this;
assert(t60);
int t61 = t60->size;
struct array_1024* t62 = __this;
assert(t62);
int t63 = t62->alloc;
boolean t64 = t61 >= t63;

if(t64)
{
struct array_1024* t65 = __this;
assert(t65);
int t66 = t65->alloc;
struct array_1024* t67 = __this;
assert(t67);
int t68 = t67->alloc;
int t69 = 2;
int t70 = t68 * t69;
assert(t65);
t65->alloc = t70;

int t71 = sizeof(char);
struct array_1024* t72 = __this;
assert(t72);
int t73 = t72->alloc;
int t74 = t71 * t73;
struct ptr_1024* t75 = hmalloc(sizeof(struct ptr_1024));
ptr_init_2_1024(t75, t74);
struct ptr_1024* ndata = t75;


{
int t76 = 0;
int i = t76;

for(;;)
{
int t77 = i;
struct array_1024* t78 = __this;
assert(t78);
int t79 = t78->size;
boolean t80 = t77 < t79;
boolean t81 = !t80;

if(t81)
{

break;

}

struct ptr_1024* t86 = ndata;
int t87 = i;
struct array_1024* t88 = __this;
assert(t88);
struct ptr_1024* t89 = t88->data;
int t90 = i;
char t91 = ptr_get_8_1024(t89, t90);
char t92 = ptr_set_11_1024(t86, t87, t91);

int t82 = i;
int t83 = i;
int t84 = 1;
int t85 = t83 + t84;
i = t85;


}


}

struct array_1024* t93 = __this;
assert(t93);
struct ptr_1024* t94 = t93->data;
ptr_destroy_5_1024(t94);

struct array_1024* t95 = __this;
assert(t95);
struct ptr_1024* t96 = t95->data;
struct ptr_1024* t97 = ndata;
assert(t95);
t95->data = t97;


}

struct array_1024* t98 = __this;
assert(t98);
struct ptr_1024* t99 = t98->data;
struct array_1024* t100 = __this;
assert(t100);
int t101 = t100->size;
char t102 = e;
char t103 = ptr_set_11_1024(t99, t101, t102);

struct array_1024* t104 = __this;
assert(t104);
int t105 = t104->size;
struct array_1024* t106 = __this;
assert(t106);
int t107 = t106->size;
int t108 = 1;
int t109 = t107 + t108;
assert(t104);
t104->size = t109;


}

int array_size_26_1024(struct array_1024* __this)
{
struct array_1024* t110 = __this;
assert(t110);
int t111 = t110->size;


return t111;

}

char array_get_29_1024(struct array_1024* __this, int index)
{
int t112 = index;
int t113 = 0;
boolean t114 = t112 >= t113;
assert_true(t114);

int t115 = index;
struct array_1024* t116 = __this;
assert(t116);
int t117 = t116->size;
boolean t118 = t115 < t117;
assert_true(t118);

struct array_1024* t119 = __this;
assert(t119);
struct ptr_1024* t120 = t119->data;
int t121 = index;
char t122 = ptr_get_8_1024(t120, t121);


return t122;

}

char array_set_32_1024(struct array_1024* __this, int index, char e)
{
int t123 = index;
int t124 = 0;
boolean t125 = t123 >= t124;
assert_true(t125);

int t126 = index;
struct array_1024* t127 = __this;
assert(t127);
int t128 = t127->size;
boolean t129 = t126 < t128;
assert_true(t129);

struct array_1024* t130 = __this;
assert(t130);
struct ptr_1024* t131 = t130->data;
int t132 = index;
char t133 = e;
char t134 = ptr_set_11_1024(t131, t132, t133);


return t134;

}

void ptr_init_2_1027(struct ptr_1027* __this, int size)
{
int t135 = size;
int t136 = 0;
boolean t137 = t135 > t136;
assert_true(t137);

struct ptr_1027* t138 = __this;
assert(t138);
struct string** t139 = t138->raw_data;
struct ptr_1027* t140 = __this;
assert(t140);
struct string** t141 = t140->raw_data;
int t142 = size;
struct string** t143 = std_mem_malloc_1025_1026(t141, t142);
assert(t138);
t138->raw_data = t143;

struct ptr_1027* t144 = __this;
assert(t144);
int t145 = t144->size;
int t146 = size;
assert(t144);
t144->size = t146;


}

void ptr_destroy_5_1027(struct ptr_1027* __this)
{
struct ptr_1027* t147 = __this;
assert(t147);
struct string** t148 = t147->raw_data;
std_mem_free_1025(t148);


}

struct string* ptr_get_8_1027(struct ptr_1027* __this, int at)
{
int t149 = at;
struct ptr_1027* t150 = __this;
assert(t150);
int t151 = t150->size;
boolean t152 = t149 < t151;
assert_true(t152);

struct ptr_1027* t153 = __this;
assert(t153);
struct string** t154 = t153->raw_data;
int t155 = at;
struct string* t156 = std_mem_get_1025_1026(t154, t155);


return t156;

}

struct string* ptr_set_11_1027(struct ptr_1027* __this, int at, struct string* e)
{
int t157 = at;
struct ptr_1027* t158 = __this;
assert(t158);
int t159 = t158->size;
boolean t160 = t157 < t159;
assert_true(t160);

struct ptr_1027* t161 = __this;
assert(t161);
struct string** t162 = t161->raw_data;
int t163 = at;
struct string* t164 = std_mem_get_1025_1026(t162, t163);
struct string* old = t164;

struct ptr_1027* t165 = __this;
assert(t165);
struct string** t166 = t165->raw_data;
int t167 = at;
struct string* t168 = e;
struct string* t169 = std_mem_set_1025_1026_1027(t166, t167, t168);

struct string* t170 = old;


return t170;

}

int ptr_size_14_1027(struct ptr_1027* __this)
{
struct ptr_1027* t171 = __this;
assert(t171);
int t172 = t171->size;


return t172;

}

void array_init_17_1027(struct array_1027* __this)
{
struct array_1027* t173 = __this;
assert(t173);
int t174 = t173->size;
int t175 = 0;
assert(t173);
t173->size = t175;

struct array_1027* t176 = __this;
assert(t176);
int t177 = t176->alloc;
int t178 = 2;
assert(t176);
t176->alloc = t178;

struct array_1027* t179 = __this;
assert(t179);
struct ptr_1027* t180 = t179->data;
int t181 = sizeof(struct string*);
struct array_1027* t182 = __this;
assert(t182);
int t183 = t182->alloc;
int t184 = t181 * t183;
struct ptr_1027* t185 = hmalloc(sizeof(struct ptr_1027));
ptr_init_2_1027(t185, t184);
assert(t179);
t179->data = t185;


}

void array_add_23_1027(struct array_1027* __this, struct string* e)
{
struct array_1027* t186 = __this;
assert(t186);
int t187 = t186->size;
struct array_1027* t188 = __this;
assert(t188);
int t189 = t188->alloc;
boolean t190 = t187 >= t189;

if(t190)
{
struct array_1027* t191 = __this;
assert(t191);
int t192 = t191->alloc;
struct array_1027* t193 = __this;
assert(t193);
int t194 = t193->alloc;
int t195 = 2;
int t196 = t194 * t195;
assert(t191);
t191->alloc = t196;

int t197 = sizeof(struct string*);
struct array_1027* t198 = __this;
assert(t198);
int t199 = t198->alloc;
int t200 = t197 * t199;
struct ptr_1027* t201 = hmalloc(sizeof(struct ptr_1027));
ptr_init_2_1027(t201, t200);
struct ptr_1027* ndata = t201;


{
int t202 = 0;
int i = t202;

for(;;)
{
int t203 = i;
struct array_1027* t204 = __this;
assert(t204);
int t205 = t204->size;
boolean t206 = t203 < t205;
boolean t207 = !t206;

if(t207)
{

break;

}

struct ptr_1027* t212 = ndata;
int t213 = i;
struct array_1027* t214 = __this;
assert(t214);
struct ptr_1027* t215 = t214->data;
int t216 = i;
struct string* t217 = ptr_get_8_1027(t215, t216);
struct string* t218 = ptr_set_11_1027(t212, t213, t217);

int t208 = i;
int t209 = i;
int t210 = 1;
int t211 = t209 + t210;
i = t211;


}


}

struct array_1027* t219 = __this;
assert(t219);
struct ptr_1027* t220 = t219->data;
ptr_destroy_5_1027(t220);

struct array_1027* t221 = __this;
assert(t221);
struct ptr_1027* t222 = t221->data;
struct ptr_1027* t223 = ndata;
assert(t221);
t221->data = t223;


}

struct array_1027* t224 = __this;
assert(t224);
struct ptr_1027* t225 = t224->data;
struct array_1027* t226 = __this;
assert(t226);
int t227 = t226->size;
struct string* t228 = e;
struct string* t229 = ptr_set_11_1027(t225, t227, t228);

struct array_1027* t230 = __this;
assert(t230);
int t231 = t230->size;
struct array_1027* t232 = __this;
assert(t232);
int t233 = t232->size;
int t234 = 1;
int t235 = t233 + t234;
assert(t230);
t230->size = t235;


}

int array_size_26_1027(struct array_1027* __this)
{
struct array_1027* t236 = __this;
assert(t236);
int t237 = t236->size;


return t237;

}

struct string* array_get_29_1027(struct array_1027* __this, int index)
{
int t238 = index;
int t239 = 0;
boolean t240 = t238 >= t239;
assert_true(t240);

int t241 = index;
struct array_1027* t242 = __this;
assert(t242);
int t243 = t242->size;
boolean t244 = t241 < t243;
assert_true(t244);

struct array_1027* t245 = __this;
assert(t245);
struct ptr_1027* t246 = t245->data;
int t247 = index;
struct string* t248 = ptr_get_8_1027(t246, t247);


return t248;

}

struct string* array_set_32_1027(struct array_1027* __this, int index, struct string* e)
{
int t249 = index;
int t250 = 0;
boolean t251 = t249 >= t250;
assert_true(t251);

int t252 = index;
struct array_1027* t253 = __this;
assert(t253);
int t254 = t253->size;
boolean t255 = t252 < t254;
assert_true(t255);

struct array_1027* t256 = __this;
assert(t256);
struct ptr_1027* t257 = t256->data;
int t258 = index;
struct string* t259 = e;
struct string* t260 = ptr_set_11_1027(t257, t258, t259);


return t260;

}

void ptr_init_2_1024(struct ptr_1024* __this, int size)
{
int t261 = size;
int t262 = 0;
boolean t263 = t261 > t262;
assert_true(t263);

struct ptr_1024* t264 = __this;
assert(t264);
char* t265 = t264->raw_data;
struct ptr_1024* t266 = __this;
assert(t266);
char* t267 = t266->raw_data;
int t268 = size;
char* t269 = std_mem_malloc_1028_1026(t267, t268);
assert(t264);
t264->raw_data = t269;

struct ptr_1024* t270 = __this;
assert(t270);
int t271 = t270->size;
int t272 = size;
assert(t270);
t270->size = t272;


}

void ptr_destroy_5_1024(struct ptr_1024* __this)
{
struct ptr_1024* t273 = __this;
assert(t273);
char* t274 = t273->raw_data;
std_mem_free_1028(t274);


}

char ptr_get_8_1024(struct ptr_1024* __this, int at)
{
int t275 = at;
struct ptr_1024* t276 = __this;
assert(t276);
int t277 = t276->size;
boolean t278 = t275 < t277;
assert_true(t278);

struct ptr_1024* t279 = __this;
assert(t279);
char* t280 = t279->raw_data;
int t281 = at;
char t282 = std_mem_get_1028_1026(t280, t281);


return t282;

}

char ptr_set_11_1024(struct ptr_1024* __this, int at, char e)
{
int t283 = at;
struct ptr_1024* t284 = __this;
assert(t284);
int t285 = t284->size;
boolean t286 = t283 < t285;
assert_true(t286);

struct ptr_1024* t287 = __this;
assert(t287);
char* t288 = t287->raw_data;
int t289 = at;
char t290 = std_mem_get_1028_1026(t288, t289);
char old = t290;

struct ptr_1024* t291 = __this;
assert(t291);
char* t292 = t291->raw_data;
int t293 = at;
char t294 = e;
char t295 = std_mem_set_1028_1026_1024(t292, t293, t294);

char t296 = old;


return t296;

}

int ptr_size_14_1024(struct ptr_1024* __this)
{
struct ptr_1024* t297 = __this;
assert(t297);
int t298 = t297->size;


return t298;

}

void string_init_35_(struct string* __this, struct ptr_1024* buffer)
{
struct string* t333 = __this;
assert(t333);
struct ptr_1024* t334 = t333->buffer;
struct ptr_1024* t335 = buffer;
assert(t333);
t333->buffer = t335;


}

int string_length_38_(struct string* __this)
{
struct string* t336 = __this;
assert(t336);
struct ptr_1024* t337 = t336->buffer;
int t338 = ptr_size_14_1024(t337);


return t338;

}

char string_get_41_(struct string* __this, int index)
{
int t339 = index;
int t340 = 0;
boolean t341 = t339 >= t340;
assert_true(t341);

int t342 = index;
struct string* t343 = __this;
assert(t343);
struct ptr_1024* t344 = t343->buffer;
int t345 = ptr_size_14_1024(t344);
boolean t346 = t342 < t345;
assert_true(t346);

struct string* t347 = __this;
assert(t347);
struct ptr_1024* t348 = t347->buffer;
int t349 = index;
char t350 = ptr_get_8_1024(t348, t349);


return t350;

}


int main_class_main_46_()
{
struct array_1027* t299 = hmalloc(sizeof(struct array_1027));
array_init_17_1027(t299);
struct array_1027* x = t299;

struct array_1024* t300 = hmalloc(sizeof(struct array_1024));
array_init_17_1024(t300);
struct array_1024* y = t300;

struct array_1027* t301 = x;
struct string* t302 = hmalloc(sizeof(struct string));
string_init_35_(t302, t303);
array_add_23_1027(t301, t302);

struct array_1027* t304 = x;
struct string* t305 = hmalloc(sizeof(struct string));
string_init_35_(t305, t306);
array_add_23_1027(t304, t305);

struct array_1027* t307 = x;
struct string* t308 = hmalloc(sizeof(struct string));
string_init_35_(t308, t309);
array_add_23_1027(t307, t308);

struct array_1027* t310 = x;
struct string* t311 = hmalloc(sizeof(struct string));
string_init_35_(t311, t312);
array_add_23_1027(t310, t311);

struct array_1027* t313 = x;
struct string* t314 = hmalloc(sizeof(struct string));
string_init_35_(t314, t315);
array_add_23_1027(t313, t314);


{
int t316 = 0;
int i = t316;

for(;;)
{
int t317 = i;
struct array_1027* t318 = x;
int t319 = array_size_26_1027(t318);
boolean t320 = t317 < t319;
boolean t321 = !t320;

if(t321)
{

break;

}

struct array_1027* t326 = x;
int t327 = i;
struct string* t328 = array_get_29_1027(t326, t327);
struct string* c = t328;

struct string* t329 = hmalloc(sizeof(struct string));
string_init_35_(t329, t330);
struct string* t331 = c;
std_print_1027_1027(t329, t331);

int t322 = i;
int t323 = i;
int t324 = 1;
int t325 = t323 + t324;
i = t325;


}


}

int t332 = 0;


return t332;

}
int main(int argc, char** argv) 
{
    int result = main_class_main_46_();

    printf("%d\n", result);
    return result;

}
