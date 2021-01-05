package njast.ast_parsers;

import njast.ast_checkers.TypeRecognizer;
import njast.parse.Parse;
import njast.types.Type;

public class ParseType {
  private final Parse parser;

  public ParseType(Parse parser) {
    this.parser = parser;
  }

  public Type parse() {
    TypeRecognizer typeRecognizer = new TypeRecognizer(parser);
    return typeRecognizer.getType();
  }

}
