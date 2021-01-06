package njast.main;

import njast.ast_nodes.clazz.ClassDeclaration;
import njast.parse.Parse;
import njast.parse.ParseState;
import njast.symtab.IdentMap;

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
