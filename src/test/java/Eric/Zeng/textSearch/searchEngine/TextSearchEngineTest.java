package Eric.Zeng.textSearch.searchEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class TextSearchEngineTest {
  @Test
  public void testGetWords() {
    String[] result = TextSearchEngine.getWords("Word1, word2, word3. Word4 word5.");
    assertEquals(5, result.length);
    assertTrue(result[0].equals("Word1"));
    assertTrue(result[1].equals("word2"));
    assertTrue(result[2].equals("word3"));
    assertTrue(result[3].equals("Word4"));
    assertTrue(result[4].equals("word5"));
  }

  @Test
  public void testGetAllTextFiles() {
    File dir = new File(getClass().getClassLoader().getResource(".").getFile());
    List<File> res = TextSearchEngine.getAllTextFiles(dir);
    assertEquals(3, res.size());
  }

  @Test(expected = IllegalArgumentException.class)
  // TODO: need improve - see comment
  public void testGetAllTextFiles_InvalidDir() {
    File dir = new File("C:\\testSearch");// I used windows, this need to be improved to can be used
                                          // on any system.
    List<File> res = TextSearchEngine.getAllTextFiles(dir);
    assertEquals(3, res.size());
  }

  @Test
  public void testGetMatchedResult() {
    String[] words = {"This", "building", "interactive"};
    File file = new File(getClass().getClassLoader().getResource("test1.txt").getFile());
    String res = TextSearchEngine.getMatchedResult(words, file);
    assertTrue(res.equals("test1.txt: 100.00%"));

    file = new File(getClass().getClassLoader().getResource("test2.txt").getFile());
    String res2 = TextSearchEngine.getMatchedResult(words, file);
    assertTrue(res2.equals("test2.txt: 66.67%"));

    file = new File(getClass().getClassLoader().getResource("test4.txt").getFile());
    String res3 = TextSearchEngine.getMatchedResult(words, file);
    assertTrue(res3.equals("test4.txt: 66.67%"));
  }

  @Test
  public void testGetMatchedResult_NotFound() {
    String[] words = {"Thisa", "buildingb", "interactivec"};
    File file = new File(getClass().getClassLoader().getResource("test1.txt").getFile());
    String res = TextSearchEngine.getMatchedResult(words, file);
    assertNull(res);
  }


}
