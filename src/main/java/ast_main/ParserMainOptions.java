package ast_main;

public abstract class ParserMainOptions {
  public static final boolean GENERATE_CALL_STACK = false;
  public static final boolean GENERATE_FIELD_ACCESS_ASSERTS = false;
  public static final boolean GENERATE_LOCAL_DESTRUCTORS_CALLS = false;
  public static final boolean WARN_SELF_REFERENCED_DESTRUCTORS = false;

  public static final boolean INJECT_BUILTIN_ARR = false;
  public static final boolean INJECT_BUILTIN_STR = false;
  public static final boolean INJECT_BUILTIN_STD = false;

  public static final boolean FOLD_LINEARIZED_EXPRESSIONS = true;
  public static final boolean GENERATE_DESTRUCTOR_BODY = false;
}
