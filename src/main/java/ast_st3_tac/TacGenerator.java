package ast_st3_tac;

import static ast_expr.ExpressionBase.EARRAY_INSTANCE_CREATION;
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
import static ast_expr.ExpressionBase.ESELF;
import static ast_expr.ExpressionBase.EUNARY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprArrayAccess;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprSelf;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_expr.FuncArg;
import ast_st2_annotate.Lvalue;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import tokenize.Token;
import utils_oth.NullChecker;

public class TacGenerator {

  private List<Quad> quadsTmp = new ArrayList<>();
  private HashMap<ResultName, Quad> allops = new HashMap<>();
  private List<Quad> quads = new ArrayList<>();
  private HashMap<String, ResultName> hashResultName = new HashMap<>();

  public TacGenerator() {
    this.quadsTmp = new ArrayList<>();
    this.allops = new HashMap<>();
    this.quads = new ArrayList<>();
    this.hashResultName = new HashMap<>();
  }

  private ResultName h(String result) {
    if (hashResultName.containsKey(result)) {
      return hashResultName.get(result);
    }
    ResultName newEntry = new ResultName(result);
    hashResultName.put(result, newEntry);
    return newEntry;
  }

  private void quads(Quad quad) {
    quadsTmp.add(0, quad);
    quads.add(quad);
    allops.put(quad.getResult(), quad);
  }

  private Quad pop() {
    return quadsTmp.remove(0);
  }

  private ResultName popResultName() {
    final Quad quad = pop();
    return quad.getResult();
  }

  public String txt1(String end) {
    StringBuilder sb = new StringBuilder();
    for (Quad s : quads) {
      sb.append(s.toString().trim() + end);
    }
    return sb.toString().trim();
  }

  private String t() {
    return String.format("t%d", Counter.next());
  }

  private ResultName ht() {
    return h(t());
  }

  private List<ResultName> genArgs(final ExprMethodInvocation fcall) {

    for (FuncArg arg : fcall.getArguments()) {
      gen(arg.getExpression());
    }

    List<ResultName> args = new ArrayList<>();
    for (int i = 0; i < fcall.getArguments().size(); i++) {
      args.add(0, popResultName());
    }

    return args;
  }

  private void store(Type resultType) {
    final Quad svSrc = pop();
    final Quad svDst = pop();

    NullChecker.check(svDst.getBase());

    final ResultName rvalue = svSrc.getResult();

    if (svDst.getBase() == QuadOpc.ID_DECL) {
      // id
      StoreDst storeDst = new StoreDst(svDst.getLhs());
      quads(new Quad(ht(), resultType, storeDst, rvalue));
    }

    else if (svDst.getBase() == QuadOpc.FIELD_ACCESS) {
      // field
      StoreDst storeDst = new StoreDst('.', svDst.getLhs(), svDst.getRhs());
      quads(new Quad(ht(), resultType, storeDst, rvalue));
    }

    else if (svDst.getBase() == QuadOpc.ARRAY_ACCESS) {
      // array
      StoreDst storeDst = new StoreDst('[', svDst.getLhs(), svDst.getRhs());
      quads(new Quad(ht(), resultType, storeDst, rvalue));
    }

    else {
      throw new AstParseException("unknown store operation for: " + svDst.toString());
    }

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

      final Quad svR = pop();
      final Quad svL = pop();
      quads(new Quad(QuadOpc.BINOP, ht(), e.getResultType(), op.getValue(), svL.getResult(), svR.getResult()));

    }

    else if (base == EUNARY) {
      final ExprUnary unary = e.getUnary();
      final Token op = unary.getOperator();
      gen(unary.getOperand());

      final Quad svL = pop();
      quads(new Quad(QuadOpc.UNOP, ht(), e.getResultType(), op.getValue(), svL.getResult()));
    }

    else if (base == EPRIMARY_IDENT) {
      final ExprIdent exprId = e.getIdent();

      final String identStrName = exprId.getIdentifier().getName();
      final VarDeclarator var = exprId.getVariable();

      final Quad quad = new Quad(QuadOpc.ID_DECL, ht(), var.getType(), h(identStrName));
      quad.setVarSym(var);
      quads(quad);

      //load(e.getResultType());
    }

    else if (base == EPRIMARY_STRING) {
      throw new RuntimeException(base.toString() + " ???");
    }

    else if (base == EPRIMARY_NUMBER) {
      final int iconst = (int) e.getNumber().getInteger();
      final String itoa = String.format("%d", iconst);
      final Quad quad = new Quad(QuadOpc.NUM_DECL, ht(), new Type(TypeBase.TP_I32, e.getBeginPos()), h(itoa));

      quad.setNumberSym(e.getNumber());
      quads(quad);
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

      final List<ResultName> args = genArgs(fcall);
      final ResultName obj = popResultName();
      final ResultName fun = h(fcall.getFuncname().getName());

      final Quad quad = new Quad(ht(), fcall.getMethod().getType(), obj, fun, args);
      quad.setMethodSym(fcall.getMethod());
      quads(quad);

      //load(e.getResultType());
    }

    else if (base == EFIELD_ACCESS) {
      final ExprFieldAccess fieldAccess = e.getFieldAccess();
      gen(fieldAccess.getObject());

      final String fName = fieldAccess.getFieldName().getName();
      final Type fType = fieldAccess.getField().getType();

      final Quad quad = new Quad(QuadOpc.FIELD_ACCESS, ht(), fType, popResultName(), h(fName));
      quad.setVarSym(fieldAccess.getField());
      quads(quad);

      //load(e.getResultType());
    }

    else if (base == ECLASS_INSTANCE_CREATION) {
      // @1
      // token_type tp = new token_type(128);
      //   token_type* tp = NULL;
      //   token_type* _tmp0_;
      //   _tmp0_ = token_type_new (128);
      //   tp = _tmp0_;

      throw new RuntimeException(base.toString() + " ???");

    }

    else if (base == ESELF) {
      final ExprSelf exprSelf = e.getSelfExpression();
      final ClassDeclaration clazz = exprSelf.getClazz();
      final Type classType = new Type(new ClassTypeRef(clazz, new ArrayList<>()), e.getBeginPos());
      quads(new Quad(QuadOpc.SELF_DECL, ht(), classType, h("self")));
    }

    else if (base == EARRAY_INSTANCE_CREATION) {
      throw new RuntimeException(base.toString() + " ???");

    }

    else if (base == ExpressionBase.EARRAY_ACCESS) {
      ExprArrayAccess arrayAccess = e.getArrayAccess();
      gen(arrayAccess.getArray());
      gen(arrayAccess.getIndex());

      Quad index = pop();
      Quad array = pop();
      Quad res = new Quad(QuadOpc.ARRAY_ACCESS, ht(), e.getResultType(), array.getResult(), index.getResult());
      quads(res);
    }

    else {
      throw new RuntimeException(base.toString() + ": unimplemented");
    }

  }

}