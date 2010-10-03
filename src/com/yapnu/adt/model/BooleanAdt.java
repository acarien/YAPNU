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
import java.util.ArrayList;

/**
 *
 * @author adrien
 */
public class BooleanAdt {

    private static final Sort sort = new Sort("bool");    
    private final Adt adt;
    private static BooleanAdt instance;

    private BooleanAdt() {
        this.adt = BuildAdt();
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

    private static Adt BuildAdt() {
        Adt adt = new Adt(sort);

        Constant trueTerm = new Constant("true", sort);
        Constant falseTerm = new Constant("false", sort);
        adt.addTerm(trueTerm);
        adt.addTerm(falseTerm);

        ArrayList<Sort> domain = new ArrayList<Sort>();
        domain.add(sort);
        domain.add(sort);

        OperationSignature andTerm = new OperationSignature("and", domain, sort, false);
        OperationSignature orTerm = new OperationSignature("or", domain, sort, false);
        OperationSignature notTerm = new OperationSignature("not", sort, sort, false);
        adt.addTerm(andTerm);
        adt.addTerm(orTerm);
        adt.addTerm(notTerm);

        Term[] falseTermArray = {(Term) falseTerm};
        Term[] trueTermArray = {(Term) trueTerm};
        Term[] falseFalse = {(Term) falseTerm, (Term)falseTerm};
        Term[] trueFalse = {(Term) trueTerm, (Term)falseTerm};
        Term[] falseTrue = {(Term) falseTerm, (Term)trueTerm};
        Term[] trueTrue = {(Term) trueTerm, (Term)trueTerm};

        Axiom andAxiom1 = new Axiom(andTerm.instantiates(falseFalse), falseTerm);
        Axiom andAxiom2 = new Axiom(andTerm.instantiates(falseTrue), falseTerm);
        Axiom andAxiom3 = new Axiom(andTerm.instantiates(trueFalse), falseTerm);
        Axiom andAxiom4 = new Axiom(andTerm.instantiates(trueTrue), trueTerm);
        adt.addAxiom(andAxiom1);
        adt.addAxiom(andAxiom2);
        adt.addAxiom(andAxiom3);
        adt.addAxiom(andAxiom4);

        Axiom orAxiom1 = new Axiom(orTerm.instantiates(falseFalse), falseTerm);
        Axiom orAxiom2 = new Axiom(orTerm.instantiates(falseTrue), trueTerm);
        Axiom orAxiom3 = new Axiom(orTerm.instantiates(trueFalse), trueTerm);
        Axiom orAxiom4 = new Axiom(orTerm.instantiates(trueTrue), trueTerm);
        adt.addAxiom(orAxiom1);
        adt.addAxiom(orAxiom2);
        adt.addAxiom(orAxiom3);
        adt.addAxiom(orAxiom4);
        
        Axiom notAxiom1 = new Axiom(notTerm.instantiates(falseTermArray), trueTerm);
        Axiom notAxiom2 = new Axiom(notTerm.instantiates(trueTermArray), falseTerm);
        adt.addAxiom(notAxiom1);
        adt.addAxiom(notAxiom2);

        return adt;
    }    
}
