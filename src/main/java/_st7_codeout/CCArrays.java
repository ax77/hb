package _st7_codeout;

public abstract class CCArrays {

  public static String genArrayStructImpl(String datatype) {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("struct array_@DATATYPE@ { \n");
    sb.append("    @DATATYPE@ * data;    \n");
    sb.append("    size_t size, alloc;   \n");
    sb.append("};                        \n");
    //@formatter:on

    String src = sb.toString();
    src = src.replace("@DATATYPE@", datatype);

    return src;
  }

  public static String genArrayStructTypedef(String datatype) {

    String src = "typedef struct array_@DATATYPE@ * array_@DATATYPE@; \n";
    src = src.replace("@DATATYPE@", datatype);

    return src;
  }

  /// TODO:CLEAN:CLEAN:CLEAN

  public static String genArrayAddBlock(String datatype, boolean terminated) {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("{ \n");
    sb.append("    assert(__this); \n");
    sb.append("    assert(__this->data); \n");
    //
    sb.append("    if (__this->size >= __this->alloc) { \n");
    if(terminated) {
      sb.append("        __this->alloc += 1; \n");
    }
    sb.append("        __this->alloc *= 2; \n");
    sb.append("        @DATATYPE@ *ndata = get_memory(__this->data, sizeof(@DATATYPE@) * __this->alloc, TD_ARRAY_TABLE);  \n");
    sb.append("        for(size_t i=0; i<__this->size; i+=1) \n{\n");
    sb.append("            ndata[i] = __this->data[i];\n");
    sb.append("        }\n");
    sb.append("        __this->data = ndata; \n");
    sb.append("    } \n");
    sb.append("    __this->data[__this->size++] = e; \n");
    
    if(terminated) {
      sb.append("    __this->data[__this->size] = ((@DATATYPE@) 0); \n");
    }
    
    sb.append("} \n");
    //@formatter:on

    String src = sb.toString();
    src = src.replace("@DATATYPE@", datatype);

    return src;
  }

  public static String genArrayGetBlock(String datatype) {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("{                                 \n");
    sb.append("    assert(__this);               \n");
    sb.append("    assert(__this->data);         \n");
    sb.append("    assert(index < __this->size); \n");
    //
    sb.append("    return __this->data[index];   \n");
    sb.append("}                                 \n");
   //@formatter:on

    String src = sb.toString();
    src = src.replace("@DATATYPE@", datatype);

    return src;
  }

  public static String genArraySetBlock(String datatype) {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("{                                           \n");
    sb.append("    assert(__this);                         \n");
    sb.append("    assert(__this->data);                   \n");
    sb.append("    assert(index < __this->size);           \n");
    //
    sb.append("    @DATATYPE@ old = __this->data[index];   \n");
    sb.append("    __this->data[index] = e;                \n");
    sb.append("    return old;                             \n");
    sb.append("}                                           \n");
   //@formatter:on

    String src = sb.toString();
    src = src.replace("@DATATYPE@", datatype);

    return src;
  }

  public static String genArraySizeBlock(String datatype) {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("{                                           \n");
    sb.append("    assert(__this);                         \n");
    sb.append("    return __this->size;                    \n");
    sb.append("}                                           \n");
   //@formatter:on

    String src = sb.toString();
    src = src.replace("@DATATYPE@", datatype);

    return src;
  }

  public static String genArrayAllocBlock(String datatype) {
    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("{                                                                  \n");
    sb.append("    assert(__this);                                                \n");
    sb.append("    __this->alloc = 2;                                             \n");
    sb.append("    __this->size = 0;                                              \n");
    sb.append("    __this->data = get_memory(sizeof(@DATATYPE@) * __this->alloc, TD_ARRAY_TABLE);    \n");
    sb.append("    for (size_t i = 0; i < __this->alloc; i++) {                   \n");
    sb.append("        __this->data[i] = 0;                                       \n");
    sb.append("    }                                                              \n");
    sb.append("}                                                                  \n");
   //@formatter:on

    String src = sb.toString();
    src = src.replace("@DATATYPE@", datatype);

    return src;
  }

}
