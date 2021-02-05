package ast_builtins;

import static ast_builtins.BuiltinNames.BUILTIN_PANIC;
import static ast_builtins.BuiltinNames.BUILTIN_READ_FILE;
import static ast_builtins.BuiltinNames.BUILTIN_WRITE_FILE;

import java.util.List;

import ast_expr.ExprBuiltinFn;
import ast_expr.FuncArg;
import ast_types.Type;
import tokenize.Token;

public abstract class BuiltinFunctionsCreator {

  /// builtin.read_file(path: "main.c") -> [u8]
  ///
  public static final ExprBuiltinFn read_file_fn(Token beginPos, List<FuncArg> arguments) {

    final Type returnType = new Type(/*new ArrayType(TypeBindings.make_u8(beginPos), -1), */beginPos);

    return new ExprBuiltinFn(BUILTIN_READ_FILE, arguments, returnType);
  }

  /// builtin.write_file(path: "./output.txt", content: res);
  ///
  public static final ExprBuiltinFn write_file_fn(Token beginPos, List<FuncArg> arguments) {
    return new ExprBuiltinFn(BUILTIN_WRITE_FILE, arguments, new Type(beginPos));
  }

  /// builtin.panic(message: "something.");
  ///
  public static final ExprBuiltinFn panic_fn(Token beginPos, List<FuncArg> arguments) {
    return new ExprBuiltinFn(BUILTIN_PANIC, arguments, new Type(beginPos));
  }
}
