package _st2_annotate;

import java.io.IOException;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_parsers.ParseStatement;
import ast_parsers.ParseTypeDeclarations;
import ast_stmt.StmtBlock;
import ast_symtab.BuiltinNames;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import tokenize.Token;

public abstract class BuildOpAssignMethod {

  public static ClassMethodDeclaration opAssign(InstantiationUnit instantiationUnit, ClassDeclaration object)
      throws IOException {

    final Token beginPos = object.getBeginPos();
    final Type typename = new Type(new ClassTypeRef(object, object.getTypeParametersT()), beginPos);

    final Parse parser = initParser(instantiationUnit, object, getMethodTemplate(object));
    final List<VarDeclarator> params = new ParseTypeDeclarations(parser).parseMethodParameters();
    final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);

    //@formatter:off
    ClassMethodDeclaration result = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC
        , new Modifiers()
        , object
        , BuiltinNames.opAssign_ident
        , params
        , typename
        , block
        , beginPos
    );
    //@formatter:on

    result.setGeneratedByDefault();
    return result;
  }

  private static StringBuilder getMethodTemplate(ClassDeclaration object) {

    final String className = object.getIdentifier().getName();

    StringBuilder sb = new StringBuilder();
    sb.append("(TYPENAME rvalue) {                    \n"); // TYPENAME lvalue, 
    sb.append("  return rvalue;                       \n");
    sb.append("}                                      \n");

    String res = sb.toString();
    res = res.replace("TYPENAME", className);

    return new StringBuilder(res);

  }

  private static Parse initParser(InstantiationUnit instantiationUnit, ClassDeclaration object, final StringBuilder sb)
      throws IOException {
    Parse parser = new ParserMain(sb).initiateParse();
    for (ClassDeclaration c : instantiationUnit.getClasses()) {
      parser.defineClassName(c);
    }
    parser.setCurrentClass(object);
    return parser;
  }

}
