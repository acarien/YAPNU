/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
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
        if (name.startsWith(Axiom.VARIABLE_PREFIX)) {
            throw new IllegalArgumentException("Invalid name");
        }
        
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

        if (term.getName().startsWith(Axiom.VARIABLE_PREFIX)) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (!this.sort.equals(term.getSort())) {
            throw new IllegalArgumentException("Term should be of type " + sort + ".");
        }
        
        if (this.variables.containsKey(term.getName())) {
            throw new IllegalArgumentException("Term " + term.toString() + " is already defined for the current adt.");
        }
        
        this.variables.put(term.getName(), term);
    }

    public void addOperationSignature(OperationSignature operationSignature) {
        if (operationSignature == null) {
            throw new IllegalArgumentException("Operation Signature cannot be null.");
        }

        if (this.operationTerms.containsKey(operationSignature.getName())) {
            throw new IllegalArgumentException("Operation Signature " + operationSignature.toString() + " is already defined for the current adt.");
        }

        this.operationTerms.put(operationSignature.getName(), operationSignature);
        if (!operationSignature.getSort().equals(this.sort)) {
            this.additionalCodomains.add(operationSignature.getSort());
        }
    }

    public boolean hasAxiom(Term leftTerm) {
        return this.getAxiom(leftTerm) != null;
    }

    public Axiom getAxiom(Term leftTerm) {
        SubstitutionBag substitutions = new SubstitutionBag();

        Axiom axiom = this.axioms.get(leftTerm);
        if (axiom != null) {
            return axiom;
        }

        if (this.axiomPerName.containsKey(leftTerm.getName())) {

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

    public boolean hasAxiomPerName(String name) {
        return this.axiomPerName.containsKey(name);
    }

    public LinkedList<Axiom> getAxiomPerName(String name) {
        return this.axiomPerName.get(name);
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
