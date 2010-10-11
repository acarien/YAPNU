/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt.model;

import com.yapnu.adt.Adt;
import com.yapnu.adt.Axiom;
import com.yapnu.adt.Constant;
import com.yapnu.adt.OperationSignature;
import com.yapnu.adt.Sort;
import com.yapnu.adt.Variable;

/**
 *
 * @author adrien
 */
public class IntegerAdt {
    private static final Sort sort = new Sort("int");
    private final Adt adt;
    private static IntegerAdt instance;

    private IntegerAdt() {
        this.adt = buildAdt();
    }

    public static IntegerAdt instance() {
        if (instance == null) {
             instance = new IntegerAdt();
        }

        return instance;
    }

    public Adt getAdt() {      
        return adt;
    }

    private Adt buildAdt() {
        Adt adt = new Adt(sort);

        adt.addTerm(new Constant("0", sort));
        adt.addTerm(new OperationSignature("succ", true, sort, sort));

        Variable x = new Variable("x", sort);
        Variable y = new Variable("y", sort);
        adt.addTerm(x);
        adt.addTerm(y);

        addAddOperation(adt);
        addEqualsOperation(adt);

        return adt;
    }

    private static void addAddOperation(Adt adt) {
        OperationSignature add = new OperationSignature("add", false, sort, sort, sort);
        adt.addTerm(add);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature succSignature = adt.getOperationSignature("succ");
        Constant zeroTerm = adt.getConstant("0");        

        adt.addAxiom(new Axiom(add.instantiates(zeroTerm, x), x));
        adt.addAxiom(new Axiom(add.instantiates(x, zeroTerm), x));
        adt.addAxiom(new Axiom(add.instantiates(succSignature.instantiates(x) ,y), succSignature.instantiates(add.instantiates(x, y))));
    }

    private static void addEqualsOperation(Adt adt) {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        OperationSignature equals = new OperationSignature("=", false, boolAdt.getSort(), sort, sort);
        adt.addTerm(equals);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature succSignature = adt.getOperationSignature("succ");
        Constant zeroTerm = adt.getConstant("0");
        Constant falseTerm = boolAdt.getConstant("false");
        Constant trueTerm = boolAdt.getConstant("true");
                
        adt.addAxiom(new Axiom(equals.instantiates(zeroTerm, zeroTerm), trueTerm));
        adt.addAxiom(new Axiom(equals.instantiates(zeroTerm, succSignature.instantiates(x)), falseTerm));
        adt.addAxiom(new Axiom(equals.instantiates(succSignature.instantiates(x), zeroTerm), falseTerm));
        adt.addAxiom(new Axiom(equals.instantiates(succSignature.instantiates(x), succSignature.instantiates(y)),  equals.instantiates(x, y)));
    }    
}
