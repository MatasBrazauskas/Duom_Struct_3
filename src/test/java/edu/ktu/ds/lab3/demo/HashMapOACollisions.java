package edu.ktu.ds.lab3.demo;

import edu.ktu.ds.lab3.utils.HashMapOa;
import edu.ktu.ds.lab3.utils.HashManager;

public class HashMapOACollisions<K, V> extends HashMapOa<K, V>
{
    @Override
    public int hash(int hashCode, int tableSize, HashManager.HashType ht){
        return 1;
    }
}
