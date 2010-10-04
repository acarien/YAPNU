/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author adrien
 */
public class TermRewritter {
    
    private final HashMap<Sort, LinkedList<Adt>> adts;

    public TermRewritter(Adt adt) {        
        this.adts = new HashMap<Sort, LinkedList<Adt>>();
        this.addAdt(adt);
        
    }

    public TermRewritter(ArrayList<Adt> adts) {        
        this.adts = new HashMap<Sort, LinkedList<Adt>>();
        for (Adt adt : adts) {
            this.addAdt(adt);
        }
    }

    private void addAdt(Adt adt) {
        if (!this.adts.containsKey(adt.getSort())) {
            this.adts.put(adt.getSort(), new LinkedList<Adt>());
        }

        this.adts.get(adt.getSort()).addFirst(adt);

        LinkedList<Sort> otherSorts = adt.getAdditionalCodomains();
        for (Sort sort : otherSorts) {
            if (!this.adts.containsKey(sort)) {
                this.adts.put(sort, new LinkedList<Adt>());
            }

            this.adts.get(sort).addLast(adt);
        }
    }

    public Term rewritte(Term term) {
       return this.rewritte(term, null);
    }

    private Term rewritte(Term term, Term previousTerm) {
        Term currentTerm = term;

        if (term == null) {
            
        }

        while (!(currentTerm.isGenerator() && currentTerm.isNormalForm())) {

            if (!this.adts.containsKey(currentTerm.getSort())) {
                
            }

            LinkedList<Adt> adts = this.adts.get(currentTerm.getSort());            
            boolean hasFound = false;
            for (Iterator<Adt> iterator = adts.descendingIterator(); !hasFound && iterator.hasNext();) {
                Adt adt = iterator.next();
                Axiom axiom = adt.getAxiom(currentTerm);
                if (axiom != null) {                    
                    previousTerm = currentTerm;
                    currentTerm = axiom.getRightTerm();
                    hasFound = true;
                }
            }

            if (hasFound) {
                continue;
            }
            
            /*if (this.adts.get(currentTerm.getSort()).hasAxiom(currentTerm)) {
                Axiom axiom = this.adts.get(currentTerm.getSort()).getAxiom(currentTerm);
                previousTerm = currentTerm;
                currentTerm = axiom.getRightTerm();                
                continue;
            }*/

             if (currentTerm.equals(previousTerm) || currentTerm instanceof Variable) {                 
                throw new IllegalArgumentException("Cannot rewrite the term " + previousTerm + ".");
            }  

            Operation instantiatedTerm = (Operation) currentTerm;

            Term[] newParameters = new Term[instantiatedTerm.getParameters().length];
            for (int i = 0; i < newParameters.length; i++) {
                newParameters[i] = this.rewritte(instantiatedTerm.getParamter(i), instantiatedTerm);
            }

            previousTerm = currentTerm;
            currentTerm = new Operation(instantiatedTerm.getOperationSignature(), newParameters);
        }

        return currentTerm;
    }
}
