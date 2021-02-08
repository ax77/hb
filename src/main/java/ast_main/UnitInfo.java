package ast_main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_symtab.Keywords;
import errors.AstParseException;
import parse.Tokenlist;
import tokenize.Env;
import tokenize.Ident;
import tokenize.Stream;
import tokenize.T;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;

public class UnitInfo {

  private final List<String> classLocations;
  private final List<Token> tokenlist;
  private final List<ClassDeclaration> typenames;

  public UnitInfo(String unitName) throws IOException {
    this.classLocations = new ArrayList<>();
    this.tokenlist = new ArrayList<>();
    this.typenames = new ArrayList<>();

    buildLocations(unitName);
    buildTokenlist();
    buildTypenames();
  }

  private void buildTokenlist() throws IOException {
    for (String absolutePath : classLocations) {
      final FileWrapper fw = new FileWrapper(absolutePath);
      final Stream stream = new Stream(absolutePath, fw.readToString(FileReadKind.AS_IS));
      final List<Token> tokens = stream.getTokenlist();
      for (Token tok : tokens) {
        // ignoring EOF, and stream-markers
        if (tok.typeIsSpecialStreamMarks()) {
          continue;
        }
        tokenlist.add(tok);
      }
    }
    // !!! EOF here
    tokenlist.add(Env.EOF_TOKEN_ENTRY);
  }

  // key   :: type-name in its pure form like 'list', 'string', 'node', etc...
  // value :: full path to this class
  //
  private void buildLocations(final String unitName) throws IOException {
    final UnitImportsSet importsResolver = new UnitImportsSet(unitName);
    for (String s : importsResolver.getFullyRead()) {
      FileWrapper fw = new FileWrapper(s);
      fw.assertIsExists();
      fw.assertIsFile();
      classLocations.add(fw.getFullname());
    }
  }

  public List<Token> getTokenlist() {
    return tokenlist;
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