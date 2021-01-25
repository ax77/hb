package ast_sourceloc;

import java.io.Serializable;

import hashed.Hash_strings;
import tokenize.Token;

public class SourceLocation implements Serializable {
  private static final long serialVersionUID = -635987520233195844L;

  protected final String filename;
  protected final int line;
  protected final int column;

  public SourceLocation(Token tok) {
    this.filename = Hash_strings.getHashedString(tok.getFilename());
    this.line = tok.getRow();
    this.column = tok.getColumn();
  }

  public SourceLocation(String filename, int line, int column) {
    this.filename = Hash_strings.getHashedString(filename);
    this.line = line;
    this.column = column;
  }

  public SourceLocation(SourceLocation from) {
    this.filename = Hash_strings.getHashedString(from.filename);
    this.line = from.line;
    this.column = from.column;
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
    return filename + ":" + line + ":" + column + " ";
  }

}
