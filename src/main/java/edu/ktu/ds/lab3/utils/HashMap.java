package edu.ktu.ds.lab3.utils;

import java.util.Arrays;

public class HashMap<K, V> implements EvaluableMap<K, V> {

    protected static class Node<K, V> {

        protected K key;
        protected V value;
        protected Node<K, V> next;

        protected Node() {
        }

        protected Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    public static final int DEFAULT_INITIAL_CAPACITY = 8;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final HashManager.HashType DEFAULT_HASH_TYPE = HashManager.HashType.DIVISION;

    protected Node<K, V>[] table;
    protected int size = 0;
    protected float loadFactor;
    protected HashManager.HashType ht;

    protected int maxChainSize = 0;
    protected int rehashesCounter = 0;
    protected int lastUpdatedChain = 0;
    protected int chainsCounter = 0;

    public HashMap() {
        this(DEFAULT_HASH_TYPE);
    }

    public HashMap(HashManager.HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, ht);
    }

    public HashMap(int initialCapacity, HashManager.HashType ht) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    public HashMap(float loadFactor, HashManager.HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor, ht);
    }

    public HashMap(int initialCapacity, float loadFactor, HashManager.HashType ht) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if ((loadFactor <= 0.0) || (loadFactor > 1.0)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.ht = ht;
    }

    public int hash(int hashCode, int tableSize, HashManager.HashType ht) {
        return HashManager.hash(hashCode, tableSize, ht);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        lastUpdatedChain = 0;
        maxChainSize = 0;
        rehashesCounter = 0;
        chainsCounter = 0;
    }

    @Override
    public boolean contains(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in contains(K key)");
        }

        return get(key) != null;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value is null in put(K key, V value)");
        }
        int index = hash(key.hashCode(), table.length, ht);
        if (table[index] == null) {
            chainsCounter++;
        }

        Node<K, V> node = getInChain(key, table[index]);
        if (node == null) {
            table[index] = new Node<>(key, value, table[index]);
            size++;

            if (size > table.length * loadFactor) {
                rehash();
            } else {
                lastUpdatedChain = index;
            }
        } else {
            node.value = value;
            lastUpdatedChain = index;
        }

        return value;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in get(K key)");
        }

        int index = hash(key.hashCode(), table.length, ht);
        Node<K, V> node = getInChain(key, table[index]);
        return node == null ? null : node.value;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in remove(K key)");
        }

        int index = hash(key.hashCode(), table.length, ht);

        for (Node<K, V> curr = table[index], prev = null; curr != null; prev = curr, curr = curr.next) {

            var oldVal = curr.value;

            if (curr.key.equals(key)) {

                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }

                if (table[index] == null) {
                    chainsCounter--;
                }

                lastUpdatedChain = index;
                size--;
                return oldVal;
            }
        }

        throw new IllegalArgumentException("Remove rakto nera Linked List");
    }

    private void rehash() {
        HashMap<K, V> newMap = new HashMap<>(table.length * 2, loadFactor, ht);
        for (int i = 0; i < table.length; i++) {
            while (table[i] != null) {
                newMap.put(table[i].key, table[i].value);
                table[i] = table[i].next;
            }
        }
        table = newMap.table;
        maxChainSize = newMap.maxChainSize;
        chainsCounter = newMap.chainsCounter;
        lastUpdatedChain = newMap.lastUpdatedChain;
        rehashesCounter++;
    }

    private Node<K, V> getInChain(K key, Node<K, V> node) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in getInChain(K key, Node node)");
        }
        int chainSize = 0;
        for (Node<K, V> n = node; n != null; n = n.next) {
            chainSize++;
            if (n.key.equals(key)) {
                return n;
            }
        }
        maxChainSize = Math.max(maxChainSize, chainSize + 1);
        return null;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();

        for (Node<K, V> node : table) {
            if (node != null) {

                var llsb = new StringBuilder();

                sb.append("[" + node.key + "] = ");

                for (var i = node; i != null; i = i.next) {
                    llsb.append("( " + i.key + ":" + i.value + " ) -> ");
                }

                sb.append(llsb.toString() + "null\n");
            }
        }
        return sb.toString();
    }

    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new IllegalArgumentException("Raktas, sena arba nauja verte yra nulines asile!!!");
        }

        int index = hash(key.hashCode(), table.length, ht);
        var node = getInChain(key, table[index]);

        if(node != null && node.value.equals(oldValue)){
            node.value = newValue;
            lastUpdatedChain = index;
            return true;
        }

        return false;
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value is null in containsValue(Object value)");
        }

        for(var node : table){
            if(node != null){
                for(var i = node; i != null; i = i.next){
                    if(i.value.equals(value)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public int getMaxChainSize() {
        return maxChainSize;
    }

    @Override
    public int getRehashesCounter() {
        return rehashesCounter;
    }

    @Override
    public int getTableCapacity() {
        return table.length;
    }

    @Override
    public int getLastUpdated() {
        return lastUpdatedChain;
    }

    @Override
    public int getNumberOfOccupied() {
        return chainsCounter;
    }
}
