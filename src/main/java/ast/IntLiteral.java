package ast;

import java.io.Serializable;

import ast.parse.NullChecker;
import ast.types.TypeBase;

public class IntLiteral implements Serializable {
  private static final long serialVersionUID = -5869192653078818614L;

  private final String input; // what was given, for debug printing
  private final TypeBase type;
  private final long integer;
  private final double floating;

  public IntLiteral(String input, TypeBase type, long integer) {
    NullChecker.check(input, type);

    this.input = input;
    this.type = type;
    this.integer = integer;
    this.floating = (double) integer;
  }

  public IntLiteral(String input, TypeBase type, double floating) {
    NullChecker.check(input, type);

    this.input = input;
    this.type = type;
    this.integer = (long) floating;
    this.floating = floating;
  }

  public TypeBase getType() {
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

    return "IntLiteral [input=" + input + ", type=" + type + ", integer=" + integer + ", floating=" + floating + "]";
  }

}
