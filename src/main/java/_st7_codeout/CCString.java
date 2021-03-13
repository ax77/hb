package _st7_codeout;

public class CCString {

  public static String genString() {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("struct string                                 \n");
    sb.append("{                                             \n");
    sb.append("    char *buffer;                             \n");
    sb.append("    size_t len;                               \n");
    sb.append("};                                            \n");
    
    sb.append("void string_init(string __this, char *buf);   \n");
    sb.append("void string_deinit(string __this);            \n");
    sb.append("void string_destroy(string __this);           \n");

    sb.append("void string_init(string __this, char *buf)    \n");
    sb.append("{                                             \n");
    sb.append("    assert(__this);                           \n");
    sb.append("    assert(buf);                              \n");
    sb.append("    __this->buffer = hstrdup(buf);            \n");
    sb.append("    __this->len = strlen(buf);                \n");
    sb.append("    __this->buffer[__this->len] = '\\0';      \n");
    sb.append("}                                             \n");
    sb.append("char string_get(string __this, size_t index)  \n");
    sb.append("{                                             \n");
    sb.append("    assert(__this);                           \n");
    sb.append("    assert(index < __this->len);              \n");
    sb.append("    return __this->buffer[index];             \n");
    sb.append("}                                             \n");
    sb.append("void string_deinit(string __this)             \n");
    sb.append("{                                             \n");
    sb.append("}                                             \n");
    sb.append("void string_destroy(string __this)            \n");
    sb.append("{                                             \n");
    sb.append("    assert(__this);                           \n");
    sb.append("    free(__this->buffer);                     \n");
    sb.append("    free(__this);                             \n");
    sb.append("}                                             \n");
    //@formatter:on

    return sb.toString();
  }

}
