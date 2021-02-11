package ast_parsers;

import java.util.List;

import ast_builtins.BuiltinFunctionsCreator;
import ast_builtins.BuiltinNames;
import ast_expr.ExprBuiltinFn;
import ast_expr.ExprExpression;
import ast_types.Type;
import ast_types.TypeBase;
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
    final Token beginPos = parser.checkedMove(BuiltinNames.builtin_ident);
    parser.checkedMove(T.T_DOT);

    final Ident funcname = parser.getIdent();
    checkIsCorrectBuiltinIdent(funcname);

    /// IO

    if (funcname.equals(BuiltinNames.read_file_ident)) {
      final List<ExprExpression> arguments = parseArglist();
      return new ExprExpression(BuiltinFunctionsCreator.read_file_fn(beginPos, arguments), beginPos);
    }

    if (funcname.equals(BuiltinNames.write_file_ident)) {
      final List<ExprExpression> arguments = parseArglist();
      return new ExprExpression(BuiltinFunctionsCreator.write_file_fn(beginPos, arguments), beginPos);
    }

    /// error - handling

    if (funcname.equals(BuiltinNames.panic_ident)) {
      final List<ExprExpression> arguments = parseArglist();
      return new ExprExpression(BuiltinFunctionsCreator.panic_fn(beginPos, arguments), beginPos);
    }

    /// array

    if (funcname.equals(BuiltinNames.array_add_ident) || funcname.equals(BuiltinNames.array_size_ident)
        || funcname.equals(BuiltinNames.array_get_ident)) {

      /// builtin.array_get<T>(table, index);
      /// ..................^..^......^
      /// T is a type-argument
      /// table, index - are the call-arguments

      final List<Type> typeArguments = new ParseType(parser).getTypeArguments();
      if (typeArguments.size() != 1) {
        parser.unreachable("expect `one` type-argument for builtin.array");
      }

      final List<ExprExpression> fcallArguments = parseArglist();

      Type restype = null;
      if (funcname.equals(BuiltinNames.array_add_ident)) {
        restype = new Type(beginPos);
      } else if (funcname.equals(BuiltinNames.array_size_ident)) {
        restype = new Type(TypeBase.TP_int, beginPos);
      } else if (funcname.equals(BuiltinNames.array_get_ident)) {
        restype = typeArguments.get(0);
      } else {
        parser.unreachable("expect type for builtin fn");
      }

      final ExprBuiltinFn builtinFn = new ExprBuiltinFn(funcname, typeArguments, fcallArguments, restype);
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
