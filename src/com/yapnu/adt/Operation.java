/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author adrien
 */
public class Operation implements Term {

    private final OperationSignature signature;
    private final Term[] parameters;

    public Operation(OperationSignature signature, Term[] parameters) {
        this.signature = signature;
        this.parameters = parameters;
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

    @Override
    public boolean isGenerator() {
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
    public boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag substitutions) {
        if (substitutions == null) {
            throw new IllegalArgumentException("Substitutions cannot be null.");
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

    public Operation substitutes(Substitution substitution) {
        ArrayList<Substitution> substitutions = new ArrayList<Substitution>();
        substitutions.add(substitution);
        return this.substitutes(substitutions);
    }

   @Override
    public Operation substitutes(List<Substitution> substitutions) {
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
    public boolean isNormalForm() {
       if (!isGenerator()) {
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
        hash = 53 * hash + (this.signature != null ? this.signature.hashCode() : 0);
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
}
