package jscan.cspec;

import static jscan.main.Env.HC_FEOF;

import java.util.Formatter;

import jscan.main.ScanExc;

public class CBuf {
  private final char buffer[];
  private final int size;
  private int offset;
  private int line;
  private int column;
  private int prevc;
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

  public int peekc() {

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    int save_prevc = prevc;
    int save_eofs = eofs;

    int c = nextc();

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return c;
  }

  public int peekc2() {

    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    int save_prevc = prevc;
    int save_eofs = eofs;

    int c = nextc();
    c = nextc();

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return c;
  }

  public int nextc() {

    // when you build buffer, allocate more space to avoid IOOB check
    // for example: source = { '1', '2', '3', '\0' }, buffer = { '1', '2', '3', '\0', '\0', '\0', '\0', '\0' }

    for (;;) {

      if (eofs > 0) {
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

      int next = buffer[offset++];
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

    StringBuilder sb = new StringBuilder();
    Formatter fm = new Formatter(sb);
    fm.format("[offset=%5d, line=%5d, column=%2d]", offset, line, column);

    return sb.toString();
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public int getPrevc() {
    return prevc;
  }

  public void shiftOffset(int npos) {
    if (npos < 0 || npos >= size) {
      throw new ScanExc("Incorrect offset...");
    }
    offset = npos;
  }

  // new test research
  // see TestBuf.java

  private boolean nextis_internal(String what) {

    final int srclen = what.length();
    final int bound = Integer.min(srclen, size);

    for (int i = 0; i < bound; i++) {
      char expect = what.charAt(i);
      char actual = (char) nextc();
      if (expect != actual) {
        return false;
      }
    }

    return true;
  }

  public boolean nextis(String what) {
    // don't be too smart ;)
    int save_offset = offset;
    int save_line = line;
    int save_column = column;
    int save_prevc = prevc;
    int save_eofs = eofs;

    boolean result = nextis_internal(what);

    offset = save_offset;
    line = save_line;
    column = save_column;
    prevc = save_prevc;
    eofs = save_eofs;

    return result;
  }

  public String nexts(int howMuch) {
    if (howMuch < 0 || howMuch >= size) {
      throw new ScanExc("overflow nexts: you want " + howMuch + " but buffer.size=" + size);
    }

    StringBuilder sb = new StringBuilder();
    while (--howMuch >= 0) {
      sb.append((char) nextc());
    }
    return sb.toString();
  }

}

// OLD stuff...
//

//	private int nx0() {
//		if (eofTwice > 0) {
//			throw new RuntimeException("Infinite loop for stream : " + filename);
//		}
//
//		if (offset == size) {
//			eofTwice++;
//			return Env.HC_FEOF;
//		}
//
//		int c = buffer[offset];
//		if (c == 0) {
//			eofTwice++;
//			return Env.HC_FEOF;
//		}
//
//		offset++;
//		return c;
//	}
//
//	private int nx1() {
//		for (;;) {
//			int c0 = nx0();
//			if (c0 == '\r') {
//				int save = offset;
//				int c1 = nx0();
//				if (c1 == '\n') {
//					offset = save;
//					continue;
//				}
//				offset = save;
//				return '\n';
//			}
//			return c0;
//		}
//	}
//
//	public int nx() {
//		for (;;) {
//			if (prevChar == '\n') {
//				row++;
//
//				if (!bsHandle) {
//					numAtLine = 0;
//					if (!tokenlist.isEmpty()) {
//						Token last = tokenlist.get(tokenlist.size() - 1);
//						last.setNewLine(true);
//					}
//				} else {
//					bsHandle = false;
//				}
//			}
//
//			int nextchar = nx1();
//			if (nextchar == '\\') {
//				int save = offset;
//				if (nx1() == '\n') {
//					prevChar = '\n';
//					bsHandle = true;
//					continue;
//				}
//				prevChar = '\\';
//				offset = save;
//				return '\\';
//			}
//			prevChar = nextchar;
//			return nextchar;
//		}
//	}
