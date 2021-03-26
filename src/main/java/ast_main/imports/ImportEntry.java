package ast_main.imports;

import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;
import utils_fio.FileWrapper;
import utils_oth.Normalizer;

public class ImportEntry {
  private final String original;
  private final String fullname;
  private final Ident classname;

  public ImportEntry(String original) {
    this.original = original;
    this.fullname = getFullFilenameFromImport(original);
    this.classname = getClassNameFromImport(original);

    FileWrapper fw = new FileWrapper(fullname);
    fw.assertIsExists();
    fw.assertIsFile();
  }

  private String getFullFilenameFromImport(String input) {
    if (!input.contains("::")) {
      throw new AstParseException("import name is incorrect");
    }

    final String dir = System.getProperty("user.dir");
    String found = input.substring(0, input.lastIndexOf("::")).trim();

    found = dir + "/" + found.replace('.', '/');
    found = Normalizer.normalize(found);

    return found.trim() + ".hb";
  }

  private Ident getClassNameFromImport(String input) {
    if (!input.contains("::")) {
      throw new AstParseException("import name is incorrect");
    }
    return Hash_ident.getHashedIdent(input.substring(input.lastIndexOf("::") + 2).trim());
  }

  public String getOriginal() {
    return original;
  }

  public String getFullname() {
    return fullname;
  }

  public Ident getClassname() {
    return classname;
  }

  @Override
  public String toString() {
    return original;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((original == null) ? 0 : original.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ImportEntry other = (ImportEntry) obj;
    if (original == null) {
      if (other.original != null)
        return false;
    } else if (!original.equals(other.original))
      return false;
    return true;
  }

}
