/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author adrien
 */
public class Unification implements Iterable<SubstitutionBag> {
    final static Unification FAIL = new Unification(false);

    private boolean success = false;
    private Set<SubstitutionBag> set = new HashSet<SubstitutionBag>();

    public Unification() {
        this(true);
    }

    public Unification(SubstitutionBag... substitutions) {
        this(true);
        for (SubstitutionBag substitution : substitutions) {
            this.add(substitution);
        }
    }
    
    private Unification(boolean success) {
        this.success = success;
    }

    void setSuccess(boolean success) {
        this.success = success;
        if (!success) {
            this.set.clear();
        }
    }

    public boolean isSuccess() {
        return success;
    }

    void add(SubstitutionBag bag) {
        set.add(bag);
    }

    void addAll(Unification other) {
        set.addAll(other.set);
    }

    void clear() {
        this.set.clear();
    }

    public int size() {
        return set.size();
    }
    
    public ImmutableList<Substitution> getSubstitutions(Variable substituted) {
        LinkedList<Substitution> substitutionsForTerm = new LinkedList<Substitution>();
        for (SubstitutionBag substitutions : set) {
            if (substitutions.hasSubstitution(substituted)) {
                substitutionsForTerm.addLast(new Substitution(substituted, substitutions.getValue(substituted)));
            }
        }

        return ImmutableList.copyOf(substitutionsForTerm);
    }

    @Override
    public Iterator<SubstitutionBag> iterator() {
        return set.iterator();
    }

    public static Unification Distribute(ArrayList<Unification> unifications) {
        if (unifications == null) {
            throw new IllegalArgumentException("Unifications cannot be null.");
        }
         
        Unification result = new Unification();
        if (unifications.size() == 0) {
            return result;
        }

        for (Unification unification : unifications) {
            if (unification == null) {
                throw new IllegalArgumentException("A unification is null.");
            }

            if (!unification.isSuccess()) {
                return Unification.FAIL;
            }

            if (unification.size() == 0) {
                unification.add(new SubstitutionBag());
            }
        }
                        
        boolean hasSucceeded = false;
        for (SubstitutionBag unification : unifications.get(0)) {
            if (CanDistribute(unifications, 1, unification, result)) {
                hasSucceeded = true;
            }
        }

        result.setSuccess(hasSucceeded);
        return result;
    }    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Unification other = (Unification) obj;
        if (this.success != other.success) {
            return false;
        }
        if (this.set != other.set && (this.set == null || !this.set.equals(other.set))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.success ? 1 : 0);
        hash = 37 * hash + (this.set != null ? this.set.hashCode() : 0);
        return hash;
    }

    private static boolean CanDistribute(ArrayList<Unification> bags, int index, SubstitutionBag current, Unification result) {
        if (index >= bags.size()) {
            SubstitutionBag res = new SubstitutionBag();
            res.tryAddSubstitutions(current);
            result.add(res);
            return true;
        }

        SubstitutionBag copy = new SubstitutionBag();
        copy.tryAddSubstitutions(current);

        boolean hasSucceeded = false;
        for (SubstitutionBag bag : bags.get(index)) {
            current.clear();
            current.tryAddSubstitutions(copy);

            if (!current.tryAddSubstitutions(bag)) {
                continue;
            }

            if (CanDistribute(bags, index + 1, current, result)) {
                hasSucceeded = true;
            }
        }

        return hasSucceeded;
    }
}
