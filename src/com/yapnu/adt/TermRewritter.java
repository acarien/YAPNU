/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author adrien
 */
public class TermRewritter {
    
    private final HashMap<Sort, Adt> adts;

    public TermRewritter(Adt adt) {        
        this.adts = new HashMap<Sort, Adt>();
        this.adts.put(adt.getSort(), adt);
    }

    public TermRewritter(ArrayList<Adt> adts) {        
        this.adts = new HashMap<Sort, Adt>();
        for (Adt adt : adts) {
            this.adts.put(adt.getSort(), adt);
        }
    }
    
    public Term Rewritte(Term term) {
       return this.Rewritte(term, null);
    }

    public Term Rewritte(Term term, Term previousTerm) {
        Term currentTerm = term;

        if (term == null) {
            
        }

        while (!(currentTerm.isGenerator() && currentTerm.isNormalForm())) {

            if (!this.adts.containsKey(currentTerm.getSort())) {
                
            }
            
            if (this.adts.get(currentTerm.getSort()).hasAxiom(currentTerm)) {
                Axiom axiom = this.adts.get(currentTerm.getSort()).getAxiom(currentTerm);
                previousTerm = currentTerm;
                currentTerm = axiom.getRightTerm();                
                continue;
            }

             if (currentTerm.equals(previousTerm) || currentTerm instanceof Variable) {                 
                throw new IllegalArgumentException("Cannot rewrite the term " + previousTerm + ".");
            }  

            Operation instantiatedTerm = (Operation) currentTerm;

            Term[] newParameters = new Term[instantiatedTerm.getParameters().length];
            for (int i = 0; i < newParameters.length; i++) {
                newParameters[i] = this.Rewritte(instantiatedTerm.getParamter(i), instantiatedTerm);
            }

            previousTerm = currentTerm;
            currentTerm = new Operation(instantiatedTerm.getOperationSignature(), newParameters);
        }

        return currentTerm;
    }
}
