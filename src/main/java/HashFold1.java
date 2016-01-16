import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HashFold1<S,K,V> implements IHashFold<S,K,V> {
    public Map<K,V> start(List<S> inputs) {
        Map<K,V> hash = new HashMap<>();
        for(S input : inputs) {
            Map<K,V> localHash = map(input);
            hash.putAll(localHash);
        }
        return hash;
    }
}
