package ast_st3_tac;

import java.util.List;

import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import ast_vars.VarDeclarator;
import literals.IntLiteral;
import utils_oth.NullChecker;

public class Quad {
  // type result := op lhs rhs
  //
  // TYP | RES | OPC   | L  | R  | PARAMS(fcall only)              
  //     |     |       |    |    |                  
  // i32 | t0  | *     | t1 | t2 |                 
  // i32 | t0  | store | t1 | t2 |                 
  // i32 | t0  | load  | t1 |    |                 
  // i32 | t0  | .     | t1 | t2 |                 
  // i32 | t0  | ()    | t1 | t2 | ( )                
  // i32 | t0  | ()    | t1 | t2 | (t2,t4,t5)  

  private final QuadOpc base;
  private final ResultName result;
  private final Type type;
  private String op;
  private ResultName lhs;
  private ResultName rhs;
  private List<ResultName> args;

  // 1) a.b = 32
  // 2) a = 32
  // 3) a[b] = 32
  private StoreDst storeDst;
  private ResultName storeSrc;

  // metainfo
  // note: 'self' has no varSym
  private VarDeclarator varSym; // var|field
  private ClassMethodDeclaration methodSym;
  private IntLiteral numberSym;

  public Quad(ResultName result, Type type, StoreDst storeDst, ResultName storeSrc) {
    NullChecker.check(result, type, storeDst, storeSrc);

    this.base = QuadOpc.STORE;
    this.result = result;
    this.type = type;
    this.storeDst = storeDst;
    this.storeSrc = storeSrc;
  }

  // id|number|self
  public Quad(QuadOpc base, ResultName result, Type type, ResultName lhs) {
    NullChecker.check(base, result, type, lhs);

    this.base = base;
    this.result = result;
    this.type = type;
    this.lhs = lhs;
  }

  // binary
  public Quad(QuadOpc base, ResultName result, Type type, String op, ResultName lhs, ResultName rhs) {
    NullChecker.check(base, result, type, op, lhs, rhs);

    this.base = base;
    this.result = result;
    this.type = type;
    this.op = op;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  // array|field
  public Quad(QuadOpc base, ResultName result, Type type, ResultName lhs, ResultName rhs) {
    NullChecker.check(base, result, type, lhs, rhs);

    this.base = base;
    this.result = result;
    this.type = type;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  // call
  public Quad(ResultName result, Type type, ResultName lhs, ResultName rhs, List<ResultName> args) {
    NullChecker.check(result, type, lhs, rhs, args);

    this.base = QuadOpc.FCALL;
    this.result = result;
    this.type = type;
    this.lhs = lhs;
    this.rhs = rhs;
    this.args = args;
  }

  public VarDeclarator getVarSym() {
    return varSym;
  }

  public void setVarSym(VarDeclarator varSym) {
    this.varSym = varSym;
  }

  public ClassMethodDeclaration getMethodSym() {
    return methodSym;
  }

  public void setMethodSym(ClassMethodDeclaration methodSym) {
    this.methodSym = methodSym;
  }

  public IntLiteral getNumberSym() {
    return numberSym;
  }

  public void setNumberSym(IntLiteral numberSym) {
    this.numberSym = numberSym;
  }

  public StoreDst getStoreDst() {
    return storeDst;
  }

  public ResultName getStoreSrc() {
    return storeSrc;
  }

  public ResultName getResult() {
    return result;
  }

  public Type getType() {
    return type;
  }

  public String getOp() {
    return op;
  }

  public ResultName getLhs() {
    return lhs;
  }

  public ResultName getRhs() {
    return rhs;
  }

  public List<ResultName> getArgs() {
    return args;
  }

  public QuadOpc getBase() {
    return base;
  }

  private String argsToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (int i = 0; i < args.size(); i++) {
      sb.append(args.get(i).toString());
      if (i + 1 < args.size()) {
        sb.append(",");
      }
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  public String toString() {

    // store operation
    if (base == QuadOpc.STORE) {
      return storeDst.toString() + " = " + storeSrc.toString();
    }

    final String typeToStr = String.format("%s ", type.toString());
    final String resultToStr = String.format("%s", result.getResult());

    // declarations|constants
    if (base.equals(QuadOpc.ID_DECL) || base.equals(QuadOpc.SELF_DECL) || base.equals(QuadOpc.NUM_DECL)) {
      return typeToStr + resultToStr + " = " + lhs.toString();
    }

    // function-call
    // TODO: distinct functions with or without return
    if (base.equals(QuadOpc.FCALL)) {
      if (type.is_void_stub()) {
        return "/* tp=void, id=" + resultToStr + " */ " + lhs + "." + rhs + argsToString();
      }
      return typeToStr + resultToStr + " = " + lhs + "." + rhs + argsToString();
    }

    if (base == QuadOpc.ARRAY_ACCESS) {
      return typeToStr + resultToStr + " = " + lhs + "[" + rhs + "]";
    }

    // bin
    NullChecker.check(op);
    return typeToStr + resultToStr + " = " + lhs + op + rhs;
  }

}
