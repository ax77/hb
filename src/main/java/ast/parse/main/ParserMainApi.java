package ast.parse.main;

import java.io.IOException;

import ast.ast.nodes.unit.CompilationUnit;
import ast.parse.Parse;
import jscan.main.Tokenlist;

public interface ParserMainApi {

  public Tokenlist preprocess() throws IOException;

  public Parse initiateParse() throws IOException;

  public CompilationUnit parseUnit() throws IOException;

}
