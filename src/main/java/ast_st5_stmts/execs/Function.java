package ast_st5_stmts.execs;

import ast_method.ClassMethodDeclaration;
import ast_printers.TypePrinters;
import ast_st3_tac.ir.CopierNamer;

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

    sb.append(methodSignature.getModifiers().toString());
    sb.append(" ");
    sb.append(methodSignature.getType().toString());
    sb.append(" ");
    sb.append(CopierNamer.getMethodName(methodSignature));

    if (methodSignature.isConstructor()) {
      sb.append(TypePrinters.typeArgumentsToString(methodSignature.getClazz().getTypeParametersT()));
    }

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
