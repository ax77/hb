package tokenize;

import java.util.ArrayList;
import java.util.List;

public class CEscaper {

  private final CBuf buffer;

  public CEscaper(CBuf s) {
    this.buffer = s;
  }

  public int[] escape() {

    List<Integer> ints = new ArrayList<Integer>();

    for (;;) {
      int c = buffer.nextc();
      if (c == Env.HC_FEOF) {
        break;
      }
      if (c != '\\') {
        ints.add(c);
        continue;
      }
      int c2 = readesc();
      ints.add(c2);
    }

    int len = ints.size();
    int result[] = new int[len + 1];
    for (int i = 0; i < ints.size(); i++) {
      result[i] = ints.get(i);
    }

    result[len] = '\0';
    return result;
  }

  private int readesc() {
    int c = buffer.nextc();

    if (c == '\'' || c == '\\' || c == '\"' || c == '?') {
      return c;
    }

    if (c == 'x') {
      String hxval = "";
      for (;;) {
        if (!Env.isHex(buffer.peekc())) {
          break;
        }
        hxval += (char) buffer.nextc();
      }
      return (int) Env.hexValue(hxval);
    }

    if (Env.isOct(c)) {
      String octval = "";
      octval += (char) c;
      if (Env.isOct(buffer.peekc())) {
        octval += (char) buffer.nextc();
        if (Env.isOct(buffer.peekc())) {
          octval += (char) buffer.nextc();
        }
      }
      return (int) Env.octValue(octval);
    }

    if (c == 'n') {
      return '\n';
    }
    if (c == 'r') {
      return '\r';
    }
    if (c == 't') {
      return '\t';
    }

    return c;
  }
}

//	#include <stdio.h>
//	#include <string.h>
//
//	int main() {
//		const char s[] = "\"\'\\0\'\x1\x2\1\2\001\002\r\n\"";
//		int i, len;
//		for(i = 0, len = strlen(s); i < len; i++) {
//			printf("%3d : [%3d]\n", i, s[i]);
//		}
//		return 0;
//	}
//
//	/*
//
//	  0 : [ 34]
//	  1 : [ 39]
//	  2 : [ 92]
//	  3 : [ 48]
//	  4 : [ 39]
//	  5 : [  1]
//	  6 : [  2]
//	  7 : [  1]
//	  8 : [  2]
//	  9 : [  1]
//	 10 : [  2]
//	 11 : [ 13]
//	 12 : [ 10]
//	 13 : [ 34]
//
//	*/
