package edu.ktu.ds.lab3.demo;

import edu.ktu.ds.lab3.utils.HashManager;
import edu.ktu.ds.lab3.utils.HashMap;

public class HashMapLLCollisions<K,V> extends HashMap<K,V>
{

    @Override
    public int hash(int hashCode, int tableSize, HashManager.HashType ht){
        return 1;
    }
}
