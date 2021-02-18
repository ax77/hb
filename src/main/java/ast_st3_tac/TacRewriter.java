package ast_st3_tac;

import java.util.ArrayList;
import java.util.List;

import ast_builtins.BuiltinNames;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.vars.Code;
import ast_st3_tac.vars.CodeItem;
import ast_st3_tac.vars.CopierNamer;
import ast_st3_tac.vars.StoreLeaf;
import ast_st3_tac.vars.TempVarAssign;
import ast_st3_tac.vars.store.AllocObject;
import ast_st3_tac.vars.store.Call;
import ast_st3_tac.vars.store.FieldAccess;
import ast_st3_tac.vars.store.Lvalue;
import ast_st3_tac.vars.store.Rvalue;
import ast_st3_tac.vars.store.Var;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;

public class TacRewriter {

  /// the result after rewriting
  private final Code rewrittenResult;

  public TacRewriter(final Code rawResult) {
    this.rewrittenResult = new Code();
    rewrite(rawResult);
  }

  public TacRewriter(VarDeclarator var, Code rawResult) {

    /// strtemp x1 = new strtemp(1); 
    /// ::
    /// int __t13 = 1;
    /// strtemp __t14 = new strtemp();
    /// strtemp_init_0(__t14, __t13);
    /// strtemp x1 = null;
    /// x1 = opAssign(x1, __t14);

    this.rewrittenResult = new Code();
    rewrite(rawResult);
    appendVarL(var);
  }

  public Code getRewrittenResult() {
    return rewrittenResult;
  }

  private void appendVarL(VarDeclarator var) {
    CodeItem last = rewrittenResult.getLast();
    if (last.isVoidCall()) {
      Var lvalue = CopierNamer.copyVarDecl(var);
      putRewrittenVarAssign(lvalue, last.getVoidCall().getArgs().get(0).getVar());
     return;
    }
    if (last.isStore()) {
      Var lvalue = CopierNamer.copyVarDecl(var);
      putRewrittenVarAssign(lvalue, last.getStore().getLvalue().getDstVar());
     return;
    }

    throw new AstParseException("expected void-call for class-creation");
  }

  private void rewrite(final Code rawResult) {
    for (CodeItem item : rawResult.getItems()) {
      if (isConstructorTempVarAssignCall(item)) {
        rewriteAllocObject(item);
        continue;
      }
      if (isTempVarClassAssignToVar(item)) {
        rewriteToAssignOp(item);
        continue;
      }
      if (isStoreFieldRefType(item)) {
        rewriteStoreField(item);
        continue;
      }
      rewrittenResult.appendItemLast(item);
    }
  }

  private boolean isStoreFieldRefType(CodeItem item) {
    if (!item.isStore()) {
      return false;
    }

    final Lvalue lvalue = item.getStore().getLvalue();
    if (!lvalue.isDstField()) {
      return false;
    }

    final Var field = lvalue.getDstField().getField();
    return field.getType().is_class();
  }

  private void rewriteStoreField(CodeItem item) {
    StoreLeaf leaf = item.getStore();
    Lvalue lvalue = leaf.getLvalue();
    Rvalue rvalue = leaf.getRvalue();
    FieldAccess access = lvalue.getDstField();
    Var field = access.getField();

    /// __t29.type = __t31
    ///::
    /// __t29.type = opAssign(__t29.type, __t31)

    ClassMethodDeclaration opAssign = field.getType().getClassTypeFromRef()
        .getPredefinedMethod(BuiltinNames.opAssign_ident);
    final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(opAssign));
    List<Rvalue> args = new ArrayList<>();
    args.add(new Rvalue(lvalue.getDstField()));
    args.add(rvalue);
    final Call call = new Call(opAssign.getType(), fn, args, false);
    StoreLeaf leafN = new StoreLeaf(new Lvalue(access), new Rvalue(call));
    rewrittenResult.appendItemLast(new CodeItem(leafN));
  }

  private void putRewrittenVarAssign(Var lvalue, Var rvalue) {

    //1
    TempVarAssign lhsvar = new TempVarAssign(lvalue, new Rvalue(0));

    //2
    ClassMethodDeclaration opAssign = lvalue.getType().getClassTypeFromRef()
        .getPredefinedMethod(BuiltinNames.opAssign_ident);
    final Ident fn = Hash_ident.getHashedIdent(CopierNamer.getMethodName(opAssign));
    List<Rvalue> args = new ArrayList<>();
    args.add(new Rvalue(lvalue));
    args.add(new Rvalue(rvalue));
    final Call call = new Call(opAssign.getType(), fn, args, false);
    StoreLeaf leaf = new StoreLeaf(new Lvalue(lvalue), new Rvalue(call));

    rewrittenResult.appendItemLast(new CodeItem(lhsvar));
    rewrittenResult.appendItemLast(new CodeItem(leaf));
  }

  private void rewriteToAssignOp(CodeItem item) {

    /// strtemp __t29 = x1
    /// ::
    /// 1) strtemp __t29 = null
    /// 2) __t29 = opAssign(__t29, x1)

    final TempVarAssign assign = item.getVarAssign();
    final Var lvalue = assign.getVar();
    final Rvalue rvalue = assign.getRvalue();

    putRewrittenVarAssign(lvalue, rvalue.getVar());

  }

  private boolean isTempVarClassAssignToVar(final CodeItem item) {
    if (!item.isVarAssign()) {
      return false;
    }

    final TempVarAssign assign = item.getVarAssign();
    final Var lvalue = assign.getVar();
    final Rvalue rvalue = assign.getRvalue();

    if (!lvalue.getType().is_class()) {
      return false;
    }

    if (!rvalue.isVar()) {
      return false;
    }

    return true;
  }

  private boolean isConstructorTempVarAssignCall(final CodeItem item) {
    if (!item.isVarAssign()) {
      return false;
    }

    final TempVarAssign assign = item.getVarAssign();
    final Rvalue rvalue = assign.getRvalue();

    if (!rvalue.isCall()) {
      return false;
    }

    Call fcall = assign.getRvalue().getCall();
    if (!fcall.isConstructor()) {
      return false;
    }

    return true;
  }

  private void rewriteAllocObject(final CodeItem item) {

    final TempVarAssign assign = item.getVarAssign();
    final Var lvalue = assign.getVar();
    Call fcall = assign.getRvalue().getCall();

    // strtemp __t1 = strtemp_init_0(__t0)
    // ::
    // ::
    // 1) strtemp __t1 = new strtemp()
    // 2) strtemp_init_0(__t1, __t0)
    //
    AllocObject allocObject = new AllocObject(lvalue.getType());
    TempVarAssign newTempVarAssign = new TempVarAssign(lvalue, new Rvalue(allocObject));
    rewrittenResult.appendItemLast(new CodeItem(newTempVarAssign));

    fcall.getArgs().add(0, new Rvalue(lvalue));
    rewrittenResult.appendItemLast(new CodeItem(fcall));

  }
}
