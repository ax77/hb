package ast_sourceloc;

import tokenize.Token;

public interface ILocation {
  SourceLocation getLocation();

  String getLocationToString();

  Token getBeginPos();
}
