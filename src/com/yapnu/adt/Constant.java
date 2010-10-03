/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.List;

/**
 *
 * @author adrien
 */
public class Constant implements Term {

    private final String name;
    private final Sort sort;

    public Constant(String name, Sort sort) {
        this.name = name;
        this.sort = sort;
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
        return true;
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
        return true;
    }

    @Override
    public boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag bag) {
        return this.equals(other);
    }

    @Override
    public Constant substitutes(List<Substitution> substitutions) {
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
        int hash = 5;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }    
}