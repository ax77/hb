package njast.main;

import java.io.IOException;

import jscan.Tokenlist;
import njast.ast_top.TypeDeclarationsList;
import njast.parse.Parse;

public interface ParserMainApi {

  public Tokenlist preprocess() throws IOException;

  public Parse initiateParse() throws IOException;

  public TypeDeclarationsList parseUnit() throws IOException;
  
}
