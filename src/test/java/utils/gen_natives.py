fnames = """
  native_malloc
, native_calloc
, native_free
, native_ptr_access_at
, native_ptr_set_at
, native_memcpy
, native_panic
, native_assert_true
, native_open
, native_close
, native_read
"""

# public static final Ident zero_ident = g("zero");

q = '\"'

for s in fnames.split(','):
  name = s.strip()
  print('public static final Ident ' + name + '_ident = g(' + q+name+q+ ');')
