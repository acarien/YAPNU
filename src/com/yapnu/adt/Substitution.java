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
    private Variable substituted;
    private Term value;
    
    public Substitution(Variable substituted, Term value) {
        if (substituted == null) {
            throw new IllegalArgumentException("Substituted cannot be null.");
        }

        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }

        if (!value.getSort().equals(substituted.getSort())) {
            throw new IllegalArgumentException("Each member of a substitution must be of the same sort.");
        }

        this.substituted = substituted;
        this.value = value;
    }
    
    public Term getValue() {
        return value;
    }

    public Variable getSubstituted() {
        return substituted;
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
        if (this.substituted != other.substituted && (this.substituted == null || !this.substituted.equals(other.substituted))) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.substituted != null ? this.substituted.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "<" + substituted.getName() + ", " + value.toString() + ">";
    }
}
