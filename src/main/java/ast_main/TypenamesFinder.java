package ast_main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_symtab.Keywords;
import errors.AstParseException;
import parse.Tokenlist;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class TypenamesFinder {

  private final List<Token> tokenlist;
  private final List<ClassDeclaration> typenames;

  public TypenamesFinder(List<Token> tokenlist) {
    this.tokenlist = tokenlist;
    this.typenames = new ArrayList<>();
    buildTypenames();
  }

  public List<ClassDeclaration> getTypenames() {
    return typenames;
  }

  private void buildTypenames() {

    // silly type-names resolver :)
    final Tokenlist toResolve = new Tokenlist(Collections.unmodifiableList(tokenlist));

    while (toResolve.hasNext()) {

      final Token tok = toResolve.next();

      if (tok.ofType(T.TOKEN_EOF)) {
        break;
      }

      /// class Tree<T> {
      /// ^.....^...^
      /// 
      /// class Tree {
      /// ^.....^....^
      /// 
      /// class Tree implements Equal {
      /// ^.....^....^
      ///
      /// id + <
      /// id + {
      /// id + implements
      ///
      final boolean isKeyword = tok.isIdent(Keywords.class_ident) || tok.isIdent(Keywords.interface_ident);
      if (!isKeyword) {
        continue;
      }

      final Ident keyword = tok.getIdent();
      final Token id = toResolve.next();
      if (!id.ofType(T.TOKEN_IDENT)) {
        throw new AstParseException("expect class-name, but was: " + id.getValue());
      }

      final ClassDeclaration clazz = new ClassDeclaration(keyword, id.getIdent(), new ArrayList<>(), id);
      typenames.add(clazz);

      final Token peek = toResolve.peek();
      final boolean isOkRest = peek.ofType(T.T_LEFT_BRACE) || peek.ofType(T.T_LT)
          || peek.isIdent(Keywords.implements_ident);
      if (!isOkRest) {
        throw new AstParseException("expect class-name, but was: " + peek.getValue());
      }
    }

  }
}
