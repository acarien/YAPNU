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
public interface Term {

    String getName();

    Sort getSort();

    int size();

    boolean isNormalForm();

    Term substitutes(SubstitutionBag substitutions);

    boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag bag);

    ImmutableSet<Variable> getVariables();

    Term rewritte(TermRewritter termRewritter);

    Term renameVariables(SubstitutionBag bag);

    Unification canUnify(TermUnifier termUnifier, Term expectedValue);
}
