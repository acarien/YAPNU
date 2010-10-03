/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.petriNets;

import java.util.LinkedList;

/**
 *
 * @author adrien
 */
public interface PetriNetSequencer {
    public void deploy();

    public long getMaxTransitionsFired();

    public void setMaxTransitionsFired(long maxTransitionsFired);

    public LinkedList<Integer> getFiredTransitions();

    public LinkedList<int[]> getMarkings();
}
