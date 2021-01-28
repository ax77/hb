package ast_parsers;

import static ast_symtab.IdentMap.do_ident;
import static ast_symtab.IdentMap.else_ident;
import static ast_symtab.IdentMap.for_ident;
import static ast_symtab.IdentMap.if_ident;
import static ast_symtab.IdentMap.return_ident;
import static ast_symtab.IdentMap.while_ident;
import static tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtStatement;
import ast_stmt.Stmt_for;
import ast_stmt.Stmt_if;
import ast_symtab.IdentMap;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseStatement {
  private final Parse parser;

  public ParseStatement(Parse parser) {
    this.parser = parser;
  }

  public StmtBlock parseBlock(VarBase varBase) {

    Token lbrace = parser.checkedMove(T.T_LEFT_BRACE);

    if (parser.is(T.T_RIGHT_BRACE)) {
      Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);
      return new StmtBlock();
    }

    List<StmtBlockItem> bs = new ArrayList<StmtBlockItem>();
    while (parser.tp() != T.T_RIGHT_BRACE) {
      StmtBlockItem oneBlock = parseOneBlock(varBase);
      bs.add(oneBlock);
    }
    StmtBlock block = new StmtBlock(bs);

    Token rbrace = parser.checkedMove(T.T_RIGHT_BRACE);
    return block;
  }

  private StmtBlockItem parseOneBlock(VarBase varBase) {

    //  BlockStatement:
    //    LocalVariableDeclarationStatement
    //    ClassOrInterfaceDeclaration
    //    [Identifier :] Statement
    //
    //  LocalVariableDeclarationStatement:
    //    { VariableModifier } Type VariableDeclarators ;

    if (parser.isTypeWithOptModifiersBegin()) {
      VarDeclarator localVar = new ParseVarDeclarator(parser).parse(varBase);

      ClassDeclaration currentClass = parser.getCurrentClass(true);
      currentClass.registerTypeSetter(localVar);

      return new StmtBlockItem(localVar);
    }

    StmtStatement stmt = parseStatement();
    if (stmt == null) {
      parser.perror("something wrong in a statement");
    }
    return new StmtBlockItem(stmt);
  }

  private ExprExpression e_expression() {
    return new ParseExpression(parser).e_expression();
  }

  private StmtStatement parseStatement() {

    parser.errorStraySemicolon();

    if (parser.is(while_ident) || parser.is(do_ident)) {
      parser.perror("while/do loops are deprecated by design. use for-loop instead.");
    }

    // return ... ;
    // return ;

    if (parser.is(return_ident)) {
      Token from = parser.checkedMove(return_ident);

      if (parser.tp() == T_SEMI_COLON) {
        parser.move();
        return new StmtStatement(StatementBase.SRETURN, null, from);
      }

      ExprExpression expr = e_expression();

      parser.checkedMove(T_SEMI_COLON);
      return new StmtStatement(StatementBase.SRETURN, expr, from);
    }

    // for( ;; )

    if (parser.is(for_ident)) {
      return parseForLoop();
    }

    // if ( expr ) stmt else stmt

    if (parser.is(if_ident)) {
      return parse_if();
    }

    // {  }

    if (parser.is(T.T_LEFT_BRACE)) {
      Token from = parser.tok();
      return new StmtStatement(parseBlock(VarBase.LOCAL_VAR), from);
    }

    // expression-statement by default
    //
    StmtStatement ret = new StmtStatement(StatementBase.SEXPR, e_expression(), parser.tok());
    parser.semicolon();
    return ret;
  }

  private StmtStatement parse_if() {
    Token from = parser.checkedMove(if_ident);

    ExprExpression ifexpr = new ParseExpression(parser).e_expression();
    StmtBlock ifstmt = parseBlock(VarBase.LOCAL_VAR);
    StmtBlock ifelse = null;

    if (parser.is(else_ident)) {
      Token elsekw = parser.checkedMove(else_ident);
      ifelse = parseBlock(VarBase.LOCAL_VAR);
      return new StmtStatement(new Stmt_if(ifexpr, ifstmt, ifelse), from);
    }

    return new StmtStatement(new Stmt_if(ifexpr, ifstmt, ifelse), from);
  }

  private StmtStatement parseForLoop() {

    // for item in list {}

    Token from = parser.checkedMove(for_ident);
    Ident iter = parser.getIdent();

    parser.checkedMove(IdentMap.in_ident);
    Ident collection = parser.getIdent();

    StmtBlock loop = parseBlock(VarBase.LOCAL_VAR);
    return new StmtStatement(new Stmt_for(iter, collection, loop, from), from);
  }

}
