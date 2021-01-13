package njast.parse;

import jscan.sourceloc.SourceLocation;

public interface ILocation {
  public SourceLocation getLocation();

  public String getLocationToString();
}
