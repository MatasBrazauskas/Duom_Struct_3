package edu.ktu.ds.lab3.demo;

import edu.ktu.ds.lab3.utils.HashManager;
import edu.ktu.ds.lab3.utils.HashMapOaWithoutDelete;

public class HashMapOaWithoutDeleteCollisions<K, V> extends HashMapOaWithoutDelete<K, V> {

    @Override
    public int hash(int hashCode, int tableSize, HashManager.HashType ht){
        return 1;
    }
}
