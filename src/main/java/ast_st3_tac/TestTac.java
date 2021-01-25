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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

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
import ast_st1_templates.InstatantiationUnitBuilder;
import ast_st2_annotate.Lvalue;
import ast_types.ClassType;
import ast_types.Type;
import ast_types.TypeBase;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import parse.ImportsResolver;
import parse.Parse;
import parse.ParserMain;
import tokenize.Token;
import utils_oth.NullChecker;

public class TestTac {

  private static int counter = 0;
  private List<Quad> quadsTmp = new ArrayList<>();
  private HashMap<ResultName, Quad> allops = new HashMap<>();
  private List<Quad> quads = new ArrayList<>();
  private HashMap<String, ResultName> hashResultName = new HashMap<>();

  @Before
  public void before() {
    counter = 0;
    quadsTmp = new ArrayList<>();
    allops = new HashMap<>();
    quads = new ArrayList<>();
    hashResultName = new HashMap<>();
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

  private String txt1(String end) {
    StringBuilder sb = new StringBuilder();
    for (Quad s : quads) {
      sb.append(s.toString().trim() + end);
    }
    return sb.toString().trim();
  }

  private String t() {
    return String.format("t%d", counter++);
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

  private void load(Type resultType) {
    // quads(new Quad(ht(), resultType, "LD", popResultName()));
  }

  private void gen(ExprExpression e) {
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
      final Quad quad = new Quad(QuadOpc.NUM_DECL, ht(), new Type(TypeBase.TP_I32), h(itoa));

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
      final Type classType = new Type(new ClassType(clazz, new ArrayList<>()));
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

  @Test
  public void test5() throws IOException {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  class token_type {                                      \n");
    sb.append(" /*002*/      var tp: i32;                                        \n");
    sb.append(" /*003*/      init (tp: i32)                                      \n");
    sb.append(" /*004*/      {                                                   \n");
    sb.append(" /*005*/          self.tp = tp;                                   \n");
    sb.append(" /*006*/      }                                                   \n");
    sb.append(" /*007*/      func get_tp(flag: i32) -> i32                                \n");
    sb.append(" /*008*/      {                                                   \n");
    sb.append(" /*009*/          return tp;                                      \n");
    sb.append(" /*010*/      }  func set_tp(tp: i32) { self.tp = tp; }                                                 \n");
    sb.append(" /*011*/  }                                                       \n");
    sb.append(" /*012*/  class token {                                           \n");
    sb.append(" /*013*/      var type: token_type;                               \n");
    sb.append(" /*014*/      init (type: token_type)                             \n");
    sb.append(" /*015*/      {                                                   \n");
    sb.append(" /*016*/          self.type = type;                               \n");
    sb.append(" /*017*/      }                                                   \n");
    sb.append(" /*018*/      func get_type(x: i32, y: i32)                                     \n");
    sb.append(" /*019*/          -> token_type                                   \n");
    sb.append(" /*020*/      {                                                   \n");
    sb.append(" /*021*/          return type;                                    \n");
    sb.append(" /*022*/      }    func stub(f: i32) {}                                               \n");
    sb.append(" /*023*/  }                                                       \n");
    sb.append(" /*024*/  class test                                              \n");
    sb.append(" /*025*/  {   var f: i32; var arr: [[i32]]; func fnz()->i32{return 0;}                                                   \n");
    sb.append(" /*026*/      func fn4() -> i32                                   \n");
    sb.append(" /*027*/      {                                                   \n");
    sb.append(" /*028*/          var tp: token_type = new token_type(tp: 128);   \n");
    sb.append(" /*029*/          var tok: token = new token(type: tp);           \n");
    sb.append(" /*030*/          tok.type.tp = 12;                               \n");
    sb.append(" /*031*/          var xxx: i32;                                   \n");
    sb.append(" /*031*/          arr[1][2] = -1 + 1024;                         \n");
    sb.append(" /*032*/          return xxx;                                     \n");
    sb.append(" /*033*/      }                                                   \n");
    sb.append(" /*034*/  }                                                       \n");
    //@formatter:on

    Parse mainParser = new ParserMain(new ImportsResolver(sb).getSource()).initiateParse();

    CompilationUnit unit = mainParser.parse();
    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    ExprExpression expression2 = instantiationUnit.getClasses().get(2).getMethods().get(1).getBlock()
        .getBlockStatements().get(4).getStatement().getExprStmt();

    gen(expression2);

    for (Entry<ResultName, Quad> e : allops.entrySet()) {
      // System.out.println(e.getKey().toString() + "::" +e.getValue().getType().toString());
    }

    // // const folding: that is :)
    // List<Quad> toRemove = new ArrayList<>();
    // for (Quad q : quads) {
    //   if (q.getBase() == QuadOpc.NUM_DECL) {
    //     toRemove.add(q);
    //     hashResultName.get(q.getResult().getResult()).setResult(q.getLhs().toString());
    //   }
    // }
    // for (Quad q : toRemove) {
    //   quads.remove(q);
    // }

    System.out.println(txt1(";\n"));

  }

}
