package _st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprDefaultValueForType;
import ast_expr.ExprDelete;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprUnary;
import ast_main.ParserMainOptions;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StmtBlock;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_vars.VarDeclarator;
import tokenize.T;
import tokenize.Token;

public abstract class BuildDefaultDestructor {

  public static ClassMethodDeclaration build(ClassDeclaration object) {

    final StmtBlock block = new StmtBlock();
    for (StmtStatement s : deinits(object)) {
      block.pushItemBack(s);
    }

    final Token beginPos = object.getBeginPos();
    final ClassMethodDeclaration destructor = new ClassMethodDeclaration(object, block, beginPos);

    destructor.setGeneratedByDefault();
    return destructor;

  }

  private static List<StmtStatement> deinits(ClassDeclaration object) {
    final List<VarDeclarator> fields = object.getFields();
    final List<StmtStatement> rv = new ArrayList<>();

    if (!fields.isEmpty()) {
      for (int i = fields.size() - 1; i >= 0; i -= 1) {
        final VarDeclarator field = fields.get(i);
        final Type type = field.getType();
        final boolean needIt = type.isClass() || type.isInterface();
        if (!needIt) {
          continue;
        }

        if (type.getClassTypeFromRef().isEqualTo(object)) { // self-referenced struct
          if (ParserMainOptions.WARN_SELF_REFERENCED_DESTRUCTORS) {
            System.out.println("warning: Default destructor-call for self-referenced field! \n"
                + "You have to provide the correct deinitialization for the field, \n"
                + "or it may cause a deep recursion: " + field.getIdentifier() + " " + field.getLocationToString()
                + "\n");
          }
          //continue;
        }

        StmtStatement deinit = guardedDeinitCallForField(object, field);
        rv.add(deinit);

      }
    }

    final Token beginPos = object.getBeginPos();
    final ExprExpression idExpr = new ExprExpression(object, beginPos);

    final Type type = new Type(new ClassTypeRef(object, object.getTypeParametersT()));
    ExprDefaultValueForType defaultValueForType = new ExprDefaultValueForType(type);

    List<ExprExpression> fnamearg = new ArrayList<>();
    fnamearg.add(idExpr);
    fnamearg.add(new ExprExpression(defaultValueForType, beginPos));

    ExprBuiltinFunc setBit = new ExprBuiltinFunc(Keywords.set_deletion_bit_ident, fnamearg);
    final StmtStatement item = new StmtStatement(new ExprExpression(setBit, beginPos), beginPos);

    // hide the generated code in {  }
    final StmtBlock guardBlock = new StmtBlock();
    guardBlock.pushItemBack(item);

    rv.add(new StmtStatement(guardBlock, beginPos));

    return rv;
  }

  public static StmtStatement guardedDeinitCallForField(ClassDeclaration object, VarDeclarator field) {
    // if(!has_deletion_bit(field)) {
    //     set_deletion_bit(field);
    // }
    // set_deletion_bit(__this);

    final StmtBlock resultBlock = new StmtBlock();
    final Token beginPos = object.getBeginPos();
    final ExprExpression idExpr = new ExprExpression(new ExprIdent(field.getIdentifier()), beginPos);

    ExprDefaultValueForType defaultValueForType = new ExprDefaultValueForType(field.getType());

    final List<ExprExpression> args = new ArrayList<>();
    final ExprMethodInvocation deinitInvoke = new ExprMethodInvocation(idExpr, Keywords.deinit_ident, args);
    final StmtStatement deinitCallStmt = new StmtStatement(new ExprExpression(deinitInvoke, beginPos), beginPos);

    // 1
    ExprFieldAccess exprFieldAccess = new ExprFieldAccess(new ExprExpression(object, beginPos), field.getIdentifier());
    List<ExprExpression> fnamearg = new ArrayList<>();
    fnamearg.add(new ExprExpression(exprFieldAccess, beginPos));
    fnamearg.add(new ExprExpression(defaultValueForType, beginPos));
    ExprBuiltinFunc hasBit = new ExprBuiltinFunc(Keywords.has_deletion_bit_ident, fnamearg);
    ExprUnary nothas = new ExprUnary(new Token("!", T.T_EXCLAMATION, beginPos.getLocation()),
        new ExprExpression(hasBit, beginPos));

    StmtBlock trueStatement = new StmtBlock();
    StmtSelect select = new StmtSelect(new ExprExpression(nothas, beginPos), trueStatement, null);
    trueStatement.pushItemBack(deinitCallStmt);
    resultBlock.pushItemBack(new StmtStatement(select, beginPos));

    // 2
    ExprBuiltinFunc setBit = new ExprBuiltinFunc(Keywords.set_deletion_bit_ident, fnamearg);
    resultBlock.pushItemBack(new StmtStatement(new ExprExpression(setBit, beginPos), beginPos));

    return new StmtStatement(resultBlock, beginPos);

    //    final List<ExprExpression> args = new ArrayList<>();
    //    final Token beginPos = object.getBeginPos();
    //    final ExprExpression idExpr = new ExprExpression(new ExprIdent(field.getIdentifier()), beginPos);
    //
    //    @SuppressWarnings("unused")
    //    final ExprMethodInvocation deinitInvoke = new ExprMethodInvocation(idExpr, Keywords.deinit_ident, args);
    //
    //    //TODO:TODO:TODO:delete_expr
    //    ExprDelete exprDelete = new ExprDelete(field.getIdentifier());
    //    ExprExpression exptDropPtr = new ExprExpression(exprDelete, beginPos);
    //    //
    //
    //    StmtBlock trueStatement = new StmtBlock();
    //    trueStatement.pushItemBack(new StmtStatement(exptDropPtr, beginPos));
    //    //trueStatement.pushItemBack(new StmtStatement(new ExprExpression(deinitInvoke, beginPos), beginPos));
    //
    //    Token op = new Token("!=", T.T_NE, beginPos.getLocation());
    //    ExprDefaultValueForType defaultValueForType = new ExprDefaultValueForType(field.getType());
    //    ExprBinary binary = new ExprBinary(op, idExpr, new ExprExpression(defaultValueForType, beginPos));
    //
    //    ExprExpression guard = new ExprExpression(binary, beginPos);
    //    StmtSelect select = new StmtSelect(guard, trueStatement, null);
    //
    //    return new StmtStatement(select, beginPos);
  }

}
