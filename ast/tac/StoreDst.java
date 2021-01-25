package ast.tac;

public class StoreDst {

  // a.b = 32
  // xxx = 32
  // a[b] = 32

  private final char opc;
  private ResultName obj;
  private ResultName var;

  public StoreDst(char opc, ResultName obj, ResultName var) {
    this.opc = opc;
    this.obj = obj;
    this.var = var;
  }

  public StoreDst(ResultName var) {
    this.opc = 'v';
    this.var = var;
  }

  public char getOpc() {
    return opc;
  }

  public ResultName getObj() {
    return obj;
  }

  public ResultName getVar() {
    return var;
  }

  @Override
  public String toString() {
    if (opc == '.') {
      return obj.toString() + "." + var.toString();
    }
    if (opc == '[') {
      return obj.toString() + "[" + var.toString() + "]";
    }
    return var.toString();
  }

}
