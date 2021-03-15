#ifndef MEM_H_
#define MEM_H_

#include "headers.h"

void* hmalloc(size_t size);
void* hrealloc(void* old, size_t newsize);
void *hcalloc(size_t count, size_t eltsize);

#endif
