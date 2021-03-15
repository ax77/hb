#include "str.h"
#include "../generated_types.h"

struct str {
    size_t len, alloc;
    char *str;
};

int strstarts(char *what, char *with)
{
    assert(what);
    assert(with);

    size_t L1 = strlen(what);
    size_t L2 = strlen(with);
    if (L1 == 0 || L2 == 0 || (L2 > L1)) {
        return 0;
    }

    for (size_t i = 0; i < L2; i++) {
        int c1 = what[i];
        int c2 = with[i];
        if (c1 != c2) {
            return 0;
        }
    }
    return 1;
}

int strends(char *what, char *with)
{
    assert(what);
    assert(with);

    size_t L1 = strlen(what);
    size_t L2 = strlen(with);
    if (L1 == 0 || L2 == 0 || (L2 > L1)) {
        return 0;
    }

    for (ptrdiff_t i = L1, j = L2; --i >= 0 && --j >= 0;) {
        int c1 = what[i];
        int c2 = with[j];
        if (c1 != c2) {
            return 0;
        }
    }
    return 1;
}

struct str *sb_new()
{
    struct str *rv = get_memory(sizeof(struct str), TD_STRING);
    rv->len = 0;
    rv->alloc = 8;
    rv->str = get_memory(rv->alloc * sizeof(char), TD_CHAR_PTR);
    rv->str[rv->len] = '\0';
    return rv;
}

struct str *sb_news(char * str)
{
    struct str *rv = sb_new();
    sb_adds(rv, str);
    return rv;
}

static void sb_grow(struct str *s)
{
    s->alloc *= 2;

    char* newbuff = get_memory(s->alloc * sizeof(char), TD_CHAR_PTR);
    strcpy(newbuff, s->str);
    newbuff[s->len] = '\0';
    s->str = newbuff;
}

void sb_addc(struct str *s, char c)
{
    if (!c) {
        return;
    }
    if ((s->len + 2) == s->alloc) {
        sb_grow(s);
    }
    s->str[s->len++] = c;
    s->str[s->len] = '\0';
}

void sb_adds(struct str *s, char *news)
{
    if (!news) {
        return;
    }
    for (size_t i = 0; news[i]; i++) {
        sb_addc(s, news[i]);
    }
}

char* sb_cstr(struct str *sb)
{
    assert(sb);
    return sb->str;
}

size_t sb_len(struct str *sb)
{
    assert(sb);
    return sb->len;
}

struct str *sb_copy(struct str *what)
{
    struct str *res = sb_new();
    sb_adds(res, what->str);
    return res;
}

struct str *sb_left(struct str *from, size_t much)
{
    assert(from && from->str);

    struct str *res = sb_new();
    // I) empty one or another.
    if (from->len == 0 || much == 0) {
        return res;
    }
    // II) overflow, return full content of src
    if (much >= from->len) {
        return sb_copy(from);
    }
    // III) normal cases
    for (size_t i = 0; i < much; i++) {
        sb_addc(res, from->str[i]);
    }
    return res;
}

struct str *sb_right(struct str *from, size_t much)
{
    assert(from && from->str);

    struct str *res = sb_new();
    // I) empty one or another.
    if (from->len == 0 || much == 0) {
        return res;
    }
    // II) overflow, return full content of src
    if (much >= from->len) {
        return sb_copy(from);
    }
    //III) normal cases
    size_t start = from->len - much;
    for (size_t i = start; i < from->len; i++) {
        sb_addc(res, from->str[i]);
    }
    return res;
}

struct str *sb_mid(struct str *from, size_t begin, size_t much)
{
    assert(from && from->str);

    struct str *res = sb_new();
    // I) empty
    if (begin >= from->len) {
        return res;
    }
    // II) overflow, return full content of src from begin to .len
    if (much >= from->len) {
        much = from->len;
    }
    size_t end = begin + much;
    if (end >= from->len) {
        end = from->len;
    }
    for (size_t i = begin; i < end; i++) {
        sb_addc(res, from->str[i]);
    }
    return res;
}

struct str *sb_trim(struct str *from)
{
    assert(from && from->str);

    struct str *res = sb_new();
    if (from->len == 0) {
        return res;
    }

    size_t start = 0;
    ptrdiff_t end = 0;

    for (start = 0; start < from->len; start += 1) {
        int c = from->str[start];
        if (c > ' ') {
            break;
        }
    }

    for (end = from->len - 1; end >= 0; end -= 1) {
        int c = from->str[end];
        if (c > ' ') {
            break;
        }
    }

    for (size_t i = start; i <= end; i++) {
        sb_addc(res, from->str[i]);
    }
    return res;
}

vec_of(char*) * sb_split_char(struct str * where, char sep, int include_empty)
{
    vec_of(char*) * lines = vec_new();
    size_t len = where->len;

    if (len == 0) {
        return lines;
    }

    struct str * sb = sb_new();
    for (size_t i = 0; i < len; i++) {
        char c = where->str[i];
        if (c == sep) {
            if (sb->len > 0 || (sb->len == 0 && include_empty)) {
                vec_add(lines, get_memory_strdup(sb->str));
            }

            sb = sb_new();

            continue;
        }
        sb_addc(sb, c);
    }

    if (sb->len > 0 || (sb->len == 0 && include_empty)) {
        vec_add(lines, get_memory_strdup(sb->str));
    }

    return lines;
}

vec_of(char*) * sb_split_str(struct str * const input, char *pattern)
{
    assert(input);
    assert(pattern);

    vec_of(char*) * lines = vec_new();

    if (input->len == 0) {
        return lines;
    }

    struct str *where = sb_copy(input);
    const size_t pattern_len = strlen(pattern);
    const size_t len = where->len;

    ptrdiff_t pos = sb_index_of(where, pattern);
    while (pos >= 0) {
        struct str *substring = sb_left(where, pos);

        if (substring->len > 0) {
            vec_add(lines, get_memory_strdup(substring->str));
        }

        // TODO: leak here...
        where = sb_mid(where, pos + pattern_len, len);
        pos = sb_index_of(where, pattern);
    }

    if (where->len > 0) {
        vec_add(lines, get_memory_strdup(where->str));
    }

    return lines;
}

int sb_next_is(struct str *input, char *pattern, size_t at)
{
    assert(input && input->str);
    assert(pattern);
    assert(at < input->len);

    const size_t pattern_len = strlen(pattern);

    size_t count_of = 0;
    for (size_t i = at, j = 0; i < input->len && j < pattern_len; i += 1, j += 1) {
        count_of += 1;

        char c1 = input->str[i];
        char c2 = pattern[j];
        if (c1 != c2) {
            return 0;
        }
    }

    return count_of == pattern_len;
}

ptrdiff_t sb_index_of(struct str *sb, char *pattern)
{
    assert(sb);
    assert(sb_cstr(sb));
    assert(pattern);

    const size_t sb_len = sb->len;
    const size_t pattern_len = strlen(pattern);

    if (pattern_len == 0) {
        return -1;
    }

    for (size_t offset = 0; offset < sb_len; offset += 1) {
        if (sb_next_is(sb, pattern, offset)) {
            return offset;
        }
    }

    return -1;
}

/// /// v.2
/// static int __sb_next_is__(struct str * input, char * pattern, size_t input_len, size_t begin_index,
///         size_t end_index, size_t patternLen)
/// {
///     if (end_index > input_len) {
///         return 0;
///     }
///     struct str * substring = sb_mid(input, begin_index, patternLen);
///     return strcmp(substring->str, pattern) == 0;
/// }

struct str * sb_replace(struct str * input, char * pattern, char *replacement)
{
    assert(replacement);

    if (input == NULL) {
        // it is more clear and simple to return empty value instead of null or exception.
        // because otherwise you should check the return value that it isn't null, and so on.
        // who cares about that? null or not null, we return empty string here.
        // and we'll work with this empty string in invocation point instead of that null.
        return sb_new();
    }

    if (pattern == NULL || strlen(pattern) == 0) {
        return sb_news(input->str);
    }
    size_t inputLen = input->len;
    size_t patternLen = strlen(pattern);
    size_t replacementLen = strlen(replacement);

    struct str * sb = sb_new();

    for (size_t offset = 0; offset < inputLen;) {
        // size_t endIndex = patternLen + offset;
        // if (sb_next_is(input, pattern, inputLen, offset, endIndex, patternLen)) {
        if (sb_next_is(input, pattern, offset)) {
            if (replacementLen > 0) {
                sb_adds(sb, replacement);
            }
            offset += patternLen;
        } else {
            sb_addc(sb, input->str[offset]);
            offset++;
        }
    }
    return sb;
}

