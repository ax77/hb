package _st7_codeout.parts;

import java.util.List;

import _st7_codeout.Ccode;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_types.Type;
import ast_vars.VarDeclarator;

public class GenEmpties implements Ccode {

  private final List<ClassDeclaration> classes;
  private final StringBuilder proto;
  private final StringBuilder impls;

  public GenEmpties(List<ClassDeclaration> classes) {
    this.classes = classes;
    this.proto = new StringBuilder();
    this.impls = new StringBuilder();
    gen();
  }

  public static final String DEFAULT_EMPTY_VAL = "_default_empty_val";
  public static final String DEFAULT_EMPTY_PTR = "_default_empty_ptr";
  public static final String DEFAULT_INIT_ALL_EMPTIES_METHOD_CALL_NAME = "__init_empties_static_data__";

  @Override
  public String getProto() {
    return proto.toString();
  }

  @Override
  public String getImpls() {
    return impls.toString();
  }

  private String gen() {

    // static TYPE   NAME_PREFIX1 = 0;
    // static TYPE * NAME_PREFIX2 = &NAME_PREFIX1;
    String[] primitives1 = { "boolean", "char", "short", "int", "long", "float", "double" };
    for (String s : primitives1) {
      proto.append("static " + s + " " + s + DEFAULT_EMPTY_VAL + " = 0;\n");
      proto.append("static " + s + " *" + s + DEFAULT_EMPTY_PTR + " = &" + s + DEFAULT_EMPTY_VAL + ";\n");
    }

    impls.append("static void " + DEFAULT_INIT_ALL_EMPTIES_METHOD_CALL_NAME + "() {\n");

    for (ClassDeclaration c : classes) {
      if (c.isNamespace()) {
        continue;
      }
      if (c.isMainClass()) {
        continue;
      }

      proto.append(genEmptiesProto(c));
      impls.append(genEmptiesImpl(c));
    }

    impls.append("}\n");

    StringBuilder result = new StringBuilder();
    result.append(proto);
    result.append("\n");
    result.append(impls);
    result.append("\n");

    return result.toString();

  }

  private String getVarname(ClassDeclaration c) {
    final String hdr = ToStringsInternal.classHeaderToString(c);
    final String varname = hdr + GenEmpties.DEFAULT_EMPTY_PTR;
    return varname;
  }

  private String getNameForType(Type tp) {
    if (tp.isPrimitive()) {
      return "0";
    }
    if (tp.isClass()) {
      ClassDeclaration cd = tp.getClassTypeFromRef();
      return getVarname(cd);
    }
    return "???";
  }

  private String genEmptiesProto(ClassDeclaration c) {

    /// C99
    /// struct list_node *list_node_empty = &(struct list_node ) { };

    /// pedantic
    /// static struct node_1024 node_1024_empty_struct;
    /// static struct node_1024 *node_1024_empty = &node_1024_empty_struct;

    StringBuilder sb = new StringBuilder();

    final String hdr = ToStringsInternal.classHeaderToString(c);
    final String ptrname = hdr + GenEmpties.DEFAULT_EMPTY_PTR;
    final String strname = hdr + GenEmpties.DEFAULT_EMPTY_VAL;

    sb.append("static struct " + hdr + " " + strname + ";\n");
    sb.append("static struct " + hdr + " *" + ptrname + " = &" + strname + ";\n");

    return sb.toString();
  }

  private String genEmptiesImpl(ClassDeclaration c) {

    /// void list_node_empty_init() {
    ///     list_node_empty->item = 0;
    ///     list_node_empty->next = list_node_empty;
    ///     list_node_empty->prev = list_node_empty;
    /// }

    StringBuilder sb = new StringBuilder();
    final String varname = getVarname(c);

    if (c.isNativeArray()) {
      final Type arrayOf = c.getTypeParametersT().get(0);
      if (arrayOf.isChar()) {
        sb.append("    // INIT: " + varname + "\n");
        sb.append("    " + varname + "->data = char_default_empty_ptr;\n");
        sb.append("    " + varname + "->size = 0;\n");
        sb.append("    " + varname + "->alloc = 0;\n");
      } else {
        sb.append("    // INIT: " + varname + "\n");

        if (arrayOf.isClass()) {
          sb.append("    " + varname + "->data = &" + getNameForType(arrayOf) + ";\n");
        }

        else {
          // an array always holds pointers
          String typePrefix = ToStringsInternal.typeToString(arrayOf);
          sb.append("    " + varname + "->data = " + typePrefix + DEFAULT_EMPTY_PTR + ";\n");
        }

        sb.append("    " + varname + "->size = 0;\n");
        sb.append("    " + varname + "->alloc = 0;\n");
      }
    }

    else {
      sb.append("    // INIT: " + varname + "\n");
      for (VarDeclarator field : c.getFields()) {
        sb.append("    " + varname + "->" + field.getIdentifier() + " = " + getNameForType(field.getType()) + ";\n");
      }
    }

    sb.append("\n");
    return sb.toString();
  }
}
