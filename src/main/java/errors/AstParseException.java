package errors;

public class AstParseException extends RuntimeException {

  private static final long serialVersionUID = 1282362108596282528L;

  public AstParseException(String msg) {
    super(msg);
  }
}
