/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author adrien
 */
public final class AdtBag {
    private final HashMap<Sort, LinkedList<Adt>> adts = new HashMap<Sort, LinkedList<Adt>>();

    public AdtBag() {
    }

    public AdtBag(Adt... adts) {
        for (Adt adt : adts) {
            this.add(adt);
        }
    }

    public AdtBag(Collection<Adt> adts) {
        HashSet<Sort> sorts = new HashSet<Sort>();        
        for (Adt adt : adts) {
            if (sorts.contains(adt.getSort())) {
                throw new IllegalArgumentException("Cannot have two adts with the same main sort.");
            }

            sorts.add(adt.getSort());

            this.add(adt);
        }
    }

    public boolean hasAdt(Sort sort) {
        return this.adts.containsKey(sort);
    }

    public Collection<Adt> getAdt(Sort sort) {
        return this.adts.get(sort);
    }

    public void add(Adt adt) {
        if (!this.adts.containsKey(adt.getSort())) {
            this.adts.put(adt.getSort(), new LinkedList<Adt>());
        }

        this.adts.get(adt.getSort()).addFirst(adt);

        LinkedList<Sort> otherSorts = adt.getAdditionalCodomains();
        for (Sort sort : otherSorts) {
            if (!this.adts.containsKey(sort)) {
                this.adts.put(sort, new LinkedList<Adt>());
            }

            this.adts.get(sort).addLast(adt);
        }
    }
}
