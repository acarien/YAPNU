/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.petriNets;

/**
 *
 * @author adrien
 */
public class PetriNet {
    private final int[][] output; // transition vers place
    private final int[][] input; // place vers transition
    private final int[][] resume;
    private final int[] marking;

    public PetriNet(int[][] input, int[][] output, int[] initialMarking) {
        this.output = output;
        this.input = input;
        this.marking = initialMarking;

        this.resume = new int[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                resume[i][j] = output[i][j] - input[i][j];
            }
        }
    }

    public int getNbPlaces() {
        return this.input.length;
    }

    public int getNbTransitions() {
        return this.input[0].length;
    }

    public int getInput(int iPlace, int iTransition) {
        return this.input[iPlace][iTransition];
    }

    public int getOutput(int iPlace, int iTransition) {
        return this.output[iPlace][iTransition];
    }

    public int[] getMarking() {
        return this.marking;
    }
    
    public int getMarking(int iPlace) {
        return this.marking[iPlace];
    }

    void fireTransition(int transition) {
        for (int place = 0; place < this.getNbPlaces(); place++) {
            this.marking[place] = this.marking[place] - this.input[place][transition] + this.output[place][transition];
        }
    }
}
