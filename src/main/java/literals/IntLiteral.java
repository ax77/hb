package literals;

import java.io.Serializable;

import ast_types.Type;
import errors.AstParseException;
import utils_oth.NullChecker;

public class IntLiteral implements Serializable {
  private static final long serialVersionUID = -5869192653078818614L;

  private final String input; // what was given, for debug printing
  private final Type type;
  private final long integer;
  private final double floating;

  public IntLiteral(String input, Type type, long integer) {
    NullChecker.check(input, type);
    checkType(type);

    this.input = input;
    this.type = type;
    this.integer = integer;
    this.floating = (double) integer;
  }

  public IntLiteral(String input, Type type, double floating) {
    NullChecker.check(input, type);
    checkType(type);

    this.input = input;
    this.type = type;
    this.integer = (long) floating;
    this.floating = floating;
  }

  public void checkType(Type type) {
    boolean isOk = type.isInteger() || type.isFloating();
    if (!isOk) {
      throw new AstParseException("not a number: " + type.toString());
    }
  }

  public Type getType() {
    return type;
  }

  public long getInteger() {
    return integer;
  }

  public double getFloating() {
    return floating;
  }

  public String getInput() {
    return input;
  }

  @Override
  public String toString() {
    return input; // TODO:?
  }

}
