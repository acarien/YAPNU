/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

/**
 *
 * @author adrien
 */
public interface Term {

    String getName();

    Sort getSort();

    int size();

    boolean isNormalForm();

    Term substitutes(SubstitutionBag substitutions);

    boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag bag);

    ImmutableSet<Variable> getVariables();

    Term rewritte(TermRewritter termRewritter);

    Term renameVariables(SubstitutionBag bag);

    boolean canUnifyRecursively(TermUnifier termUnifier, Term expectedValue, Set<SubstitutionBag> substitutionSet);    

   /* public final Term rewritte(ArrayList<Adt> adts) {
        AdtMap adtMap = new AdtMap(adts);
        return this.rewritte(adtMap, null);
    }

    protected final Term rewritte(AdtMap adts, Term previousTerm) {
        Term currentTerm = this;

        while (!currentTerm.isNormalForm()) {
            if (currentTerm.equals(previousTerm)) {
                return currentTerm;
            }

            if (!adts.hasAdtForSort(currentTerm.getSort())) {
                throw new IllegalArgumentException("Cannot rewrite a term of the sort " + currentTerm.getSort().toString() + ".");
            }

            Axiom axiom = adts.getAxiom(currentTerm);
            if (axiom != null) {
                previousTerm = currentTerm;
                currentTerm = axiom.getRightTerm();
                continue;
            }

            Term currentTermCopied = currentTerm;
            currentTerm = currentTerm.rewritte(adts);
            previousTerm = currentTermCopied;
        }

        return currentTerm;
    }    

    protected class AdtMap {

        private final HashMap<Sort, LinkedList<Adt>> adts;

        public AdtMap(Adt adt) {
            this.adts = new HashMap<Sort, LinkedList<Adt>>();
            this.addAdt(adt);

        }

        public AdtMap(ArrayList<Adt> adts) {
            HashSet<Sort> sorts = new HashSet<Sort>();
            this.adts = new HashMap<Sort, LinkedList<Adt>>();
            for (Adt adt : adts) {
                if (sorts.contains(adt.getSort())) {
                    throw new IllegalArgumentException("Cannot have two adts with the same main sort.");
                }

                sorts.add(adt.getSort());

                this.addAdt(adt);
            }
        }

        private void addAdt(Adt adt) {
            if (!this.adts.containsKey(adt.getSort())) {
                this.adts.put(adt.getSort(), new LinkedList<Adt>());
            }

            this.adts.get(adt.getSort()).addFirst(adt);

            LinkedList<Sort> otherSorts = adt.getAdditionalCodomains();
            for (Sort sort : otherSorts) {
                if (!this.adts.containsKey(sort)) {
                    this.adts.put(sort, new LinkedList<Adt>());
                }

                this.adts.get(sort).addLast(adt);
            }
        }

        private boolean hasAdtForSort(Sort sort) {
            return this.adts.containsKey(sort);
        }

        private Axiom getAxiom(Term term) {
            LinkedList<Adt> foundAdts = this.adts.get(term.getSort());
            for (Iterator<Adt> iterator = foundAdts.descendingIterator(); iterator.hasNext();) {
                Adt adt = iterator.next();
                Axiom axiom = adt.getAxiom(term);
                if (axiom != null) {
                    return axiom;
                }
            }

            return null;
        }
    }*/
}
