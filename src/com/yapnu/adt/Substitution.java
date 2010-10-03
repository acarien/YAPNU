/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

/**
 *
 * @author adrien
 */
public class Substitution {
    private Variable variable;
    private Term term;

    public Substitution(Variable variable, Term term) {
        if (variable == null) {
            throw new IllegalArgumentException("Variable cannot be null.");
        }

        if (term == null) {
            throw new IllegalArgumentException("Term cannot be null.");
        }

        if (!term.getSort().equals(variable.getSort())) {
            throw new IllegalArgumentException("Each member of a substitution must be of the same sort.");
        }

        this.variable = variable;
        this.term = term;
    }

    public Term getTerm() {
        return term;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Substitution other = (Substitution) obj;
        if (this.variable != other.variable && (this.variable == null || !this.variable.equals(other.variable))) {
            return false;
        }
        if (this.term != other.term && (this.term == null || !this.term.equals(other.term))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.variable != null ? this.variable.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "<" + variable.getName() + ", " + term.toString() + ">";
    }
}
