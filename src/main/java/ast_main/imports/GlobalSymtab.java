package ast_main.imports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ast_class.ClassDeclaration;
import ast_symtab.Keywords;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public abstract class GlobalSymtab {

  // symbol table for names, for classes only
  public final static Map<Ident, ClassDeclaration> referenceTypes = new HashMap<>();

  public static void clear() {
    referenceTypes.clear();
  }

  public static void addImport(Parse parser, String s) throws IOException {
    final Ident found = ImportNamesBuilder.getClassNameFromImport(s);
    if (!isClassName(found)) {
      ClassDeclaration c = new ClassDeclaration(Keywords.class_ident, found, new ArrayList<>(), parser.tok());
      defineClassName(parser, c);
    }
  }

  //////////////////////////////////////////////////////////////////////
  // PRE-SYMTAB

  public static void defineClassName(Parse parser, ClassDeclaration clazz) {
    for (Entry<Ident, ClassDeclaration> ent : referenceTypes.entrySet()) {
      if (ent.getKey().equals(clazz.getIdentifier())) {
        parser.perror("duplicate type-name: " + clazz.getIdentifier() + ", previously defined here: "
            + ent.getValue().getLocationToString());
      }
    }
    referenceTypes.put(clazz.getIdentifier(), clazz);
  }

  public static boolean isClassName(Ident ident) {
    final ClassDeclaration sym = referenceTypes.get(ident);
    if (sym != null) {
      return true;
    }
    return false;
  }

  public static ClassDeclaration getClassType(Parse parser, Ident ident) {
    if (!isClassName(ident)) {
      parser.perror("class not found: " + ident.getName());
    }
    return referenceTypes.get(ident);
  }

  //TODO:STATIC_ACCESS
  public static ClassDeclaration getClassTypeNoErr(Parse parser, Ident ident) {
    return referenceTypes.get(ident);
  }

  public static boolean isUserDefinedIdentNoKeyword(Token what) {
    return what.ofType(T.TOKEN_IDENT) && !what.isBuiltinIdent();
  }

  public static boolean isClassName(Token tok) {
    if (isUserDefinedIdentNoKeyword(tok)) {
      return isClassName(tok.getIdent());
    }
    return false;
  }

}
