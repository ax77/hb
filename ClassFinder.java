package njast;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.print.DocFlavor.URL;

import org.junit.Test;

import njast.ast_nodes.expr.ExpressionNode;
import njast.ast_visitors.AstVisitor;

public class ClassFinder {

  private static final char PKG_SEPARATOR = '.';

  private static final char DIR_SEPARATOR = '/';

  private static final String CLASS_FILE_SUFFIX = ".class";

  private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

  public static List<Class<?>> find(String scannedPackage) {
    String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
    java.net.URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
    if (scannedUrl == null) {
      throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
    }
    File scannedDir = new File(scannedUrl.getFile());
    List<Class<?>> classes = new ArrayList<Class<?>>();
    for (File file : scannedDir.listFiles()) {
      classes.addAll(find(file, scannedPackage));
    }
    return classes;
  }

  private static List<Class<?>> find(File file, String scannedPackage) {
    List<Class<?>> classes = new ArrayList<Class<?>>();
    String resource = scannedPackage + PKG_SEPARATOR + file.getName();
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        classes.addAll(find(child, resource));
      }
    } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
      int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
      String className = resource.substring(0, endIndex);
      try {
        classes.add(Class.forName(className));
      } catch (ClassNotFoundException ignore) {
      }
    }
    return classes;
  }

  @Test
  public void test() {
    List<Class<?>> classes = ClassFinder.find("njast.ast_nodes");
    List<String> names = new ArrayList<>();
    for(Class c : classes) {
      names.add(c.getSimpleName());
    }
    Collections.sort(names);
    
    // void visit(ExpressionNode o);
    for(String classname: names) {
      System.out.println("void visit(" + classname + " o);");
    }
    
    // @Override public void accept(AstVisitor visitor) { visitor.visit(this); }
    for(String classname: names) {
      // System.out.println();
    }
  }

}
























