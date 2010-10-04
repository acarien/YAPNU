/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.ArrayList;

/**
 *
 * @author adrien
 */
public class OperationSignature {

    private final String name;
    private final ArrayList<Sort> domain;
    private final Sort codomain;
    private final boolean isGenerator;

    public OperationSignature(String name, Sort domain, Sort codomain, boolean isGenerator) {
        this.name = name;
        this.domain = new ArrayList<Sort>();
        this.domain.add(domain);
        this.codomain = codomain;
        this.isGenerator = isGenerator;
    }

    public OperationSignature(String name, ArrayList<Sort> domain, Sort codomain, boolean isGenerator) {
        this.name = name;
        this.domain = domain;
        this.codomain = codomain;
        this.isGenerator = isGenerator;
    }

    public Sort getSort() {
        return codomain;
    }

    public ArrayList<Sort> getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public boolean isGenerator() {
        return isGenerator;
    }

    public Operation instantiates(Term... parameters) {
        if (parameters.length != this.domain.size()){
            throw new IllegalArgumentException("The size of parameters is not equal to the arity of the operation.");
        }

        for (int i = 0; i < parameters.length; i++) {
            if (!parameters[i].getSort().equals(this.domain.get(i)))
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

        if (this.isGenerator != other.isGenerator) {
            return false;
        }

        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }

        if (this.codomain != other.codomain && (this.codomain == null || !this.codomain.equals(other.codomain))) {
            return false;
        }
        
        if (this.domain != other.domain && (this.domain == null || !this.domain.equals(other.domain))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.codomain != null ? this.codomain.hashCode() : 0);
        return hash;
    }
}
