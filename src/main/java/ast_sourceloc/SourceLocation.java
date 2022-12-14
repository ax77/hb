package ast_sourceloc;

import java.io.Serializable;

import hashed.Hash_strings;

public class SourceLocation implements Serializable {
  private static final long serialVersionUID = -635987520233195844L;

  private final String filename;
  private final int line;
  private final int column;

  public SourceLocation(String filename, int line, int column) {
    this.filename = Hash_strings.getHashedString(filename);
    this.line = line;
    this.column = column;
  }

  public String getFilename() {
    return filename;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public String toString() {
    final String fnameSimple = filename.substring(filename.lastIndexOf('/') + 1);
    return fnameSimple + ":" + String.format("%d", line); // + ":" + String.format("%d", column) + ":";
  }

}
