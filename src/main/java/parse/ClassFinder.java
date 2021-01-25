package parse;

import ast_class.ClassDeclaration;
import ast_symtab.IdentMap;

public class ClassFinder {

  // TODO: for speed now. rewrite of course :)
  public static void bind(Parse parser) {

    ParseState state = new ParseState(parser);

    while (!parser.isEof()) {
      if (parser.is(IdentMap.class_ident)) {
        parser.moveget(); // class kw
        parser.defineClassName(new ClassDeclaration(parser.getIdent()));
      }
      parser.move();
    }

    parser.restoreState(state);
  }

}
