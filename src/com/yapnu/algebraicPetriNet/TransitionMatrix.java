/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.algebraicPetriNet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.yapnu.adt.Term;
import java.util.Set;

/**
 *
 * @author adrien
 */
public class TransitionMatrix {

    private final int nbPlaces;
    private final int nbTransitions;
    private final ImmutableList<ImmutableList<ImmutableMultiset<Term>>> matrix;

    public TransitionMatrix(int nbPlaces, int nbTransitions, ImmutableList<ImmutableList<ImmutableMultiset<Term>>> matrix) {
        this.nbPlaces = nbPlaces;
        this.nbTransitions = nbTransitions;
        this.matrix = matrix;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public int getNbTransitions() {
        return nbTransitions;
    }

    public boolean isTransitionDefined(int placeId, int transitionId) {
        return !this.getArc(placeId, transitionId).isEmpty();
    }

    public boolean isContainedIn(int placeId, int transitionId, Multiset<Term> marking) {
       ImmutableMultiset<Term> arc = this.getArc(placeId, transitionId);

       //c'est faux ! arc peut contenir des variables !
       // add(x, succ(0)) <=> succ(succ(0)) ok avec x = succ(0)

       // axiom : add(0, x) = 0 et 0 != succ(succ(0)) quleque soit x
       // axiom: add (succ(x), y) = succ(add(x,y))) on peut utiliser
       // on sait que y = succ(0).
       // on doit donc resoudre succ(add(x,succ(0)))) <=> succ(succ(0))
       // si on reutilise le premier axiome (add(0,x) = 0) on a succ(add(0, succ(0)))
       // qui peut etre reecrit en succ(succ(0)).
       // mais, que vaut la substitution: pour x?
       // 

       // vive le backtracking sur les axioms



       /*for (Entry<Term> term : arc.entrySet()) {
            if (marking.count(term.getElement()) < term.getCount()) {
                return false;
            }
        }*/

        return true;
    }

    public Set<Multiset.Entry<Term>> getEntrySet(int placeId, int transitionId) {        
        return this.getArc(placeId, transitionId).entrySet();
    }

    private ImmutableMultiset<Term> getArc(int placeId, int transitionId) {
        if (placeId >= nbPlaces) {
            throw new IllegalArgumentException("PlaceId is not valid.");
        }

        if (transitionId >= nbTransitions) {
            throw new IllegalArgumentException("TransitionId is not valid.");
        }

        return matrix.get(placeId).get(transitionId);
    }
}
