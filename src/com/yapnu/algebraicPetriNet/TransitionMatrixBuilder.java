/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.algebraicPetriNet;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.yapnu.adt.Term;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author adrien
 */
public class TransitionMatrixBuilder {
    private final ArrayList<ArrayList<Multiset<Term>>> matrix;
    private final int nbPlaces;
    private final int nbTransitions;

    public TransitionMatrixBuilder(int nbPlaces, int nbTransitions) {
        this.nbPlaces = nbPlaces;
        this.nbTransitions = nbTransitions;

        matrix = new ArrayList<ArrayList<Multiset<Term>>>();
        for (int i = 0; i < this.nbPlaces; i++) {
            ArrayList<Multiset<Term>> transitions = new ArrayList<Multiset<Term>>();            
            for (int j = 0; j< this.nbTransitions; j++) {
                Multiset<Term> terms = HashMultiset.create();
                transitions.add(terms);
            }

            matrix.add(transitions);
        }
    }

    public TransitionMatrixBuilder Add(int placeId, int transitionId, Term term, int times) {
        if (times <= 0) {
            throw new IllegalArgumentException("Times must be a positive number.");
        }
        
        if (placeId >= this.nbPlaces) {
            throw new IllegalArgumentException("PlaceId is not a valid.");
        }

        if (transitionId >= this.nbTransitions) {
            throw new IllegalArgumentException("TransitionId is not a valid.");
        }

        if (term == null) {
            throw new IllegalArgumentException("Term canno be null");
        }

        this.matrix.get(placeId).get(transitionId).add(term, times);
        return this;
    }

    public TransitionMatrixBuilder Add(int placeId, int transitionId, Term... terms) {
        if (placeId >= this.nbPlaces) {
            throw new IllegalArgumentException("PlaceId is not a valid.");
        }

        if (transitionId >= this.nbTransitions) {
            throw new IllegalArgumentException("TransitionId is not a valid.");
        }

        for (Term term : terms) {
            if (term == null) {
                throw new IllegalArgumentException("Term canno be null");
            }
            
            this.matrix.get(placeId).get(transitionId).add(term);
        }
        
        return this;
    }

    public TransitionMatrix build() {

        LinkedList<ImmutableList<ImmutableMultiset<Term>>> places = new LinkedList<ImmutableList<ImmutableMultiset<Term>>>();

        for (int i = 0; i < this.nbPlaces; i++) {
            LinkedList<ImmutableMultiset<Term>> transitions = new LinkedList<ImmutableMultiset<Term>>();
            for (int j = 0; j < this.nbTransitions; j++) {
                ImmutableMultiset<Term> terms = ImmutableMultiset.copyOf(matrix.get(i).get(j));
                transitions.addLast(terms);
            }

            ImmutableList<ImmutableMultiset<Term>> place = ImmutableList.copyOf(transitions);
            places.addLast(place);
        }

        ImmutableList<ImmutableList<ImmutableMultiset<Term>>> immutableMatrix = ImmutableList.copyOf(places);
        return new TransitionMatrix(this.nbPlaces, this.nbTransitions, immutableMatrix);
    }

}
