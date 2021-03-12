package _st7_codeout;

public abstract class CCMacro {

  public static String genMacro() {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("#define ASSIGN(LVALUE, RVALUE, OBJNAME)               \\\n");
    sb.append("do {                                                  \\\n");
    sb.append("    if (LVALUE == NULL) {                             \\\n");
    sb.append("        if (RVALUE != NULL) {                         \\\n");
    sb.append("            /*LVALUE:null*/                           \\\n");
    sb.append("            /*RVALUE:object*/                         \\\n");
    sb.append("                                                      \\\n");
    sb.append("            /*1:REF, ASSIGN*/                         \\\n");
    sb.append("            OBJNAME##_ref(RVALUE);                    \\\n");
    sb.append("            LVALUE = RVALUE;                          \\\n");
    sb.append("        }                                             \\\n");
    sb.append("                                                      \\\n");
    sb.append("        else {                                        \\\n");
    sb.append("            /*LVALUE:null*/                           \\\n");
    sb.append("            /*RVALUE:null*/                           \\\n");
    sb.append("        }                                             \\\n");
    sb.append("    }                                                 \\\n");
    sb.append("                                                      \\\n");
    sb.append("    else {                                            \\\n");
    sb.append("        if (RVALUE != NULL) {                         \\\n");
    sb.append("            /*LVALUE:object*/                         \\\n");
    sb.append("            /*RVALUE:object*/                         \\\n");
    sb.append("                                                      \\\n");
    sb.append("            if((LVALUE) != (RVALUE)) {                \\\n");
    sb.append("                /*1:UNREF*/                           \\\n");
    sb.append("                LVALUE = OBJNAME##_deinit(LVALUE);    \\\n");
    sb.append("                                                      \\\n");
    sb.append("                /*2:REF, ASSIGN*/                     \\\n");
    sb.append("                OBJNAME##_ref(RVALUE);                \\\n");
    sb.append("                LVALUE = RVALUE;                      \\\n");
    sb.append("            }                                         \\\n");
    sb.append("        }                                             \\\n");
    sb.append("                                                      \\\n");
    sb.append("        else {                                        \\\n");
    sb.append("            /*LVALUE:object*/                         \\\n");
    sb.append("            /*RVALUE:null*/                           \\\n");
    sb.append("                                                      \\\n");
    sb.append("            /*1:UNREF*/                               \\\n");
    sb.append("            LVALUE = OBJNAME##_deinit(LVALUE);        \\\n");
    sb.append("                                                      \\\n");
    sb.append("            /*2:ASSIGN*/                              \\\n");
    sb.append("            LVALUE = NULL;                            \\\n");
    sb.append("        }                                             \\\n");
    sb.append("    }                                                 \\\n");
    sb.append("} while (0)                                             \n");
    
    sb.append("\n");
    
    sb.append("#define UNREF_RETURN(OBJNAME)        \\\n");
    sb.append("    if (__this == NULL) {            \\\n");
    sb.append("        return NULL;                 \\\n");
    sb.append("    }                                \\\n");
    sb.append("    __this->ref_count -= 1;          \\\n");
    sb.append("    if (__this->ref_count == 0) {    \\\n");
    sb.append("        OBJNAME##_destroy(__this);   \\\n");
    sb.append("        return NULL;                 \\\n");
    sb.append("    }                                \\\n");
    sb.append("    return __this;                     \n");
    //@formatter:on

    return sb.toString();
  }

}
