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
import com.yapnu.adt.Variable;
import java.util.ArrayList;

/**
 *
 * @author adrien
 */
public class IntegerAdt {
    private static final Sort sort = new Sort("int");
    private final Adt adt;
    private static IntegerAdt instance;

    private IntegerAdt() {
        this.adt = getAdt();
    }

    public static IntegerAdt instance() {
        if (instance == null) {
             instance = new IntegerAdt();
        }

        return instance;
    }

    public Adt getAdt() {
        Adt adt = new Adt(sort);
        
        adt.addTerm(new Constant("0", sort));

        ArrayList<Sort> unaryDomain = new ArrayList<Sort>();
        unaryDomain.add(sort);
        
        adt.addTerm(new OperationSignature("succ", unaryDomain, sort, true));
                                
        Variable x = new Variable("x", sort);
        Variable y = new Variable("y", sort);
        adt.addTerm(x);
        adt.addTerm(y);

        AddAddOperation(adt);
        AddEqualsOperation(adt);

        return adt;
    }

    private static void AddAddOperation(Adt adt) {
        ArrayList<Sort> dyadicDomain = new ArrayList<Sort>();
        dyadicDomain.add(sort);
        dyadicDomain.add(sort);

        OperationSignature add = new OperationSignature("add", dyadicDomain, sort, false);
        adt.addTerm(add);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature succSignature = adt.getOperationSignature("succ");
        Constant zeroTerm = adt.getConstant("0");

        Term[] xZero = {x, zeroTerm};
        Term[] zeroX = {zeroTerm, x};
        Term[] xy = {x, y};
        Term[] succXY = {succSignature.instantiates(x) ,y};

        adt.addAxiom(new Axiom(add.instantiates(zeroX), x));
        adt.addAxiom(new Axiom(add.instantiates(xZero), x));
        adt.addAxiom(new Axiom(add.instantiates(succXY), succSignature.instantiates(add.instantiates(xy))));
    }

    private static void AddEqualsOperation(Adt adt) {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        ArrayList<Sort> dyadicDomain = new ArrayList<Sort>();
        dyadicDomain.add(sort);
        dyadicDomain.add(sort);

        OperationSignature equals = new OperationSignature("=", dyadicDomain, boolAdt.getSort(), false);
        adt.addTerm(equals);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature succSignature = adt.getOperationSignature("succ");
        Constant zeroTerm = adt.getConstant("0");
        Constant falseTerm = boolAdt.getConstant("false");
        Constant trueTerm = boolAdt.getConstant("true");

        Term[] zeroZero = {zeroTerm, zeroTerm};
        Term[] zeroSucc = {zeroTerm, succSignature.instantiates(x)};
        Term[] succZero = {succSignature.instantiates(x), zeroTerm};
        Term[] succSucc = {succSignature.instantiates(x), succSignature.instantiates(y)};
        Term[] xy = {x, y};

        adt.addAxiom(new Axiom(equals.instantiates(zeroZero), trueTerm));
        adt.addAxiom(new Axiom(equals.instantiates(zeroSucc), falseTerm));
        adt.addAxiom(new Axiom(equals.instantiates(succZero), falseTerm));
        adt.addAxiom(new Axiom(equals.instantiates(succSucc),  equals.instantiates(xy)));
    }    
}
