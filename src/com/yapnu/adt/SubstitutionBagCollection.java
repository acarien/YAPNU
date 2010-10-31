/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author adrien
 */
public class SubstitutionBagCollection implements Iterable<SubstitutionBag> {
    private boolean success = false;
    private Set<SubstitutionBag> set = new HashSet<SubstitutionBag>();

    public SubstitutionBagCollection() {
    }

    public SubstitutionBagCollection(boolean success) {
        this.success = success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void add(SubstitutionBag bag) {
        set.add(bag);
    }

    public void addAll(SubstitutionBagCollection bags) {
        set.addAll(bags.set);
    }

    public int size() {
        return set.size();
    }

    public void clear() {
        this.set.clear();
    }

    public <T> T[] toArray(T[] ts) {
        return set.toArray(ts);
    }

    @Override
    public Iterator<SubstitutionBag> iterator() {
        return set.iterator();
    }

    public static boolean Distribute(ArrayList<Set<SubstitutionBag>> bags, Set<SubstitutionBag> result) {
        if (bags == null) {
            throw new IllegalArgumentException("Bags cannot be null.");
        }

        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null.");
        }

        for (Set<SubstitutionBag> bag : bags) {
            if (bag == null) {
                throw new IllegalArgumentException("A bag is null.");
            }

            if (bag.size() == 0) {
                bag.add(new SubstitutionBag());
            }
        }
        
        if (bags.size() == 0) {
            return true;
        }

        boolean hasSucceeded = false;
        for (SubstitutionBag bag : bags.get(0)) {
            if (CanDistribute(bags, 1, bag, result)) {
                hasSucceeded = true;
            }
        }

        return hasSucceeded;
    }

    private static boolean CanDistribute(ArrayList<Set<SubstitutionBag>> bags, int index, SubstitutionBag current, Set<SubstitutionBag> result) {
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
