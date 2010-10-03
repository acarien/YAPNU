/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt.model;

import com.yapnu.adt.Adt;
import com.yapnu.adt.Axiom;
import com.yapnu.adt.Term;
import com.yapnu.adt.Constant;
import com.yapnu.adt.OperationSignature;
import com.yapnu.adt.Sort;
import java.util.ArrayList;

/**
 *
 * @author adrien
 */
public class CharAdtFactory {
    private static final Sort sort = new Sort("int");

    public static Adt GetAdt() {
        Adt adt = new Adt(sort);

        Constant empty = new Constant("", sort);
        Constant a = new Constant("a", sort);
        Constant b = new Constant("b", sort);
        adt.addTerm(empty);
        adt.addTerm(a);
        adt.addTerm(b);

        ArrayList<Sort> domain = new ArrayList<Sort>();
        domain.add(sort);
        domain.add(sort);
        OperationSignature concat = new OperationSignature("concat", domain, sort, false);
        adt.addTerm(concat);

                        
        Term[] aa = {a ,a};
        Term[] bb = {b, b};
        Term[] ab = {a, b};
        Term[] aaaa = {concat.instantiates(aa), concat.instantiates(aa)};
        Term[] aaab = {concat.instantiates(aa), concat.instantiates(ab)};

        Axiom axiom1 = new Axiom(concat.instantiates(aaaa), empty);
        Axiom axiom2 = new Axiom(concat.instantiates(bb), empty);
        Axiom axiom3 = new Axiom(concat.instantiates(aaab), empty);
        adt.addAxiom(axiom1);
        adt.addAxiom(axiom2);
        adt.addAxiom(axiom3);

        return adt;
    }
}
