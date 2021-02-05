package ast_expr;

import java.util.List;

import ast_types.Type;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.Token;

public abstract class BuiltinFnNames {

  public static final Ident BUILTIN_IDENT = Hash_ident.getHashedIdent("builtin");

  /// builtin.read_file(path: "main.c") -> [u8]
  ///
  public static final Ident BUILTIN_READ_FILE = Hash_ident.getHashedIdent("read_file");

  public static final ExprBuiltinFn read_file_fn(Token beginPos, List<FuncArg> arguments) {

    final Type returnType = new Type(/*new ArrayType(TypeBindings.make_u8(beginPos), -1), */beginPos);

    return new ExprBuiltinFn(BUILTIN_READ_FILE, arguments, returnType);
  }

  /// builtin.write_file(path: "./output.txt", content: res);
  ///
  public static final Ident BUILTIN_WRITE_FILE = Hash_ident.getHashedIdent("write_file");

  public static final ExprBuiltinFn write_file_fn(Token beginPos, List<FuncArg> arguments) {
    return new ExprBuiltinFn(BUILTIN_WRITE_FILE, arguments, new Type(beginPos));
  }

  /// builtin.panic(message: "something.");
  ///
  public static final Ident BUILTIN_PANIC = Hash_ident.getHashedIdent("panic");

  public static final ExprBuiltinFn panic_fn(Token beginPos, List<FuncArg> arguments) {
    return new ExprBuiltinFn(BUILTIN_PANIC, arguments, new Type(beginPos));
  }
}
