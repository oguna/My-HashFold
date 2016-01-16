import java.util.List;
import java.util.Map;

public interface IHashFold<S,K,V> {
    V start(List<K> inputs);
    Map<K,V> map(S input);
    V fold(V v1, V v2);
}
