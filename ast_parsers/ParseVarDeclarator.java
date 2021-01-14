package njast.ast_parsers;

import java.util.ArrayList;
import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.TypeRecognizer;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.clazz.vars.VarInitializer;
import njast.ast_nodes.expr.ExprExpression;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.types.Type;

public class ParseVarDeclarator {
  private final Parse parser;

  public ParseVarDeclarator(Parse parser) {
    this.parser = parser;
  }

  public VarDeclarator parse(VarBase base) {

    // we don't support comma-initialization like: int a=1, b=a, c=32;
    // it is easy to make a mess in your code with this.

    // var counter: int = 0;
    // let counter: int = 0;

    final Modifiers modifiers = new ParseModifiers(parser).parse();

    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Ident id = tok.getIdent();
    final SourceLocation location = new SourceLocation(tok);

    final Token colon = parser.colon();
    final Type type = new TypeRecognizer(parser).getType();
    final VarDeclarator var = new VarDeclarator(base, type, id, location);

    if (parser.is(T.T_ASSIGN)) {
      Token assignTok = parser.moveget();

      if (type.isArray()) {
        List<VarInitializer> inits = new ArrayList<>();
        readInitializerListInternal(inits, type, 0);
        var.setInitializer(inits);
      }

      else {
        var.setInitializer(parseInitializer());
      }

    }

    parser.semicolon();
    return var;
  }

  private void readInitializerListInternal(List<VarInitializer> inits, Type ty, int off) {

    // check recursion deep, to prevent stack overflow.

    // this condition used between array / struct
    // if only array could be nested, condition would not be necessary

    // 1)
    if (ty.isArray()) {

      parser.checkedMove(T.T_LEFT_BRACKET);
      long arlen = ty.getArray().getCount();

      Type sub = ty.getArray().getArrayOf();
      int elsize = 0; // TODO :) sub.getSize();

      // recursion implement nested loop
      // for array: int x[3][2][2] this loop look like this:
      //
      // for (int i = 0; i < 3; i++) {
      //   for (int j = 0; j < 2; j++) {
      //     for (int k = 0; k < 2; k++) {
      //         ...
      //     }
      //   }
      // }

      for (int count = 0; count < arlen; count++) {

        Token tok = parser.tok();
        if (tok.ofType(T.T_RIGHT_BRACKET)) {
          if (count != arlen) {
            parser.perror("array initializers mismatch count.");
          }
          break;
        }

        int offsetOf = off + elsize * count;
        boolean nestedExpansion = sub.isArray();

        if (!nestedExpansion) {
          ExprExpression expr = new ParseExpression(parser).e_assign();
          inits.add(new VarInitializer(expr, offsetOf));

          parser.moveOptional(T.T_COMMA);
          continue;
        }

        // I) recursive expansion of sub-initializer
        readInitializerListInternal(inits, sub, offsetOf);
        parser.moveOptional(T.T_COMMA);

      }

      warningExcessElements("array");
      parser.checkedMove(T.T_RIGHT_BRACKET);

      if (ty.getArray().getCount() <= 0) {
        //ty.getTpArray().setArrayLen(count);
        //ty.setSize(elsize * count);
      }

    }

    else {
      parser.unreachable("array-initializer-list");
      // Type arraytype = new Type(new ArrayType(ty, 1));
      // readInitializerListInternal(inits, arraytype, off);
    }
  }

  private void warningExcessElements(String where) {
    while (!parser.isEof()) {
      Token tok = parser.tok();
      if (tok.ofType(T.T_RIGHT_BRACKET)) {
        return;
      }
      if (tok.ofType(T.TOKEN_EOF)) {
        parser.perror("unexpected EOF in initializer-list");
      }
      ExprExpression expr = new ParseExpression(parser).e_assign();
      parser.moveOptional(T.T_COMMA);

      parser.perror("excess elements in " + where + " initializer: " + expr);
    }
  }

  private List<VarInitializer> parseInitializer() {
    ExprExpression init = new ParseExpression(parser).e_assign();
    List<VarInitializer> inits = new ArrayList<>();
    inits.add(new VarInitializer(init, 0));
    return inits;
  }

}
