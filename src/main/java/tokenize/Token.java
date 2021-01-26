package tokenize;

import static tokenize.Fposition.fatbol;
import static tokenize.Fposition.fleadws;
import static tokenize.Fposition.fnewline;

import java.io.Serializable;

import ast_sourceloc.SourceLocation;
import literals.IntLiteral;

public class Token implements Serializable {
  private static final long serialVersionUID = -6400923302892741933L;

  private int fposition;
  private T type;
  private Ident ident;
  private String value;
  private SourceLocation location;

  private int charconst;
  private int[] strconst;
  private IntLiteral numconst;

  private void fillProperyValues(Token other) {

    // XXX: very important fill __ALL__ properties...
    // don't be so smart here...

    this.fposition = other.fposition;
    this.type = other.type;
    this.ident = other.ident;
    this.value = other.value;
    this.location = other.location;
    this.charconst = other.charconst;
    this.strconst = other.strconst;
    this.numconst = other.numconst;
  }

  public Token() {
    setDefaults();
  }

  public Token(Token src) {
    fillProperyValues(src);
  }

  public Token(boolean eof) {
    setDefaults();
    type = T.TOKEN_EOF;
  }

  private void setDefaults() {
    value = "";
    type = T.TOKEN_ERROR;
  }

  // source - location routine
  //

  public IntLiteral getNumconst() {
    return numconst;
  }

  public void setNumconst(IntLiteral numconst) {
    this.numconst = numconst;
  }

  public int getCharconst() {
    return charconst;
  }

  public void setCharconst(int charconst) {
    this.charconst = charconst;
  }

  public int[] getStrconst() {
    return strconst;
  }

  public void setStrconst(int[] strconst) {
    this.strconst = strconst;
  }

  public String loc() {
    if (location == null) {
      return "<unspec. source-location>"; // TODO:why, and when???
    }
    return location.getFilename() + ":" + location.getLine() + ":" + location.getColumn() + ": ";
  }

  public String getFilename() {
    if (location == null) {
      return "<unspec. source-location>"; // TODO:why, and when???
    }
    return location.getFilename();
  }

  public int getRow() {
    if (location == null) {
      return -1; // TODO:why, and when???
    }
    return location.getLine();
  }

  public int getColumn() {
    if (location == null) {
      return -1; // TODO:why, and when???
    }
    return location.getColumn();
  }

  public SourceLocation getLocation() {
    return location;
  }

  public void setLocation(SourceLocation location) {
    this.location = location;
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
    return this.ofType(T.TOKEN_EOF) || this.ofType(T.TOKEN_STREAMBEGIN) || this.ofType(T.TOKEN_STREAMEND);
  }

  public T getType() {
    return type;
  }

  public void setType(T type) {
    this.type = type;
  }

  public Ident getIdent() {
    return ident;
  }

  public void setIdent(Ident ident) {
    type = T.TOKEN_IDENT;
    value = ident.getName();
    this.ident = ident;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void set(T _type, String _value) {
    type = _type;
    value = _value;
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
