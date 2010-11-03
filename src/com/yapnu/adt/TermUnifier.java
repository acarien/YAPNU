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
public class TermUnifier {

    //private final HashMap<Sort, LinkedList<Adt>> adts;
    private Adt adt;
    private TermRewritter rewritter;

    public TermUnifier(Adt adt, TermRewritter rewritter) {
        this.adt = adt;
        this.rewritter = rewritter;
    }

    TermRewritter getRewritter() {
        return rewritter;
    }   

    public Unification canUnify(Term term, Term expectedValue) {
        Unification unification = new Unification();
        if (term.equals(expectedValue)) {
            return unification;
        }

        // si un terme ne contient pas de variable, on peut le reecrire directement.
        // quelque soit le resultat, pas de substitutions necessaires
        ImmutableSet<Variable> variables = term.getVariables();
        if (variables.size() == 0) {
            term = rewritter.rewritte(term);            
            unification.setSuccess(term.equals(expectedValue));
            return unification;
        }

        // est-ce qu'il y a une substitution directe (genre (s(x) et s(0))
        unification = this.canUnifyThroughSimpleSubstitution(term, expectedValue);
        if (unification.isSuccess()) {
            return unification;
        }

        // est-ce qu'il y a un axiome qui s'applique
        unification = adt.canUnifyThroughAxioms(this, term, expectedValue);
        if (unification.isSuccess()) {            
            return unification;
        }

        // recursivement
        unification = term.canUnify(this, expectedValue);
        if (unification.isSuccess()) {
            return unification;
        }

        return Unification.FAIL;
    }

    private Unification canUnifyThroughSimpleSubstitution(Term term, Term expectedValue) {
        SubstitutionBag substitutions = new SubstitutionBag();
        boolean existsMatch = term.tryGetMatchingSubstitutions(expectedValue, substitutions);
        if (existsMatch) {
            return new Unification(substitutions);
        }

        return Unification.FAIL;
    }
}
