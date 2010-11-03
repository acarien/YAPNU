/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import com.yapnu.adt.model.BooleanAdt;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author adrien
 */
public final class Axiom {

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

    Unification canUnify(SubstitutionBag renamedVariables, TermUnifier termUnifier, Term term, Term expectedValue) {
        
        RightTermUnifier fourth = new RightTermUnifier(termUnifier);
        PreconditionUnifier third = new PreconditionUnifier(termUnifier, fourth, renamedVariables);
        LeftTermUnifier second = new LeftTermUnifier(termUnifier, third, term);
        ConclusionUnifier first = new ConclusionUnifier(termUnifier, second);

        LinkedList<Unification> finalUnifications = new LinkedList<Unification>();
        Stack<UnificationState> states = new Stack<UnificationState>();
        states.add(new UnificationState(first, new SubstitutionBag()));
        while (!states.isEmpty()) {
            UnificationState state = states.pop();

            state.generateNextState(states, expectedValue);
            if (state.getUnification().isSuccess() && state.axiomUnifier instanceof PreconditionUnifier) {
                for (SubstitutionBag substitutions : state.unification) {
                    SubstitutionBag tmp = new SubstitutionBag();
                    tmp.tryAddSubstitutions(substitutions);
                    for (int i = 2; i < state.getPreviousSubstitutions().length; i++) {
                        tmp.tryAddSubstitutions(state.getPreviousSubstitutions()[i]);
                    }

                    Unification unification = new Unification(tmp);
                    finalUnifications.addLast(unification);
                }
            }
        }

        Unification unification = new Unification();
        boolean hasUnified = false;
        for (Unification tmpUnifcation : finalUnifications) {
            unification.addAll(tmpUnifcation);
            hasUnified = true;
        }

        unification.setSuccess(hasUnified);
        return unification;
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
        private final AxiomUnifier next;
        protected final TermUnifier termUnifier;

        private AxiomUnifier(TermUnifier termUnifier, AxiomUnifier next) {
            this.next = next;
            this.termUnifier = termUnifier;
        }        

        protected abstract Unification unify(Term expectedValue, SubstitutionBag... previousSubstitutions);        
    }

    private class ConclusionUnifier extends AxiomUnifier {                

        public ConclusionUnifier(TermUnifier termUnifier, AxiomUnifier next) {
            super(termUnifier, next);
        }

        @Override
        protected Unification unify(Term expectedValue, SubstitutionBag... previousSubstitutions) {
            return termUnifier.canUnify(rightTerm, expectedValue);
        }
    }

    private class LeftTermUnifier extends AxiomUnifier {        
        private Term term;

        public LeftTermUnifier(TermUnifier termUnifier, AxiomUnifier next, Term term) {
            super(termUnifier, next);
            this.term = term;
        }

        @Override
        protected Unification unify(Term expectedValue, SubstitutionBag... previousSubstitutions) {
            Term substitutedTerm = leftTerm;
            for (SubstitutionBag previousSubstitution : previousSubstitutions) {
                substitutedTerm = substitutedTerm.substitutes(previousSubstitution);
            }
            
            return term.canUnify(termUnifier, substitutedTerm);
        }
    }

    private class PreconditionUnifier extends AxiomUnifier {        
        private SubstitutionBag renamedVariables;

        public PreconditionUnifier(TermUnifier termUnifier, AxiomUnifier next, SubstitutionBag renamedVariables) {
            super(termUnifier, next);
            this.renamedVariables = renamedVariables;
        }

        @Override
        protected Unification unify(Term expectedValue, SubstitutionBag... previousSubstitutions) {
            if (precondition == null) {
                return new Unification();
            }

            Term substitutedTerm = precondition;
            for (SubstitutionBag previousSubstitution : previousSubstitutions) {
                substitutedTerm = substitutedTerm.substitutes(previousSubstitution);
            }

            substitutedTerm = substitutedTerm.substitutes(renamedVariables);

            return termUnifier.canUnify(substitutedTerm, BooleanAdt.instance().getAdt().getConstant("true"));
        }
    }

    private class RightTermUnifier extends AxiomUnifier {

        public RightTermUnifier(TermUnifier termUnifier) {
            super(termUnifier, null);
        }

        @Override
        protected Unification unify(Term expectedValue, SubstitutionBag... previousSubstitutions) {
            Term substitutedTerm = rightTerm;
            for (SubstitutionBag previousSubstitution : previousSubstitutions) {
                substitutedTerm = substitutedTerm.substitutes(previousSubstitution);
            }

            return termUnifier.canUnify(substitutedTerm, expectedValue);
        }
    }

    private class UnificationState {
        private final AxiomUnifier axiomUnifier;
        private final SubstitutionBag[] previousSubstitutions;
        private Unification unification;

        public UnificationState(AxiomUnifier axiomUnifier, SubstitutionBag... previousSubstitutions) {
            this.axiomUnifier = axiomUnifier;
            this.previousSubstitutions = previousSubstitutions;
        }

        public Unification getUnification() {
            return unification;
        }

        public SubstitutionBag[] getPreviousSubstitutions() {
            return previousSubstitutions;
        }

        public void generateNextState(Stack<UnificationState> states, Term expectedValue) {
            unification = this.axiomUnifier.unify(expectedValue, previousSubstitutions);
            if (!unification.isSuccess()) {
                return;
            }

            if (this.axiomUnifier.next == null) {
                return;
            }

            if (unification.size() == 0) {
                unification.add(new SubstitutionBag());
            }

            for (SubstitutionBag substitutions : unification) {
                SubstitutionBag[] bags = Arrays.copyOf(previousSubstitutions, previousSubstitutions.length + 1);
                bags[previousSubstitutions.length] = substitutions;
                states.add(new UnificationState(axiomUnifier.next, bags));
            }
        }
    }
}
