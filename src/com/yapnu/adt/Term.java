/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import java.util.List;

/**
 *
 * @author adrien
 */
public interface Term {
    String getName();
    Sort getSort();
    boolean isGenerator();
    int size();
    boolean isNormalForm();
    //Term substitutes(Substitution substitution);
    Term substitutes(List<Substitution> substitutions);
    boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag bag);    
}
