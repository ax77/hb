package _st7_codeout;

public abstract class GnAssertTrue {

  public static String prebuf() {
    StringBuilder sb = new StringBuilder();
    sb.append("static inline void assert_true(                                  \n");
    sb.append("  int cnd,                                                       \n");
    sb.append("  struct string *file,                                           \n");
    sb.append("  int line,                                                      \n");
    sb.append("  struct string *expr)                                           \n");
    sb.append("{                                                                \n");
    sb.append("    assert(file);                                                \n");
    sb.append("    assert(file->buffer);                                        \n");
    sb.append("    assert(expr);                                                \n");
    sb.append("    assert(expr->buffer);                                        \n");
    sb.append("    if (cnd == 0) {                                              \n");
    sb.append("        fprintf(stdout, \"assertion failed: (%s:%d) : [%s]\\n\"  \n");
    sb.append("          , file->buffer                                         \n");
    sb.append("          , line                                                 \n");
    sb.append("          , expr->buffer);                                       \n");
    sb.append("        exit(7);                                                 \n");
    sb.append("    }                                                            \n");
    sb.append("}                                                                \n\n");
    return sb.toString();
  }

}
