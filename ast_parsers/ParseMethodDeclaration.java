package njast.ast_parsers;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ModTypeNameHeader;
import njast.ast_checkers.TypeRecognizer;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.ast_nodes.stmt.StmtBlock;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.symtab.IdentMap;
import njast.types.Type;

public class ParseMethodDeclaration {
  private final Parse parser;

  public ParseMethodDeclaration(Parse parser) {
    this.parser = parser;
  }

  public ClassMethodDeclaration parse() {

    // func name(param: int) -> int {  }

    //1)
    final Token tok = parser.checkedMove(IdentMap.func_ident);
    final SourceLocation location = new SourceLocation(tok);

    //2)
    final Ident ident = parser.getIdent();

    //3)
    final FormalParameterList parameters = new ParseFormalParameterList(parser).parse();

    //4)
    Type type = null;
    if (parser.is(T.T_ARROW)) {
      parser.checkedMove(T.T_ARROW);
      type = new TypeRecognizer(parser).getType();
    } else {
      type = new Type(); // void stub
    }
    if (type == null) {
      parser.perror("type is not recognized for function");
    }

    //5)
    final StmtBlock block = new ParseStatement(parser).parseBlock();

    final ModTypeNameHeader header = new ModTypeNameHeader(new Modifiers(), type, ident, location);
    return new ClassMethodDeclaration(header, parameters, block);
  }

}
