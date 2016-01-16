import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WordCount extends HashFold1<String, String, Integer> {
    public static void main(String[] args) {
        WordCount wordCount = new WordCount();
        List<String> inputs = Arrays.asList(args);
        Map<String, Integer> h = wordCount.start(inputs);
    }

    public final Set<String> stopWords;

    public WordCount() {
        stopWords =  new HashSet<>(Arrays.asList("a", "an", "and", "are", "as", "be", "for", "if", "in", "is", "it", "of", "or", "the", "to", "with"));
    }

    public class MapIterator implements Iterator<Map.Entry<String,Integer>> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Map.Entry<String, Integer> next() {
            return null;
        }
    }

    @Override
    public Map<String, Integer> map(String source) {
        Map<String, Integer> hashmap = new HashMap<>();
        Path path = FileSystems.getDefault().getPath(source);
        try(BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while((line = reader.readLine()) != null) {
                for (String word : line.split("[!#$]")) {
                    word = word.toLowerCase();
                    hashmap.merge(word, 1, (v1, v2) -> v1 + v2);
                }
            }
            return hashmap;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }

    @Override
    public Integer fold(Integer v1, Integer v2) {
        return v1 + v2;
    }
}
