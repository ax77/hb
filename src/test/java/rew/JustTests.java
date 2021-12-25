package rew;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import _st1_templates.TypeSetter;
import _st7_codeout.Codeout;
import _st7_codeout.CodeoutBuilder;
import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeListsComparer;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import tokenize.Ident;
import utils.UtilSrcToStringLevel;
import utils_ser.SerializationUtils;

public class JustTests {

  class WasGenerated {
    private final Ident name;
    private final List<Type> actual;
    private final Type expanded;

    public WasGenerated(Ident name, List<Type> actual, Type expanded) {
      this.name = name;
      this.actual = actual;
      this.expanded = expanded;
    }

    public Ident getName() {
      return name;
    }

    public List<Type> getActual() {
      return actual;
    }

    public Type getExpanded() {
      return expanded;
    }

  }

  private Map<Ident, ClassDeclaration> templates = new HashMap<Ident, ClassDeclaration>();
  private List<WasGenerated> generated = new ArrayList<WasGenerated>();

  private ClassDeclaration copyClazz(ClassDeclaration given) {
    return (ClassDeclaration) SerializationUtils.clone(given);
  }

  private Type presentedInGenerated(Ident identifier, List<Type> actual) {
    for (WasGenerated item : generated) {
      if (identifier.equals(item.getName())) {
        boolean result = TypeListsComparer.typeListsAreEqual(item.getActual(), actual);
        if (result) {
          return item.getExpanded();
        }
      }
    }
    return null;
  }

  private Type possibleExpansion(Type type) {
    if (!type.isClassTemplate()) {
      return type;
    }

    final ClassTypeRef ref = type.getClassTypeRef();
    final ClassDeclaration clazz = ref.getClazz();
    final List<Type> formal = Collections.unmodifiableList(clazz.getTypeParametersT());
    final List<Type> actual = Collections.unmodifiableList(ref.getTypeArguments());

    if (formal.size() != actual.size()) {
      throw new AstParseException(
          "formal and actual parameters for the template class are in different count: " + clazz.getLocationToString());
    }

    final Type cached = presentedInGenerated(clazz.getIdentifier(), actual);
    if (cached != null) {
      return cached;
    }

    /// let's prepare the template as is
    final ClassDeclaration getTemplateByItsName = templates.get(clazz.getIdentifier());
    if (getTemplateByItsName == null) {
      throw new AstParseException("cannot find the template for the class: " + clazz.getLocationToString());
    }

    final ClassDeclaration template = copyClazz(getTemplateByItsName);
    final Type result = new Type(new ClassTypeRef(template, actual));
    generated.add(new WasGenerated(template.getIdentifier(), actual, result));

    for (int i = 0; i < actual.size(); i += 1) {
      template.getTypeParametersT().get(i).fillPropValues(actual.get(i));
    }

    final List<TypeSetter> typeSetters = template.getTypeSetters();
    for (final TypeSetter ts : typeSetters) {
      final Type maybeShouldExpandIt = ts.getType();
      final Type newtype = possibleExpansion(maybeShouldExpandIt);
      ts.setType(newtype);
    }

    return type;
  }

  @Test
  public void test1() throws IOException {

    final String dir = System.getProperty("user.dir");
    CompilationUnit unit = new ParserMain(dir + "/tests/just_tests.hb").parseCompilationUnit();

    for (ClassDeclaration c : unit.getTemplates()) {
      templates.put(c.getIdentifier(), c);
    }

    for (ClassDeclaration c : unit.getClasses()) {
      List<TypeSetter> typeSetters = c.getTypeSetters();
      for (TypeSetter ts : typeSetters) {
        Type expanded = possibleExpansion(ts.getType());
        ts.setType(expanded);
      }
    }

    for (ClassDeclaration c : unit.getClasses()) {
      System.out.println(c.toString());
    }

    for (WasGenerated w : generated) {
      final ClassDeclaration classTypeFromRef = w.getExpanded().getClassTypeFromRef();
      System.out.println(classTypeFromRef.toString());
    }

  }

}
