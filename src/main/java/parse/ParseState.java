package parse;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_stmt.StmtBlock;
import tokenize.Token;

public class ParseState {
  private final int tokenlistOffset;
  private final Token tok;
  private final List<Token> ringBuffer;
  private final String lastloc;
  private final Token prevtok;
  private final ClassDeclaration currentClass;
  private final int flags;
  private final StmtBlock currentBlock;

  public ParseState(Parse parser) {
    this.tokenlistOffset = parser.getTokenlist().getOffset();
    this.tok = parser.tok();
    this.ringBuffer = new ArrayList<Token>(parser.getRingBuffer());
    this.lastloc = parser.getLastLoc();
    this.prevtok = parser.getPrevtok();
    this.currentClass = parser.getCurrentClass(false);
    this.flags = parser.getFlags();
    this.currentBlock = parser.getCurrentBlock();
  }

  public StmtBlock getCurrentBlock() {
    return currentBlock;
  }

  public ClassDeclaration getCurrentClass() {
    return currentClass;
  }

  public int getTokenlistOffset() {
    return tokenlistOffset;
  }

  public Token getTok() {
    return tok;
  }

  public List<Token> getRingBuffer() {
    return ringBuffer;
  }

  public String getLastloc() {
    return lastloc;
  }

  public Token getPrevtok() {
    return prevtok;
  }

  public int getFlags() {
    return flags;
  }

}
