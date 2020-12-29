package njast.main;

import java.io.IOException;

import jscan.Tokenlist;
import njast.ast_nodes.top.CompilationUnit;
import njast.parse.Parse;

public interface ParserMainApi {

  public Tokenlist preprocess() throws IOException;

  public Parse initiateParse() throws IOException;

  public CompilationUnit parseUnit() throws IOException;

}
