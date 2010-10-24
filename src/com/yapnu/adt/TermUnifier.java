/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;

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

    public boolean canUnifies(Term term, Term expectingValue, SubstitutionBag substitutions) {
        if (!expectingValue.isNormalForm()) {
            throw new IllegalArgumentException("ExpectingValue must be in normal form.");
        }

        if (term.equals(expectingValue)) {
            return true;
        }

        // si un terme ne contient pas de variable, on peut le reecrire directement.
        // quelque soit le resultat, pas de substitutions necessaires
        ImmutableSet<Variable> variables = term.getVariables();
        if (variables.size() == 0) {
            term = rewritter.rewritte(term);
            return term.equals(expectingValue);
        }

        // est-ce qu'il y a une substitution directe (genre (s(x) et s(0))
        SubstitutionBag localSubstitutions = new SubstitutionBag();
        boolean existsMatch = term.tryGetMatchingSubstitutions(expectingValue, localSubstitutions);
        if (existsMatch) {
            return substitutions.tryAddSubstitutions(localSubstitutions);
        }

        // est-ce qu'il y a un axiome qui s'applique
        substitutions.clear();
        localSubstitutions.clear();
        SubstitutionBag renamedVariables = new SubstitutionBag();
        Term renamedTerm = term.renameVariables(renamedVariables);
        if (this.canUnifiesThroughAxiom(renamedTerm, expectingValue, localSubstitutions)) {            
            // on ne doit remonter les subs que pour les variables reecrites //todo
            for (Variable variable : renamedTerm.getVariables()) {
                if (localSubstitutions.hasSubstitution(variable)) {
                    if (!substitutions.tryAddSubstitution(variable, localSubstitutions.getValue(variable))) {
                        return false;
                    }
                }
            }
            
            if (!substitutions.tryAddSubstitutions(renamedVariables)) {
                return false;
            }
            
            return true;
        }

        // recursivement
        substitutions.clear();        
        if (term instanceof Operation && expectingValue instanceof Operation) {
            Operation operation = (Operation)term;
            Operation expectingOperation = (Operation)expectingValue;
            if (operation.getOperationSignature().equals(expectingOperation.getOperationSignature())) {
                boolean allParametersAreUnified = true;
                for (int i=0;i<operation.getParameters().length;i++) {                    
                    if (!this.canUnifies(operation.getParamter(i), expectingOperation.getParamter(i), localSubstitutions)) {
                        allParametersAreUnified = false;
                        break;                        
                    }                    
                }

                // appliquer les substitutions sur tout les elements puis substituer le global
                if (allParametersAreUnified) {
                    substitutions.tryAddSubstitutions(localSubstitutions);
                    return true;
                }                               
            }
        }

        substitutions.clear();
        return false;
    }

    private boolean canUnifiesThroughAxiom(Term term, Term expectedValue, SubstitutionBag substitutions) {
        if (!this.adt.hasAxiomPerName(term.getName())) {
            return false;
        }

        SubstitutionBag axiomMatchingsubstitutions = new SubstitutionBag();
        for (Axiom possibleAxiom : this.adt.getAxiomPerName(term.getName())) {
            axiomMatchingsubstitutions.clear();

            boolean canSubstitute = possibleAxiom.getLeftTerm().tryGetMatchingSubstitutions(term, axiomMatchingsubstitutions);
            if (canSubstitute) {
                Term rightTerm = possibleAxiom.getRightTerm().substitutes(axiomMatchingsubstitutions);

                SubstitutionBag unificationSubstitution = new SubstitutionBag();
                boolean success = this.canUnifies(rightTerm, expectedValue, unificationSubstitution);
                if (success) {
                    substitutions.tryAddSubstitutions(unificationSubstitution);
                    
                    ImmutableSet<Variable> leftVariables = term.getVariables();
                    for (Variable variable : leftVariables) {
                        if (axiomMatchingsubstitutions.hasSubstitution(variable)) {
                            substitutions.tryAddSubstitution(variable, axiomMatchingsubstitutions.getValue(variable));
                        }
                    }

                    return true;
                }
            }
        }
        

        return false;
    }
}
