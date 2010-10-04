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
    LinkedHashMap<Variable, Term> substitutions = new LinkedHashMap<Variable, Term>();

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

        return this.tryAddSubstitution(substitution.getVariable(), substitution.getTerm());
    }

    public boolean tryAddSubstitution(Variable variable, Term term) {
        if (variable == null) {
            throw new IllegalArgumentException("Variable cannot be null.");
        }

        if (term == null) {
            throw new IllegalArgumentException("Term cannot be null.");
        }

        if (this.substitutions.containsKey(variable)) {
            return this.substitutions.get(variable).equals(term);
        }
        else {
            this.substitutions.put(variable, term);
            return true;
        }
    }
   
    public ArrayList<Substitution> getSubstitutions() {
        ArrayList<Substitution> ret = new ArrayList<Substitution>();
        for (Entry<Variable, Term> entry : substitutions.entrySet()) {
            ret.add(new Substitution(entry.getKey(), entry.getValue()));
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
