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
extern struct type_descr *TD_TYPE;
extern struct type_descr *TD_TOKEN;
extern struct type_descr *TD_FILE_READER;
extern struct type_descr *TD_STRING;
typedef struct type * type;
typedef struct token * token;
typedef struct file_reader * file_reader;
int main_class_main_29();
void type_init_23(type __this);
void type_deinit_26(type __this);
void token_init_20(token __this);
void token_deinit_33(token __this);
void file_reader_init_14(file_reader __this);
void file_reader_close_17(file_reader __this);
void file_reader_deinit_35(file_reader __this);
struct type
{
token pos; 
string repr; 
file_reader fp; 

};
struct token
{
string value; 
int f; 

};
struct file_reader
{
int fp; 

};

#endif
