/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.petriNet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author adrien
 */
public class PetriNetFastSequencer implements PetriNetSequencer {

    private final Random random;
    private final PetriNet petriNet;
    private final ArrayList<Integer> availableTransitions = new ArrayList<Integer>();
    private final LinkedList<Integer> impliedPlacesByLastFiring = new LinkedList<Integer>();
    private final LinkedList<Integer> firedTransitions = new LinkedList<Integer>();
    private final LinkedList<int[]> markings = new LinkedList<int[]>();
    private long maxTransitionsFired = 10000;

    public PetriNetFastSequencer(Random random, PetriNet petriNet) {
        this.random = random;
        this.petriNet = petriNet;

        this.firedTransitions.add(-1);
        this.markings.add(this.petriNet.getMarking().clone());
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

    public LinkedList<int[]> getMarkings() {
        return markings;
    }

    public void deploy() {
        for (int i = 0; i < this.petriNet.getNbTransitions(); i++) {
            this.availableTransitions.add(i);
        }

        if (this.availableTransitions.size() > 0) {
            this.updateAvailableTransitions();
        }

        int nbTransitionFired = 0;
        while ((this.availableTransitions.size() > 0) && (nbTransitionFired < this.maxTransitionsFired)) {                                   
            int selectedTransitionIndex = random.nextInt(this.availableTransitions.size());
            int selectedTransition = this.availableTransitions.get(selectedTransitionIndex);
            this.fireTransition(selectedTransition);
            this.updateAvailableTransitions();

            this.firedTransitions.add(selectedTransition);
            this.markings.add(this.petriNet.getMarking().clone());
            nbTransitionFired++;
        }
    }


    private void updateAvailableTransitions() {        
        for (int iP : this.impliedPlacesByLastFiring) {
            for (int iT = 0; iT < this.petriNet.getNbTransitions(); iT++) {
                if (this.petriNet.getInput(iP, iT) > 0 || this.petriNet.getOutput(iP, iT) > 0) {
                    this.availableTransitions.add(iT);
                }
            }
        }

        LinkedList<Integer> toBeRemoved = new LinkedList<Integer>();
        for (int iT : this.availableTransitions) {

            boolean isUsed = false;
            for (int iP = 0; iP < this.petriNet.getNbPlaces(); iP++) {

                if (this.petriNet.getInput(iP, iT) > 0) {
                    isUsed = true;
                }

                if (this.petriNet.getInput(iP, iT) > this.petriNet.getMarking(iP)) {
                    toBeRemoved.addLast(iT);
                    break;
                }
            }

            if (!isUsed) {
                toBeRemoved.addLast(iT);
            }
        }

        for (int iT : toBeRemoved) {
            this.availableTransitions.remove(iT);
        }
    }

    private void fireTransition(int transition) {
        this.impliedPlacesByLastFiring.clear();

        this.petriNet.fireTransition(transition);
        
        for (int iP = 0; iP < this.petriNet.getNbPlaces(); iP++) {
            if (this.petriNet.getInput(iP, transition) > 0 || this.petriNet.getOutput(iP, transition) > 0) {
                this.impliedPlacesByLastFiring.addLast(iP);
            }
        }
    }
}