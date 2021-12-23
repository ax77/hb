package ast_main.imports;

import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;
import utils_oth.Normalizer;

public abstract class ImportNamesBuilder {

  public static String getFullFilenameFromImport(String input) {
    if (!input.contains("::")) {
      throw new AstParseException("import name is incorrect: " + input);
    }

    final String dir = System.getProperty("user.dir");
    String found = input.substring(0, input.lastIndexOf("::")).trim();

    found = dir + "/" + found.replace('.', '/');
    found = Normalizer.normalize(found);

    return found.trim() + ".hb";
  }

  public static Ident getClassNameFromImport(String input) {
    if (!input.contains("::")) {
      throw new AstParseException("import name is incorrect");
    }
    return Hash_ident.getHashedIdent(input.substring(input.lastIndexOf("::") + 2).trim());
  }
}
