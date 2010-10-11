/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.algebraicPetriNet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.yapnu.adt.Term;
import java.util.LinkedList;

/**
 *
 * @author adrien
 */
public class AlgebraicPetriNet {
    private String[] placeNames;
    private final ImmutableList<Multiset<Term>> marking;
    private TransitionMatrix input;
    private TransitionMatrix output;

    public AlgebraicPetriNet(InitialMarkingBuilder initialMarking, TransitionMatrix input, TransitionMatrix output) {
        this.marking = initialMarking.build();
        this.input = input;
        this.output = output;
    }

    public int getNbPlaces() {
        return this.input.getNbPlaces();
    }

    public int getNbTransitions() {
        return this.input.getNbTransitions();
    }

     public TransitionMatrix getInput() {
        return this.input;
    }
     
    public ImmutableList<ImmutableMultiset<Term>> getMarking() {
        LinkedList<ImmutableMultiset<Term>> clone = new LinkedList<ImmutableMultiset<Term>>();
        for (Multiset<Term> terms : marking) {
            clone.addLast(ImmutableMultiset.copyOf(terms));
        }
        
        return ImmutableList.copyOf(clone);
    }

    public ImmutableMultiset<Term> getMarking(int iPlace) {
        if (iPlace >= this.marking.size()) {
            throw new IllegalArgumentException("PlaceId is not valid.");
        }
        
        return ImmutableMultiset.copyOf(this.marking.get(iPlace));
    }
    
    public void fireTransition(int transition) {
        for (int place = 0; place < this.getNbPlaces(); place++) {
            Multiset<Term> terms = this.marking.get(place);

            // et les variables? et les termes de forme differentes
            for (Multiset.Entry<Term> entry : this.input.getEntrySet(place, transition)) {
               terms.remove(entry.getElement(), entry.getCount());
            }
            
            for (Multiset.Entry<Term> entry : this.output.getEntrySet(place, transition)) {
               terms.add(entry.getElement(), entry.getCount());
            }
        }
    }   
}
