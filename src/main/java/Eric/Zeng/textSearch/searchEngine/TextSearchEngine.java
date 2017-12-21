package Eric.Zeng.textSearch.searchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class TextSearchEngine {
  private static final String REGEX = "(\\s+|\\s*,\\s*|\\s*\\.\\s*)"; // deal with space, comma and
                                                                      // full stop

  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No directory given to index.");
    }
    final File indexableDirectory = new File(args[0]);

    List<File> allTextFiles = getAllTextFiles(indexableDirectory);
    Scanner keyboard = null;
    try {
      keyboard = new Scanner(System.in);
      while (true) {
        System.out.print("Search>");
        final String line = keyboard.nextLine();
        if (line.equals("quit")) {
          break;
        } else {
          System.out.println(getSearchResult(line, allTextFiles));
        }
      }
    } finally {
      if (keyboard != null) {
        keyboard.close();
      }
    }
  }

  protected static String getSearchResult(String line, List<File> allTextFiles) {
    if (line.isEmpty()) {
      return "Illegal empty search keyword.";
    }
    String[] keywords = line.split(" ");
    StringBuilder sb = new StringBuilder();
    for (File file : allTextFiles) {
      String result = getMatchedResult(keywords, file);
      if (result != null) {
        sb.append(result + System.lineSeparator());
      }
    }
    return sb.toString().isEmpty() ? "No match found" : sb.toString();
  }

  protected static String getMatchedResult(String[] keywords, File file) {
    TrieNode root = buildTrie(file);
    double total = keywords.length;
    int count = 0;
    for (String keyword : keywords) {
      if (findWord(root, keyword)) {
        count++;
      }
    }
    if (count == 0) {
      return null;
    } else {
      return file.getName() + ": " + String.format("%.2f", count * 100.0 / total) + "%";
    }
  }

  private static TrieNode buildTrie(File file) {
    TrieNode root = new TrieNode();
    try (InputStream in = Files.newInputStream(file.toPath());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        String[] words = getWords(line);
        for (String word : words)
          insertAWordToTrie(root, word);
      }
    } catch (IOException x) {
      System.err.println(x);
    }

    return root;
  }

  private static void insertAWordToTrie(TrieNode root, String word) {
    TrieNode cur = root;
    if (word.isEmpty()) {
      return;
    }
    for (char c : word.toCharArray()) {
      if (cur.children[c - '!'] != null) {
        cur = cur.children[c - '!'];
      } else {
        TrieNode newNode = new TrieNode();
        cur.children[c - '!'] = newNode;
        cur = newNode;
      }
    }
    cur.isWord = true;
  }

  private static boolean findWord(TrieNode root, String word) {
    if (word.isEmpty()) {
      return false;
    }
    TrieNode cur = root;
    for (char c : word.toCharArray()) {
      if (cur.children[c - '!'] != null) {
        cur = cur.children[c - '!'];
      } else {
        return false;
      }
    }
    return cur.isWord;
  }

  protected static String[] getWords(String line) {
    final Pattern p = Pattern.compile(REGEX);
    return p.split(line);
  }

  protected static List<File> getAllTextFiles(File dir) {
    if (!dir.exists()) {
      throw new IllegalArgumentException("Directory does not exist.");
    }
    File[] listOfFiles = dir.listFiles();
    List<File> result = new ArrayList<>();
    for (File file : listOfFiles) {
      if (file.isFile() && file.getName().endsWith(".txt")) {
        result.add(file);
      }
    }
    return result;
  }

  private static class TrieNode {
    private TrieNode[] children;
    private boolean isWord;

    private TrieNode() {
      children = new TrieNode[95]; // support ASCII code 33 - 127. space excluded
      isWord = false;
    }
  }
}
