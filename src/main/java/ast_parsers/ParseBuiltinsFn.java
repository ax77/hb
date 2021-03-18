package ast_parsers;

import java.util.ArrayList;
import java.util.List;

import ast_expr.ExprBuiltinFn;
import ast_expr.ExprExpression;
import ast_symtab.BuiltinNames;
import ast_types.Type;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseBuiltinsFn {
  private final Parse parser;

  public ParseBuiltinsFn(Parse parser) {
    this.parser = parser;
  }

  public ExprExpression parse() {

    // builtin .
    final Token beginPos = parser.checkedMove(BuiltinNames.std_ident);
    parser.checkedMove(T.T_DOT);

    final Ident funcname = parser.getIdent();
    checkIsCorrectBuiltinIdent(funcname);

    if (funcname.equals(BuiltinNames.read_file_ident)) {

      final List<Type> typeArguments = new ArrayList<>();

      final List<ExprExpression> fcallArguments = parseArglist();
      if (fcallArguments.size() != 1) {
        parser.unreachable("expect `one` type-argument for std.read_file");
      }

      Type restype = fcallArguments.get(0).getResultType();
      if (!restype.isClass()) {
        // TODO:
      }

      final ExprBuiltinFn builtinFn = new ExprBuiltinFn(funcname, typeArguments, fcallArguments, restype);
      return new ExprExpression(builtinFn, beginPos);

    }

    if (funcname.equals(BuiltinNames.print_ident) || funcname.equals(BuiltinNames.assert_true_ident)) {
      final List<Type> typeArguments = new ArrayList<>();
      final List<ExprExpression> fcallArguments = parseArglist();
      final Type restype = new Type(beginPos);
      final ExprBuiltinFn builtinFn = new ExprBuiltinFn(funcname, typeArguments, fcallArguments, restype);
      return new ExprExpression(builtinFn, beginPos);
    }

    if (funcname.equals(BuiltinNames.mem_malloc_ident) || funcname.equals(BuiltinNames.mem_get_ident)
        || funcname.equals(BuiltinNames.mem_free_ident) || funcname.equals(BuiltinNames.mem_set_ident)
        || funcname.equals(BuiltinNames.mem_cpy_ident)) {

      final List<Type> typeArguments = new ParseType(parser).getTypeArguments();
      if (typeArguments.isEmpty()) {
        parser.perror("empty type-arguments in std.mem function");
      }

      final List<ExprExpression> fcallArguments = parseArglist();

      Type restype = typeArguments.get(0); ///TODO:pointers
      if (funcname.equals(BuiltinNames.mem_free_ident) || funcname.equals(BuiltinNames.mem_cpy_ident)) {
        restype = new Type(beginPos);
      }

      final ExprBuiltinFn builtinFn = new ExprBuiltinFn(funcname, typeArguments, fcallArguments, restype);
      parser.getCurrentClass(true).registerTypeSetter(builtinFn);
      return new ExprExpression(builtinFn, beginPos);
    }

    parser.perror("unimplemented builtin function: " + funcname.toString());
    return null;
  }

  private void checkIsCorrectBuiltinIdent(Ident funcname) {
    if (!BuiltinNames.isCorrectBuiltinIdent(funcname)) {
      parser.perror("unimplemented builtin function: " + funcname.toString());
    }
  }

  private List<ExprExpression> parseArglist() {
    return new ParseFcallArgs(parser).parse();
  }

}
