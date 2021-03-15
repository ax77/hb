package _st7_codeout;

import java.util.Set;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import errors.AstParseException;

public abstract class GenArrays {

  public static String buildArraysProtos(Set<ClassDeclaration> arrays) {
    StringBuilder sb = new StringBuilder();
    for (ClassDeclaration c : arrays) {
      String datatype = c.getTypeParametersT().get(0).toString();
      String tpdef = CCArrays.genArrayStructTypedef(datatype);
      sb.append(tpdef);
    }
    return sb.toString();
  }

  public static String buildArraysImplsStructs(Set<ClassDeclaration> arrays) {
    StringBuilder sb = new StringBuilder();
    for (ClassDeclaration c : arrays) {
      String datatype = c.getTypeParametersT().get(0).toString();
      String tpdef = CCArrays.genArrayStructImpl(datatype);
      sb.append(tpdef);
    }
    return sb.toString();
  }

  public static String buildArraysImplsMethods(Set<Function> arrayMethods) {
    StringBuilder sb = new StringBuilder();
    for (Function f : arrayMethods) {
      final ClassMethodDeclaration method = f.getMethodSignature();
      final ClassDeclaration clazz = method.getClazz();
      final Type type = clazz.getTypeParametersT().get(0);
      final String datatype = type.toString();
      final boolean terminated = type.isChar();

      if (method.getIdentifier().getName().equals("add")) {
        String block = CCArrays.genArrayAddBlock(datatype, terminated);
        sb.append(f.signToString());
        sb.append(block);
      }

      else if (method.getIdentifier().getName().equals("get")) {
        String block = CCArrays.genArrayGetBlock(datatype);
        sb.append(f.signToString());
        sb.append(block);
      }

      else if (method.getIdentifier().getName().equals("set")) {
        String block = CCArrays.genArraySetBlock(datatype);
        sb.append(f.signToString());
        sb.append(block);
      }

      else if (method.getIdentifier().getName().equals("opAssign")) {
        String block = " \n{\n return rvalue; \n}\n ";
        sb.append(f.signToString());
        sb.append(block);
      }

      else if (method.getIdentifier().getName().equals("size")) {
        String block = CCArrays.genArraySizeBlock(datatype);
        sb.append(f.signToString());
        sb.append(block);
      }

      else {

        if (method.isConstructor()) {
          String block = CCArrays.genArrayAllocBlock(datatype);
          sb.append(f.signToString());
          sb.append(block);
        } else if (method.isDestructor()) {
          String block = " \n{\n  \n}\n ";
          sb.append(f.signToString());
          sb.append(block);
        }

        else {
          throw new AstParseException("unimpl. array method: " + method.getIdentifier().getName());
        }

      }
    }
    return sb.toString();
  }

}
