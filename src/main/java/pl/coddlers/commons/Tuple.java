package pl.coddlers.commons;

import lombok.Data;

@Data
public class Tuple<K, V> {
    public final K key;
    public final V value;

    public Tuple(K k, V v) {
        this.key = k;
        this.value = v;
    }
}
