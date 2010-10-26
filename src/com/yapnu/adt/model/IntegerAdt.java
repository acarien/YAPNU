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
import com.yapnu.adt.Term;
import com.yapnu.adt.Variable;

/**
 *
 * @author adrien
 */
public class IntegerAdt {

    private static final Sort sort = new Sort("int");
    private static IntegerAdt instance;
    private final Adt adt;

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

    public Term getNumber(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Only positive number.");
        }

        if (number == 0) {
            return this.adt.getConstant("0");
        }

        Term result = this.adt.getConstant("0");
        OperationSignature succSignature = this.adt.getOperationSignature("succ");
        for (int i = 0; i < number; i++) {
            result = succSignature.instantiates(result);
        }

        return result;
    }

    private Adt buildAdt() {
        Adt adt = new Adt(sort);

        adt.addTerm(new Constant("0", sort));
        adt.addOperationSignature(new OperationSignature("succ", true, sort, sort));

        Variable x = new Variable("x", sort);
        Variable y = new Variable("y", sort);
        adt.addTerm(x);
        adt.addTerm(y);

        addAddOperation(adt);
        addEqualsOperation(adt);
        addLessOperation(adt);
        addLessOrEqualOperation(adt);
        addGreaterOperation(adt);
        addGreaterOrEqualOperation(adt);
        addSubstractionOperation(adt);

        return adt;
    }

    private static void addAddOperation(Adt adt) {
        OperationSignature add = new OperationSignature("add", false, sort, sort, sort);
        adt.addOperationSignature(add);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature succSignature = adt.getOperationSignature("succ");
        Constant zeroTerm = adt.getConstant("0");

        adt.addAxiom(new Axiom(add.instantiates(zeroTerm, x), x));
        adt.addAxiom(new Axiom(add.instantiates(x, zeroTerm), x));
        adt.addAxiom(new Axiom(add.instantiates(succSignature.instantiates(x), y), succSignature.instantiates(add.instantiates(x, y))));
    }

    private static void addEqualsOperation(Adt adt) {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        OperationSignature equals = new OperationSignature("=", false, boolAdt.getSort(), sort, sort);
        adt.addOperationSignature(equals);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature succSignature = adt.getOperationSignature("succ");
        Constant zeroTerm = adt.getConstant("0");
        Constant falseTerm = boolAdt.getConstant("false");
        Constant trueTerm = boolAdt.getConstant("true");

        adt.addAxiom(new Axiom(equals.instantiates(zeroTerm, zeroTerm), trueTerm));
        adt.addAxiom(new Axiom(equals.instantiates(zeroTerm, succSignature.instantiates(x)), falseTerm));
        adt.addAxiom(new Axiom(equals.instantiates(succSignature.instantiates(x), zeroTerm), falseTerm));
        adt.addAxiom(new Axiom(equals.instantiates(succSignature.instantiates(x), succSignature.instantiates(y)), equals.instantiates(x, y)));
    }

    private static void addLessOperation(Adt adt) {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        OperationSignature less = new OperationSignature("<", false, boolAdt.getSort(), sort, sort);
        adt.addOperationSignature(less);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature succSignature = adt.getOperationSignature("succ");
        Constant zeroTerm = adt.getConstant("0");
        Constant falseTerm = boolAdt.getConstant("false");
        Constant trueTerm = boolAdt.getConstant("true");

        adt.addAxiom(new Axiom(less.instantiates(zeroTerm, zeroTerm), falseTerm));
        adt.addAxiom(new Axiom(less.instantiates(zeroTerm, succSignature.instantiates(x)), trueTerm));
        adt.addAxiom(new Axiom(less.instantiates(succSignature.instantiates(x), zeroTerm), falseTerm));
        adt.addAxiom(new Axiom(less.instantiates(succSignature.instantiates(x), succSignature.instantiates(y)), less.instantiates(x, y)));
    }

    private static void addLessOrEqualOperation(Adt adt) {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        OperationSignature lessOrEqual = new OperationSignature("<=", false, boolAdt.getSort(), sort, sort);
        adt.addOperationSignature(lessOrEqual);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature notSignature = boolAdt.getOperationSignature("not");
        OperationSignature lessSignature = adt.getOperationSignature("<");

        adt.addAxiom(new Axiom(lessOrEqual.instantiates(x, y), notSignature.instantiates(lessSignature.instantiates(y, x))));
    }

    private static void addGreaterOperation(Adt adt) {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        OperationSignature greater = new OperationSignature(">", false, boolAdt.getSort(), sort, sort);
        adt.addOperationSignature(greater);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature notSignature = boolAdt.getOperationSignature("not");
        OperationSignature lessOrEqualSignature = adt.getOperationSignature("<=");

        adt.addAxiom(new Axiom(greater.instantiates(x, y), notSignature.instantiates(lessOrEqualSignature.instantiates(x, y))));
    }

    private static void addGreaterOrEqualOperation(Adt adt) {
        Adt boolAdt = BooleanAdt.instance().getAdt();

        OperationSignature greaterOrEqual = new OperationSignature(">=", false, boolAdt.getSort(), sort, sort);
        adt.addOperationSignature(greaterOrEqual);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature notSignature = boolAdt.getOperationSignature("not");
        OperationSignature lessSignature = adt.getOperationSignature("<");

        adt.addAxiom(new Axiom(greaterOrEqual.instantiates(x, y), notSignature.instantiates(lessSignature.instantiates(x, y))));
    }

    private static void addSubstractionOperation(Adt adt) {
        OperationSignature substraction = new OperationSignature("-", false, sort, sort, sort);
        adt.addOperationSignature(substraction);

        Variable x = adt.getVariable("x");
        Variable y = adt.getVariable("y");
        OperationSignature succSignature = adt.getOperationSignature("succ");        
        Constant zeroTerm = adt.getConstant("0");
              
        adt.addAxiom(new Axiom(substraction.instantiates(zeroTerm, x), zeroTerm));
        adt.addAxiom(new Axiom(substraction.instantiates(succSignature.instantiates(y), zeroTerm), succSignature.instantiates(y)));
        adt.addAxiom(new Axiom(substraction.instantiates(succSignature.instantiates(y), succSignature.instantiates(x)), substraction.instantiates(y, x)));
    }
}
