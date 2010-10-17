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
public class BooleanAdt {

    private static final Sort sort = new Sort("bool");    
    private final Adt adt;
    private static BooleanAdt instance;

    private BooleanAdt() {
        this.adt = buildAdt();
    }

    public static BooleanAdt instance() {
        if (instance == null) {
            instance = new BooleanAdt();
        }

        return instance;
    }

    public Adt getAdt() {
        return adt;
    }

    private static Adt buildAdt() {
        Adt adt = new Adt(sort);

        Constant trueTerm = new Constant("true", sort);
        Constant falseTerm = new Constant("false", sort);
        adt.addTerm(trueTerm);
        adt.addTerm(falseTerm);

        Variable x = new Variable("x", sort);
        Variable y = new Variable("y", sort);
        adt.addTerm(x);
        adt.addTerm(y);

        OperationSignature andTerm = new OperationSignature("and", false, sort, sort, sort);
        OperationSignature orTerm = new OperationSignature("or", false, sort, sort, sort);
        OperationSignature notTerm = new OperationSignature("not", false, sort, sort);
        adt.addOperationSignature(andTerm);
        adt.addOperationSignature(orTerm);
        adt.addOperationSignature(notTerm);

        Axiom andAxiom1 = new Axiom(andTerm.instantiates(falseTerm, falseTerm), falseTerm);
        Axiom andAxiom2 = new Axiom(andTerm.instantiates(falseTerm, trueTerm), falseTerm);
        Axiom andAxiom3 = new Axiom(andTerm.instantiates(trueTerm, falseTerm), falseTerm);
        Axiom andAxiom4 = new Axiom(andTerm.instantiates(trueTerm, trueTerm), trueTerm);
        adt.addAxiom(andAxiom1);
        adt.addAxiom(andAxiom2);
        adt.addAxiom(andAxiom3);
        adt.addAxiom(andAxiom4);

        Axiom orAxiom1 = new Axiom(orTerm.instantiates(falseTerm, falseTerm), falseTerm);
        Axiom orAxiom2 = new Axiom(orTerm.instantiates(falseTerm, trueTerm), trueTerm);
        Axiom orAxiom3 = new Axiom(orTerm.instantiates(trueTerm, falseTerm), trueTerm);
        Axiom orAxiom4 = new Axiom(orTerm.instantiates(trueTerm, trueTerm), trueTerm);
        adt.addAxiom(orAxiom1);
        adt.addAxiom(orAxiom2);
        adt.addAxiom(orAxiom3);
        adt.addAxiom(orAxiom4);
        
        Axiom notAxiom1 = new Axiom(notTerm.instantiates(falseTerm), trueTerm);
        Axiom notAxiom2 = new Axiom(notTerm.instantiates(trueTerm), falseTerm);
        adt.addAxiom(notAxiom1);
        adt.addAxiom(notAxiom2);

        return adt;
    }    
}
