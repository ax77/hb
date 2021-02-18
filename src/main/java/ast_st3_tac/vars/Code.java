package ast_st3_tac.vars;

import java.util.ArrayList;
import java.util.List;

import errors.AstParseException;

public class Code {
  private List<CodeItem> items;

  public Code() {
    this.items = new ArrayList<>();
  }

  public void appendItemLast(CodeItem e) {
    this.items.add(e);
  }

  public void pushItem(CodeItem e) {
    items.add(0, e);
  }

  public CodeItem popItem() {
    return items.remove(0);
  }

  public List<CodeItem> getItems() {
    return items;
  }

  public CodeItem getLast() {
    if (items.isEmpty()) {
      throw new AstParseException("empty items");
    }
    return items.get(items.size() - 1);
  }

  @Override
  public String toString() {
    return items.toString();
  }

}
