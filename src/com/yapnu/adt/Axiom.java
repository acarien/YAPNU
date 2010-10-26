/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import com.yapnu.adt.model.BooleanAdt;
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

    boolean canUnify(TermUnifier termUnifier, Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        // est-ce que l'axiom peut s'appliquer (en verifiant la conclusion de l'axiom) ?
        HashSet<SubstitutionBag> axiomConclusionSubstitutionSet = new HashSet<SubstitutionBag>();
        boolean canUseAxiom = termUnifier.canUnify(this.rightTerm, expectedValue, axiomConclusionSubstitutionSet);
        if (!canUseAxiom) {
            return false;
        }

        if (axiomConclusionSubstitutionSet.size() == 0) {
            axiomConclusionSubstitutionSet.add(new SubstitutionBag());
        }

        boolean canAxiomBeUnified = false;
        for (SubstitutionBag axiomConclusionSubstitutions : axiomConclusionSubstitutionSet) {
            // est-ce qu'on peut unifier le membre de gauche de l'axiome avec la substitution faite sur le droit?
            Term leftTermSubstituted = this.leftTerm.substitutes(axiomConclusionSubstitutions);
            HashSet<SubstitutionBag> leftTermSubstitutionSet = new HashSet<SubstitutionBag>();
            boolean canSubstituteLeftTermWithConclusionSubstitutions = term.canUnifyRecursively(termUnifier, leftTermSubstituted, leftTermSubstitutionSet);
            if (!canSubstituteLeftTermWithConclusionSubstitutions) {
                continue;
            }

            if (leftTermSubstitutionSet.size() == 0) {
                leftTermSubstitutionSet.add(new SubstitutionBag());
            }

            for (SubstitutionBag leftTermSubstitutions : leftTermSubstitutionSet) {
                if (this.precondition != null) {
                    Term substitutedPrecondition = this.precondition.substitutes(leftTermSubstitutions).substitutes(axiomConclusionSubstitutions);
                    Term rewrittenPrecondition = termUnifier.getRewritter().rewritte(substitutedPrecondition);
                    if (!rewrittenPrecondition.equals(BooleanAdt.instance().getAdt().getConstant("true"))) {
                        continue;
                    }
                }

                HashSet<SubstitutionBag> tmpSet = new HashSet<SubstitutionBag>();
                Term rightTermSubstituted = this.rightTerm.substitutes(leftTermSubstitutions);
                boolean success = termUnifier.canUnify(rightTermSubstituted, expectedValue, tmpSet);
                if (!success) {
                    continue;
                }

                substitutionSet.add(leftTermSubstitutions);
                canAxiomBeUnified = true;
            }
        }

        return canAxiomBeUnified;
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
        for (Variable variable : secondVariables)
        {
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
}
