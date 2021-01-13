package njast.ast_parsers;

import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.TypeRecognizer;
import njast.ast_nodes.ModTypeNameHeader;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
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

  public ClassMethodDeclaration parse(ClassDeclaration clazz) {

    // func name(param: int) -> int {  }

    //1)
    final Token tok = parser.checkedMove(IdentMap.func_ident);
    final SourceLocation location = new SourceLocation(tok);

    //2)
    final Ident ident = parser.getIdent();

    //3)
    final List<ModTypeNameHeader> parameters = new ParseFormalParameterList(parser).parse();

    //4)
    Type type = new Type(); // void stub
    if (parser.is(T.T_ARROW)) {
      parser.checkedMove(T.T_ARROW);
      type = new TypeRecognizer(parser).getType();
    }

    //5)
    final StmtBlock block = new ParseStatement(parser).parseBlock();

    final ModTypeNameHeader header = new ModTypeNameHeader(new Modifiers(), type, ident);
    return new ClassMethodDeclaration(clazz, header, parameters, block, location);
  }

}
