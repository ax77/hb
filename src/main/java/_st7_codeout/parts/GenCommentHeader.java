package _st7_codeout.parts;

public abstract class GenCommentHeader {

  public static String gen(String message) {
    String hdr = "\n//////////////////////////////////////////////////////////////////////\n";
    return hdr + "/// " + message + hdr + "\n";
  }

}
