package parse;

import java.io.IOException;

import ast_unit.CompilationUnit;

public interface ParserMainApi {

  public Tokenlist preprocess() throws IOException;

  public Parse initiateParse() throws IOException;

  public CompilationUnit parseUnit() throws IOException;

}
