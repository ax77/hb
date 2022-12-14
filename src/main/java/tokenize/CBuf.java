package tokenize;

import static tokenize.Env.HC_FEOF;

public class CBuf {
  private static final int EOFS_PADDING_BUFLEN = 8;

  private final char buffer[];
  private final int size;
  private int offset;
  private int line;
  private int column;
  private char prevc;
  private int eofs;

  public CBuf(final String input) {

    final StringBuilder sb = new StringBuilder();
    sb.append(input);
    for (int i = 0; i < EOFS_PADDING_BUFLEN; i += 1) {
      sb.append(HC_FEOF);
    }

    this.buffer = sb.toString().toCharArray();
    this.size = buffer.length;
    this.line = 1;
    this.column = 0;
    this.prevc = '\0';
    this.eofs = -1;
  }

  public boolean isEof() {
    return eofs >= EOFS_PADDING_BUFLEN;
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

    while (!isEof()) {

      if (eofs > EOFS_PADDING_BUFLEN) {
        throw new RuntimeException("Infinite loop handling...");
      }

      if (prevc == '\n') {
        line += 1;
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
        eofs += 1;
        return HC_FEOF; // '\0';
      }

      char next = buffer[offset];
      offset += 1;
      column += 1;
      prevc = next;

      if (next == '\0') {
        eofs += 1;
        return HC_FEOF; // '\0';
      }

      return next;
    }

    return HC_FEOF;
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
