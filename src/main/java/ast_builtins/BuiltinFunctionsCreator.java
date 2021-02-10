package ast_builtins;

import static ast_builtins.BuiltinNames.panic_ident;
import static ast_builtins.BuiltinNames.read_file_ident;
import static ast_builtins.BuiltinNames.write_file_ident;

import java.util.ArrayList;
import java.util.List;

import ast_expr.ExprBuiltinFn;
import ast_expr.ExprExpression;
import ast_types.Type;
import tokenize.Token;

public abstract class BuiltinFunctionsCreator {

  /// builtin.read_file(path: "main.c") -> [u8]
  ///
  public static final ExprBuiltinFn read_file_fn(Token beginPos, List<ExprExpression> arguments) {

    final Type returnType = new Type(/*new ArrayType(TypeBindings.make_u8(beginPos), -1), */beginPos);

    return new ExprBuiltinFn(read_file_ident, new ArrayList<>(), arguments, returnType);
  }

  /// builtin.write_file(path: "./output.txt", content: res);
  ///
  public static final ExprBuiltinFn write_file_fn(Token beginPos, List<ExprExpression> arguments) {
    return new ExprBuiltinFn(write_file_ident, new ArrayList<>(), arguments, new Type(beginPos));
  }

  /// builtin.panic(message: "something.");
  ///
  public static final ExprBuiltinFn panic_fn(Token beginPos, List<ExprExpression> arguments) {
    return new ExprBuiltinFn(panic_ident, new ArrayList<>(), arguments, new Type(beginPos));
  }
}
