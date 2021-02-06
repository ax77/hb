package ast_types;

public interface TypeApi {
  //@formatter:off
  public boolean is_char();
  public boolean is_short();
  public boolean is_int();
  public boolean is_long();
  public boolean is_float();
  public boolean is_double();
  public boolean is_boolean();
  public boolean is_void_stub();
  public boolean is_typename_id();
  public boolean is_class();
  public int     get_size();
  public int     get_align();
  public boolean is_equal_to(Type another);
  public boolean is_class_template();
  public boolean is_iterated();
  public boolean is_reference();
  public boolean is_primitive();
  public boolean is_numeric();
  public boolean is_integer();
  public boolean is_floating();
  public boolean is_builtin_array();
}
