/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import java.util.Collection;

/**
 *
 * @author adrien
 */
public class TermRewritter {
    
    private final AdtBag adtBag;

    public TermRewritter(AdtBag adts) {
        this.adtBag = adts;
    }
                
    public Term rewritte(Term term) {
       return this.rewritte(term, null);
    }

     Term rewritte(Term term, Term previousTerm) {
        if (term == null) {
            throw new IllegalArgumentException("Cannot rewrite a null term.");
        }

        Term currentTerm = term;        
        while (!currentTerm.isNormalForm()) {
            if (currentTerm.equals(previousTerm)) {
                return currentTerm;
            }

            if (!this.adtBag.hasAdt(currentTerm.getSort())) {
                throw new IllegalArgumentException("Cannot rewrite a term of the sort " + currentTerm.getSort().toString() + ".");
            }

            Axiom axiom = this.getAxiom(currentTerm);
            if (axiom != null) {
                previousTerm = currentTerm;
                currentTerm = axiom.getRightTerm();
                continue;
            }

            Term currentTermCopied = currentTerm;
            currentTerm = currentTerm.rewritte(this);
            previousTerm = currentTermCopied;
        }

        return currentTerm;
    }

    private Axiom getAxiom(Term term) {
        Collection<Adt> foundAdts = this.adtBag.getAdt(term.getSort());
        for (Adt adt : foundAdts) {            
            Axiom axiom = adt.getAxiom(this, term);
            if (axiom != null) {
                return axiom;
            }
        }

        return null;
    }
}
