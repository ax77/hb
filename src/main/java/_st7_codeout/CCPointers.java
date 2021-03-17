package _st7_codeout;

public abstract class CCPointers {

  public static String genMemMalloc(String pointer, String fullname) {

    //@formatter:off
    
    StringBuilder sb = new StringBuilder();
    sb.append("@POINTER@ @FUNC_NAME_mem_malloc@ (@POINTER@ stub__, size_t size)   \n");
    sb.append("{                                                                  \n");
    sb.append("  @POINTER@ ptr = (@POINTER@) hmalloc(size);                       \n");
    sb.append("  return ptr;                                                      \n");
    sb.append("}                                                                  \n");

    //@formatter:on

    String src = sb.toString();
    src = src.replace("@POINTER@", pointer);
    src = src.replace("@FUNC_NAME_mem_malloc@", fullname);

    return src;
  }

  public static String genMemGet(String elemtype, String fullname, String pointer) {

    //@formatter:off
    
    StringBuilder sb = new StringBuilder();
    sb.append("@DATATYPE@ @FUNC_NAME_mem_get@ (@POINTER@ raw_data, size_t index) \n");
    sb.append("{                                                                  \n");
    sb.append("  assert(raw_data);                                                \n");
    sb.append("  return raw_data[index];                                          \n");
    sb.append("}                                                                  \n");

    //@formatter:on

    String src = sb.toString();
    src = src.replace("@DATATYPE@", elemtype);
    src = src.replace("@FUNC_NAME_mem_get@", fullname);
    src = src.replace("@POINTER@", pointer);

    return src;
  }

  public static String genMemSet(String elemtype, String fullname, String pointer) {

    //@formatter:off

    StringBuilder sb = new StringBuilder();
    sb.append("@DATATYPE@ @FUNC_NAME_mem_set@ (@POINTER@ raw_data, size_t index, @DATATYPE@ e) \n");
    sb.append("{                                                                                \n");
    sb.append("  assert(raw_data);                                                              \n");
    sb.append("  @DATATYPE@ old = raw_data[index];                                              \n");
    sb.append("  raw_data[index] = e;                                                           \n");
    sb.append("  return old;                                                                    \n");
    sb.append("}                                                                                \n");
    
    //@formatter:on

    String src = sb.toString();
    src = src.replace("@DATATYPE@", elemtype);
    src = src.replace("@FUNC_NAME_mem_set@", fullname);
    src = src.replace("@POINTER@", pointer);

    return src;
  }

}
