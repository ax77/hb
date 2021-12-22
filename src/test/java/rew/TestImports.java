package rew;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ast_parsers.ParsePackageName;
import ast_symtab.Keywords;
import errors.AstParseException;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Stream;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;
import utils_oth.Normalizer;

public class TestImports {

  private String getFullFilenameFromImport(String input) {
    if (!input.contains("::")) {
      throw new AstParseException("import name is incorrect: " + input);
    }

    final String dir = System.getProperty("user.dir");
    String found = input.substring(0, input.lastIndexOf("::")).trim();

    found = dir + "/" + found.replace('.', '/');
    found = Normalizer.normalize(found);

    return found.trim() + ".hb";
  }

  class ImportsFound {
    final Set<String> result;
    final Set<String> importsNames;

    public ImportsFound() {
      this.result = new HashSet<>();
      this.importsNames = new HashSet<>();
    }

    public Set<String> getResult() {
      return result;
    }

    public Set<String> getImportsNames() {
      return importsNames;
    }

    public void addFullpath(String fullpath) {
      this.result.add(fullpath);
    }

    public void addImportName(String name) {
      this.importsNames.add(name);
    }

  }

  private ImportsFound prepareImportsDirty(String firstFile) throws IOException {

    final ImportsFound result = new ImportsFound();

    // without normalizations there may be possible
    // duplication if the path was given with straight or reverse slash /\
    // main/folder/file.hb and main\folder\file.hb
    firstFile = Normalizer.normalize(firstFile).trim();

    final LinkedList<String> stack = new LinkedList<String>();
    stack.add(firstFile);

    // imports we've already parsed
    final Set<String> processed = new HashSet<>();

    while (!stack.isEmpty()) {
      String curfile = stack.removeFirst();

      // we've already been read the full content
      // of the given file, parse that file, and 
      // extract all the imports from it.
      // it may happen - if we imported the same file
      // from many different places, a common practice.
      // for example: you import array.hb file 100 times per-project :)
      if (processed.contains(curfile)) {
        continue;
      }
      processed.add(curfile);

      final String content = new FileWrapper(curfile).readToString(FileReadKind.APPEND_LF);
      final Stream stream = new Stream(curfile, content);
      final Parse parser = new Parse(new Tokenlist(stream.getTokenlist()));
      final Set<String> importsFromTheFile = new HashSet<>();

      while (!parser.isEof()) {
        if (parser.is(Keywords.import_ident)) {
          String importname = ParsePackageName.parse(parser, Keywords.import_ident);
          importsFromTheFile.add(importname);
        } else {
          parser.move();
        }
      }

      for (String originalImportName : importsFromTheFile) {
        String fullpath = getFullFilenameFromImport(originalImportName);
        stack.addLast(fullpath);

        result.addFullpath(fullpath);
        result.addImportName(originalImportName);
      }

    }

    return result;

  }

  private String prepareTypesDirty(String path) throws IOException {

    final ImportsFound result = prepareImportsDirty(path);

    StringBuilder sb = new StringBuilder();

    sb.append("// all imports found\n\n");
    for (String s : result.getImportsNames()) {
      sb.append("import ");
      sb.append(s);
      sb.append(";\n");
    }

    for (String s : result.getResult()) {
      String content = new FileWrapper(s).readToString(FileReadKind.APPEND_LF).trim();
      content += "\n\n";
      sb.append(content);
    }

    return sb.toString();
  }

  @Test
  public void test1() throws IOException {

    final String dir = System.getProperty("user.dir");
    final String path = dir + "/tests/imports2/main.hb";

    System.out.println(prepareTypesDirty(path));
  }
}
