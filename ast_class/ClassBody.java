package njast.ast_class;

import java.util.ArrayList;
import java.util.List;

public class ClassBody {
  private List<ClassBodyDeclaration> classBodyDeclarations;

  public ClassBody() {
    this.classBodyDeclarations = new ArrayList<ClassBodyDeclaration>();
  }

  public void put(ClassBodyDeclaration e) {
    this.classBodyDeclarations.add(e);
  }

  public List<ClassBodyDeclaration> getClassBodyDeclarations() {
    return classBodyDeclarations;
  }

}
