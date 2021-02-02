package parse;

public class ParseFlags {
  //@formatter:off
  
  // we should know the length in array-creation expression:
  // new [2: i32]
  // we should not want the length in type-declaration:
  // let array: [i32];
  public static final int f_expect_array_length = 0x00000001; 
}
