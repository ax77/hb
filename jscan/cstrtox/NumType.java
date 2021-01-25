package jscan.cstrtox;

/*
Integer literal syntax

>>-+-decimal_constant-----+--+---------------+-----------------><
   +-octal_constant-------+  +-+-l--+--+---+-+   
   '-hexadecimal_constant-'  | +-L--+  +-u-+ |   
                             | +-ll-+  '-U-' |   
                             | '-LL-'        |   
                             '-+-u-+--+----+-'   
                               '-U-'  +-l--+     
                                      +-L--+     
                                      +-ll-+     
                                      '-LL-'     
                                     */

public enum NumType {
    N_ERROR, N_INT, N_UINT, N_LONG, N_ULONG, N_LONG_LONG, N_ULONG_LONG, N_FLOAT, N_DOUBLE, N_LONG_DOUBLE,
}
