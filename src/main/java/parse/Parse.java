package parse;

import static tokenize.T.TOKEN_EOF;
import static tokenize.T.TOKEN_IDENT;
import static tokenize.T.T_COLON;
import static tokenize.T.T_SEMI_COLON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ast_class.ClassDeclaration;
import ast_parsers.ParseType;
import ast_parsers.ParseTypeDeclarations;
import ast_st2_annotate.Mods;
import ast_unit.CompilationUnit;
import errors.AstParseException;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class Parse {

  // main thing's
  private final Tokenlist tokenlist;
  private Token tok;

  // location, error-handling
  private String lastloc;
  private List<Token> ringBuffer;
  private Token prevtok;

  // symbol table for names, for classes only
  private Map<Ident, ClassDeclaration> referenceTypes;
  private ClassDeclaration currentClass;

  private int flags = 0;

  public Parse(List<Token> tokens) {
    this.tokenlist = new Tokenlist(tokens);
    initParser();
  }

  public Parse(Tokenlist tokenlist) {
    this.tokenlist = tokenlist;
    initParser();
  }

  private void initParser() {
    initDefaults();
    initScopes();
    move();
  }

  private void initDefaults() {
    this.ringBuffer = new ArrayList<Token>(0);
    this.lastloc = "";
  }

  private void initScopes() {
    this.referenceTypes = new HashMap<Ident, ClassDeclaration>();
  }

  public void move() {

    tok = tokenlist.next();
    if (tok.ofType(T.TOKEN_STREAMBEGIN) || tok.ofType(T.TOKEN_STREAMEND)) {
      tok = tokenlist.next();
    }

    addLoc();
  }

  public Token tok() {
    return tok;
  }

  public boolean hasBit(int m) {
    return (flags & m) != 0;
  }

  public void setBit(int f) {
    flags |= f;
  }

  public void clearBit(int f) {
    flags &= ~f;
  }

  public int getFlags() {
    return flags;
  }

  //////////////////////////////////////////////////////////////////////
  // PRE-SYMTAB

  public ClassDeclaration getCurrentClass(boolean shouldBe) {
    if (shouldBe && currentClass == null) {
      perror("current class was not be set");
    }
    return currentClass;
  }

  public void setCurrentClass(ClassDeclaration currentClass) {
    this.currentClass = currentClass;
  }

  public void defineClassName(ClassDeclaration cd) {
    for (Entry<Ident, ClassDeclaration> ent : referenceTypes.entrySet()) {
      if (ent.getKey().equals(cd.getIdentifier())) {
        perror("duplicate: " + cd.getIdentifier());
      }
    }
    this.referenceTypes.put(cd.getIdentifier(), cd);
  }

  public boolean isClassName(Ident ident) {
    final ClassDeclaration sym = referenceTypes.get(ident);
    return sym != null;
  }

  public ClassDeclaration getClassType(Ident ident) {
    if (!isClassName(ident)) {
      perror("class not found: " + ident.getName());
    }
    return referenceTypes.get(ident);
  }

  public boolean isUserDefinedIdentNoKeyword(Token what) {
    return what.ofType(T.TOKEN_IDENT) && !what.isBuiltinIdent();
  }

  public boolean isClassName() {
    if (isUserDefinedIdentNoKeyword(tok)) {
      return isClassName(tok.getIdent());
    }
    return false;
  }

  private boolean isPrimitiveOrReferenceTypeBegin() {
    ParseType typeRecognizer = new ParseType(this);
    return typeRecognizer.isType();
  }

  public boolean isTypeWithOptModifiersBegin() {
    return isPrimitiveOrReferenceTypeBegin() || Mods.isAnyModifier(tok());
  }

  //////////////////////////////////////////////////////////////////////
  // ROUTINE

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

  public Tokenlist getTokenlist() {
    return tokenlist;
  }

  private void addLoc() {

    if (ringBuffer.size() >= 230) {
      ringBuffer.remove(0);
    }
    ringBuffer.add(tok);

    lastloc = (prevtok == null ? tok.getLocationToString() : prevtok.getLocationToString());
    prevtok = tok;
  }

  //////////////////////////////////////////////////////////////////////
  // STATE

  public void perror(String m) {

    StringBuilder sb = new StringBuilder();
    sb.append("error: " + m + "\n");
    sb.append("  --> " + lastloc + "\n\n");
    sb.append(RingBuf.ringBufferToStringLines(ringBuffer) + "\n");

    throw new AstParseException(sb.toString());
  }

  public void pwarning(String m) {

    StringBuilder sb = new StringBuilder();
    sb.append("warning: " + m + "\n");
    sb.append("  --> " + lastloc + "\n\n");
    sb.append(RingBuf.ringBufferToStringLines(ringBuffer) + "\n");

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

  public Token moveget() {
    Token tok = tok();
    move();
    return tok;
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

  public Token colon() {
    return checkedMove(T_COLON);
  }

  public Token lt() {
    return checkedMove(T.T_LT);
  }

  public Token gt() {
    return checkedMove(T.T_GT);
  }

  //////////////////////////////////////////////////////////////////////

  public boolean isEof() {
    return tok.ofType(T.TOKEN_EOF);
  }

  public void restoreState(ParseState state) {
    this.tokenlist.setOffset(state.getTokenlistOffset());
    this.tok = state.getTok();
    this.ringBuffer = new ArrayList<Token>(state.getRingBuffer());
    this.lastloc = state.getLastloc();
    this.prevtok = state.getPrevtok();
    this.currentClass = state.getCurrentClass();
    this.flags = state.getFlags();
  }

  //////////////////////////////////////////////////////////////////////
  // ENTRY

  public void errorCommaExpression() {
    StringBuilder sb = new StringBuilder();
    sb.append("Comma-expression list is deprecated, cause it makes such a mess sometimes -> [a=1, b=a, c=b].");
    sb.append("You may use comma-list only in for-loop -> for(int i=0, j=0; i<10; i+=1, j+=2)");

    if (is(T.T_COMMA)) {
      perror(sb.toString());
    }
  }

  public void errorStraySemicolon() {
    if (is(T.T_SEMI_COLON)) {
      perror("stray semicolons [;] are deprecated by design.");
    }
  }

  public CompilationUnit parse() throws IOException {
    CompilationUnit unit = new CompilationUnit();

    errorStraySemicolon();
    while (!tok.ofType(TOKEN_EOF)) {
      errorStraySemicolon();
      new ParseTypeDeclarations(this).parse(unit);
    }

    return unit;
  }

}
