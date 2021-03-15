#ifndef GENERATED_TYPES_H_                  
#define GENERATED_TYPES_H_                  
#include "hrt/headers.h"                  

typedef int boolean;                        
typedef struct string * string;             

struct string                               
{                                           
    char *buffer;                           
    size_t len;                             
};                                          

void string_init(string __this, char *buf); 
void string_deinit(string __this);          
void string_destroy(string __this);         

struct type_descr;                          
extern struct type_descr *TD_CHAR_PTR;      
extern struct type_descr *TD_ARRAY;         
extern struct type_descr *TD_ARRAY_TABLE;   

extern struct type_descr *TD_MAIN_CLASS;
extern struct type_descr *TD_OPT;
extern struct type_descr *TD_STRING;

typedef struct opt * opt;

int main_class_main_26();
void opt_init_18(opt __this);
void opt_fn_21(opt __this);
void opt_deinit_30(opt __this);

struct opt
{
int flag; 
string value; 

};



#endif
