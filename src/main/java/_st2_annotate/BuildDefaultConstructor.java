package _st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_types.Type;
import ast_vars.VarDeclarator;
import tokenize.Token;

public abstract class BuildDefaultConstructor {

  public static ClassMethodDeclaration build(ClassDeclaration object) {

    final StmtBlock block = BuildDefaultInitializersBlockForAllFields.createEmptifiiers(object);
    final List<VarDeclarator> parameters = new ArrayList<>();

    final Token beginPos = object.getBeginPos();
    final Type returnType = new Type(beginPos); // void

    final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR,
        new Modifiers(), object, object.getIdentifier(), parameters, returnType, block, beginPos);

    constructor.setGeneratedByDefault();
    return constructor;

  }

}
