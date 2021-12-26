package ast_parsers;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_modifiers.Modifiers;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseVarDeclarator {
  private final Parse parser;

  public ParseVarDeclarator(Parse parser) {
    this.parser = parser;
  }

  public VarDeclarator parse(VarBase base, Modifiers modifiers, Type type, Ident id, Token beginPos) {

    final VarDeclarator var = new VarDeclarator(base, modifiers, type, id, beginPos);

    if (parser.is(T.T_ASSIGN)) {
      parser.move();
      var.setSimpleInitializer(parseInitializer());

      // if (type.is_array() && parser.is(T.T_LEFT_BRACKET)) {
      // 
      //   if (parser.is(Keywords.new_ident)) {
      //     var.setSimpleInitializer(parseInitializer());
      //   }
      // 
      //   else {
      //     List<VarArrayInitializerItem> inits = new ArrayList<>();
      //     readInitializerListInternal(inits, type, 0);
      //     var.setArrayInitializer(new VarArrayInitializer(inits));
      //   }
      // 
      // }
      // 
      // else {
      //   var.setSimpleInitializer(parseInitializer());
      // }

    }

    parser.semicolon();
    return var;
  }

  private VarDeclarator getLocalVar(Modifiers mods, Type type) {
    final Token pos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident name = pos.getIdent();
    final VarDeclarator var = new VarDeclarator(VarBase.LOCAL_VAR, mods, type, name, pos);

    if (!parser.is(T.T_ASSIGN)) {
      parser.perror("expected initializer in for-loop declaration statement");
    }

    parser.checkedMove(T.T_ASSIGN);
    var.setSimpleInitializer(parseInitializer());

    final ClassDeclaration currentClass = parser.getCurrentClass(true);
    currentClass.registerTypeSetter(var);

    return var;
  }

  public List<VarDeclarator> parseVarDeclaratorListForLoop() {

    final Modifiers mods = new ParseModifiers(parser).parse();
    final Type type = new ParseType(parser).getType();

    final List<VarDeclarator> vars = new ArrayList<VarDeclarator>();
    vars.add(getLocalVar(mods, type));

    while (parser.is(T.T_COMMA)) {
      parser.move();
      vars.add(getLocalVar(mods, type));
    }

    parser.semicolon();
    return vars;
  }

  //  private void readInitializerListInternal(List<VarArrayInitializerItem> inits, Type ty, int off) {
  //
  //    // check recursion deep, to prevent stack overflow.
  //
  //    // this condition used between array / struct
  //    // if only array could be nested, condition would not be necessary
  //
  //    // 1)
  //    if (ty.is_array()) {
  //
  //      parser.checkedMove(T.T_LEFT_BRACKET);
  //      long arlen = ty.getArrayType().getLength();
  //
  //      Type sub = ty.getArrayType().getType();
  //      int elsize = sub.get_size();
  //
  //      // recursion implement nested loop
  //      // for array: int x[3][2][2] this loop look like this:
  //      //
  //      // for (int i = 0; i < 3; i++) {
  //      //   for (int j = 0; j < 2; j++) {
  //      //     for (int k = 0; k < 2; k++) {
  //      //         ...
  //      //     }
  //      //   }
  //      // }
  //
  //      int count = 0;
  //      for (count = 0; count < arlen; count++) {
  //
  //        Token tok = parser.tok();
  //        if (tok.ofType(T.T_RIGHT_BRACKET)) {
  //          if (count != arlen) {
  //            parser.perror("array initializers mismatch count.");
  //          }
  //          break;
  //        }
  //
  //        int offsetOf = off + elsize * count;
  //        boolean nestedExpansion = sub.is_array();
  //
  //        if (!nestedExpansion) {
  //          ExprExpression expr = new ParseExpression(parser).e_assign();
  //          inits.add(new VarArrayInitializerItem(expr, offsetOf));
  //
  //          parser.moveOptional(T.T_COMMA);
  //          continue;
  //        }
  //
  //        // I) recursive expansion of sub-initializer
  //        readInitializerListInternal(inits, sub, offsetOf);
  //        parser.moveOptional(T.T_COMMA);
  //
  //      }
  //
  //      warningExcessElements("array");
  //      parser.checkedMove(T.T_RIGHT_BRACKET);
  //
  //      // TODO: fixed/dynamic
  //      if (ty.getArrayType().getLength() <= 0) {
  //        ty.getArrayType().setLength(count);
  //        ty.set_size(elsize * count);
  //      }
  //
  //    }
  //
  //    else {
  //      parser.unreachable("array-initializer-list");
  //      // Type arraytype = new Type(new ArrayType(ty, 1));
  //      // readInitializerListInternal(inits, arraytype, off);
  //    }
  //  }
  //
  //  private void warningExcessElements(String where) {
  //    while (!parser.isEof()) {
  //      Token tok = parser.tok();
  //      if (tok.ofType(T.T_RIGHT_BRACKET)) {
  //        return;
  //      }
  //      if (tok.ofType(T.TOKEN_EOF)) {
  //        parser.perror("unexpected EOF in initializer-list");
  //      }
  //      ExprExpression expr = new ParseExpression(parser).e_assign();
  //      parser.moveOptional(T.T_COMMA);
  //
  //      parser.perror("excess elements in " + where + " initializer: " + expr);
  //    }
  //  }

  private ExprExpression parseInitializer() {
    return new ParseExpression(parser).e_assign();
  }

}
