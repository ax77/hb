package njast.ast.parsers;

import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast.checkers.TypeRecognizer;
import njast.ast.nodes.ClassDeclaration;
import njast.ast.nodes.method.ClassMethodBase;
import njast.ast.nodes.method.ClassMethodDeclaration;
import njast.ast.nodes.method.MethodParameter;
import njast.ast.nodes.method.MethodSignature;
import njast.ast.nodes.stmt.StmtBlock;
import njast.parse.Parse;
import njast.symtab.IdentMap;
import njast.types.Type;

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