/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt.model;

import com.yapnu.adt.Adt;
import com.yapnu.adt.Constant;
import com.yapnu.adt.Sort;

/**
 *
 * @author adrien
 */
public class BlackTokenAdt {
 private static final Sort sort = new Sort("BlackToken");
    private final Adt adt;
    private static BlackTokenAdt instance;

    private BlackTokenAdt() {
        this.adt = buildAdt();
    }

    public static BlackTokenAdt instance() {
        if (instance == null) {
             instance = new BlackTokenAdt();
        }

        return instance;
    }

    public Adt getAdt() {
        return adt;
    }
        
    private Adt buildAdt() {
        Adt adt = new Adt(sort);
        adt.addTerm(new Constant("token", sort));
        return adt;
    }

}
