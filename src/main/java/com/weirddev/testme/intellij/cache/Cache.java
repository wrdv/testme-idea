package com.weirddev.testme.intellij.cache;

public interface Cache<K, V> {

    /**
     * Retrieves an item from the cache by its key. If the item is not present,
     * the supplied lambda expression is used to compute the value and store it in the cache.
     *
     * @param key the key of the item to retrieve.
     * @param valueSupplier a lambda expression or method reference that supplies the value if it's missing.
     * @return the value associated with the key, either from the cache or computed by the supplied lambda.
     */
    V getOrCompute(K key, java.util.function.Supplier<V> valueSupplier);

    LruCache.CacheStats getUsageStats();
}
