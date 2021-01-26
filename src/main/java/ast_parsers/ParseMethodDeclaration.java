package ast_parsers;

import java.util.List;

import ast_checkers.TypeRecognizer;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_method.MethodParameter;
import ast_method.MethodSignature;
import ast_sourceloc.SourceLocation;
import ast_stmt.StmtBlock;
import ast_symtab.IdentMap;
import ast_types.Type;
import ast_vars.VarBase;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseMethodDeclaration {
  private final Parse parser;

  public ParseMethodDeclaration(Parse parser) {
    this.parser = parser;
  }

  public ClassMethodDeclaration parse(ClassDeclaration clazz) {

    // func name(param: int) -> int {  }

    //1)
    final Token tok = parser.checkedMove(IdentMap.func_ident);
    final SourceLocation location = new SourceLocation(tok);

    //2)
    final Ident ident = parser.getIdent();

    //3)
    final List<MethodParameter> parameters = new ParseFormalParameterList(parser).parse();

    //4)
    Type returnType = new Type(); // void stub
    if (parser.is(T.T_ARROW)) {
      parser.checkedMove(T.T_ARROW);
      if (parser.is(IdentMap.void_ident)) {
        parser.move(); // void stub
      } else {
        returnType = new TypeRecognizer(parser).getType();
      }
    }

    //5)
    final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);

    final MethodSignature signature = new MethodSignature(ident, parameters);

    return new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, clazz, signature, returnType, block, location);
  }

}
