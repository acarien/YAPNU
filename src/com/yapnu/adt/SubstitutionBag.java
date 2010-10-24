/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.HashMultimap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author adrien
 */
public class SubstitutionBag {
    private final LinkedHashMap<Variable, Term> substitutions = new LinkedHashMap<Variable, Term>();
    private boolean hasBeenModified = false;
    private boolean isRecomputing = false;

    public SubstitutionBag() {
    }

    public SubstitutionBag(Substitution... substitutions) {
        for (Substitution substitution : substitutions) {
            this.tryAddSubstitution(substitution);
        }

        this.hasBeenModified = true;
    }

    public void clear() {
        this.substitutions.clear();
        this.hasBeenModified = false;
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

    public boolean tryAddSubstitution(Variable substituted, Term value) {
        if (substituted == null) {
            throw new IllegalArgumentException("Substituted cannot be null.");
        }

        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }        
        
        if (this.substitutions.containsKey(substituted)) {
            return this.substitutions.get(substituted).equals(value);
        }
        else {
            this.substitutions.put(substituted, value);
            this.hasBeenModified = true;
            return true;
        }
    }

    public boolean tryAddSubstitutions(SubstitutionBag bag) {
        if (bag == null) {
            throw new IllegalArgumentException("Bag cannot be null.");
        }

        bag.needToComputeSubstitutions();
        for (Variable variable : bag.substitutions.keySet()) {
            if (!this.tryAddSubstitution(variable, bag.getValue(variable))) {
                return false;
            }
        }

        return true;
    }

    public boolean hasSubstitution(Variable substituted) {
        return this.substitutions.containsKey(substituted);
    }
    
    public Term getValue(Variable substituted) {
        this.needToComputeSubstitutions();
        return this.substitutions.get(substituted);
    }

    public void retainsAll(Set<Variable> variablesToKeep) {
        this.needToComputeSubstitutions();

        this.substitutions.keySet().retainAll(variablesToKeep);
        /*LinkedList<Variable> toBeRemoved = new LinkedList<Variable>();
        this.substitutions.keySet().retainAll(toBeRemoved);
        for (Variable variable : this.substitutions.keySet()) {
            if (!variablesToKeep.contains(variable)) {
                toBeRemoved.addLast(variable);
            }
        }

        for (Variable variable : toBeRemoved) {
            this.substitutions.remove(variable);
        }*/
    }

    /*public ImmutableList<Substitution> getSubstitutions() {
        this.needToComputeSubstitutions();
        LinkedList<Substitution> list = new LinkedList<Substitution>();
        for (Entry<Variable, Term> entry : this.substitutions.entrySet()) {
            list.addFirst(new Substitution(entry.getKey(), entry.getValue()));
        }

        return ImmutableList.copyOf(list);
    }*/

    
    private void needToComputeSubstitutions() {
        if (this.hasBeenModified && this.substitutions.size() > 1 && !this.isRecomputing) {
            this.isRecomputing = true;
            this.computeSubstitutions();
            this.hasBeenModified = false;
            this.isRecomputing = false;
        }
    }

    private void computeSubstitutions() {
        HashMultimap<Variable, Variable> variableDependencies = this.getVariableDependencies();
        TopologicalSort<Variable> sort = new TopologicalSort<Variable>(variableDependencies);
        sort.sort();
        
        ArrayList<Variable> variables = new ArrayList<Variable>(sort.getSortedElements());
        for (Variable variable : variables) {
            Term originalSubstitution = this.substitutions.get(variable);
            if (originalSubstitution == null) {
                continue;
            }

            this.substitutions.remove(variable);
            Term finalSubstitution = originalSubstitution.substitutes(this);            
            this.tryAddSubstitution(variable, finalSubstitution);
        }
    }

    private HashMultimap<Variable, Variable> getVariableDependencies() {
        HashMultimap<Variable, Variable> connectivityList = HashMultimap.create();
        for (Entry<Variable, Term> entry : substitutions.entrySet()) {
            Set<Variable> dependentVariables = entry.getValue().getVariables();
            if (dependentVariables.size() == 0) {
                connectivityList.put(entry.getKey(), null);
            } else {
                connectivityList.putAll(entry.getKey(), dependentVariables);
            }
        }
        
        return connectivityList;
    }

    @Override
    public String toString() {
        this.needToComputeSubstitutions();

        StringBuilder builder = new StringBuilder();
        for (Entry<Variable, Term> substitution : this.substitutions.entrySet()) {
            builder.append("<");
            builder.append(substitution.getKey());
            builder.append(", ");
            builder.append(substitution.getValue());
            builder.append(">");
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubstitutionBag other = (SubstitutionBag) obj;
        if (this.substitutions != other.substitutions && (this.substitutions == null || !this.substitutions.equals(other.substitutions))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.substitutions != null ? this.substitutions.hashCode() : 0);
        return hash;
    }
}
