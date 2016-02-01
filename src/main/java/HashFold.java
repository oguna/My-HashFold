import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiFunction;

public abstract class HashFold<S,K,V> {
    public static class Context<K,V> {
        public final BiFunction<V,V,V> fold;
        public final Map<K, V> hashMap;
        public Context(BiFunction<V,V,V> fold, Map<K,V> hashMap) {
            this.fold = fold;
            this.hashMap = hashMap;
        }
        public void write(K k, V v) {
            hashMap.merge(k, v, fold);
        }
    }

    public final ExecutorService executor;

    public HashFold(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    public HashFold() {
        executor = Executors.newCachedThreadPool();
    }

    public Map<K,V> start(Collection<S> inputs) throws InterruptedException {
        ConcurrentMap<K,V> hashMap = new ConcurrentHashMap<>();
        Context<K,V> context = new Context<>(this::fold, hashMap);
        for(S input: inputs) {
            executor.submit(() -> map(input, context));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
        return hashMap;
    }

    public abstract void map(S input, Context<K,V> context);
    public abstract V fold(V v1, V v2);
}
