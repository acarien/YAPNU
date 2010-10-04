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

    private final Sort sort;
    private final HashMap<String, Constant> constants = new HashMap<String, Constant>();
    private final HashMap<String, OperationSignature> operationTerms = new HashMap<String, OperationSignature>();
    private final HashMap<String, Variable> variables = new HashMap<String, Variable>();
    private final HashMap<Term, Axiom> axioms = new HashMap<Term, Axiom>();
    private final HashMap<String, LinkedList<Axiom>> axiomPerName = new HashMap<String,LinkedList<Axiom>>();
    private final LinkedList<Sort> additionalCodomains = new LinkedList<Sort>();

    public Adt(Sort sort) {
        if (sort == null) {
            throw new IllegalArgumentException("Sort cannot be null.");
        }
        
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }

    public LinkedList<Sort> getAdditionalCodomains() {
        return additionalCodomains;
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
        if (term == null) {
            throw new IllegalArgumentException("Term cannot be null.");
        }

        if (!this.sort.equals(term.getSort())) {
            throw new IllegalArgumentException("Term should be of type " + sort + ".");
        }

        if (this.constants.containsKey(term.getName())) {
            throw new IllegalArgumentException("Term " + term.toString() + " is already defined for the current adt.");
        }

        this.constants.put(term.getName(), term);
    }

    public void addTerm(Variable term) {
        if (term == null) {
            throw new IllegalArgumentException("Term cannot be null.");
        }

        if (!this.sort.equals(term.getSort())) {
            throw new IllegalArgumentException("Term should be of type " + sort + ".");
        }
        
        if (this.variables.containsKey(term.getName())) {
            throw new IllegalArgumentException("Term " + term.toString() + " is already defined for the current adt.");
        }
        
        this.variables.put(term.getName(), term);
    }

    public void addTerm(OperationSignature term) {
        if (term == null) {
            throw new IllegalArgumentException("Term cannot be null.");
        }

        if (this.operationTerms.containsKey(term.getName())) {
            throw new IllegalArgumentException("Term " + term.toString() + " is already defined for the current adt.");
        }

        this.operationTerms.put(term.getName(), term);
        if (!term.getSort().equals(this.sort)) {
            this.additionalCodomains.add(term.getSort());
        }
    }

    public boolean hasAxiom(Term leftTerm) {
        return this.getAxiom(leftTerm) != null;
    }

    public Axiom getAxiom(Term leftTerm) {
        Axiom axiom = this.axioms.get(leftTerm);
        if (axiom != null) {
            return axiom;
        }
        
        if (this.axiomPerName.containsKey(leftTerm.getName())) {                                   
            SubstitutionBag substitutions = new SubstitutionBag();
            for (Axiom possibleAxiom : this.axiomPerName.get(leftTerm.getName())) {
                substitutions.clear();                
                boolean canSubstitute = possibleAxiom.getLeftTerm().tryGetMatchingSubstitutions(leftTerm, substitutions);
                if (canSubstitute) {                    
                    Term rightTerm = possibleAxiom.getRightTerm().substitutes(substitutions);
                    return new Axiom(leftTerm, rightTerm);
                }                
            }
        }
        
        return null;
    }

    public void addAxiom(Axiom axiom) {
        if (axiom == null) {
            throw new IllegalArgumentException("Axiom cannot be null.");
        }

        if (this.axiomPerName.get(axiom.getLeftTerm().getName()) == null) {
            this.axiomPerName.put(axiom.getLeftTerm().getName(), new LinkedList<Axiom>());
        }

        this.axiomPerName.get(axiom.getLeftTerm().getName()).addLast(axiom);
        this.axioms.put(axiom.getLeftTerm(), axiom);
    }
}
