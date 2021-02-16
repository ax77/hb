package ast_st3_tac;

import static ast_expr.ExpressionBase.EASSIGN;
import static ast_expr.ExpressionBase.EBINARY;
import static ast_expr.ExpressionBase.ECAST;
import static ast_expr.ExpressionBase.ECLASS_INSTANCE_CREATION;
import static ast_expr.ExpressionBase.EFIELD_ACCESS;
import static ast_expr.ExpressionBase.EMETHOD_INVOCATION;
import static ast_expr.ExpressionBase.EPRIMARY_IDENT;
import static ast_expr.ExpressionBase.EPRIMARY_NULL_LITERAL;
import static ast_expr.ExpressionBase.EPRIMARY_NUMBER;
import static ast_expr.ExpressionBase.EPRIMARY_STRING;
import static ast_expr.ExpressionBase.ETHIS;
import static ast_expr.ExpressionBase.EUNARY;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprThis;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_printers.ExprPrinters;
import ast_st2_annotate.Lvalue;
import ast_st2_annotate.Mods;
import ast_st2_annotate.Symbol;
import ast_st3_tac.vars.Code;
import ast_st3_tac.vars.CodeItem;
import ast_st3_tac.vars.CopierNamer;
import ast_st3_tac.vars.StoreLeaf;
import ast_st3_tac.vars.TempVarAssign;
import ast_st3_tac.vars.arith.Binop;
import ast_st3_tac.vars.arith.Unop;
import ast_st3_tac.vars.store.AllocObject;
import ast_st3_tac.vars.store.Call;
import ast_st3_tac.vars.store.ELvalue;
import ast_st3_tac.vars.store.ERvalue;
import ast_st3_tac.vars.store.FieldAccess;
import ast_st3_tac.vars.store.Var;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;
import literals.IntLiteral;
import tokenize.Token;
import utils_oth.NullChecker;

public class TacGenerator {

  private Code codeTmp = new Code();
  private Code codeOut = new Code();

  public TacGenerator() {
    codeTmp = new Code();
    codeOut = new Code();
  }

  /// if (result_name)
  /// return (result_name)
  public String getLastResultNameToString() {
    return "";
  }

  private void outCode(CodeItem item) {
    codeTmp.pushItem(item);
    codeOut.appendItemLast(item);
  }

  private CodeItem popCode() {
    return codeTmp.popItem();
  }

  public String txt1(String end) {
    StringBuilder sb = new StringBuilder();
    for (CodeItem item : codeOut.getItems()) {
      sb.append(item.toString().trim() + end);
    }
    return sb.toString().trim();
  }

  private List<Var> genArgsVars(final List<ExprExpression> arguments) {

    for (ExprExpression arg : arguments) {
      gen(arg);
    }

    List<Var> args = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      final CodeItem item = popCode();
      if (!item.isVarAssign()) {
        throw new AstParseException("unimpl.");
      }
      final TempVarAssign varAssign = item.getVarAssign();
      args.add(0, varAssign.getVar());
    }

    return args;
  }

  private void store(Type resultType) {

    //E
    final CodeItem srcItem = popCode();
    final CodeItem dstItem = popCode();

    if (dstItem.isVarAssign() && srcItem.isVarAssign()) {

      final TempVarAssign dstVarAssign = dstItem.getVarAssign();
      final TempVarAssign srcVarAssign = srcItem.getVarAssign();

      if (dstVarAssign.getRvalue().isVar()) {

        final ELvalue lvalueE = new ELvalue(dstVarAssign.getRvalue().getVar());

        final StoreLeaf storeOp = new StoreLeaf(lvalueE, srcVarAssign.getVar());
        outCode(new CodeItem(storeOp));
      }

      else if (dstVarAssign.getRvalue().isFieldAccess()) {
        final ELvalue lvalueE = new ELvalue(dstVarAssign.getRvalue().getFieldAccess());

        final StoreLeaf storeOp = new StoreLeaf(lvalueE, srcVarAssign.getVar());
        outCode(new CodeItem(storeOp));
      }

      else {
        throw new AstParseException("unimpl...");
      }

    } else {
      throw new AstParseException("unimpl...");
    }
    //

    //     final Quad svSrc = pop();
    //     final Quad svDst = pop();
    //     
    //     NullChecker.check(svDst.getBase());
    //     
    //     final ResultName rvalue = svSrc.getResult();
    //     
    //     if (svDst.getBase() == QuadOpc.ID_DECL) {
    //       // id
    //       StoreDst storeDst = new StoreDst(svDst.getLhs());
    //       final Quad storeResult = new Quad(ht(), resultType, storeDst, rvalue);
    //       quads(storeResult);
    //     }
    //     
    //     else if (svDst.getBase() == QuadOpc.FIELD_ACCESS) {
    //       // field
    //       StoreDst storeDst = new StoreDst('.', svDst.getLhs(), svDst.getRhs());
    //       quads(new Quad(ht(), resultType, storeDst, rvalue));
    //     }
    //     
    //     else if (svDst.getBase() == QuadOpc.ARRAY_ACCESS) {
    //       // array
    //       StoreDst storeDst = new StoreDst('[', svDst.getLhs(), svDst.getRhs());
    //       quads(new Quad(ht(), resultType, storeDst, rvalue));
    //     }
    //     
    //     else {
    //       throw new AstParseException("unknown store operation for: " + svDst.toString());
    //     }

  }

  @SuppressWarnings("unused")
  private void load(Type resultType) {
    // quads(new Quad(ht(), resultType, "LD", popResultName()));
  }

  public void gen(ExprExpression e) {
    NullChecker.check(e);
    ExpressionBase base = e.getBase();

    if (base == EASSIGN) {

      final ExprAssign assing = e.getAssign();

      final ExprExpression lvalue = assing.getLvalue();
      gen(lvalue);
      Lvalue.checkHard(lvalue);

      gen(assing.getRvalue());
      store(e.getResultType());

    }

    else if (base == EBINARY) {

      final ExprBinary binary = e.getBinary();
      final Token op = binary.getOperator();

      gen(binary.getLhs());
      gen(binary.getRhs());

      //E
      CodeItem Ritem = popCode();
      CodeItem Litem = popCode();

      if (Ritem.isVarAssign() && Litem.isVarAssign()) {
        final TempVarAssign lvar = Litem.getVarAssign();
        final TempVarAssign rvar = Ritem.getVarAssign();

        final Var lvarRes = lvar.getVar();
        final Var rvarRes = rvar.getVar();

        final Binop binop = new Binop(lvarRes, op.getValue(), rvarRes);
        final TempVarAssign tempVarAssign = new TempVarAssign(CopierNamer.copyVarAddNewName(lvarRes),
            new ERvalue(binop));

        outCode(new CodeItem(tempVarAssign));

      } else {
        throw new AstParseException("unimpl...");
      }

    }

    else if (base == EUNARY) {
      final ExprUnary unary = e.getUnary();
      final Token op = unary.getOperator();
      gen(unary.getOperand());

      //E
      CodeItem Litem = popCode();

      if (Litem.isVarAssign()) {
        final TempVarAssign lvar = Litem.getVarAssign();

        final Var lvarRes = lvar.getVar();

        final Unop unop = new Unop(op.getValue(), lvarRes);
        final TempVarAssign tempVarAssign = new TempVarAssign(CopierNamer.copyVarAddNewName(lvarRes),
            new ERvalue(unop));

        outCode(new CodeItem(tempVarAssign));

      } else {
        throw new AstParseException("unimpl...");
      }
    }

    else if (base == EPRIMARY_IDENT) {
      final ExprIdent exprId = e.getIdent();
      final Symbol sym = exprId.getSym();

      if (sym.isClazz()) {
        throw new AstParseException("unimpl. abstract class method/field access.");
      }
      final VarDeclarator var = sym.getVariable();

      /// rewrite an identifier to [this.field] form
      /// if it is possible
      if (var.is(VarBase.CLASS_FIELD)) {

        final ClassDeclaration clazz = var.getClazz();
        final ExprExpression ethis = new ExprExpression(new ExprThis(clazz), clazz.getBeginPos());
        final ExprFieldAccess eFaccess = new ExprFieldAccess(ethis, var.getIdentifier());
        eFaccess.setSym(new Symbol(var));

        final ExprExpression generated = new ExprExpression(eFaccess, clazz.getBeginPos());
        gen(generated);

        return;
      }

      //E
      final Var lvalueTmp = CopierNamer.copyVarDeclAddNewName(var);
      final ERvalue rvalueTmp = new ERvalue(CopierNamer.copyVarDecl(var));
      final TempVarAssign tempVarAssign = new TempVarAssign(lvalueTmp, rvalueTmp);
      outCode(new CodeItem(tempVarAssign));
      //

      //load(e.getResultType());
    }

    else if (base == EPRIMARY_STRING) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EPRIMARY_NUMBER) {
      final IntLiteral number = e.getNumber();

      //E
      final Var lhsVar = new Var(VarBase.LOCAL_VAR, Mods.letMods(), number.getType(), CopierNamer.tmpIdent());
      final ERvalue rhsValue = new ERvalue(number);
      final CodeItem item = new CodeItem(new TempVarAssign(lhsVar, rhsValue));
      outCode(item);
      //
    }

    else if (base == EPRIMARY_NULL_LITERAL) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == ECAST) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EMETHOD_INVOCATION) {
      final ExprMethodInvocation fcall = e.getMethodInvocation();
      gen(fcall.getObject());

      final ClassMethodDeclaration method = fcall.getSym().getMethod();

      //E
      final List<Var> argsX = genArgsVars(fcall.getArguments());
      final CodeItem objX = popCode();
      argsX.add(0, objX.getVarAssign().getVar());

      final Call call = new Call(method.getType(), Hash_ident.getHashedIdent(CopierNamer.getMethodName(method)), argsX);
      if (method.isVoid()) {
        CodeItem item = new CodeItem(call);
        outCode(item);
      } else {
        CodeItem item = new CodeItem(new TempVarAssign(
            new Var(VarBase.LOCAL_VAR, new Modifiers(), method.getType(), CopierNamer.tmpIdent()), new ERvalue(call)));
        outCode(item);
      }
      //

      //load(e.getResultType());
    }

    else if (base == EFIELD_ACCESS) {
      final ExprFieldAccess fieldAccess = e.getFieldAccess();
      gen(fieldAccess.getObject());

      final VarDeclarator variable = fieldAccess.getSym().getVariable();

      //E
      CodeItem thisItem = popCode();
      if (thisItem.isVarAssign()) {

        final FieldAccess access = new FieldAccess(thisItem.getVarAssign().getVar(), CopierNamer.copyVarDecl(variable));
        final Var lhsvar = CopierNamer.copyVarDeclAddNewName(variable);
        final CodeItem item = new CodeItem(new TempVarAssign(lhsvar, new ERvalue(access)));
        outCode(item);

      } else {
        throw new AstParseException("unimpl...");
      }
      //

      //load(e.getResultType());
    }

    else if (base == ECLASS_INSTANCE_CREATION) {
      genClassCreation(e);
    }

    else if (base == ETHIS) {

      final ExprThis exprSelf = e.getSelfExpression();
      final ClassDeclaration clazz = exprSelf.getClazz();
      final Type classType = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()), e.getBeginPos());

      //E
      // main_class __t2 = _this_
      final Var lhsVar = new Var(VarBase.LOCAL_VAR, new Modifiers(), classType, CopierNamer.tmpIdent());
      final Var rhsVar = new Var(VarBase.METHOD_PARAMETER, Mods.letMods(), classType, CopierNamer._this_());
      final CodeItem item = new CodeItem(new TempVarAssign(lhsVar, new ERvalue(rhsVar)));
      outCode(item);
      //

    }

    else {
      throw new RuntimeException(base.toString() + ": unimplemented");
    }

  }

  // TODO:
  private void genClassCreation(ExprExpression e) {

    ExprClassCreation classCreation = e.getClassCreation();
    ClassDeclaration clazz = classCreation.getType().getClassTypeFromRef();

    List<ExprExpression> arguments = classCreation.getArguments();
    ClassMethodDeclaration constructor = clazz.getConstructor(arguments);

    if (constructor == null) {
      throw new AstParseException("class has no constructor for args: " + ExprPrinters.funcArgsToString(arguments));
    }

    final Symbol sym = classCreation.getSym();

    Var obj = null;

    if (sym == null) {
      /// new token(new type(1));
      /// ..........^
      /// the class-creation without variable
      /// we have to generate the temporary name

      obj = new Var(VarBase.LOCAL_VAR, new Modifiers(), classCreation.getType(), CopierNamer.tmpIdent());

    } else {

      /// token tok = new token();
      /// ......^
      /// there we have a name

      VarDeclarator var = sym.getVariable();
      obj = CopierNamer.copyVarDecl(var);
    }

    AllocObject allocObject = new AllocObject(
        new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()), clazz.getBeginPos()));

    CodeItem item3 = new CodeItem(new TempVarAssign(obj, new ERvalue(allocObject)));
    outCode(item3);

  }

}
