package ast_st3_tac.vars;

import java.util.ArrayList;
import java.util.List;

public class Code {
  private List<CodeItem> items;

  public Code() {
    this.items = new ArrayList<>();
  }

  public void addItem(CodeItem e) {
    this.items.add(e);
  }
}
