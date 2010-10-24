/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;

/**
 *
 * @author adrien
 */
public class Axiom {

    public static final String VARIABLE_PREFIX = "__in_axiom_";
    private Term leftTerm;
    private Term rightTerm;

    public Axiom(Term leftTerm, Term rightTerm) {
        if (leftTerm == null) {
            throw new IllegalArgumentException("LeftTerm cannot be null.");
        }

        if (rightTerm == null) {
            throw new IllegalArgumentException("RightTerm cannot be null.");
        }

        this.leftTerm = leftTerm;
        this.rightTerm = rightTerm;
        
        if (this.leftTerm.equals(this.rightTerm)) {
            throw new IllegalArgumentException("Both sides cannot be equals.");
        }

        this.variablesMustHaveSameSortInBothSide();
        this.validatesOccursCheck();
    }

    public Term getLeftTerm() {
        return leftTerm;
    }

    public Term getRightTerm() {
        return rightTerm;
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

    private void variablesMustHaveSameSortInBothSide() {
        ImmutableSet<Variable> leftVariables = this.leftTerm.getVariables();
        ImmutableSet<Variable> rightVariables = this.rightTerm.getVariables();

        HashMap<String, Variable> rightVariablesPerName = new HashMap<String, Variable>();
        for (Variable variable : rightVariables)
        {
            rightVariablesPerName.put(variable.getName(), variable);
        }
        
        for (Variable leftVariable : leftVariables) {
            if (rightVariablesPerName.containsKey(leftVariable.getName())) {
                Variable rightVariable = rightVariablesPerName.get(leftVariable.getName());
                if (!leftVariable.equals(rightVariable)) {
                    throw new IllegalArgumentException("This axiom contains two variables with the same name but different sorts.");
                }
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

    @Override
    public String toString() {
        return this.leftTerm.toString() + " = " + this.rightTerm.toString();
    }

}
