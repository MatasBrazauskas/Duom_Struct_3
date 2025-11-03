package edu.ktu.ds.lab3.demo;

import edu.ktu.ds.lab3.utils.HashMapOa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashMapTestOA {
    private HashMapOa<Integer, Integer> map;
    @BeforeEach
    public void addItems(){
        map = new HashMapOa<>();

        for(int i = 0; i < 5; i++){
            map.put(i, i * 2);
        }
    }

    @Test
    public void removeOnEmptyMap()
    {
        this.map = new HashMapOa<>();
        assertThrows(IllegalArgumentException.class, () -> map.remove(1));
        System.out.println("Map\n" + map.toString());
    }

    @Test
    public void removeNull()
    {
        assertThrows(IllegalArgumentException.class, () -> map.remove(null));
        System.out.println("Map\n" + map.toString());
    }

    @Test
    public void removeThenNotFound(){
        assertThrows(IllegalArgumentException.class, () -> map.remove(-1));
        System.out.println("Map\n" + map.toString());
    }

    @Test
    public void removeThenFound(){
        assertEquals(2, map.remove(1));
        System.out.println("Map\n" + map.toString());
    }

    @Test
    public void containsNull(){
        assertThrows(IllegalArgumentException.class, () -> map.contains(null));
        System.out.println("Map\n" + map.toString());
    }

    @Test
    public void containsNotFound(){
        assertFalse(map.contains(-1));
        System.out.println("Map\n" + map.toString());
    }

    @Test
    public void containsFound(){
        assertTrue(map.contains(1));
        System.out.println("Map\n" + map.toString());
    }
}
