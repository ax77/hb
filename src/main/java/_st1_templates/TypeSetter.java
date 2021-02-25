package _st1_templates;

import ast_types.Type;

/// a type-setter is an object which can change its type
/// during the template-expansion
/// it may be a variable, field, method-parameter, 
/// class-instance-creation, and so on.
/// if the type of that object contains a typename_id
/// it will be replaced with a 'real' given type
/// the ClassDeclaration holds all type-setters which depends
/// on that 'templated' class.
/// and we can easily iterate over the list of type-setters, and
/// set the new type we want, neither than iterate over the whole
/// syntax tree which includes expressions, statements, etc...
///
/// it is easy to imagine that type-setters as the macro-replacement list
/// and type-parameters of the templated class as macro-parameters.
/// it looks like macros, but its not, because we work here with
/// the AST instead of the raw lexems.
///
public interface TypeSetter {
  void setType(Type typeToSet);

  Type getType();
}
