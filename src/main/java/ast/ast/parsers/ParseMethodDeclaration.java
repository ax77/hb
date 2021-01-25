package ast.ast.parsers;

import java.util.List;

import ast.ast.checkers.TypeRecognizer;
import ast.ast.nodes.ClassDeclaration;
import ast.ast.nodes.method.ClassMethodBase;
import ast.ast.nodes.method.ClassMethodDeclaration;
import ast.ast.nodes.method.MethodParameter;
import ast.ast.nodes.method.MethodSignature;
import ast.ast.nodes.stmt.StmtBlock;
import ast.parse.Parse;
import ast.symtab.IdentMap;
import ast.types.Type;
import jscan.sourceloc.SourceLocation;
import jscan.tokenize.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;

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
      returnType = new TypeRecognizer(parser).getType();
    }

    //5)
    final StmtBlock block = new ParseStatement(parser).parseBlock();

    final MethodSignature signature = new MethodSignature(ident, parameters);

    return new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, clazz, signature, returnType, block, location);
  }

}
