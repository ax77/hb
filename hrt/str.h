#ifndef STRBUILDER_H_
#define STRBUILDER_H_

#include "vec.h"
#include "heap.h"

struct str;

struct str *sb_new(void);
struct str *sb_news(char * str);
void sb_addc(struct str *s, char c);
void sb_adds(struct str *s, char *news);
struct str *sb_copy(struct str *what);
struct str *sb_left(struct str *from, size_t much);
struct str *sb_right(struct str *from, size_t much);
struct str *sb_mid(struct str *from, size_t begin, size_t much);
struct str *sb_trim(struct str *from);
char* sb_cstr(struct str *from);
size_t sb_len(struct str *sb);

/// string starts with pattern at index 'at'
/// sb_next_is("abcde", "bc", 1) -> true
int sb_next_is(struct str *input, char *pattern, size_t at);

ptrdiff_t sb_index_of(struct str *sb, char *pattern);

vec_of(char*) * sb_split_char(struct str * where, char sep, int include_empty);
vec_of(char*) * sb_split_str( struct str * const input, char *pattern);

struct str * sb_replace(struct str * input, char *pattern, char *replacement);

int strstarts(char *what, char *with);
int strends(char *what, char *with);

#endif /* STRBUILDER_H_ */
