/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.algebraicPetriNet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.yapnu.adt.Term;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author adrien
 */
public class AlgebraicPetriNetSimpleSequencer {
    
    private final Random random;
    private final AlgebraicPetriNet petriNet;
    private final LinkedList<Integer> firedTransitions = new LinkedList<Integer>();
    private final LinkedList<ImmutableList<ImmutableMultiset<Term>>> markings = new LinkedList<ImmutableList<ImmutableMultiset<Term>>>();
    private long maxTransitionsFired = 10000;

    public AlgebraicPetriNetSimpleSequencer(Random random, AlgebraicPetriNet petriNet) {
        this.random = random;
        this.petriNet = petriNet;

        this.firedTransitions.add(-1);
        this.markings.add(this.petriNet.getMarking());
    }

    public long getMaxTransitionsFired() {
        return maxTransitionsFired;
    }

    public void setMaxTransitionsFired(long maxTransitionsFired) {
        this.maxTransitionsFired = maxTransitionsFired;
    }

    public LinkedList<Integer> getFiredTransitions() {
        return firedTransitions;
    }

    public void deploy() {
        int nbTransitionFired = 0;
        while (nbTransitionFired < this.maxTransitionsFired) {
            ArrayList<Integer> availableTransitions = this.getAvailableTransitions();
            if (availableTransitions.size() == 0) {
                return;
            }

            int selectedTransitionIndex = random.nextInt(availableTransitions.size());
            int selectedTransition = availableTransitions.get(selectedTransitionIndex);
            this.petriNet.fireTransition(selectedTransition);

            this.firedTransitions.add(selectedTransition);
            this.markings.add(this.petriNet.getMarking());
            nbTransitionFired++;
        }
    }

    private ArrayList<Integer> getAvailableTransitions() {
        ArrayList<Integer> availableTransitions = new ArrayList<Integer>();
        for (int iT = 0; iT < this.petriNet.getNbTransitions(); iT++) {
            boolean isAvailable = true;
            boolean isUsed = false;

            for (int iP = 0; iP < this.petriNet.getNbPlaces(); iP++) {
                if (this.petriNet.getInput().isTransitionDefined(iP, iT)) {
                    isUsed = true;
                }

                if (!this.petriNet.getInput().isContainedIn(iP, iT, this.petriNet.getMarking(iP))) {
                    isAvailable = false;
                    break;
                }
            }

            if (isUsed && isAvailable) {
                availableTransitions.add(iT);
            }
        }

        return availableTransitions;
    }
}
