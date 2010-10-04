/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

/**
 *
 * @author adrien
 */
public class Variable implements Term {
    private final Sort sort;
    private final String name;

    public Variable(String name, Sort sort) {
        this.sort = sort;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public boolean isGenerator() {
        return false;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isNormalForm() {
        return false;
    }

    @Override
    public boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag substitutions) {
        if (substitutions == null) {
            throw new IllegalArgumentException("Substitutions cannot be null.");
        }

        if (other == null) {
            return false;
        }

        if (!this.getSort().equals(other.getSort())) {
            return false;
        }
        
        return substitutions.tryAddSubstitution(this, other);        
    }

    @Override
    public Term substitutes(SubstitutionBag substitutions) {
        if (substitutions == null) {
            throw new IllegalArgumentException("Substitutions cannnot be null.");
        }

         for (Substitution substitution : substitutions.getSubstitutions()) {
             if (substitution.getVariable().equals(this)) {
                 return substitution.getTerm();
             }
         }

         return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return this.equals((Variable) obj);
        
    }

    public boolean equals(Variable other) {
        if (other == null) {
            return false;
        }

        if (this.sort != other.sort && (this.sort == null || !this.sort.equals(other.sort))) {
            return false;
        }

        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.sort != null ? this.sort.hashCode() : 0);
        hash = 61 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
