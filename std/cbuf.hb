public class cbuf {

  private final arr<char>buffer;
  private final int size;
  private int offset;
  private int line;
  private int column;
  private char prevc;
  private int eofs;

  public cbuf(final str input) {

    this.buffer = new arr<char>();
    this.size = input.length() + 8;
    this.offset = 0;
    this.line = 1;
    this.column = 0;
    this.prevc = '\0';
    this.eofs = -1;
    
    fill_buffer(input);
  }

  void fill_buffer(str input) {
    for (int i = 0; i < input.length(); i += 1) {
      char c = input.get(i);
      buffer.add(c);
    }
    for (int i = 0; i < 8; i += 1) {
      buffer.add('\0');
    }
  }

  public boolean isEof() {
    return eofs >= 8;
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

  public arr<char> peekc3() {

    arr<char> res = new arr<char>();

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    char save_prevc = prevc;
    int save_eofs = eofs;

    res.add(nextc());
    res.add(nextc());
    res.add(nextc());

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return res;
  }

  public char nextc() {

    while (!isEof()) {

      if (eofs > 8) {
        //throw new RuntimeException("Infinite loop handling...");
      }

      if (prevc == '\n') {
        line += 1;
        column = 0;
      }

      if (buffer.get(offset) == '\\') {
        if (buffer.get(offset + 1) == '\r') {
          if (buffer.get(offset + 2) == '\n') {
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
        if (buffer.get(offset + 1) == '\n') {
          offset += 2;
          prevc = '\n';
          continue;
        }
      }

      if (buffer.get(offset) == '\r') {
        if (buffer.get(offset + 1) == '\n') {
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
        return '\0'; 
      }

      char next = buffer.get(offset);
      offset += 1;
      column += 1;
      prevc = next;

      if (next == '\0') {
        eofs += 1;
        return '\0'; 
      }

      return next;
    }

    return '\0';
  }
  
  test "backslash-newline handling" {
    cbuf buf = new cbuf("a\\\nb");
    assert_true(buf.nextc() == 'a');
    assert_true(buf.nextc() == 'b');
  }
  
  test "backslash-newline with comments handling" {
    cbuf buf = new cbuf("/\\\n/a");
    assert_true(buf.nextc() == '/');
    assert_true(buf.nextc() == '/');
    assert_true(buf.nextc() == 'a');
  }

}

