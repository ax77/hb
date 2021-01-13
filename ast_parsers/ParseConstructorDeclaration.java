package njast.ast_parsers;

import jscan.tokenize.Token;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.ast_nodes.stmt.StmtBlock;
import njast.parse.Parse;
import njast.symtab.IdentMap;

public class ParseConstructorDeclaration {
  private final Parse parser;

  public ParseConstructorDeclaration(Parse parser) {
    this.parser = parser;
  }

  //  init() {}

  public ClassConstructorDeclaration parse() {

    Token tok = parser.checkedMove(IdentMap.init_ident);

    FormalParameterList formalParameterList = new ParseFormalParameterList(parser).parse();

    StmtBlock block = parseBody();

    return new ClassConstructorDeclaration(formalParameterList, block);
  }

  private StmtBlock parseBody() {
    StmtBlock blockStatements = new ParseStatement(parser).parseBlock();
    return blockStatements;
  }

}
