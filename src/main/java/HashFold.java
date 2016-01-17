import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class HashFold<S,K,V> {
    public static class Context<S,K,V> {
        public final HashFold<S,K,V> parent;
        public final Map<K, V> hashMap;
        public Context(HashFold<S,K,V> parent, Map<K,V> hashMap) {
            this.parent = parent;
            this.hashMap = hashMap;
        }
        public void write(K k, V v) {
            hashMap.merge(k, v, parent::fold);
        }
    }

    public final ExecutorService executor;

    public HashFold(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    public HashFold() {
        executor = Executors.newCachedThreadPool();
    }

    public Map<K,V> start(Collection<S> inputs) {
        ConcurrentMap<K,V> hashMap = new ConcurrentHashMap<>();
        Context<S,K,V> context = new Context<>(this, hashMap);
        for(S input: inputs) {
            executor.submit(() -> map(input, context));
        }
        return hashMap;
    }

    public abstract void map(S input, Context<S,K,V> context);
    public abstract V fold(V v1, V v2);
}
