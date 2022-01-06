package _st7_codeout.parts;

import java.util.ArrayList;
import java.util.List;

import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public abstract class GenStructsBodies {

  public static String gen(List<ClassDeclaration> classes) {
    StringBuilder sb = new StringBuilder();

    for (ClassDeclaration c : classes) {
      if (c.isMainClass()) {
        continue;
      }
      if (c.isNamespace()) {
        continue;
      }
      if (c.isNativeArray()) {
        continue;
      }
      if (c.isInterface()) {
        sb.append(interfaceToString(c));
        continue;
      }

      sb.append(classToString(c));
      sb.append("\n");
    }

    return sb.toString();
  }

  //xxxxx
  /// interface
  /// struct markable {
  ///     /// Is an actual pointer to an implementor.
  ///     /// I.e. a real class, which implements this interface,
  ///     /// and 'hidden' under this skin right now.
  ///     ///
  ///     void *object;
  /// 
  ///     /// This is an interface method.
  ///     /// Note: the parameter '__this' is a pointer to an
  ///     /// implementor, and NOT a pointer to this very structure.
  ///     /// Here's a difference you should know.
  ///     /// It's just a pointer to a real method from a real class.
  ///     /// The method from an interface is defined below,
  ///     /// and its signature is different.
  ///     ///
  ///     int (*mark)(void *__this);
  ///     void (*flush)(void *__this, int newvalue);
  /// };
  ///
  /// NOTE: it's quite possible to add a union
  /// with all the structures that implemented this interface.
  /// And then by using a flag choose the right method to invoke.
  /// But: it will be 'if' statement then, and a hidden control flow,
  /// and it's not a good idea.
  /// The second thought: it's to use a _Generic from C11, but:
  /// it works statically, think about this.
  ///
  /// So: we have to use a pointer to void in this case,
  /// while I'm not prepared to implement a better design.
  /// It's not so bad, actually.
  /// It's a first place when we really need a void*.
  ///
  /// int markable_mark(struct markable *__this) {
  ///     assert(__this);
  ///     assert(__this->object);
  ///     assert(__this->mark);
  ///     return __this->mark(__this->object);
  /// }
  /// 
  /// void markable_flush(struct markable *__this, int newvalue) {
  ///     assert(__this);
  ///     assert(__this->object);
  ///     assert(__this->flush);
  ///     __this->flush(__this->object, newvalue);
  /// }

  private static String interfaceToString(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct ");
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(ToStringsInternal.typeArgumentsToString(c.getTypeParametersT()));
    }

    sb.append("\n{\n");
    sb.append("  void *object;\n");

    for (ClassMethodDeclaration method : c.getMethods()) {
      String type = ToStringsInternal.typeToString(method.getType());
      String name = "(*" + method.getIdentifier().getName() + ")";

      final List<VarDeclarator> parameters = new ArrayList<>(method.getParameters()); //XXX: copy, do not modify original!
      if (parameters.isEmpty()) {
        throw new AstParseException("empty parameters for interface: " + c.toString());
      }
      parameters.remove(0); // __this

      String params = ToStringsInternal.parametersToString(parameters);
      if (params.startsWith("(") && params.endsWith(")")) {
        String sub = params.substring(1, params.length() - 1);
        String comma = sub.trim().isEmpty() ? "" : ", ";
        params = "(void *__this" + comma + sub + ")";
      } else {
        throw new AstParseException("empty parameters for interface: " + params);
      }

      sb.append(type + " " + name + params + ";\n");
    }

    sb.append("\n};\n");
    return sb.toString();
  }

  private static String classToString(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append("struct ");
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(ToStringsInternal.typeArgumentsToString(c.getTypeParametersT()));
    }

    sb.append("\n{\n");

    for (VarDeclarator var : c.getFields()) {
      sb.append(ToStringsInternal.varToString(var) + ";\n");
    }

    sb.append("\n};\n");
    return sb.toString();
  }

}
