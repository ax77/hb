package _st2_annotate;

import java.util.ArrayList;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.BlockInfo;
import ast_stmt.StmtBlock;
import ast_types.Type;
import ast_vars.VarDeclarator;
import tokenize.Token;

public abstract class BuildDefaultConstructor {

  public static ClassMethodDeclaration build(ClassDeclaration object) {
    final Token beginPos = object.getBeginPos();
    final ArrayList<VarDeclarator> emptyParams = new ArrayList<>();
    final Type voidType = new Type(beginPos);
    final Modifiers emptyMods = new Modifiers();
    final StmtBlock block = new StmtBlock(new BlockInfo());

    //@formatter:off
    final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR
        , emptyMods
        , object
        , object.getIdentifier()
        , emptyParams
        , voidType
        , block
        , beginPos
    );
    //@formatter:on

    constructor.setGeneratedByDefault();
    return constructor;
  }

}
