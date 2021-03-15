package _st7_codeout;

//@formatter:off
public abstract class CCMacro {

  public static String genMacro() {

    StringBuilder sb = new StringBuilder();
    sb.append("#define assert_true(expr) do {                               \\\n");
    sb.append("  if( !(expr) ) {                                            \\\n");
    sb.append("    fprintf(stderr, \"assert fail: (%s:%s():%d) : [%s]\\n\"     \\\n");
    sb.append("    , __FILE__, __func__, __LINE__, #expr);                  \\\n");
    sb.append("    exit(128);                                               \\\n");
    sb.append("  }                                                          \\\n");
    sb.append("} while(0) \n\n");
    
    return sb.toString();
  }

}
