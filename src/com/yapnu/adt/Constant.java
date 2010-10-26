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
public class Constant implements Term {

    private final String name;
    private final Sort sort;
    private final boolean isGenerator;

    public Constant(String name, Sort sort) {
        this(name, sort, true);
    }

    public Constant(String name, Sort sort, boolean isGenerator) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Constant name cannot be null or empty.");
        }

        if (sort == null) {
            throw new IllegalArgumentException("Constant sort cannot be null.");
        }

        this.name = name;
        this.sort = sort;
        this.isGenerator = isGenerator;
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
    public String toString() {
        return this.name;
    }

    @Override
    public int size() {
        return 1;
    }

    public boolean isNormalForm() {
        return this.isGenerator;
    }

    @Override
    public boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag substitutions) {
        if (substitutions == null) {
            throw new IllegalArgumentException("Substitutions cannot be null.");
        }

        if (other == null) {
            return false;
        }

        if (other instanceof Variable) {
            return other.tryGetMatchingSubstitutions(this, substitutions);
        }
        
        return this.equals(other);
    }

    @Override
    public Constant substitutes(SubstitutionBag substitutions) {
        return this;
    }

    @Override
    public ImmutableSet<Variable> getVariables() {
        return ImmutableSet.of();
    }

    @Override
    public Term rewritte(TermRewritter termRewritter) {
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
        
        return this.equals((Constant) obj);
    }

    public boolean equals(Constant other) {
        if (other == null) {
            return false;
        }

        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }

        if (this.sort != other.sort && (this.sort == null || !this.sort.equals(other.sort))) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 89 * hash + (this.sort != null ? this.sort.hashCode() : 0);
        return hash;
    }

    @Override
    public Term renameVariables(SubstitutionBag substitutions) {
        return this;
    }

    @Override
    public boolean canUnifyRecursively(TermUnifier termUnifier, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        return false;
    }
}
