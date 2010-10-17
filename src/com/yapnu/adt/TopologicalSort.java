/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.HashMultimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author adrien
 */
public class TopologicalSort<T> {

    private final HashMultimap<T, T> connectivityList;
    private final LinkedList<T> sortedElements = new LinkedList<T>();
    private final HashSet<T> hasBeenVisisted = new HashSet<T>();

    public TopologicalSort(HashMultimap<T, T> connectivityList) {
        this.connectivityList = connectivityList;
    }

    public LinkedList<T> getSortedElements() {
        return sortedElements;
    }


    public void sort() {
        for (T node : this.getNodesWithNoIncomingEdge()) {
            this.visit(node);
        }
    }

   private HashSet<T> getNodesWithNoIncomingEdge() {
       HashSet<T> nodes = new HashSet<T>(this.connectivityList.keySet());
       for (T dependentNode : this.connectivityList.values()) {
           nodes.remove(dependentNode);
       }

       return nodes;
   }

    private void visit(T node) {
        if (!this.hasBeenVisisted.contains(node)) {
            this.hasBeenVisisted.add(node);
            for (T connectedNode : this.connectivityList.get(node)) {
                if (connectedNode != null) {
                    this.visit(connectedNode);
                }
            }

            this.sortedElements.addLast(node);
        }
    }
}
