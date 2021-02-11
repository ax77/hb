package ast_parsers;

import static ast_builtins.BuiltinNames.panic_ident;
import static ast_builtins.BuiltinNames.write_file_ident;

import java.util.ArrayList;
import java.util.List;

import ast_builtins.BuiltinNames;
import ast_class.ClassDeclaration;
import ast_expr.ExprBuiltinFn;
import ast_expr.ExprExpression;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBase;
import ast_types.TypeBindings;
import ast_types.TypeBuiltinArray;
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

      /// builtin.read_file(path: "main.c") -> [u8]
      ///

      final List<ExprExpression> fcallArguments = parseArglist();

      final List<Type> typeArguments = new ArrayList<>();
      typeArguments.add(TypeBindings.make_char(beginPos));

      final ClassDeclaration arrayListClass = parser.getClassType(BuiltinNames.ArrayList_ident);
      final Type restype = new Type(new ClassTypeRef(arrayListClass, typeArguments), beginPos);

      final ExprBuiltinFn builtinFn = new ExprBuiltinFn(funcname, new ArrayList<>(), fcallArguments, restype);
      return new ExprExpression(builtinFn, beginPos);
    }

    if (funcname.equals(BuiltinNames.write_file_ident)) {

      /// builtin.write_file(path: "./output.txt", content: res);
      ///

      final List<ExprExpression> arguments = parseArglist();
      final ExprBuiltinFn builtinFn = new ExprBuiltinFn(write_file_ident, new ArrayList<>(), arguments,
          new Type(beginPos));
      return new ExprExpression(builtinFn, beginPos);
    }

    /// error - handling

    if (funcname.equals(BuiltinNames.panic_ident)) {

      /// builtin.panic(message: "something.");
      ///

      final List<ExprExpression> arguments = parseArglist();
      final ExprBuiltinFn builtinFn = new ExprBuiltinFn(panic_ident, new ArrayList<>(), arguments, new Type(beginPos));
      return new ExprExpression(builtinFn, beginPos);
    }

    /// array

    if (funcname.equals(BuiltinNames.array_add_ident) || funcname.equals(BuiltinNames.array_size_ident)
        || funcname.equals(BuiltinNames.array_get_ident) || funcname.equals(BuiltinNames.array_set_ident)
        || funcname.equals(BuiltinNames.array_allocate_ident)) {

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
      }

      else if (funcname.equals(BuiltinNames.array_size_ident)) {
        restype = new Type(TypeBase.TP_int, beginPos);
      }

      else if (funcname.equals(BuiltinNames.array_get_ident) || funcname.equals(BuiltinNames.array_set_ident)) {
        restype = typeArguments.get(0);
      }

      else if (funcname.equals(BuiltinNames.array_allocate_ident)) {
        restype = new Type(new TypeBuiltinArray(typeArguments.get(0)), beginPos);
      }

      else {
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
