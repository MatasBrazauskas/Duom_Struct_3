package edu.ktu.ds.lab3.utils;

import java.util.Arrays;
import java.util.Objects;

public class HashMapOaWithoutDelete<K, V> extends HashMapOa<K, V> {

    public HashMapOaWithoutDelete(int initialCapacity, float loadFactor, HashManager.HashType ht, OpenAddressingType oaType) {
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

    public HashMapOaWithoutDelete() {

    }

    @Override
    public int hash(int hashCode, int tableSize, HashManager.HashType ht){
        return HashManager.hash(hashCode, tableSize, ht);
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value is null in put(K key, V value)");
        }
        int position = this.findPosition(key);
        if (position == -1) {
            rehash();
            return put(key, value);
        }

        if (table[position] == null) {
            table[position] = new Entry(key, value);
            size++;

            if (size > table.length * loadFactor) {
                rehash();
            } else {
                numberOfOccupied++;
                lastUpdated = position;
            }
        } else {
            table[position].value = value;
            lastUpdated = position;
        }

        return value;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in get(K key)");
        }

        int position = this.findPosition(key);
        if (position != -1 && table[position] != null) {
            return table[position].value;
        }

        return null;
    }

    @Override
    public V remove(K key) {
        //System.out.println("Remove called");
        if(key == null){
            throw new IllegalArgumentException("Key is null in remove(K key)");
        }

        int index = hash(key.hashCode(), table.length, ht);
        //System.out.println(String.format("Hash index for %s is %d", key, index));
        int findPosition = this.findPosition(key);
        //System.out.println(String.format("Position for %s is %d", key, findPosition));

        int position = index;
        int probCount = 0;

        while(table[position] != null) {
            var curr = table[position];
            //System.out.println(String.format("Trying to remove %s from position %d", curr, position));

            if(curr.key.equals(key)){
                //System.out.println(String.format("Removing %s from position %d", curr, position));

                var removedItem = curr.value;
                numberOfOccupied--;
                lastUpdated = position;

                this.shift(index, position, probCount, key);

                return removedItem;
            }
            position = calculatePosition(index, probCount++, key);
        }

        throw new IllegalArgumentException("End gentelmen");
    }

    protected void rehash() {
        HashMapOaWithoutDelete<K, V> newMap = new HashMapOaWithoutDelete<>(table.length * 2, loadFactor, ht, oaType);
        Arrays.stream(table).filter(Objects::nonNull).forEach(kvEntry -> newMap.put(kvEntry.key, kvEntry.value));
        table = newMap.table;
        numberOfOccupied = newMap.numberOfOccupied;
        lastUpdated = newMap.lastUpdated;
        rehashesCounter++;
    }

    private void shift(int index, int position, int probCount, K key) {
        //System.out.println("Is it even called?");
        int nextIndex = calculatePosition(index, probCount++, key);

        while(table[position] != null){
            table[position] = table[nextIndex];

            position = nextIndex;
            nextIndex = calculatePosition(index, probCount++, key);
        }

        table[position] = null;
    }

    public boolean replace(K key, V oldValue, V newValue) {

        if(key == null || oldValue == null || newValue == null){
            throw new IllegalArgumentException("Key, oldValue or newValue is null in replace(K key,  V oldValue, V newValue)");
        }

        int index = hash(key.hashCode(), table.length, ht);

        int probCount = 0;
        int position = index;

        for(var curr = table[index]; curr != null; curr = table[position], probCount++){

            if(curr != null && curr.key.equals(key)){
                if(curr.value.equals(oldValue)){
                    table[position].value = newValue;
                    lastUpdated = position;
                    return true;
                }
                return false;
            }
            position = calculatePosition(index, probCount, key);
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {

        if(value == null){
            throw new IllegalArgumentException("Value is null in containsValue(Object value)");
        }

        for(var i = 0; i < table.length; i++){
            var entry = table[i];
            if(entry != null && entry.value.equals(value)){
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            var entry = table[i];
            if (entry != null) {
                int hashIndex = hash(entry.key.hashCode(), table.length, ht);
                sb.append(String.format("[Arr index %d] %s (hash=%d)%n", i, entry, hashIndex));
            }
        }
        return sb.toString();
    }

    public void PrintTable(){
        for(var i = 0; i < table.length; i++){
            var entry = table[i];
            if(entry != null){
                System.out.println(String.format("[Arr index %d] %s", i, entry));
            }
            else {
                System.out.println(String.format("[Arr index %d] is null", i));
            }
        }
        System.out.println();
    }

    private int findPosition(K key) {
        int index = hash(key.hashCode(), table.length, ht);

        int position = index;
        for (int i = 0; i < table.length; i++) {
            if (table[position] == null) {
                return position;
            }

            if (table[position] != null && table[position].key.equals(key)) {
                return position;
            }

            position = calculatePosition(index, i, key);
        }
        return -1;
    }
}
