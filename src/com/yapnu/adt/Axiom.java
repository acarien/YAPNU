/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import com.yapnu.adt.model.BooleanAdt;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author adrien
 */
public class Axiom {

    private Term precondition;
    private Term leftTerm;
    private Term rightTerm;

    public Axiom(Term leftTerm, Term rightTerm) {
        this(null, leftTerm, rightTerm);
    }

    public Axiom(Term precondition, Term leftTerm, Term rightTerm) {
        if (leftTerm == null) {
            throw new IllegalArgumentException("LeftTerm cannot be null.");
        }

        if (rightTerm == null) {
            throw new IllegalArgumentException("RightTerm cannot be null.");
        }

        this.precondition = precondition;
        this.leftTerm = leftTerm;
        this.rightTerm = rightTerm;

        if (this.leftTerm.equals(this.rightTerm)) {
            throw new IllegalArgumentException("Both sides cannot be equals.");
        }

        if (!this.leftTerm.getSort().equals(this.rightTerm.getSort())) {
            throw new IllegalArgumentException("Right term sort must be equals to the left term sort.");
        }

        if (this.precondition != null) {
            if (!this.precondition.getSort().equals(BooleanAdt.instance().getAdt().getSort())) {
                throw new IllegalArgumentException("Precondition must be of Boolean sort.");
            }

            this.areAllPreconditionVariablesContainedInLeftTerm();
            this.preconditionMustContainSomeVariables();
        }

        this.variablesMustHaveSameSortInBothSide(this.leftTerm, this.rightTerm);
        this.validatesOccursCheck();
    }

    public Term getLeftTerm() {
        return leftTerm;
    }

    public Term getRightTerm() {
        return rightTerm;
    }

    public boolean tryGetMatchingSubstitutions(TermRewritter termRewritter, Term term, SubstitutionBag substitutions) {
        if (!this.leftTerm.tryGetMatchingSubstitutions(term, substitutions)) {
            return false;
        }

        if (this.precondition == null) {
            return true;
        }

        Term substitutedPrecondition = this.precondition.substitutes(substitutions);
        Term rewrittenPrecondition = termRewritter.rewritte(substitutedPrecondition);
        return rewrittenPrecondition.equals(BooleanAdt.instance().getAdt().getConstant("true"));
    }

    boolean canUnify(SubstitutionBag renamedVariables, TermUnifier termUnifier, Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        RightTermUnifier fourth = new RightTermUnifier(termUnifier);
        PreconditionUnifier third = new PreconditionUnifier(fourth, termUnifier, renamedVariables);
        LeftTermUnifier second = new LeftTermUnifier(third, termUnifier, term);
        ConclusionUnifier first = new ConclusionUnifier(second, termUnifier);

        return first.execute(termUnifier, expectedValue, substitutionSet, new SubstitutionBag());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Axiom other = (Axiom) obj;
        if (this.precondition != other.precondition && (this.precondition == null || !this.precondition.equals(other.precondition))) {
            return false;
        }
        if (this.leftTerm != other.leftTerm && (this.leftTerm == null || !this.leftTerm.equals(other.leftTerm))) {
            return false;
        }
        if (this.rightTerm != other.rightTerm && (this.rightTerm == null || !this.rightTerm.equals(other.rightTerm))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.leftTerm != null ? this.leftTerm.hashCode() : 0);
        return hash;
    }

    private void variablesMustHaveSameSortInBothSide(Term firstTerm, Term secondTerm) {
        ImmutableSet<Variable> firstVariables = firstTerm.getVariables();
        ImmutableSet<Variable> secondVariables = secondTerm.getVariables();

        HashMap<String, Variable> secondVariablesPerName = new HashMap<String, Variable>();
        for (Variable variable : secondVariables) {
            secondVariablesPerName.put(variable.getName(), variable);
        }

        for (Variable firstVariable : firstVariables) {
            if (secondVariablesPerName.containsKey(firstVariable.getName())) {
                Variable rightVariable = secondVariablesPerName.get(firstVariable.getName());
                if (!firstVariable.equals(rightVariable)) {
                    throw new IllegalArgumentException("This axiom contains two variables with the same name but different sorts.");
                }
            }
        }

    }

    private void areAllPreconditionVariablesContainedInLeftTerm() {
        ImmutableSet<Variable> preconditionVariables = this.precondition.getVariables();
        ImmutableSet<Variable> leftTermVariables = this.leftTerm.getVariables();
        for (Variable preconditionVariable : preconditionVariables) {
            if (!leftTermVariables.contains(preconditionVariable)) {
                throw new IllegalArgumentException("The precondition contains some variables that are not contained it the left term.");
            }
        }
    }

    private void validatesOccursCheck() {
        if (this.leftTerm instanceof Variable) {
            ImmutableSet<Variable> variables = this.rightTerm.getVariables();
            if (variables.contains((Variable) leftTerm)) {
                throw new IllegalArgumentException("OccursCheck error.");
            }
        }
    }

    private void preconditionMustContainSomeVariables() {
        if (this.precondition.getVariables().size() == 0) {
            throw new IllegalArgumentException("The precondition must contain at least on variable.");
        }
    }

    @Override
    public String toString() {
        return this.leftTerm.toString() + " = " + this.rightTerm.toString();
    }

    private abstract class AxiomUnifier {
        private AxiomUnifier next;

        private AxiomUnifier(AxiomUnifier next) {
            this.next = next;
        }

        boolean execute(TermUnifier termUnifier, Term expectedValue, Set<SubstitutionBag> finalSubstitutions, SubstitutionBag... previousSubstitutions) {
            Set<SubstitutionBag> substitutionSet = new HashSet<SubstitutionBag>();
            boolean canUseAxiom = this.unify(expectedValue, substitutionSet, previousSubstitutions);
            if (!canUseAxiom) {
                return false;
            }

            if (next == null) {
                return true;
            }

            if (substitutionSet.size() == 0) {
                substitutionSet.add(new SubstitutionBag());
            }

            boolean canUnify = false;
            SubstitutionBag[] bags = Arrays.copyOf(previousSubstitutions, previousSubstitutions.length + 1);
            for (SubstitutionBag substitutions : substitutionSet) {
                bags[previousSubstitutions.length] = substitutions;
                if (next.execute(termUnifier, expectedValue, finalSubstitutions, bags)) {
                    canUnify = true;
                }

                add(finalSubstitutions, substitutions, previousSubstitutions);
            }

            return canUnify;
        }

        protected abstract boolean unify(Term expectedValue, Set<SubstitutionBag> substitutionSet, SubstitutionBag... previousSubstitutions);

        protected abstract void add(Set<SubstitutionBag> substitutionSet, SubstitutionBag substitutions, SubstitutionBag... previousSubstitutions);
    }

    private class ConclusionUnifier extends AxiomUnifier {
        private TermUnifier termUnifier;

        public ConclusionUnifier(AxiomUnifier next, TermUnifier termUnifier) {
            super(next);
            this.termUnifier = termUnifier;
        }

        @Override
        protected boolean unify(Term expectedValue, Set<SubstitutionBag> substitutionSet, SubstitutionBag... previousSubstitutions) {
            return termUnifier.canUnify(rightTerm, expectedValue, substitutionSet);
        }

        @Override
        protected void add(Set<SubstitutionBag> substitutionSet, SubstitutionBag substitutions, SubstitutionBag... previousSubstitutions) {
        }
    }

    private class LeftTermUnifier extends AxiomUnifier {
        private TermUnifier termUnifier;
        private Term term;

        public LeftTermUnifier(AxiomUnifier next, TermUnifier termUnifier, Term term) {
            super(next);
            this.termUnifier = termUnifier;
            this.term = term;
        }

        @Override
        protected boolean unify(Term expectedValue, Set<SubstitutionBag> substitutionSet, SubstitutionBag... previousSubstitutions) {
            Term substitutedTerm = leftTerm;
            for (SubstitutionBag previousSubstitution : previousSubstitutions) {
                substitutedTerm = substitutedTerm.substitutes(previousSubstitution);
            }

            return term.canUnifyRecursively(termUnifier, substitutedTerm, substitutionSet);
        }

        @Override
        protected void add(Set<SubstitutionBag> substitutionSet, SubstitutionBag substitutions, SubstitutionBag... previousSubstitutions) {
        }
    }

    private class PreconditionUnifier extends AxiomUnifier {
        private TermUnifier termUnifier;
        private SubstitutionBag renamedVariables;

        public PreconditionUnifier(AxiomUnifier next, TermUnifier termUnifier, SubstitutionBag renamedVariables) {
            super(next);
            this.termUnifier = termUnifier;
            this.renamedVariables = renamedVariables;
        }

        @Override
        protected boolean unify(Term expectedValue, Set<SubstitutionBag> substitutionSet, SubstitutionBag... previousSubstitutions) {
            if (precondition == null) {
                return true;
            }

            Term substitutedTerm = precondition;
            for (SubstitutionBag previousSubstitution : previousSubstitutions) {
                substitutedTerm = substitutedTerm.substitutes(previousSubstitution);
            }

            substitutedTerm = substitutedTerm.substitutes(renamedVariables);

            return termUnifier.canUnify(substitutedTerm, BooleanAdt.instance().getAdt().getConstant("true"), substitutionSet);
        }

        @Override
        protected void add(Set<SubstitutionBag> substitutionSet, SubstitutionBag substitutions, SubstitutionBag... previousSubstitutions) {
            SubstitutionBag tmp = new SubstitutionBag();
            tmp.tryAddSubstitutions(substitutions);
            for (int i = 2; i < previousSubstitutions.length; i++) {
                tmp.tryAddSubstitutions(previousSubstitutions[i]);
            }

            substitutionSet.add(tmp);
        }
    }

    private class RightTermUnifier extends AxiomUnifier {
        private TermUnifier termUnifier;

        public RightTermUnifier(TermUnifier termUnifier) {
            super(null);
            this.termUnifier = termUnifier;
        }

        @Override
        protected boolean unify(Term expectedValue, Set<SubstitutionBag> substitutionSet, SubstitutionBag... previousSubstitutions) {
            Term substitutedTerm = rightTerm;
            for (SubstitutionBag previousSubstitution : previousSubstitutions) {
                substitutedTerm = substitutedTerm.substitutes(previousSubstitution);
            }

            return termUnifier.canUnify(substitutedTerm, expectedValue, new HashSet<SubstitutionBag>());
        }

        @Override
        protected void add(Set<SubstitutionBag> substitutionSet, SubstitutionBag substitutions, SubstitutionBag... previousSubstitutions) {
        }
    }
}
