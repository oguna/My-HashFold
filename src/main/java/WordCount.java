import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordCount extends HashFold<String, String, Integer> {
    public static void main(String[] args) {
        WordCount wordCount = new WordCount();
        List<String> inputs = Arrays.asList(args);
        Map<String, Integer> hash = wordCount.start(inputs);
        hash.entrySet().stream().sorted((e1, e2) -> e2.getValue() - e1.getValue()).limit(20).forEach(e -> System.out.format("%d: %s%n", e.getValue(), e.getKey()));
    }

    public final Set<String> stopWords;
    public final String delimiter;

    public WordCount() {
        stopWords =  new HashSet<>(Arrays.asList("a", "an", "and", "are", "as", "be", "for", "if", "in", "is", "it", "of", "or", "the", "to", "with"));
        delimiter = "!#\"$%&'()*+,-./:;<=>?@[\\]^_`{|}~ ";
    }

    @Override
    public void map(String source, Context<String,String,Integer> context) {
        Path path = Paths.get(source);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, delimiter);
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    token = token.toLowerCase();
                    if (!stopWords.contains(token)) {
                        context.write(token, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer fold(Integer v1, Integer v2) {
        return v1 + v2;
    }
}
