package edu.ktu.ds.lab3.utils;

public class HashMapOaWithoutDelete<K, V> extends HashMapOa<K, V> {
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
        if(key == null){
            throw new IllegalArgumentException("Key is null in remove(K key)");
        }

        int index = hash(key.hashCode(), table.length, ht);

        int position = index;

        //for(var curr = table[position]; curr != null; curr = table[position]){
        for(int i = 0; table[position] != null ; i++){
            System.out.println(table[position] != null ? table[position].key : "null");

            if(table[position] != null && table[position].key.equals(key)){
                System.out.println(String.format("Removing %s from position %d", table[position], position));
                var removedItem = table[position].value;
                numberOfOccupied--;
                lastUpdated = position;

                this.shift(index, position, i, key);

                return removedItem;
            }
            position = calculatePosition(index, i, key);
        }

        throw new IllegalArgumentException("");
    }

    private void shift(int index, int position, int probCount, K key) {
        System.out.println("Is it even called?");
        int nextIndex = calculatePosition(index, probCount++, key);

        while(table[position] != null){
            table[position] = table[nextIndex];
            table[nextIndex] = null;

            position = nextIndex;
            nextIndex = calculatePosition(index, probCount++, key);
        }
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
                sb.append(String.format("[%d] %s (hash=%d)%n", i, entry, hashIndex));
            }
        }
        return sb.toString();
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
