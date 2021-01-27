package tokenize;

import static tokenize.Env.HC_FEOF;

public class CBuf {
  private final char buffer[];
  private final int size;
  private int offset;
  private int line;
  private int column;
  private char prevc;
  private int eofs;

  public CBuf(String b) {
    b += "\0\0\0\0\0\0\0\0"; // this some bytes help us... we don't check IOOB sometimes...
    buffer = b.toCharArray();
    size = buffer.length;
    setDefaults();
  }

  private void setDefaults() {
    line = 1;
    column = 0;
    prevc = '\0';
    eofs = -1;
  }

  public char peekc() {

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    char save_prevc = prevc;
    int save_eofs = eofs;

    char c = nextc();

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return c;
  }

  public char[] peekc3() {

    char[] res = new char[3];

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    char save_prevc = prevc;
    int save_eofs = eofs;

    res[0] = nextc();
    res[1] = nextc();
    res[2] = nextc();

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return res;
  }

  public char nextc() {

    // when you build buffer, allocate more space to avoid IOOB check
    // for example: source = { '1', '2', '3', '\0' }, buffer = { '1', '2', '3', '\0', '\0', '\0', '\0', '\0' }

    for (;;) {

      if (eofs > 8) {
        throw new RuntimeException("Infinite loop handling...");
      }

      if (prevc == '\n') {
        line++;
        column = 0;
      }

      if (buffer[offset] == '\\') {
        if (buffer[offset + 1] == '\r') {
          if (buffer[offset + 2] == '\n') {
            // DOS: [\][\r][\n]
            offset += 3;
          } else {
            // OSX: [\][\r]
            offset += 2;
          }

          prevc = '\n';
          continue;
        }

        // UNX: [\][\n]
        if (buffer[offset + 1] == '\n') {
          offset += 2;
          prevc = '\n';
          continue;
        }
      }

      if (buffer[offset] == '\r') {
        if (buffer[offset + 1] == '\n') {
          // DOS: [\r][\n]
          offset += 2;
        } else {
          // OSX: [\r]
          offset += 1;
        }
        prevc = '\n';
        return '\n';
      }

      if (offset == size) {
        eofs++;
        return HC_FEOF; // '\0';
      }

      char next = buffer[offset++];
      column++;
      prevc = next;

      if (next == '\0') {
        eofs++;
        return HC_FEOF; // '\0';
      }

      return next;
    }
  }

  @Override
  public String toString() {
    return String.format("[offset=%d, line=%d, column=%d]", offset, line, column);
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public int getPrevc() {
    return prevc;
  }

}
