/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.petriNets;

import com.yapnu.petriNet.PetriNetSequencer;
import com.yapnu.petriNet.PetriNet;
import java.util.LinkedList;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author adrien
 */
public abstract class AbstractTestPetriNetSequencer {

    protected abstract PetriNetSequencer GetPetriNetSequencer(Random random, PetriNet petriNet);

    @Test()
    public void TestSequence() {
        int[][] input = {{1}, {0}};
        int[][] output = {{0}, {1}};
        int[] marking = {2, 0};

       PetriNet petriNet = new PetriNet(input, output, marking);
       PetriNetSequencer sequencer = this.GetPetriNetSequencer(new Random(1), petriNet);
       sequencer.deploy();
       LinkedList<Integer> firedTransitions = sequencer.getFiredTransitions();

       LinkedList<Integer> reference = new LinkedList<Integer>();
       reference.addLast(-1);
       reference.addLast(0);
       reference.addLast(0);
       Assert.assertEquals(firedTransitions, reference);

       int[] finalMarking = petriNet.getMarking();
       int[] referenceMarking = {0, 2};
       Assert.assertArrayEquals(finalMarking, referenceMarking);
    }

    @Test()
    public void TestSeveralOutputs() {
        int[][] input = {{1}, {0}, {0}};
        int[][] output = {{0}, {1}, {1}};
        int[] marking = {2, 0, 0};

       PetriNet petriNet = new PetriNet(input, output, marking);
       PetriNetSequencer sequencer = this.GetPetriNetSequencer(new Random(1), petriNet);
       sequencer.deploy();
       LinkedList<Integer> firedTransitions = sequencer.getFiredTransitions();
       LinkedList<Integer> reference = new LinkedList<Integer>();
       reference.addLast(-1);
       reference.addLast(0);
       reference.addLast(0);
       Assert.assertEquals(firedTransitions, reference);

       int[] finalMarking = petriNet.getMarking();
       int[] referenceMarking = {0, 2, 2};
       Assert.assertArrayEquals(finalMarking, referenceMarking);
    }

    @Test()
    public void TestSeveralInputs() {
        int[][] input = {{1}, {1}, {0}};
        int[][] output = {{0}, {0}, {1}};
        int[] marking = {2, 2, 0};

       PetriNet petriNet = new PetriNet(input, output, marking);
       PetriNetSequencer sequencer = this.GetPetriNetSequencer(new Random(1), petriNet);
       sequencer.deploy();
       LinkedList<Integer> firedTransitions = sequencer.getFiredTransitions();
       LinkedList<Integer> reference = new LinkedList<Integer>();
       reference.addLast(-1);
       reference.addLast(0);
       reference.addLast(0);
       Assert.assertEquals(firedTransitions, reference);

       int[] finalMarking = petriNet.getMarking();
       int[] referenceMarking = {0, 0, 2};
       Assert.assertArrayEquals(finalMarking, referenceMarking);
    }

    @Test()
    public void TestUnfireableTransition() {
        int[][] input = {{2}, {1}, {0}};
        int[][] output = {{0}, {0}, {1}};
        int[] marking = {2, 2, 0};

       PetriNet petriNet = new PetriNet(input, output, marking);
       PetriNetSequencer sequencer = this.GetPetriNetSequencer(new Random(1), petriNet);
       sequencer.deploy();
       LinkedList<Integer> firedTransitions = sequencer.getFiredTransitions();
       LinkedList<Integer> reference = new LinkedList<Integer>();
       reference.addLast(-1);
       reference.addLast(0);
       Assert.assertEquals(firedTransitions, reference);

       int[] finalMarking = petriNet.getMarking();
       int[] referenceMarking = {0, 1, 1};
       Assert.assertArrayEquals(finalMarking, referenceMarking);
    }

    @Test()
    public void TestSetMaxTransitionsFired() {
        int[][] input = {{1}, {1}, {0}};
        int[][] output = {{1}, {1}, {1}};
        int[] marking = {2, 2, 0};

       PetriNet petriNet = new PetriNet(input, output, marking);
       PetriNetSequencer sequencer = this.GetPetriNetSequencer(new Random(1), petriNet);
       sequencer.setMaxTransitionsFired(1000);
       sequencer.deploy();

       LinkedList<Integer> firedTransitions = sequencer.getFiredTransitions();
       Assert.assertEquals(firedTransitions.size(), 1001);
    }

}