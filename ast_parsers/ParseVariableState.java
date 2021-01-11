package njast.ast_parsers;

public enum ParseVariableState {
    // we allow [,]comma declarations only in for-loop
    // for(int a=0, b=1, c=2; test; step) { loop; }
    STATE_FOR_LOOP, STATE_OTHER,
}
