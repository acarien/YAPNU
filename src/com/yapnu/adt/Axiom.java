/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

/**
 *
 * @author adrien
 */
public class Axiom {

    private Term leftTerm;
    private Term rightTerm;

    public Axiom(Term leftTerm, Term rightTerm) {        
        this.leftTerm = leftTerm;
        this.rightTerm = rightTerm;
    }

    public Term getLeftTerm() {
        return leftTerm;
    }

    public Term getRightTerm() {
        return rightTerm;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Axiom other = (Axiom) obj;
        if (this.leftTerm != other.leftTerm && (this.leftTerm == null || !this.leftTerm.equals(other.leftTerm))) {
            return false;
        }
        if (this.rightTerm != other.rightTerm && (this.rightTerm == null || !this.rightTerm.equals(other.rightTerm))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.leftTerm != null ? this.leftTerm.hashCode() : 0);
        return hash;
    }

    /*private void SetMembersByShortlexOrder(Term leftTerm, Term rightTerm) {
        int leftSize = leftTerm.size();
        int rightSize = rightTerm.size();
        if (leftSize > rightSize) {
            this.leftTerm = leftTerm;
            this.rightTerm = rightTerm;
        }
        else if (leftSize == rightSize) {
            int nameComparison = leftTerm.getName().compareTo(rightTerm.getName());
            if (nameComparison >= 0) {
                this.leftTerm = leftTerm;
                this.rightTerm = rightTerm;
            }
            else {
                this.leftTerm = rightTerm;
                this.rightTerm = leftTerm;
            }
        }
        else {
            this.leftTerm = rightTerm;
                this.rightTerm = leftTerm;                                                
        }
    }*/

    @Override
    public String toString() {
        return this.leftTerm.toString() + " = " + this.rightTerm.toString();
    }

}
