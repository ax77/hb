package njast;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.Token;
import njast.ast_checkers.IdentRecognizer;
import njast.ast_kinds.ExpressionBase;
import njast.ast_nodes.FuncArg;
import njast.ast_nodes.clazz.vars.VarBase;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprAssign;
import njast.ast_nodes.expr.ExprBinary;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.expr.ExprFieldAccess;
import njast.ast_nodes.expr.ExprMethodInvocation;
import njast.ast_nodes.expr.ExprIdent;
import njast.errors.EParseException;
import njast.types.Type;

public class DumpExpression {

  class tac {
    private final Type type;
    private final String instr;

    public tac(Type type, String instr) {
      this.type = type;
      this.instr = instr;
    }

    public tac(String instr) {
      this.type = new Type();
      this.instr = instr;
    }

    public Type getType() {
      return type;
    }

    public String getInstr() {
      return instr;
    }

    @Override
    public String toString() {
      if (type.isVoidStub()) {
        return instr;
      }
      return type.toString() + " " + instr;
    }

  }

  // 1) int[] arr;
  // 2) int arr[];
  // both declarations are the same, but the second form is deprecated, because:
  //
  // int[] a, b[]; -> will produce: int a[], b[][]

  // do { stmt; } while(e);
  // while(e) { stmt; }
  // for(; e;) { }

  private final ExprExpression expression;
  private StringBuilder sb = new StringBuilder();

  private static int counter = 0;
  private ArrayList<tac> ops = new ArrayList<>();
  private StringBuilder writerTmp = new StringBuilder();

  private void toops(tac x) {
    ops.add(0, x);
    writerTmp.append(x.toString() + ";\n");
  }

  public DumpExpression(ExprExpression expression) {
    this.expression = expression;
    dump(this.expression);
  }

  private void o(String s) {
    sb.append("  ");
    sb.append(s);
    sb.append("\n");
  }

  public String getSb() {
    return sb.toString() + "\n\n\n" + writerTmp.toString();
  }

  private String t() {
    return String.format("t%d", counter++);
  }

  private void err(String msg) {
    throw new EParseException(msg);
  }

  private void genAddr(ExprExpression e) {
    if (e.is(ExpressionBase.EPRIMARY_IDENT)) {
      final ExprIdent literalIdentifier = e.getIdent();

      final String identStrName = literalIdentifier.getIdentifier().getName();

      o("lea rax, [rbp - var_offset(" + identStrName + ")]");
      o("push rax");

      //!
      //temp=op
      final VarDeclarator var = literalIdentifier.getVariable();
      String instr = identStrName;
      if (var.getBase() == VarBase.CLASS_FIELD) {
        instr = "self->" + identStrName;
      }
      toops(new tac(var.getType(), t() + "=" + instr));
      //?

    }

    else if (e.is(ExpressionBase.EFIELD_ACCESS)) {
      final ExprFieldAccess fieldAccess = e.getFieldAccess();

      genAddr(fieldAccess.getObject());

      //!
      tac addr = ops.remove(0);
      String res[] = addr.getInstr().split("=");
      toops(
          new tac(fieldAccess.getField().getType(), t() + "=" + res[0] + "->" + fieldAccess.getFieldName().getName()));
      //?

      o("pop rax");
      o("add rax, field_offset(" + fieldAccess.getFieldName().getName() + ")");
      o("push rax");

    }

    else if (e.is(ExpressionBase.ESELF)) {
      toops(new tac(e.getResultType(), t() + "=self"));
    }

    else if (e.is(ExpressionBase.EMETHOD_INVOCATION)) {
      final ExprMethodInvocation fcall = e.getMethodInvocation();

      if (fcall.isMethodInvocation()) {
        genAddr(fcall.getObject());
      }

      ExprMethodInvocation methodInvocation = e.getMethodInvocation();

      List<FuncArg> args = methodInvocation.getArguments();
      for (FuncArg arg : args) {
        dump(arg.getExpression());
      }
      List<tac> argsgen = new ArrayList<>();
      for (int i = 0; i < methodInvocation.getArguments().size(); i++) {
        argsgen.add(0, ops.remove(0));
      }
      String callArgs = "";
      int i = 0;
      while (!argsgen.isEmpty()) {
        String s = argsgen.remove(0).getInstr().split("=")[0];
        callArgs += s;
        if (i + 1 < argsgen.size()) {
          callArgs += ",";
        }
        i += 1;
      }

      //!
      tac addr = ops.remove(0);
      String res[] = addr.getInstr().split("=");
      if (methodInvocation.getMethod().isVoid()) {
        toops(new tac(res[0] + "->" + fcall.getMethod().getIdentifier().getName() + "(" + callArgs + ")"));
      } else {
        toops(new tac(fcall.getMethod().getType(),
            t() + "=" + res[0] + "->" + fcall.getMethod().getIdentifier().getName() + "(" + callArgs + ")"));
      }
      //?

    }
  }

  private void load(Type resultType) {
    //!
    tac op = ops.remove(0);
    String res[] = op.getInstr().split("=");
    toops(new tac(resultType, t() + "=" + res[0]));
    //?

    o("// load: " + resultType.toString());

    o("pop rax");
    o("mov rax, [rax]");
    o("push rax\n");
  }

  private void store(Type resultType) {
    //!
    tac rvalue = ops.remove(0);
    tac lvalue = ops.remove(0);
    toops(new tac(lvalue.getInstr().split("=")[1] + "=" + rvalue.getInstr().split("=")[0]));
    //?

    o("pop rdi");
    o("pop rax");
    o("mov [rax], rdi");
    o("push rdi");
  }

  private void dump(ExprExpression e) {

    if (e == null) {
      return;
    }

    ExpressionBase base = e.getBase();

    if (base == ExpressionBase.EBINARY) {

      ExprBinary binary = e.getBinary();
      Token operator = binary.getOperator();

      ExprExpression lhs = binary.getLhs();
      ExprExpression rhs = binary.getRhs();

      dump(lhs);
      dump(rhs);

      o("// " + e.toString());

      o("pop rdi");
      o("pop rax");
      o("add rax, rdi");
      o("push rax");

      //!
      tac rvalue = ops.remove(0);
      tac lvalue = ops.remove(0);
      toops(new tac(lhs.getResultType(),
          t() + "=" + lvalue.getInstr().split("=")[0] + operator.getValue() + rvalue.getInstr().split("=")[0]));
      //?
    }

    else if (base == ExpressionBase.EASSIGN) {
      ExprAssign assing = e.getAssign();
      ExprExpression lhs = assing.getLvalue();
      ExprExpression rhs = assing.getRvalue();

      o("// " + e.toString());

      genAddr(lhs);
      dump(rhs);
      store(e.getResultType());
    }

    else if (base == ExpressionBase.EPRIMARY_IDENT) {
      o("// " + e.toString());

      genAddr(e);
      load(e.getResultType());
    }

    else if (base == ExpressionBase.EMETHOD_INVOCATION) {
      ExprMethodInvocation methodInvocation = e.getMethodInvocation();
      List<FuncArg> args = methodInvocation.getArguments();
      for (FuncArg arg : args) {
        dump(arg.getExpression());
      }

      o("// " + e.toString());
      genAddr(e);
      load(e.getResultType());
    }

    else if (base == ExpressionBase.EFIELD_ACCESS) {
      o("// " + e.toString());

      genAddr(e);
      load(e.getResultType());

    }

    else if (base == ExpressionBase.ESELF) {
      System.out.println("???self");
    }

    else if (base == ExpressionBase.EPRIMARY_NUMBER) {
      final String itoa = String.format("%d", e.getNumber().getClong());
      o("push " + itoa);

      toops(new tac(Type.INT_TYPE, t() + "=" + itoa));
    }

    else {
      throw new EParseException("unimpl. expr.:" + base.toString());
    }

  }

}
