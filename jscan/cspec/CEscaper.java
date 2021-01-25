package jscan.cspec;

import static jscan.main.Env.hexValue;
import static jscan.main.Env.isHex;
import static jscan.main.Env.isOct;
import static jscan.main.Env.octValue;

import java.util.ArrayList;
import java.util.List;

import jscan.main.Env;

public class CEscaper {

  private final CBuf buffer;
  private final String location;

  public CEscaper(CBuf s, String location) {
    this.buffer = s;
    this.location = location;
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
        if (!isHex(buffer.peekc())) {
          break;
        }
        hxval += (char) buffer.nextc();
      }
      int hx = (int) hexValue(hxval);
      return hx;
    }

    if (isOct(c)) {
      String octval = "";
      octval += (char) c;
      if (isOct(buffer.peekc())) {
        octval += (char) buffer.nextc();
        if (isOct(buffer.peekc())) {
          octval += (char) buffer.nextc();
        }
      }
      int ov = (int) octValue(octval);
      return ov;
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
    // throw new ScanExc(location + " error : unrecognized escape sequence: " + (char) c);
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
