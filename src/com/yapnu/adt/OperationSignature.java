/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.Arrays;

/**
 *
 * @author adrien
 */
public final class OperationSignature {

    private final String name;
    private final Sort[] domain;
    private final Sort codomain;
    private final boolean isGenerator;

    public OperationSignature(String name, boolean isGenerator, Sort codomain, Sort... domain) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("OperationSignature name cannot be null or empty.");
        }

        if (codomain == null) {
            throw new IllegalArgumentException("OperationSignature codomain cannot be null.");
        }

        if (domain == null || domain.length == 0) {
            throw new IllegalArgumentException("OperationSignature domain cannot be null.");
        }

        this.name = name;
        this.domain = domain;
        this.codomain = codomain;
        this.isGenerator = isGenerator;
    }

    public Sort getSort() {
        return codomain;
    }

    public Sort[] getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public boolean isGenerator() {
        return isGenerator;
    }

    public Operation instantiates(Term... parameters) {
        if (parameters.length != this.domain.length){
            throw new IllegalArgumentException("The size of parameters is not equal to the arity of the operation.");
        }

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] == null) {
                throw new IllegalArgumentException("A parameter cannot be null.");
            }

            if (!parameters[i].getSort().equals(this.domain[i]))
            {
                throw new IllegalArgumentException("Parameter " + parameters[i] + " has not the right domain.");
            }            
        }

        return new Operation(this, parameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        return this.equals((OperationSignature) obj);
        
    }

    public boolean equals(OperationSignature other) {
        if (other == null) {
            return false;
        }

        if (!this.name.equals(other.name)) {
            return false;
        }

        if (this.codomain != other.codomain && !this.codomain.equals(other.codomain)) {
            return false;
        }

        if (!Arrays.deepEquals(this.domain, other.domain)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.name.hashCode();
        hash = 97 * hash + this.codomain.hashCode();
        return hash;
    }
}
