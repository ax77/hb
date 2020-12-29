package njast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

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

  private static List<String> strSplitChar(String where, char sep, boolean includeEmpty) {

    List<String> lines = new ArrayList<String>(0);
    final int len = where.length();

    if (len == 0) {
      return lines;
    }

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < len; i++) {
      char c = where.charAt(i);
      if (c == sep) {
        if (sb.length() > 0 || (sb.length() == 0 && includeEmpty)) {
          lines.add(sb.toString());
        }
        sb = new StringBuilder();
        continue;
      }
      sb.append(c);
    }
    if (sb.length() > 0 || (sb.length() == 0 && includeEmpty)) {
      lines.add(sb.toString());
    }

    return lines;
  }

  @Ignore
  @Test
  public void test() {

    List<Class<?>> classes = ClassFinder.find("njast.ast_nodes");
    List<String> names = new ArrayList<>();
    for (Class c : classes) {
      names.add(c.getSimpleName());
    }
    Collections.sort(names);

    // njast.ast_nodes.clazz.ClassConstructorDeclaration

    // void visit(ExpressionNode o);
    System.out.println("//@formatter:off");
    for (String classname : names) {
      System.out.println("void visit(" + classname + " o);");
    }
    System.out.println("//@formatter:on");

  }

}
