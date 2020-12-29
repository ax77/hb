package njast.parse;

import static jscan.tokenize.T.TOKEN_EOF;
import static jscan.tokenize.T.TOKEN_IDENT;
import static jscan.tokenize.T.T_SEMI_COLON;

import java.util.ArrayList;
import java.util.List;

import jscan.Tokenlist;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.ast_nodes.top.TopLevelCompilationUnit;
import njast.ast_nodes.top.TopLevelTypeDeclaration;
import njast.ast_parsers.ParseTypeDeclarationsList;
import njast.errors.EParseErrors;
import njast.errors.EParseException;
import njast.symtab.ScopeLevels;
import njast.symtab.Symtab;

public class Parse {

  // main thing's
  private final Tokenlist tokenlist;
  private Token tok;

  // location, error-handling
  private String lastloc;
  private List<Token> ringBuffer;
  private Token prevtok;

  public Token tok() {
    return tok;
  }

  //////////////////////////////////////////////////////////////////////////////////////

  // a simple and spupid symbol-table, where we'll put all classes we found, to distinct 
  // class-type and simple types like int/char/etc.
  private Symtab<Ident, Boolean> referenceTypes;

  //  Types
  //
  //  <type> ::= <primitive type> | <reference type>
  //
  //  <primitive type> ::=  byte | short | int | long | char | boolean
  //
  //  <class or interface type> ::= <class type> | <interface type>
  //
  //  <class type> ::= <type name>
  //
  //  <interface type> ::= <type name>

  public void pushscope(ScopeLevels level, String name) {
    referenceTypes.pushscope(level, name);
  }

  public void popscope() {
    referenceTypes.popscope();
  }

  public void defineClassName(Ident name) {
    this.referenceTypes.addsym(name, true);
  }

  public boolean isClassName(Ident ident) {
    return referenceTypes.getsym(ident);
  }

  public boolean isClassName() {
    if (IsIdent.isUserDefinedIdentNoKeyword(tok)) {
      return isClassName(tok.getIdent());
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////////////

  public Parse(List<Token> tokens) {
    this.tokenlist = new Tokenlist(tokens);

    initDefaults();
    initScopes();
    move();
  }

  public Parse(Tokenlist tokenlist) {
    this.tokenlist = tokenlist;

    initDefaults();
    initScopes();
    move();
  }

  private void initDefaults() {
    this.ringBuffer = new ArrayList<Token>(0);
    this.lastloc = "";
  }

  private void initScopes() {
    this.referenceTypes = new Symtab<Ident, Boolean>();
  }

  public String getLastLoc() {
    return lastloc;
  }

  public Token getPrevtok() {
    return prevtok;
  }

  public void setPrevtok(Token prevtok) {
    this.prevtok = prevtok;
  }

  public List<Token> getRingBuffer() {
    return ringBuffer;
  }

  public boolean is(T toktype) {
    return tok.getType().equals(toktype);
  }

  public boolean is(Ident ident) {
    return tok.ofType(T.TOKEN_IDENT) && ident.equals(tok.getIdent());
  }

  public T tp() {
    return tok.getType();
  }

  public void move() {

    tok = tokenlist.next();
    if (tok.ofType(T.TOKEN_STREAMBEGIN) || tok.ofType(T.TOKEN_STREAMEND)) {
      tok = tokenlist.next();
    }

    addLoc();
  }

  public Token moveget() {
    Token tok = tok();
    move();
    return tok;
  }

  private void addLoc() {

    if (ringBuffer.size() >= 230) {
      ringBuffer.remove(0);
    }
    ringBuffer.add(tok);

    lastloc = (prevtok == null ? tok.loc() : prevtok.loc());
    prevtok = tok;
  }

  //////////////////////////////////////////////////////////////////////

  public void perror(String m) {

    StringBuilder sb = new StringBuilder();
    sb.append("error: " + m + "\n");
    sb.append("  --> " + lastloc + "\n\n");
    sb.append(RingBuf.ringBufferToStringLines(ringBuffer) + "\n");

    throw new EParseException(sb.toString());
  }

  public void pwarning(String m) {

    StringBuilder sb = new StringBuilder();
    sb.append("warning: " + m + "\n");
    sb.append("  --> " + lastloc + "\n\n");
    sb.append(RingBuf.ringBufferToStringLines(ringBuffer) + "\n");

  }

  public void perror(EParseErrors code) {
    perror(code.toString());
  }

  public Token checkedMove(Ident expect) {
    if (!tok.isIdent(expect)) {
      perror("expect id: " + expect.getName() + ", but was: " + tok.getValue());
    }
    Token saved = tok();
    move();
    return saved;
  }

  public Ident getIdent() {
    if (!tok.ofType(TOKEN_IDENT)) {
      perror("expect ident, but was: " + tok.getValue());
    }
    Token saved = tok;
    move();
    final Ident ident = saved.getIdent();
    if (ident.isBuiltin()) {
      perror("unexpected builtin ident: " + ident.getName());
    }
    return ident;
  }

  public Token checkedMove(T expect) {
    if (tp() != expect) {
      perror("expect: " + expect.toString() + ", but was: " + tok.getValue());
    }
    Token saved = tok;
    move();
    return saved;
  }

  public boolean moveOptional(T t) {
    if ((tp() == t)) {
      move();
      return true;
    }
    return false;
  }

  public void unexpectedEof() {
    if (tok.ofType(TOKEN_EOF)) {
      perror("EOF unexpected at this context");
    }
  }

  public void unimplemented(String what) {
    perror("unimplemented: " + what);
  }

  public void unreachable(String what) {
    perror("unreachable: " + what);
  }

  public Token peek() {
    return tokenlist.peek();
  }

  public Token lparen() {
    return checkedMove(T.T_LEFT_PAREN);
  }

  public Token rparen() {
    return checkedMove(T.T_RIGHT_PAREN);
  }

  public Token lbracket() {
    return checkedMove(T.T_LEFT_BRACKET);
  }

  public Token lbrace() {
    return checkedMove(T.T_LEFT_BRACE);
  }

  public Token rbrace() {
    return checkedMove(T.T_RIGHT_BRACE);
  }

  public Token rbracket() {
    return checkedMove(T.T_RIGHT_BRACKET);
  }

  public Token semicolon() {
    return checkedMove(T_SEMI_COLON);
  }

  //////////////////////////////////////////////////////////////////////

  public boolean isEof() {
    return tok.ofType(T.TOKEN_EOF);
  }

  public Tokenlist getTokenlist() {
    return tokenlist;
  }

  public void restoreState(ParseState parseState) {
    this.tokenlist.setOffset(parseState.getTokenlistOffset());
    this.tok = parseState.getTok();
    this.ringBuffer = new ArrayList<Token>(parseState.getRingBuffer());
    this.lastloc = parseState.getLastloc();
    this.prevtok = parseState.getPrevtok();
  }

  ///////////////////////////////////////////////////////////////////
  // ENTRY

  private void moveStraySemicolon() {
    while (tp() == T.T_SEMI_COLON) {
      move();
    }
  }

  public TopLevelCompilationUnit parse() {
    TopLevelCompilationUnit tu = new TopLevelCompilationUnit();
    pushscope(ScopeLevels.FILE_SCOPE, "unit");

    // top-level
    moveStraySemicolon();

    while (!tok.ofType(TOKEN_EOF)) {

      // before each function or global declaration
      moveStraySemicolon();

      TopLevelTypeDeclaration ed = new ParseTypeDeclarationsList(this).parse();
      tu.put(ed);
    }

    popscope();
    return tu;
  }

}
