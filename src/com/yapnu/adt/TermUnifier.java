/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
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

    public boolean canUnify(Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        if (!expectedValue.isNormalForm()) {
            throw new IllegalArgumentException("ExpectingValue must be in normal form.");
        }

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
        if (canUnifyThroughAxioms(term, expectedValue, substitutionSet)) {
            return true;
        }

        // recursivement
        if (canUnifyRecursively(term, expectedValue, substitutionSet)) {
            return true;
        }
        
        return false;
    }

    private boolean canUnifyThroughSimpleSubstitution(Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        SubstitutionBag substitutions = new SubstitutionBag();
        boolean existsMatch = term.tryGetMatchingSubstitutions(expectedValue, substitutions);
        if (existsMatch) {
            substitutionSet.add(substitutions);
            return substitutions.tryAddSubstitutions(substitutions);
        }

        return false;
    }    

    private boolean canUnifyRecursively(Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        if (term instanceof Operation && expectedValue instanceof Operation) {
            Operation operation = (Operation)term;
            Operation expectingOperation = (Operation)expectedValue;
            if (operation.getOperationSignature().equals(expectingOperation.getOperationSignature())) {
                boolean allParametersAreUnified = true;
                HashSet<SubstitutionBag> localSubstitutionSet = new HashSet<SubstitutionBag>();
                for (int i=0;i<operation.getParameters().length;i++) {
                    if (!this.canUnify(operation.getParamter(i), expectingOperation.getParamter(i), localSubstitutionSet)) {
                        allParametersAreUnified = false;
                        break;
                    }
                }

                // appliquer les substitutions sur tout les elements puis substituer le global
                if (allParametersAreUnified) {                    
                    substitutionSet.addAll(localSubstitutionSet);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean canUnifyThroughAxioms(Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        SubstitutionBag renamedVariables = new SubstitutionBag();
        Term renamedTerm = term.renameVariables(renamedVariables);        

        if (!this.adt.hasAxiomPerName(term.getName())) {
            return false;
        }

        boolean hasUnified = false;
        HashSet<SubstitutionBag> localSubstitutionSet = new HashSet<SubstitutionBag>();        
        for (Axiom possibleAxiom : this.adt.getAxiomPerName(renamedTerm.getName())) {
            SubstitutionBag bag = new SubstitutionBag();
            if (canUnifiesThroughAxiom(renamedTerm, expectedValue, possibleAxiom, bag)) {
                localSubstitutionSet.add(bag);
                hasUnified = true;
            }
        }

        if (!hasUnified) {
            return false;
        }
                
        for (SubstitutionBag substitutions : localSubstitutionSet) {
            // on ne doit remonter les subs que pour les variables reecrites
            if (!substitutions.tryAddSubstitutions(renamedVariables)) {
                return false;
            }

            substitutions.retainsAll(term.getVariables());
            substitutionSet.add(substitutions);
        }

        return true;
    }

    private boolean canUnifiesThroughAxiom(Term term, Term expectedValue, Axiom possibleAxiom, SubstitutionBag substitutions) {
        SubstitutionBag axiomMatchingSubstitutions = new SubstitutionBag();

        boolean canSubstitute = possibleAxiom.getLeftTerm().tryGetMatchingSubstitutions(term, axiomMatchingSubstitutions);
        if (!canSubstitute) {
            return false;
        }

        HashSet<SubstitutionBag> unificationSet = new HashSet<SubstitutionBag>();
        Term rightTerm = possibleAxiom.getRightTerm().substitutes(axiomMatchingSubstitutions);
        boolean success = this.canUnify(rightTerm, expectedValue, unificationSet);
        if (!success) {
            return false;
        }

        if (unificationSet.size() > 0) {
            for (SubstitutionBag unificationSubstitution : unificationSet) {
                substitutions.tryAddSubstitutions(axiomMatchingSubstitutions);
                if (!substitutions.tryAddSubstitutions(unificationSubstitution)) {
                    return false;
                }

                substitutions.retainsAll(term.getVariables());
            }

            return true;
        }

        substitutions.tryAddSubstitutions(axiomMatchingSubstitutions);
        substitutions.retainsAll(term.getVariables());
        return true;
    }
}
