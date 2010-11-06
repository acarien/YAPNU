/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author adrien
 */
public final class Operation implements Term {

    private final OperationSignature signature;
    private final Term[] parameters;

    Operation(OperationSignature signature, Term[] parameters) {
        this.signature = signature;
        this.parameters = parameters;
        this.variablesMustHaveSameSort();
    }

    public Term[] getParameters() {
        return parameters;
    }

    public Term getParamter(int index) {
        return parameters[index];
    }

    public OperationSignature getOperationSignature() {
        return signature;
    }

    @Override
    public String getName() {
        return this.signature.getName();
    }
    
    private boolean isGenerator() {
        return this.signature.isGenerator();
    }

    @Override
    public Sort getSort() {
        return this.signature.getSort();
    }

    @Override
    public int size() {
        return this.parameters.length + 1;
    }

    @Override
    public ImmutableSet<Variable> getVariables() {
        Builder<Variable> builder = ImmutableSet.builder();
        for (Term parameter : this.parameters) {
            builder.addAll(parameter.getVariables());
        }

        return builder.build();
    }

    @Override
    public boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag substitutions) {
        if (substitutions == null) {
            throw new IllegalArgumentException("Substitutions cannot be null.");
        }
        
        if (other instanceof Variable) {
            return other.tryGetMatchingSubstitutions(this, substitutions);
        }
        
        if (!(other instanceof Operation)) {
            return false;
        }

        Operation otherOperation = (Operation) other;
        if (!this.getOperationSignature().equals(otherOperation.getOperationSignature())) {
            return false;
        }

        for (int i = 0; i < this.parameters.length; i++) {
            if (!this.parameters[i].tryGetMatchingSubstitutions(otherOperation.getParamter(i), substitutions)) {
                return false;
            }
        }

        return true;
    }

   @Override
    public Operation substitutes(SubstitutionBag substitutions) {
       if (substitutions == null) {
            throw new IllegalArgumentException("Substitutions cannnot be null.");
        }
       
        Term[] newParameters = new Term[this.parameters.length];
        for (int i = 0;i < this.parameters.length; i++) {
            newParameters[i] = this.parameters[i].substitutes(substitutions);
        }

        return new Operation(this.signature, newParameters);
    } 

   @Override
    public Term rewritte(TermRewritter termRewritter) {
        Term[] newParameters = new Term[this.parameters.length];
        for (int i = 0; i < newParameters.length; i++) {
            newParameters[i] = termRewritter.rewritte(this.parameters[i], null);
        }

        return this.signature.instantiates(newParameters);
    }
   
   @Override
    public boolean isNormalForm() {
       if (!this.isGenerator()) {
           return false;
       }

        for(Term parameter : this.parameters) {
            if (!parameter.isNormalForm()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        return this.equals((Operation) obj);
    }

    public boolean equals(Operation other) {
        if (other == null) {
            return false;
        }

        if (this.signature != other.signature && (this.signature == null || !this.signature.equals(other.signature))) {
            return false;
        }

        if (!Arrays.deepEquals(this.parameters, other.parameters)) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.signature.hashCode();
        hash = 53 * hash + Arrays.deepHashCode(this.parameters);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getName());
        builder.append(" ( ");
        for (Term parameter : this.parameters) {
            builder.append(parameter.toString());
            builder.append(", ");
        }

        builder.append(")");
        return builder.toString();
    }

    private void variablesMustHaveSameSort() {
        ImmutableSet<Variable> variables = this.getVariables();
        HashMap<String, Variable> conflicts = new HashMap<String, Variable>();
        for (Variable variable : variables) {
            if (conflicts.containsKey(variable.getName()) &&
                !variable.getSort().equals(conflicts.get(variable.getName()).getSort())) {
                throw new IllegalArgumentException("Variables having the same name must be of the same sort.");
            }
            else {
                conflicts.put(variable.getName(), variable);
            }
        }
    } 

    @Override
    public Term renameVariables(SubstitutionBag substitutions) {        
        Term[] newParameters = new Term[this.parameters.length];
        for (int i = 0; i < newParameters.length; i++) {
            newParameters[i] = this.parameters[i].renameVariables(substitutions);
        }

        return this.signature.instantiates(newParameters);
    }

    @Override
    public Unification canUnify(TermUnifier termUnifier, Term expectedValue) {
        if (!(expectedValue instanceof Operation)) {
            return Unification.FAIL;
        }

        Operation expectingOperation = (Operation) expectedValue;
        if (!this.signature.equals(expectingOperation.getOperationSignature())) {
            return Unification.FAIL;
        }

        boolean allParametersAreUnified = true;
        ArrayList<Unification> allSubstitutionSet = new ArrayList<Unification>();

        for (int i = 0; i < this.parameters.length; i++) {
            Unification unification = termUnifier.canUnify(this.parameters[i], expectingOperation.getParamter(i));
            if (!unification.isSuccess()) {
                allParametersAreUnified = false;
                break;
            }

            allSubstitutionSet.add(unification);
        }

        if (allParametersAreUnified) {            
            // on doit filtrer le resultat par rapport aux variables du terme
            return Unification.distribute(allSubstitutionSet);
        }
        
        return Unification.FAIL;
    }    
}
