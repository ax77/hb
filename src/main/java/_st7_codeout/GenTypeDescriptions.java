package _st7_codeout;

import java.util.List;

import ast_class.ClassDeclaration;

public abstract class GenTypeDescriptions {

  public static String genTypeDescrsImpl(List<ClassDeclaration> pods) {

    StringBuilder sb = new StringBuilder();

    // extern struct type_descr *TD_ARRAY_TABLE; 
    // struct type_descr *TD_STR = &(struct type_descr ) { .description = "TD_STR", };

    sb.append("struct type_descr *TD_STRING = &(struct type_descr ) { .description = \"string\", };       \n");
    sb.append("struct type_descr *TD_CHAR_PTR = &(struct type_descr ) { .description = \"TD_CHAR_PTR\", };       \n");
    sb.append("struct type_descr *TD_ARRAY = &(struct type_descr ) { .description = \"TD_ARRAY\", };             \n");
    sb.append("struct type_descr *TD_ARRAY_TABLE = &(struct type_descr ) { .description = \"TD_ARRAY_TABLE\", }; \n");

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNativeArray() || c.isNativeString()) {
        continue;
      }

      String tdName = c.typedescName();
      String cName = "\"" + c.getIdentifier().toString() + "\"";

      sb.append("struct type_descr *" + tdName + " = &(struct type_descr ) { .description = " + cName + ", };\n");
    }

    sb.append("\n");
    return sb.toString();
  }

  public static String genTypeDescrsExtern(List<ClassDeclaration> pods) {
    StringBuilder sb = new StringBuilder();

    // extern struct type_descr *TD_ARRAY_TABLE; 

    sb.append("extern struct type_descr *TD_STRING;      \n");
    sb.append("extern struct type_descr *TD_CHAR_PTR;      \n");
    sb.append("extern struct type_descr *TD_ARRAY;         \n");
    sb.append("extern struct type_descr *TD_ARRAY_TABLE;   \n\n");

    for (ClassDeclaration c : pods) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNativeArray() || c.isNativeString()) {
        continue;
      }

      sb.append("extern struct type_descr *" + c.typedescName() + ";\n");
    }

    return sb.toString();
  }

}
