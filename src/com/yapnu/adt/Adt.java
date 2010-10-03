/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author adrien
 */
public class Adt {

    private Sort sort;
    private HashMap<String, Constant> constants = new HashMap<String, Constant>();
    private HashMap<String, OperationSignature> operationTerms = new HashMap<String, OperationSignature>();
    private HashMap<String, Variable> variables = new HashMap<String, Variable>();
    private HashMap<Term, Axiom> axioms = new HashMap<Term, Axiom>();
    private HashMap<String, LinkedList<Axiom>> axiomPerName = new HashMap<String,LinkedList<Axiom>>();

    public Adt(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }

    public OperationSignature getOperationSignature(String name) {
        return this.operationTerms.get(name);
    }

    public Constant getConstant(String name) {
        return this.constants.get(name);
    }

    public Variable getVariable(String name) {
        return this.variables.get(name);
    }

    public void addTerm(Constant term) {
        this.constants.put(term.getName(), term);
    }

    public void addTerm(Variable term) {
        this.variables.put(term.getName(), term);
    }

    public void addTerm(OperationSignature term) {        
        this.operationTerms.put(term.getName(), term);
    }

    public boolean hasAxiom(Term leftTerm) {
        return this.getAxiom(leftTerm) != null;
    }

    public Axiom getAxiom(Term leftTerm) {
        Axiom axiom = this.axioms.get(leftTerm);
        if (axiom != null) {
            return axiom;
        }

        if (leftTerm instanceof Constant) {
            // throw exception
        }

        if (this.axiomPerName.containsKey(leftTerm.getName())) {                                   
            SubstitutionBag substitutions = new SubstitutionBag();
            for (Axiom possibleAxiom : this.axiomPerName.get(leftTerm.getName())) {
                substitutions.clear();                
                boolean canSubstitute = possibleAxiom.getLeftTerm().tryGetMatchingSubstitutions(leftTerm, substitutions);
                if (canSubstitute) {                    
                    Term rightTerm = possibleAxiom.getRightTerm().substitutes(substitutions.getSubstitutions());
                    return new Axiom(leftTerm, rightTerm);
                }                
            }
        }
        
        return null;
    }

    public void addAxiom(Axiom axiom) {        
        if (this.axiomPerName.get(axiom.getLeftTerm().getName()) == null) {
            this.axiomPerName.put(axiom.getLeftTerm().getName(), new LinkedList<Axiom>());
        }

        this.axiomPerName.get(axiom.getLeftTerm().getName()).addLast(axiom);
        this.axioms.put(axiom.getLeftTerm(), axiom);
    }
}
