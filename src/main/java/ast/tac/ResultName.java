package ast.tac;

import ast.parse.NullChecker;

//for const-folding/propagation/simplification
//we store each ID as unique pointer
//and we can replace it at once in the whole quad-list

public class ResultName {
  private String result;

  public ResultName(String result) {
    NullChecker.check(result);
    this.result = result;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    NullChecker.check(result);
    this.result = result;
  }

  @Override
  public String toString() {
    return result;
  }

}