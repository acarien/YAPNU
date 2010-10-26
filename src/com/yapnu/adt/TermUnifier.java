/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
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
        /*if (canUnifyThroughAxioms(term, expectedValue, substitutionSet)) {
            return true;
        }*/
        if (adt.canUnifyThroughAxioms(this, term, expectedValue, substitutionSet)) {
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
            return true;
        }

        return false;
    }

    boolean canUnifyRecursively(Term term, Term expectedValue, Set<SubstitutionBag> substitutionSet) {
        if (term instanceof Operation && expectedValue instanceof Operation) {
            Operation operation = (Operation) term;
            Operation expectingOperation = (Operation) expectedValue;
            if (operation.getOperationSignature().equals(expectingOperation.getOperationSignature())) {
                boolean allParametersAreUnified = true;
                ArrayList<Set<SubstitutionBag>> allSubstitutionSet = new ArrayList<Set<SubstitutionBag>>();

                for (int i = 0; i < operation.getParameters().length; i++) {
                    Set<SubstitutionBag> localSubstitutionSet = new HashSet<SubstitutionBag>();
                    if (!this.canUnify(operation.getParamter(i), expectingOperation.getParamter(i), localSubstitutionSet)) {
                        allParametersAreUnified = false;
                        break;
                    }

                    allSubstitutionSet.add(localSubstitutionSet);
                }
                
                if (allParametersAreUnified) {
                    substitutionSet.addAll(Distribute(allSubstitutionSet));
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
            /*if (canUnifyThroughAxiom(renamedTerm, expectedValue, possibleAxiom, localSubstitutionSet)) {
                hasUnified = true;
            }*/
            if (possibleAxiom.canUnify(this, renamedTerm, expectedValue, localSubstitutionSet)) {
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

    private boolean canUnifyThroughAxiom(Term term, Term expectedValue, Axiom possibleAxiom, Set<SubstitutionBag> substitutionSet) {
        // est-ce que l'axiom peut s'appliquer (en verifiant la conclusion de l'axiom) ?
        HashSet<SubstitutionBag> axiomConclusionSubstitutionSet = new HashSet<SubstitutionBag>();
        boolean canUseAxiom = this.canUnify(possibleAxiom.getRightTerm(), expectedValue, axiomConclusionSubstitutionSet);
        if (!canUseAxiom) {
            return false;
        }

        boolean canAxiomBeUnified = false;
        for (SubstitutionBag axiomConclustionSubstitutions : axiomConclusionSubstitutionSet) {
            // est-ce qu'on peut unifier le membre de gauche de l'axiome avec la substitution faite sur le droit?
            Term leftTerm = possibleAxiom.getLeftTerm().substitutes(axiomConclustionSubstitutions);
            HashSet<SubstitutionBag> leftTermSubstitutionSet = new HashSet<SubstitutionBag>();
            boolean canSubstituteLeftTermWithConclustionSubstitutions = this.canUnifyRecursively(term, leftTerm, leftTermSubstitutionSet);
            if (!canSubstituteLeftTermWithConclustionSubstitutions) {
                continue;
            }

            if (leftTermSubstitutionSet.size() == 0) {
                leftTermSubstitutionSet.add(new SubstitutionBag());
            }

            for (SubstitutionBag leftTermSubstitutions : leftTermSubstitutionSet) {
                HashSet<SubstitutionBag> tmpSet = new HashSet<SubstitutionBag>();
                Term rightTerm = possibleAxiom.getRightTerm().substitutes(leftTermSubstitutions);
                boolean success = this.canUnify(rightTerm, expectedValue, tmpSet);
                if (!success) {
                    continue;
                }

                substitutionSet.add(leftTermSubstitutions);
                canAxiomBeUnified = true;
            }
        }

        return canAxiomBeUnified;
    }

    private static Set<SubstitutionBag> Distribute(ArrayList<Set<SubstitutionBag>> bags) {
        for (Set<SubstitutionBag> bag : bags) {
            if (bag.size() == 0) {
                bag.add(new SubstitutionBag());
            }
        }

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        if (bags.size() >= 0) {
            for (SubstitutionBag bag : bags.get(0)) {
                CanDistribute(bags, 1, bag, result);
            }
        }

        return result;
    }

    private static void CanDistribute(ArrayList<Set<SubstitutionBag>> bags, int index, SubstitutionBag current, Set<SubstitutionBag> result) {
        if (index >= bags.size()) {
            SubstitutionBag res = new SubstitutionBag();
            res.tryAddSubstitutions(current);
            result.add(res);
            return;
        }

        SubstitutionBag copy = new SubstitutionBag();
        copy.tryAddSubstitutions(current);

        for (SubstitutionBag bag : bags.get(index)) {
            current.clear();
            current.tryAddSubstitutions(copy);

            if (!current.tryAddSubstitutions(bag)) {
                return;
            }

            CanDistribute(bags, index + 1, current, result);
        }
    }
}
