package com.weirddev.testme.intellij.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class LruCache<K, V> implements Cache<K, V> {
    private final Map<K, V> cache;
    private AtomicLong requests;
    private AtomicLong hits;

    public LruCache(final int maxSize) {
        this.cache = Collections.synchronizedMap(new LinkedHashMap<K, V>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        });
        requests = new AtomicLong();
        hits = new AtomicLong();
    }

    @Override
    public V getOrCompute(K key, Supplier<V> valueSupplier) {
        // Synchronized block to ensure thread safety
        synchronized (cache) {
            requests.incrementAndGet();
            // Return value from cache if present
            if (cache.containsKey(key)) {
                hits.incrementAndGet();
                return cache.get(key);
            }
            // Compute value if absent, store in cache, and return
            V value = valueSupplier.get();
            cache.put(key, value);
            return value;
        }
    }
    @Override
    public CacheStats getUsageStats() {
        return new CacheStats(hits.get(), requests.get(), cache.size());
    }

    public static class CacheStats {
        private final long hits;
        private final long requests;
        private final long size;

        public CacheStats(long hits, long requests, long size) {
            this.hits = hits;
            this.requests = requests;
            this.size = size;
        }

        @Override
        public String toString() {
            return "hits/req:" + hits + "/"+ requests + ". size:" + size;
        }
    }
}
