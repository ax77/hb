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
import njast.ast_parsers.ParseTypeDeclarationsList;
import njast.ast_top.CompilationUnit;
import njast.ast_top.TypeDeclaration;
import njast.errors.EParseErrors;
import njast.errors.EParseException;

public class Parse {

  // main thing's
  private final Tokenlist tokenlist;
  private Token tok;

  //  // need for labels, also for binding local variable's
  //  private FunctionDefinition currentFn;
  //
  //  // symbol-tables
  //  private Symtab<Ident, CSymbol> symbols;
  //  private Symtab<Ident, CSymbol> tags;

  // location, error-handling
  private String lastloc;
  private List<Token> ringBuffer;
  private Token prevtok;

  //  public Symtab<Ident, CSymbol> getSymbols() {
  //    return symbols;
  //  }
  //
  //  public Symtab<Ident, CSymbol> getTags() {
  //    return tags;
  //  }
  //
  public Token tok() {
    return tok;
  }
  //
  //  public FunctionDefinition getCurrentFn() {
  //    return currentFn;
  //  }

  //  public void defineSym(Ident key, CSymbol sym) {
  //
  //    //    CSymbol prevsym = symbols.getsymFromCurrentScope(key);
  //    //    if (prevsym != null) {
  //    //      if (prevsym.getBase() == CSymbolBase.SYM_TYPEDEF) {
  //    //        if (!prevsym.getType().isEqualTo(sym.getType())) {
  //    //          perror("redefinition, previous defined here: " + prevsym.getLocationToString());
  //    //        }
  //    //      } else {
  //    //
  //    //        if (sym.isFunction() && prevsym.getType().isEqualTo(sym.getType())) {
  //    //          // TODO: normal prototype logic.
  //    //        } else {
  //    //          perror("redefinition, previous defined here: " + prevsym.getLocationToString());
  //    //        }
  //    //
  //    //      }
  //    //    }
  //    //
  //    //    if (currentFn != null) {
  //    //      currentFn.addLocal(sym);
  //    //    }
  //    //
  //    //    symbols.addsym(key, sym);
  //  }

  //  public void defineTag(Ident key, CSymbol sym) {
  //    tags.addsym(key, sym);
  //  }
  //
  //  public boolean isHasTag(Ident name) {
  //    return getTag(name) != null;
  //  }
  //
  //  public boolean isHasTagInCurrentScope(Ident name) {
  //    return tags.getsymFromCurrentScope(name) != null;
  //  }
  //
  //  public CSymbol getTagFromCurrentScope(Ident name) {
  //    return tags.getsymFromCurrentScope(name);
  //  }
  //
  //  public CSymbol getSym(Ident name) {
  //    return symbols.getsym(name);
  //  }
  //
  //  public CSymbol getTag(Ident name) {
  //    return tags.getsym(name);
  //  }

  //TODO:SEMANTIC
  //
  public void pushscope() {
    //    tags.pushscope();
    //    symbols.pushscope();
  }

  public void popscope() {
    //    tags.popscope();
    //    symbols.popscope();
  }

  //
  // TODO:SEMANTIC

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
    //    this.symbols = new Symtab<Ident, CSymbol>();
    //    this.tags = new Symtab<Ident, CSymbol>();
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

  public CompilationUnit parse() {
    CompilationUnit tu = new CompilationUnit();
    pushscope();

    // top-level
    moveStraySemicolon();

    while (!tok.ofType(TOKEN_EOF)) {

      // before each function or global declaration
      moveStraySemicolon();

      TypeDeclaration ed = new ParseTypeDeclarationsList(this).parse();
      tu.put(ed);
    }

    popscope();
    return tu;
  }

}
