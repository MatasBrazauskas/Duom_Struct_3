package edu.ktu.ds.lab3.demo;

import edu.ktu.ds.lab3.utils.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class HashMapTestsLL
{

    private HashMap<Integer, Integer> map;
    @BeforeEach
    public void addItems(){
        map = new HashMap<>();

        for(int i = 0; i < 5; i++){
            map.put(i, i * 2);
        }
    }

    @Test
    public void removeOnEmptyMap()
    {
        this.map = new HashMap<>();
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
    public void removeColOnEmpty(){
        var colMap = new HashMapCollisions<Integer, Integer>();
        assertThrows(IllegalArgumentException.class, () -> colMap.remove(1));
        System.out.println("Map\n" + colMap.toString());
    }

    @Test
    public void removeColThenNotFound(){
        var colMap = new HashMapCollisions<Integer, Integer>();

        for(int i = 0; i < 5; i++){
            colMap.put(i, i * 2);
        }

        assertThrows(IllegalArgumentException.class, () -> colMap.remove(-1));
        System.out.println("Map\n" + colMap.toString());
    }

    @Test
    public void removeColThenFound(){
        var colMap = new HashMapCollisions<Integer, Integer>();

        for(int i = 0; i < 5; i++){
            colMap.put(i, i * 2);
        }

        System.out.println("Map\n" + colMap.toString());

        assertEquals(2, colMap.remove(1));
        assertEquals(0, colMap.remove(0));
        assertThrows(IllegalArgumentException.class, () -> colMap.remove(0));
        assertEquals(8, colMap.remove(4));
        System.out.println("Map\n" + colMap.toString());
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

    @Test
    public void containsColNull(){
        var colMap = new HashMapCollisions<Integer, Integer>();

        for(int i = 0; i < 5; i++){
            colMap.put(i, i * 2);
        }

        assertThrows(IllegalArgumentException.class, () -> colMap.contains(null));
        System.out.println("Map\n" + colMap.toString());
    }

    @Test
    public void containsColNotFound(){
        var colMap = new HashMapCollisions<Integer, Integer>();

        for(int i = 0; i < 5; i++){
            colMap.put(i, i * 2);
        }

        assertFalse(colMap.contains(-1));
        System.out.println("Map\n" + colMap.toString());
    }

    @Test
    public void containsColFound(){
        var colMap = new HashMapCollisions<Integer, Integer>();

        for(int i = 0; i < 5; i++){
            colMap.put(i, i * 2);
        }

        assertTrue(colMap.contains(1));
        System.out.println("Map\n" + colMap.toString());
    }

    @Test
    public void replaceNotFound(){
        var colMap = new HashMapCollisions<Integer, Integer>();

        for(int i = 0; i < 5; i++){
            colMap.put(i, i * 2);
        }

        assertFalse(colMap.replace(-1, 0, 0));
        System.out.println("Map\n" + colMap.toString());
    }

    @Test
    public void replaceFound(){
        var colMap = new HashMapCollisions<Integer, Integer>();

        for(int i = 0; i < 5; i++){
            colMap.put(i, i * 2);
        }

        assertTrue(colMap.replace(1, 2, -2));
        System.out.println("Map\n" + colMap.toString());

        assertTrue(colMap.replace(0, 0, 100));
        System.out.println("Map\n" + colMap.toString());
    }
}
