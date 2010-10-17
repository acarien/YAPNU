/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class TopologicalSortTest {

    /**
     * Test of sort method, of class TopologicalSort.
     */
    @Test
    public void testAllElementsAreSorted() {
        HashMultimap<Integer, Integer> connectivityList = HashMultimap.create();
        connectivityList.put(0, 1);
        connectivityList.put(0, 2);

        TopologicalSort<Integer> instance = new TopologicalSort<Integer>(connectivityList);
        instance.sort();
        ArrayList<Integer> sortedElements = new ArrayList<Integer>(instance.getSortedElements());
        assertTrue(sortedElements.size() == 3);
        assertTrue(sortedElements.get(0) == 1);
        assertTrue(sortedElements.get(1) == 2);
        assertTrue(sortedElements.get(2) == 0);
    }

    @Test
    public void testSortingIsStable() {
        HashMultimap<Integer, Integer> connectivityList = HashMultimap.create();
        connectivityList.put(0, 1);
        connectivityList.put(0, 2);
        connectivityList.put(1, 3);

        TopologicalSort<Integer> instance = new TopologicalSort<Integer>(connectivityList);
        instance.sort();
        ArrayList<Integer> sortedElements = new ArrayList<Integer>(instance.getSortedElements());
        assertTrue(sortedElements.size() == 4);
        assertTrue(sortedElements.get(0) == 3);
        assertTrue(sortedElements.get(1) == 1);
        assertTrue(sortedElements.get(2) == 2);
        assertTrue(sortedElements.get(3) == 0);

        instance.sort();
        sortedElements = new ArrayList<Integer>(instance.getSortedElements());
        assertTrue(sortedElements.size() == 4);
        assertTrue(sortedElements.get(0) == 3);
        assertTrue(sortedElements.get(1) == 1);
        assertTrue(sortedElements.get(2) == 2);
        assertTrue(sortedElements.get(3) == 0);
    }

    @Test
    public void testSortingHandleCycle() {
        HashMultimap<Integer, Integer> connectivityList = HashMultimap.create();
        connectivityList.put(0, 1);
        connectivityList.put(1, 2);
        connectivityList.put(2, 0);

        TopologicalSort<Integer> instance = new TopologicalSort<Integer>(connectivityList);
        instance.sort();
        ArrayList<Integer> sortedElements = new ArrayList<Integer>(instance.getSortedElements());
        assertTrue(sortedElements.size() == 0);        
    }

    @Test
    public void testSortingGeneralCase() {
        HashMultimap<Integer, Integer> connectivityList = HashMultimap.create();
        connectivityList.put(0, 1);
        connectivityList.put(0, 2);
        connectivityList.put(1, 3);
        connectivityList.put(1, 2);
        connectivityList.put(2, 4);
        connectivityList.put(2, 5);
        connectivityList.put(6, null);
        connectivityList.put(7, 8);

        TopologicalSort<Integer> instance = new TopologicalSort<Integer>(connectivityList);
        instance.sort();
        ArrayList<Integer> sortedElements = new ArrayList<Integer>(instance.getSortedElements());        
        assertTrue(sortedElements.size() == 9);      
        assertTrue(sortedElements.get(0) == 4);
        assertTrue(sortedElements.get(1) == 5);
        assertTrue(sortedElements.get(2) == 2);
        assertTrue(sortedElements.get(3) == 3);
        assertTrue(sortedElements.get(4) == 1);
        assertTrue(sortedElements.get(5) == 0);
        assertTrue(sortedElements.get(6) == 6);
        assertTrue(sortedElements.get(7) == 8);
        assertTrue(sortedElements.get(8) == 7);
    }

}