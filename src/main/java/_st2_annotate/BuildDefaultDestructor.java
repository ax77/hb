package _st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_main.ParserMainOptions;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_symtab.Keywords;
import ast_types.Type;
import ast_vars.VarDeclarator;
import tokenize.Token;

public abstract class BuildDefaultDestructor {

  public static ClassMethodDeclaration build(ClassDeclaration object) {

    final StmtBlock block = new StmtBlock();
    for (StmtStatement s : deinits(object)) {
      block.pushItemBack(s);
    }

    final Token beginPos = object.getBeginPos();
    final ClassMethodDeclaration destructor = new ClassMethodDeclaration(new Modifiers(), object, block, beginPos);

    destructor.setGeneratedByDefault();
    return destructor;

  }

  // TODO: apply destructors for all plain fields.
  // and after that apply destructors for self-referenced fields.

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
        if (type.isEnum()) {
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

    // and here: drop all of the fields
    final StmtBlock emptifiers = BuildDefaultInitializersBlockForAllFields.createEmptifiiers(object);
    rv.add(new StmtStatement(emptifiers, object.getBeginPos()));

    return rv;
  }

  /// static void node_deinit_69_1025(struct node_1025* __this)
  /// {
  ///     if(!is_alive(__this)) {
  ///         return;
  ///     }
  ///     set_deletion_bit(__this);
  ///     
  ///     if(is_alive(__this->item)) {
  ///         str_deinit_23(__this->item);
  ///         set_deletion_bit(__this->item);
  ///     }
  ///     
  ///     if(is_alive(__this->next)) {
  ///         node_deinit_69_1025(__this->next);
  ///         set_deletion_bit(__this->next);
  ///     }
  ///     
  ///     
  ///     if(is_alive(__this->prev)) {
  ///         node_deinit_69_1025(__this->prev);
  ///         set_deletion_bit(__this->prev);
  ///     }
  ///     
  ///     drop_ptr((void**)&__this->item, str_default_empty_ptr);
  ///     drop_ptr((void**)&__this->next, node_1025_default_empty_ptr);
  ///     drop_ptr((void**)&__this->prev, node_1025_default_empty_ptr);
  /// }

  public static StmtStatement guardedDeinitCallForField(ClassDeclaration object, VarDeclarator field) {

    final StmtBlock resultBlock = new StmtBlock();
    final Token beginPos = object.getBeginPos();
    final ExprExpression idExpr = new ExprExpression(new ExprIdent(field.getIdentifier()), beginPos);
    final List<ExprExpression> args = new ArrayList<>();
    final ExprMethodInvocation deinitInvoke = new ExprMethodInvocation(idExpr, Keywords.deinit_ident, args);
    final StmtStatement deinitCallStmt = new StmtStatement(new ExprExpression(deinitInvoke, beginPos), beginPos);

    final ExprFieldAccess thisDotField = new ExprFieldAccess(new ExprExpression(object, beginPos),
        field.getIdentifier());
    final List<ExprExpression> fnamearg = new ArrayList<>();
    fnamearg.add(new ExprExpression(thisDotField, beginPos));

    // the block of the 'if-statement'
    final StmtBlock trueStatement = new StmtBlock();
    trueStatement.pushItemBack(deinitCallStmt);

    final ExprBuiltinFunc setBit = new ExprBuiltinFunc(Keywords.set_deletion_bit_ident, fnamearg);
    trueStatement.pushItemBack(new StmtStatement(new ExprExpression(setBit, beginPos), beginPos));

    final ExprBuiltinFunc hasBit = new ExprBuiltinFunc(Keywords.is_alive_ident, fnamearg);
    final StmtSelect select = new StmtSelect(new ExprExpression(hasBit, beginPos), trueStatement, null);
    resultBlock.pushItemBack(new StmtStatement(select, beginPos));

    return new StmtStatement(resultBlock, beginPos);
  }

}
