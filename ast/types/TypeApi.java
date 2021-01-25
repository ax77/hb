package ast.types;

public interface TypeApi {
  //@formatter:off
  public boolean is_i8();
  public boolean is_u8();
  public boolean is_i16();
  public boolean is_u16();
  public boolean is_i32();
  public boolean is_u32();
  public boolean is_i64();
  public boolean is_u64();
  public boolean is_f32();
  public boolean is_f64();
  public boolean is_boolean();
  public boolean is_void_stub();
  public boolean is_type_var();
  public boolean is_class();
  public boolean is_function();
  public boolean is_array();
  public boolean is_tuple();
  public int get_size();
  public int get_align();
  public boolean is_equal_to(Type another);
  public boolean is_class_template();
  public boolean is_iterated();
  public boolean is_reference();
  public boolean is_primitive();
  public boolean is_has_signedness();
  public boolean is_signed();
  public boolean is_unsigned();
  public boolean is_numeric();
  public boolean is_integer();
  public boolean is_floating();
}
