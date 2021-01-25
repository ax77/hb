package ast.parse.main;

import ast.ast.nodes.ClassDeclaration;
import ast.parse.Parse;
import ast.parse.ParseState;
import ast.symtab.IdentMap;

public class BindAllClassTypes {

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
