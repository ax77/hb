import tests.imports2.test1::class1;
import std.natives.string::string;
import std.natives.arr::array;
import std.natives.fmt::fmt;
import std.natives.opt::opt;
import std.natives.fd::fd;
import std.natives.fd::fd;
import tests.imports2.main::main_class; // how about that?
import std.cbuf::cbuf;
import std.file::file;
import std.stdio::stdio;

class main_class {
  int main(int argc) {
    array<char> arr = new array<char>();
    string s = "123";
    array<string> args = new array<string>();
    args.add(s);
    fmt.print(args.get(0)); 
    
    opt<string> op = new opt<string>();
    if(op.is_some()) {}
    
    opt<array<class1>> op2 = new opt<array<class1>>();
    
    return 0;
  }
}


















