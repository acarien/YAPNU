/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 *
 * @author adrien
 */
public class SubstitutionBag {
    private final LinkedHashMap<Term, Term> substitutions = new LinkedHashMap<Term, Term>();

    public SubstitutionBag() {
    }

    public SubstitutionBag(Substitution... substitutions) {
        for (Substitution substitution : substitutions) {
            this.tryAddSubstitution(substitution);
        }
    }

    public void clear() {
        this.substitutions.clear();
    }

    public int size() {
        return this.substitutions.size();
    }

    public boolean tryAddSubstitution(Substitution substitution) {
        if (substitution == null) {
            throw new IllegalArgumentException("Substitution cannot be null.");
        }

        return this.tryAddSubstitution(substitution.getSubstituted(), substitution.getValue());
    }

    public boolean tryAddSubstitution(Term substituted, Term value) {
        if (substituted == null) {
            throw new IllegalArgumentException("Substituted cannot be null.");
        }

        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }

        if (!((substituted instanceof Variable) || (value instanceof Variable))) {
            throw new IllegalArgumentException("One of the members of the substitution must be a variable.");
        }
        
        if (this.substitutions.containsKey(substituted)) {
            return this.substitutions.get(substituted).equals(value);
        }
        else {
            this.substitutions.put(substituted, value);
            return true;
        }
    }
   
    public ArrayList<Substitution> getSubstitutions() {
        ArrayList<Substitution> ret = new ArrayList<Substitution>();
        for (Entry<Term, Term> entry : substitutions.entrySet()) {
            ret.add(Substitution.creates(entry.getKey(), entry.getValue()));
        }

        return ret;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Substitution substitution : this.getSubstitutions()) {
            builder.append(substitution.toString());
        }

        return builder.toString();
    }

    
}
