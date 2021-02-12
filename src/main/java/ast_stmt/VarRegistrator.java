package ast_stmt;

import java.util.List;

import ast_vars.VarDeclarator;

/// each block contains the local variables, 
/// for reference-counting routine.
/// and each return-statement also contains
/// all the variables from return expression, if any.
///
public interface VarRegistrator {
  void registerVariable(VarDeclarator var);

  List<VarDeclarator> getRelatedVariables();
}
