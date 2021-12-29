package _st7_codeout.parts;

import java.util.ArrayList;
import java.util.List;

import _st7_codeout.Function;
import _st7_codeout.ToStringsInternal;
import ast_method.ClassMethodDeclaration;
import ast_symtab.BuiltinNames;
import ast_types.Type;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import tokenize.Ident;

public abstract class GenNativeStdFunction {

  private static StringBuilder sb = new StringBuilder();

  private static void lineBuiltin(String string) {
    sb.append(string);
    sb.append("\n");
  }

  public static String gen(Function func) {
    sb = new StringBuilder();

    final ClassMethodDeclaration method = func.getMethodSignature();
    final Ident name = method.getIdentifier();
    final List<VarDeclarator> params = method.getParameters();

    //
    final String methodType = ToStringsInternal.typeToString(method.getType());
    final String methodCallsHeader = "static " + methodType + " " + ToStringsInternal.signToStringCall(method)
        + ToStringsInternal.parametersToString(method.getParameters()) + " {";

    /// native_open_ident = g("native_open");
    /// native_close_ident = g("native_close");
    /// native_read_ident = g("native_read");
    /// 
    /// native int native_open(*char filename, int mode);
    /// native int native_close(int fd);
    /// native int native_read(int fd, *char buffer, int size);

    if (name.equals(BuiltinNames.native_open_ident)) {
      lineBuiltin(methodCallsHeader);
      lineBuiltin("    assert(filename);");
      lineBuiltin("    assert(filename->buffer);");
      lineBuiltin("    return open(filename->buffer, O_RDONLY);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_close_ident)) {
      lineBuiltin(methodCallsHeader);
      lineBuiltin("    return close(fd);");
      lineBuiltin("}");
    }
    if (name.equals(BuiltinNames.native_read_ident)) {
      lineBuiltin(methodCallsHeader);
      lineBuiltin("    assert(fd != -1);");
      lineBuiltin("    assert(buffer);");
      lineBuiltin("    assert(buffer->data);");
      lineBuiltin("    assert(size > 0);");
      lineBuiltin("    return read(fd, buffer->data, size);");
      lineBuiltin("}");
    }

    ///
    if (name.equals(BuiltinNames.print_ident)) {
      lineBuiltin(methodCallsHeader);

      StringBuilder printfFmtNames = new StringBuilder();
      StringBuilder printfArgNames = new StringBuilder();
      StringBuilder asserts = new StringBuilder();

      for (int i = 0; i < params.size(); i += 1) {
        VarDeclarator param = params.get(i);

        PrintfPair printfPair = printfArgExpand(param);
        printfFmtNames.append(printfPair.getFmt());
        asserts.append(printfPair.assertsToStr());

        printfArgNames.append(printfPair.getArg());
        if (i + 1 < params.size()) {
          printfArgNames.append(", ");
        }
      }

      String quotedFmt = "\"" + printfFmtNames.toString() + "\\n\"";

      if (asserts.length() > 0) {
        lineBuiltin(asserts.toString());
      }

      lineBuiltin("    printf(" + quotedFmt + ", " + printfArgNames.toString() + ");");
      lineBuiltin("}\n");
    }

    return sb.toString();
  }

  static class PrintfPair {
    private final String fmt;
    private final String arg;
    private final List<String> asserts;

    public PrintfPair(String fmt, String arg, List<String> asserts) {
      this.fmt = fmt;
      this.arg = arg;
      this.asserts = asserts;
    }

    public String getFmt() {
      return fmt;
    }

    public String getArg() {
      return arg;
    }

    public String assertsToStr() {
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < asserts.size(); i += 1) {
        String param = asserts.get(i);
        sb.append("    assert(" + param + ");\n");
      }
      return sb.toString();
    }

    @Override
    public String toString() {
      return "printf(\"" + fmt + "\", " + arg + ")";
    }

  }

  private static String fmtStr(Type tp) {
    if (tp.isChar()) {
      return "%c";
    }
    if (tp.isInt() || tp.isShort() || tp.isBoolean() || tp.isLong()) {
      return "%d";
    }
    if (tp.isClass()) {
      if (tp.isCharArray() || tp.isString()) {
        return "%s";
      }
      return "%p";
    }
    if (tp.isFloat() || tp.isDouble()) {
      return "%f";
    }
    throw new AstParseException("unimplemented type to printf: " + ToStringsInternal.typeToString(tp));
  }

  private static PrintfPair printfArgExpand(VarDeclarator param) {
    Type tp = param.getType();

    String fmt = fmtStr(tp);
    String arg = param.getIdentifier().getName();
    List<String> asserts = new ArrayList<>();

    if (tp.isClass()) {
      asserts.add(param.getIdentifier().getName());

      if (tp.isCharArray()) {
        asserts.add(param.getIdentifier().getName() + "->data");
        arg = param.getIdentifier().getName() + "->data";
      }

      if (tp.isString()) {
        asserts.add(param.getIdentifier().getName() + "->buffer");
        arg = param.getIdentifier().getName() + "->buffer";
      }
    }

    return new PrintfPair(fmt, arg, asserts);
  }

}
