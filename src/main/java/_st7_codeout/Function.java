package _st7_codeout;

import java.util.List;

import _st3_linearize_expr.ir.CopierNamer;
import _st4_linearize_stmt.LinearBlock;
import ast_method.ClassMethodDeclaration;
import ast_printers.TypePrinters;
import ast_types.Type;

public class Function {
  private final ClassMethodDeclaration methodSignature;
  private final LinearBlock block;

  public Function(ClassMethodDeclaration methodSignature, LinearBlock block) {
    this.methodSignature = methodSignature;
    this.block = block;
  }

  public ClassMethodDeclaration getMethodSignature() {
    return methodSignature;
  }

  public LinearBlock getBlock() {
    return block;
  }

  public String signToString() {
    StringBuilder sb = new StringBuilder();
    sb.append(methodSignature.getType().toString());
    sb.append(" ");
    sb.append(CopierNamer.getMethodName(methodSignature));
    sb.append(methodSignature.parametersToString());
    return sb.toString();
  }

  public String signToStringCall() {
    StringBuilder sb = new StringBuilder();
    sb.append(CopierNamer.getMethodName(methodSignature));
    sb.append(methodSignature.parametersToString());
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(signToString());
    sb.append(block.toString());
    return sb.toString();
  }

}
