package ast_main.imports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ast_parsers.ParsePackageName;
import ast_symtab.Keywords;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Env;
import tokenize.Stream;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;
import utils_oth.Normalizer;

public abstract class ImportsPreparer {

  static class ImportsFound {
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

  private static ImportsFound prepareImportsDirty(String firstFile) throws IOException {

    final ImportsFound result = new ImportsFound();

    // without normalizations there may be possible
    // duplication if the path was given with straight or reverse slash /\
    // main/folder/file.hb and main\folder\file.hb
    firstFile = Normalizer.normalize(firstFile).trim();
    result.addFullpath(firstFile); // it's important not to forget this very file :)

    final LinkedList<String> stack = new LinkedList<String>();
    stack.add(firstFile);

    // Let's add all of the predefined classes.
    // We almost always need these classes to be imported.
    final String dir = System.getProperty("user.dir");
    final String[] names = { "arr", "string" };
    for (String s : names) {
      final String nativeFileName = Normalizer.normalize(dir + "/std/natives/" + s + ".hb");
      stack.add(nativeFileName);
      result.addFullpath(nativeFileName);
    }

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
        String fullpath = ImportNamesBuilder.getFullFilenameFromImport(originalImportName);
        stack.addLast(fullpath);

        result.addFullpath(fullpath);
        result.addImportName(originalImportName);
      }

    }

    return result;

  }

  private static void removeEOF(List<Token> tokenlist) {
    if (!tokenlist.isEmpty()) {
      final int lastIdx = tokenlist.size() - 1;
      if (tokenlist.get(lastIdx).typeIsSpecialStreamMarks()) {
        tokenlist.remove(lastIdx);
      }
    }
  }

  public static List<Token> prepareTypesDirty(String path) throws IOException {

    final ImportsFound result = prepareImportsDirty(path);

    StringBuilder sb = new StringBuilder();
    for (String s : result.getImportsNames()) {
      sb.append("import ");
      sb.append(s);
      sb.append(";\n");
    }

    List<Token> tokens = new ArrayList<Token>();

    final List<Token> forwardImportTokens = new Stream("builtin", sb.toString()).getTokenlist();
    removeEOF(forwardImportTokens);
    tokens.addAll(forwardImportTokens);

    for (String s : result.getResult()) {
      String content = new FileWrapper(s).readToString(FileReadKind.APPEND_LF);
      final Stream stream = new Stream(s, content);

      final List<Token> tokenlist = stream.getTokenlist();
      removeEOF(tokenlist);
      tokens.addAll(tokenlist);
    }

    tokens.add(Env.EOF_TOKEN_ENTRY);
    return tokens;
  }

}
