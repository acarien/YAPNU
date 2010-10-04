/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

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
    Term substitutes(SubstitutionBag substitutions);
    boolean tryGetMatchingSubstitutions(Term other, SubstitutionBag bag);    
}
