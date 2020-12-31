package njast.errors;

public enum EParseErrors {
    E_INCOMPLETE_STRUCT_FIELD {
      @Override
      public String toString() {
        return "struct doe's not contain incomplete field's";
      }
    },

}
