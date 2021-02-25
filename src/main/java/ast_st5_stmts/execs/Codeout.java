package ast_st5_stmts.execs;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_printers.TypePrinters;
import ast_vars.VarDeclarator;

public class Codeout {
  private final List<ClassDeclaration> pods;
  private final List<Function> functions;

  public Codeout() {
    this.pods = new ArrayList<>();
    this.functions = new ArrayList<>();
  }

  public void add(ClassDeclaration e) {
    this.pods.add(e);
  }

  public void add(Function e) {
    this.functions.add(e);
  }

  public List<ClassDeclaration> getPods() {
    return pods;
  }

  public List<Function> getFunctions() {
    return functions;
  }

  public String classToString(ClassDeclaration c) {
    StringBuilder sb = new StringBuilder();
    sb.append(c.getKeyword().toString() + " ");
    sb.append(c.getIdentifier().getName());

    if (!c.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(TypePrinters.typeArgumentsToString(c.getTypeParametersT()));
    }

    sb.append("\n{\n");

    for (VarDeclarator var : c.getFields()) {
      sb.append(var.toString() + "\n");
    }

    sb.append("\n}\n");
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (ClassDeclaration c : pods) {
      if (c.getIdentifier().getName().equals("main_class")) {
        continue;
      }
      sb.append(classToString(c));
    }
    sb.append("class main_class {\n");
    for (Function f : functions) {
      sb.append(f.toString());
    }
    sb.append("\n}\n");
    return sb.toString();
  }

}
