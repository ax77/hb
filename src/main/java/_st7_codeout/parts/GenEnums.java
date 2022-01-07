package _st7_codeout.parts;

import java.util.List;

import _st7_codeout.Ccode;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_vars.VarDeclarator;

public class GenEnums implements Ccode {

  private final StringBuilder proto;
  private final StringBuilder impls;

  public GenEnums(List<ClassDeclaration> enums) {
    this.proto = new StringBuilder();
    this.impls = new StringBuilder();

    for (ClassDeclaration c : enums) {
      proto.append(enumToString(c));
    }
  }

  //xxxxx
  /// struct toktype_enum {
  ///     int index;
  ///     char *name;
  /// };
  /// 
  /// static struct toktype_enum T_STR_VALUE = { .index = 0, .name = "T_STR" };
  /// static struct toktype_enum *T_STR = &T_STR_VALUE;

  private String enumToString(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct ");
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(ToStringsInternal.typeArgumentsToString(c.getTypeParametersT()));
    }

    sb.append("\n{\n");
    sb.append("  int index;\n");
    sb.append("  char *name;\n");
    sb.append("\n};\n");

    final String hdr = ToStringsInternal.classHeaderToString(c);

    int i = 0;
    for (VarDeclarator var : c.getFields()) {
      final String type = ToStringsInternal.typeToString(var.getType());
      final String simpleName = var.getIdentifier().getName();
      final String fullName = c.getIdentifier() + "_" + simpleName;
      final String index = String.format("%d", i);

      //@formatter:off
      sb.append("static struct " + hdr + " " + fullName + "_VALUE = { .index = " + index + ", .name = \"" + simpleName + "\"" + " };\n");
      sb.append("static " + type + " " + fullName + " = &" + fullName + "_VALUE;\n");
      //@formatter:on

      i += 1;
    }

    return sb.toString();
  }

  @Override
  public String getProto() {
    return proto.toString();
  }

  @Override
  public String getImpls() {
    return impls.toString();
  }

}
