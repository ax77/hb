package ast_main;

import java.io.IOException;

import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import parse.Parse;

public interface ParserMainApi {

  public Parse initiateParse() throws IOException;

  public CompilationUnit parseCompilationUnit() throws IOException;

  public InstantiationUnit parseInstantiationUnit() throws IOException;

}
