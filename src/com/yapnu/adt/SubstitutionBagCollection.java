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
    private Set<SubstitutionBag> set = new HashSet<SubstitutionBag>();
    private SubstitutionBag constraint;

    public SubstitutionBagCollection() {
    }

    public SubstitutionBagCollection(SubstitutionBag constraint) {
        this.constraint = constraint;
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

    public static Set<SubstitutionBag> Distribute(ArrayList<Set<SubstitutionBag>> bags) {
        if (bags == null) {
            throw new IllegalArgumentException("Bags cannot be null.");
        }

        for (Set<SubstitutionBag> bag : bags) {
            if (bag == null) {
                throw new IllegalArgumentException("A bag is null.");
            }

            if (bag.size() == 0) {
                bag.add(new SubstitutionBag());
            }
        }

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        if (bags.size() > 0) {
            for (SubstitutionBag bag : bags.get(0)) {
                CanDistribute(bags, 1, bag, result);
            }
        }

        return result;
    }

    private static void CanDistribute(ArrayList<Set<SubstitutionBag>> bags, int index, SubstitutionBag current, Set<SubstitutionBag> result) {
        if (index >= bags.size()) {
            SubstitutionBag res = new SubstitutionBag();
            res.tryAddSubstitutions(current);
            result.add(res);
            return;
        }

        SubstitutionBag copy = new SubstitutionBag();
        copy.tryAddSubstitutions(current);

        for (SubstitutionBag bag : bags.get(index)) {
            current.clear();
            current.tryAddSubstitutions(copy);

            if (!current.tryAddSubstitutions(bag)) {
                return;
            }

            CanDistribute(bags, index + 1, current, result);
        }
    }
}
