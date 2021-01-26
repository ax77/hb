package tokenize;

public class CStr {

  private final int v[];
  private final int len;

  public CStr(int buffer[]) {
    v = buffer;
    len = v.length;
  }

  public int[] getV() {
    return v;
  }

  public int getLen() {
    return len;
  }

}
