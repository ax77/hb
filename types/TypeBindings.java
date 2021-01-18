package njast.types;

import java.util.HashMap;
import java.util.Map;

import jscan.symtab.Ident;
import njast.symtab.IdentMap;

public abstract class TypeBindings {

  public static final Map<TypeBase, String> BIND_PRIMITIVE_TO_STRING = new HashMap<>();
  static {
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_I8, "i8");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_U8, "u8");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_I16, "i16");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_U16, "u16");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_I32, "i32");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_U32, "u32");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_I64, "i64");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_U64, "u64");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_F32, "f32");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_F64, "f64");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_BOOLEAN, "boolean");
  }

  public static final Map<Ident, Type> BIND_IDENT_TO_TYPE = new HashMap<>();
  static {
    BIND_IDENT_TO_TYPE.put(IdentMap.i8_ident, Type.I8_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.u8_ident, Type.U8_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.i16_ident, Type.I16_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.u16_ident, Type.U16_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.i32_ident, Type.I32_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.u32_ident, Type.U32_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.i64_ident, Type.I64_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.u64_ident, Type.U64_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.f32_ident, Type.F32_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.f64_ident, Type.F64_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.boolean_ident, Type.BOOLEAN_TYPE);
  }
}
