package edu.ktu.ds.lab3.demo;

import edu.ktu.ds.lab3.utils.HashManager;
import edu.ktu.ds.lab3.utils.HashMapOa;
import edu.ktu.ds.lab3.utils.HashMapOaWithoutDelete;

import java.util.Arrays;
import java.util.Objects;

public class HashMapOaWithoutDeleteCollisions<K, V> extends HashMapOaWithoutDelete<K, V> {

    public HashMapOaWithoutDeleteCollisions(int initialCapacity, float loadFactor, HashManager.HashType ht, OpenAddressingType oaType) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if ((loadFactor <= 0.0) || (loadFactor > 1.0)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.table = new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.ht = ht;
        this.oaType = oaType;
    }

    public HashMapOaWithoutDeleteCollisions() {}

    public HashMapOaWithoutDeleteCollisions(boolean dummy) {
        this.DEFAULT_OPEN_ADDRESSING_TYPE = OpenAddressingType.DOUBLE_HASHING;
    }

    @Override
    public int hash(int hashCode, int tableSize, HashManager.HashType ht){
        return 1;
    }

    @Override
    protected void rehash() {
        HashMapOaWithoutDeleteCollisions<K, V> newMap = new HashMapOaWithoutDeleteCollisions<>(table.length * 2, loadFactor, ht, oaType);
        Arrays.stream(table).filter(Objects::nonNull).forEach(kvEntry -> newMap.put(((Entry<K, V>) kvEntry).key, ((Entry<K, V>) kvEntry).value));
        table = ((HashMapOaWithoutDelete<K, V>) newMap).table;
        numberOfOccupied = ((HashMapOaWithoutDelete<K, V>) newMap).getNumberOfOccupied();
        lastUpdated = ((HashMapOaWithoutDelete<K, V>) newMap).getLastUpdated();
        rehashesCounter++;
    }
}
