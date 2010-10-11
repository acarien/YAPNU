/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.algebraicPetriNet;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.yapnu.adt.Term;
import java.util.ArrayList;

/**
 *
 * @author adrien
 */
public class InitialMarkingBuilder {

    private final ArrayList<Multiset<Term>> initialMarking;
    private final int nbPlaces;

    public InitialMarkingBuilder(int nbPlaces) {
        this.nbPlaces = nbPlaces;

        this.initialMarking = new ArrayList<Multiset<Term>>();
        for (int i = 0; i < nbPlaces; i++) {
            Multiset<Term> terms = HashMultiset.create();
            this.initialMarking.add(terms);
        }
    }

    public InitialMarkingBuilder Add(int placeId, Term term) {
        return this.Add(placeId, term, 1);
    }

    public InitialMarkingBuilder Add(int placeId, Term term, int times) {
        if (times <= 0) {
            throw new IllegalArgumentException("Times must be a positive number.");
        }

        if (placeId >= nbPlaces) {
            throw new IllegalArgumentException("PlaceId is not valid.");
        }

        if (term == null) {
            throw new IllegalArgumentException("Term canno be null");
        }

        if (!term.isNormalForm()) {
            throw new IllegalArgumentException("InitialMarking can only contains term in normal form.");
        }

        this.initialMarking.get(placeId).add(term, times);
        return this;
    }

    public InitialMarkingBuilder Add(int placeId, Term... terms) {
        if (placeId >= nbPlaces) {
            throw new IllegalArgumentException("PlaceId is not valid.");
        }

        for (Term term : terms) {
            if (term == null) {
                throw new IllegalArgumentException("Term canno be null");
            }

            if (!term.isNormalForm()) {
                throw new IllegalArgumentException("InitialMarking can only contains term in 'normal form'.");
            }

            this.initialMarking.get(placeId).add(term);
        }

        return this;
    }

    public ImmutableList<Multiset<Term>> build() {
        return ImmutableList.copyOf(initialMarking);
    }
}
