package njast.types;

import java.util.HashMap;
import java.util.Map;

import jscan.symtab.Ident;
import njast.symtab.IdentMap;

public abstract class TypeBindings {

  public static final Map<TypeBase, String> BIND_PRIMITIVE_TO_STRING = new HashMap<>();
  static {
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_BYTE, "byte");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_SHORT, "short");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_CHAR, "char");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_INT, "int");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_LONG, "long");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_FLOAT, "float");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_DOUBLE, "double");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_BOOLEAN, "boolean");
  }

  public static final Map<Ident, Type> BIND_IDENT_TO_TYPE = new HashMap<>();
  static {
    BIND_IDENT_TO_TYPE.put(IdentMap.byte_ident, Type.BYTE_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.short_ident, Type.SHORT_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.char_ident, Type.CHAR_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.int_ident, Type.INT_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.long_ident, Type.LONG_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.float_ident, Type.FLOAT_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.double_ident, Type.DOUBLE_TYPE);
    BIND_IDENT_TO_TYPE.put(IdentMap.boolean_ident, Type.BOOLEAN_TYPE);
  }

}
