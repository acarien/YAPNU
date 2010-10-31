/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

/**
 *
 * @author adrien
 */
public class TermUnifier {

    private Adt adt;
    private TermRewritter rewritter;

    public TermUnifier(Adt adt, TermRewritter rewritter) {
        this.adt = adt;
        this.rewritter = rewritter;
    }

    TermRewritter getRewritter() {
        return rewritter;
    }   

    public boolean canUnify(Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        if (term.equals(expectedValue)) {
            return true;
        }

        // si un terme ne contient pas de variable, on peut le reecrire directement.
        // quelque soit le resultat, pas de substitutions necessaires
        ImmutableSet<Variable> variables = term.getVariables();
        if (variables.size() == 0) {
            term = rewritter.rewritte(term);
            return term.equals(expectedValue);
        }

        // est-ce qu'il y a une substitution directe (genre (s(x) et s(0))
        if (this.canUnifyThroughSimpleSubstitution(term, expectedValue, substitutionSet)) {
            return true;
        }

        // est-ce qu'il y a un axiome qui s'applique        
        if (adt.canUnifyThroughAxioms(this, term, expectedValue, substitutionSet)) {
            return true;
        }

        // recursivement
        if (term.canUnifyRecursively(this, expectedValue, substitutionSet)) {
            return true;
        }

        return false;
    }

    private boolean canUnifyThroughSimpleSubstitution(Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        SubstitutionBag substitutions = new SubstitutionBag();
        boolean existsMatch = term.tryGetMatchingSubstitutions(expectedValue, substitutions);
        if (existsMatch) {
            substitutionSet.add(substitutions);
            return true;
        }

        return false;
    }
}
