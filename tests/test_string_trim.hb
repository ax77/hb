

class main {
  int main() {
    string s = " \n \n 123 \t \t \n \r\n  ";
    for(int i = s.length()-1; i >= 0; i -=1) {
      int c = cast(s.get(i): int) & 0xff;
      fmt<char>.print(cast(c : char));
    }
    
    return 0;
  }
}
