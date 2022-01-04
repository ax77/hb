package tokenize;

import java.io.Serializable;

import ast_sourceloc.SourceLocation;
import literals.IntLiteral;
import utils_oth.NullChecker;

public class Token implements Serializable {
  private static final long serialVersionUID = -6400923302892741933L;

  // position flags
  private static final int fnewline = 1 << 0;
  private static final int fleadws = 1 << 1;
  private static final int fatbol = 1 << 2;

  // base
  private int fposition;
  private final String value;
  private final T type;
  private final SourceLocation location;

  // literals
  private Ident ident;
  private IntLiteral numconst;

  // simple copy constructor
  public Token(Token other) {
    NullChecker.check(other.location);

    this.fposition = other.fposition;
    this.value = other.value;
    this.type = other.type;
    this.location = other.location;
    this.ident = other.ident;
    this.numconst = other.numconst;
  }

  // copy constructor with specified value/type
  public Token(Token other, String value, T type) {
    NullChecker.check(other.location);

    this.fposition = other.fposition;
    this.value = value;
    this.type = type;
    this.location = other.location;
    this.ident = other.ident;
    this.numconst = other.numconst;
  }

  // whitespace, newline stubs
  public Token(String value, T type, SourceLocation location) {
    NullChecker.check(location);

    this.value = value;
    this.type = type;
    this.location = location;
  }

  // eof
  public Token() {
    this.value = "";
    this.type = T.TOKEN_EOF;
    this.location = new SourceLocation(this.type.toString(), -1, -1);
  }

  // ident
  public Token(Ident ident, SourceLocation location) {
    NullChecker.check(location);

    this.type = T.TOKEN_IDENT;
    this.value = ident.getName();
    this.location = location;
    this.ident = ident;
  }

  // // char
  // public Token(char charconst, SourceLocation location) {
  //   NullChecker.check(location);
  // 
  //   this.type = T.TOKEN_CHAR;
  //   this.value = String.format("%c", charconst);
  //   this.charconst = charconst;
  //   this.location = location;
  // }
  // 
  // // string
  // public Token(char[] strconst, String repr, SourceLocation location) {
  //   NullChecker.check(location);
  // 
  //   this.type = T.TOKEN_STRING;
  //   this.value = repr;
  //   this.strconst = strconst;
  //   this.location = location;
  // }

  // numerics
  public Token(IntLiteral numconst, SourceLocation location) {
    NullChecker.check(location);

    this.type = T.TOKEN_NUMBER;
    this.value = numconst.getOriginalInput();
    this.numconst = numconst;
    this.location = location;
  }

  // literals
  //

  public IntLiteral getNumconst() {
    return numconst;
  }

  // source - location routine
  //

  public String getLocationToString() {
    return location.toString();
  }

  public String getFilename() {
    return location.getFilename();
  }

  public int getLine() {
    return location.getLine();
  }

  public SourceLocation getLocation() {
    return location;
  }

  public boolean ofType(T _type) {
    return type.equals(_type);
  }

  // preprocessor - flags routine
  //

  public boolean isNewLine() {
    return (fposition & fnewline) == fnewline;
  }

  public void setNewLine(boolean isNewLine) {
    if (isNewLine) {
      fposition |= fnewline;
    } else {
      fposition &= ~fnewline;
    }
  }

  public boolean hasLeadingWhitespace() {
    return (fposition & fleadws) == fleadws;
  }

  public void setLeadingWhitespace(boolean b) {
    if (b) {
      fposition |= fleadws;
    } else {
      fposition &= ~fleadws;
    }
  }

  public boolean isAtBol() {
    return (fposition & fatbol) == fatbol;
  }

  public void setAtBol(boolean isAtBol) {
    if (isAtBol) {
      fposition |= fatbol;
    } else {
      fposition &= ~fatbol;
    }
  }

  public boolean typeIsSpecialStreamMarks() {
    return this.ofType(T.TOKEN_EOF);
  }

  public T getType() {
    return type;
  }

  public Ident getIdent() {
    return ident;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  public boolean isIdent(Ident what) {
    if (!ofType(T.TOKEN_IDENT)) {
      return false;
    }
    return getIdent().equals(what);
  }

  public boolean isBuiltinIdent() {
    return ofType(T.TOKEN_IDENT) && (ident.getNs() != 0);
  }

}
