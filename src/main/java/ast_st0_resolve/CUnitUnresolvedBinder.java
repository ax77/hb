package ast_st0_resolve;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_st1_templates.TypeSetter;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBase;
import ast_types.TypeUnresolvedId;
import ast_unit.CompilationUnit;
import errors.AstParseException;
import tokenize.Ident;

public abstract class CUnitUnresolvedBinder {

  public static void bind(CompilationUnit tu) {
    List<ClassDeclaration> all = new ArrayList<>();
    all.addAll(tu.getClasses());
    all.addAll(tu.getTemplates());
    proc(all);
  }

  private static void proc(final List<ClassDeclaration> classes) {
    for (ClassDeclaration c : classes) {
      List<TypeSetter> typeSetters = c.getTypeSetters();
      for (TypeSetter ts : typeSetters) {
        final Type tp = ts.getType();
        if (tp.is(TypeBase.TP_UNRESOLVED_ID)) {
          final TypeUnresolvedId unresolved = tp.getUnresolvedId();
          final ClassDeclaration realtype = find(unresolved, classes);
          if (realtype == null) {
            throw new AstParseException("cannot find and bind class: " + unresolved.getTypeName().toString());
          }
          final List<Type> typeArguments = unresolved.getTypeArguments();
          ts.setType(new Type(new ClassTypeRef(realtype, typeArguments), tp.getBeginPos()));
        }
      }
    }
  }

  private static ClassDeclaration find(TypeUnresolvedId unresolved, List<ClassDeclaration> classes) {
    final Ident typeName = unresolved.getTypeName();
    for (ClassDeclaration c : classes) {
      if (typeName.equals(c.getIdentifier())) {
        return c;
      }
    }
    return null;
  }

}
