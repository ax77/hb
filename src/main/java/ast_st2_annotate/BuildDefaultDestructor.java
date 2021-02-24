package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_stmt.BlockInfo;
import ast_stmt.StmtBlock;
import tokenize.Token;

public abstract class BuildDefaultDestructor {

  public static ClassMethodDeclaration build(ClassDeclaration object) {

    final StmtBlock block = new StmtBlock(new BlockInfo());
    final Token beginPos = object.getBeginPos();

    final ClassMethodDeclaration destructor = new ClassMethodDeclaration(object, block, beginPos);

    destructor.setGeneratedByDefault();
    return destructor;

  }

}
