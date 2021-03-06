/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import com.yapnu.adt.model.BooleanAdt;
import com.yapnu.adt.model.IntegerAdt;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class OperationTest {

    @Test(expected=IllegalArgumentException.class)
    public void testIsValidSubstitutionsWithoutSubstitutionBag() {
        Adt adt = BooleanAdt.instance().getAdt();

        Constant falseCte = adt.getConstant("false");
        OperationSignature andSignature = adt.getOperationSignature("and");
        Operation and = andSignature.instantiates(falseCte, falseCte);

        SubstitutionBag bag = new SubstitutionBag();
        assertFalse(and.tryGetMatchingSubstitutions(andSignature.instantiates(falseCte, falseCte), null));
    }

    @Test
    public void testIsValidSubstitutionsNotSameOperationSignature() {
        Adt adt = BooleanAdt.instance().getAdt();
        
        Constant falseCte = adt.getConstant("false");
        OperationSignature andSignature = adt.getOperationSignature("and");
        OperationSignature orSignature = adt.getOperationSignature("or");
        Operation and = andSignature.instantiates(falseCte, falseCte);

        SubstitutionBag bag = new SubstitutionBag();
        assertFalse("two different operations cannot be substituted", and.tryGetMatchingSubstitutions(orSignature.instantiates(falseCte, falseCte), bag));
        assertTrue("two different operations cannot be substituted", bag.size() == 0);

        assertFalse("an operation cannot be substituted with a constant", and.tryGetMatchingSubstitutions(falseCte, bag));
        assertTrue("an operation cannot be substituted with a constant", bag.size() == 0);

        assertTrue("an operation can be substituted with a variable", and.tryGetMatchingSubstitutions(new Variable("x", adt.getSort()), bag));
        assertTrue("an operation can be substituted with a variable", bag.size() == 1);
    }

    @Test
    public void testIsValidSubstitutionsConstants() {
        Adt adt = BooleanAdt.instance().getAdt();

        Constant trueCte = adt.getConstant("true");
        Constant falseCte = adt.getConstant("false");
        OperationSignature andSignature = adt.getOperationSignature("and");
        Operation and = andSignature.instantiates(falseCte, falseCte);

        SubstitutionBag bag = new SubstitutionBag();
        boolean canSubstitute1 = and.tryGetMatchingSubstitutions(andSignature.instantiates(falseCte, falseCte), bag);
        assertTrue("Can substitute ", canSubstitute1);
        assertTrue("No variable substituted ", bag.size() == 0);
        bag.clear();

        boolean canSubstitute2 = and.tryGetMatchingSubstitutions(andSignature.instantiates(trueCte, falseCte), bag);
        assertFalse("Cannot substitute ", canSubstitute2);
    }

    @Test
    public void testIsValidSubstitutionsOneVariable() {
        Adt adt = BooleanAdt.instance().getAdt();        

        Constant trueCte = adt.getConstant("true");
        Constant falseCte = adt.getConstant("false");
        OperationSignature andSignature = adt.getOperationSignature("and");        
        Operation and = andSignature.instantiates(adt.getVariable("x"), falseCte);

        SubstitutionBag bag = new SubstitutionBag();        
        boolean canSubstitute1 = and.tryGetMatchingSubstitutions(andSignature.instantiates(falseCte, falseCte), bag);
        assertTrue("Can substitute ", canSubstitute1);
        assertTrue("One variable substituted ", bag.size() == 1);
        assertTrue("Is the right substitution ", bag.getValue(adt.getVariable("x")).equals(falseCte));
        bag.clear();
        
        boolean canSubstitute2 = and.tryGetMatchingSubstitutions(andSignature.instantiates(trueCte, trueCte), bag);
        assertFalse("Cannot substitute ", canSubstitute2);
    }

    @Test
    public void testIsValidSubstitutionsOneVariableSeveralTimes() {
        Adt adt = BooleanAdt.instance().getAdt();
        
        Constant trueCte = adt.getConstant("true");
        Constant falseCte = adt.getConstant("false");
        OperationSignature andSignature = adt.getOperationSignature("and");        
        Operation and = andSignature.instantiates(adt.getVariable("x"), adt.getVariable("x"));
        
        SubstitutionBag bag1 = new SubstitutionBag();
        boolean canSubstitute1 = and.tryGetMatchingSubstitutions(andSignature.instantiates(falseCte, falseCte), bag1);
        assertTrue("Can substitute ", canSubstitute1);
        assertTrue("One variable substituted ", bag1.size() == 1);
        assertTrue("Is the right substitution ", bag1.getValue(adt.getVariable("x")).equals(falseCte));

        SubstitutionBag bag2 = new SubstitutionBag();        
        boolean cansubstitute2 = and.tryGetMatchingSubstitutions(andSignature.instantiates(trueCte, falseCte), bag2);
        assertFalse("Cannot substitute ", cansubstitute2);
    }

    @Test
    public void testIsValidSubstitutionsRecursive() {
        Adt adt = IntegerAdt.instance().getAdt();
        Variable variableX = new Variable("x", adt.getSort());
        
        Constant zero = adt.getConstant("0");
        
        OperationSignature succSignature = adt.getOperationSignature("succ");
        OperationSignature addSignature = adt.getOperationSignature("add");
        
        Operation add = addSignature.instantiates(variableX, zero);
        Operation succ = succSignature.instantiates(add);                        

        SubstitutionBag bag = new SubstitutionBag();
        boolean canSubstitute = succ.tryGetMatchingSubstitutions(
                succSignature.instantiates(addSignature.instantiates(succSignature.instantiates(zero), zero)),
                bag);
        assertTrue("Can substitute ", canSubstitute);
        assertTrue("One variable substituted ", bag.size() == 1);
        assertTrue("Is the right substitution ", bag.getValue(variableX).equals(succSignature.instantiates(zero)));
    }

    @Test
    public void testIsValidSubstitutionVariableSubstitutedWithOperation() {
        Sort sort = new Sort("sort");
        OperationSignature fSignature = new OperationSignature("f", false, sort, sort);
        OperationSignature gSignature = new OperationSignature("g", false, sort, sort);
        Variable varA = new Variable("A", sort);
        Variable varB = new Variable("B", sort);
        
        Term originalTerm = fSignature.instantiates(varB);
        SubstitutionBag bag = new SubstitutionBag();
        boolean canSubstitute = originalTerm.tryGetMatchingSubstitutions(fSignature.instantiates(gSignature.instantiates(varA)), bag);
        assertTrue("A variable can be substitued with an operation", canSubstitute);
        assertTrue("A variable can be substitued with an operation", bag.size() == 1);
        assertTrue("A variable can be substitued with an operation", bag.getValue(varB).equals(gSignature.instantiates(varA)));
    }

    @Test
    public void testIsValidSubstitutionVariableDependsOnAnotherVariable() {
        Sort sort = new Sort("sort");
        OperationSignature fSignature = new OperationSignature("f", false, sort, sort, sort);
        OperationSignature gSignature = new OperationSignature("g", false, sort, sort);
        Variable varA = new Variable("A", sort);
        Variable varB = new Variable("B", sort);
        Constant cte = new Constant("cte", sort);

        Term originalTerm = fSignature.instantiates(varB, cte);
        SubstitutionBag bag = new SubstitutionBag();
        boolean canSubstitute = originalTerm.tryGetMatchingSubstitutions(fSignature.instantiates(gSignature.instantiates(varA), varA), bag);
        assertTrue("A variable can be subsituted with an operation or a constant", canSubstitute);
        assertTrue("A variable can be subsituted with an operation or a constant", bag.size() == 2);
        assertTrue("A dependency exists betewen the substitutions", bag.getValue(varA).equals(cte));
        assertTrue("A dependency exists betewen the substitutions", bag.getValue(varB).equals(gSignature.instantiates(cte)));
    } 

    @Test
    public void testInstantiationWithSubstitutionOneVariableInSeveralTerms(){
        Adt adt = IntegerAdt.instance().getAdt();
        Variable variableX = new Variable("x", adt.getSort());
        OperationSignature addSignature = adt.getOperationSignature("add");
        OperationSignature succSignature = adt.getOperationSignature("succ");

        Constant zero = adt.getConstant("0");
                
        Operation add = addSignature.instantiates(variableX, succSignature.instantiates(variableX));
        Operation addAfterSubstitution = addSignature.instantiates(zero, succSignature.instantiates(zero));

        Substitution substitution = new Substitution(variableX, zero);
        
        Operation addSubstituted1 = add.substitutes(new SubstitutionBag(substitution));
        assertEquals("test syntaxic sugar ", addSubstituted1, addAfterSubstitution);
        
        Operation addSubstituted2 = add.substitutes(new SubstitutionBag(substitution));
        assertEquals("test general case", addSubstituted2, addAfterSubstitution);
    }

    @Test
    public void testInstantiationWithSubstitutionSeveralVariables(){
        Adt adt = IntegerAdt.instance().getAdt();
        Variable variableX = new Variable("x", adt.getSort());
        Variable variableY = new Variable("y", adt.getSort());
        OperationSignature addSignature = adt.getOperationSignature("add");
        OperationSignature succSignature = adt.getOperationSignature("succ");

        Constant zero = adt.getConstant("0");
                
        Operation add = addSignature.instantiates(variableY, succSignature.instantiates(variableX));
        Operation addAfterSubstitution = addSignature.instantiates(zero, succSignature.instantiates(zero));

        Substitution substitutionX = new Substitution(variableX, zero);
        Substitution substitutionY = new Substitution(variableY, zero);

        SubstitutionBag substitutions = new SubstitutionBag(substitutionX, substitutionY);
        
        Operation addSubstituted = add.substitutes(substitutions);
        assertEquals(addSubstituted, addAfterSubstitution);
    }

    @Test
    public void testIsNormalForm() {
        Adt adt = IntegerAdt.instance().getAdt();
        Constant zero = adt.getConstant("0");
        OperationSignature succ = adt.getOperationSignature("succ");
        OperationSignature add = adt.getOperationSignature("add");
        Variable variableX = new Variable("x", adt.getSort());

        assertTrue(succ.instantiates(zero).isNormalForm());
        assertTrue(succ.instantiates(succ.instantiates(zero)).isNormalForm());
        assertFalse(succ.instantiates(variableX).isNormalForm());
        assertFalse(succ.instantiates(succ.instantiates(variableX)).isNormalForm());       
                
        assertFalse(succ.instantiates(add.instantiates(zero, zero)).isNormalForm());
    }

    @Test
    public void testSize() {
      Adt adt = IntegerAdt.instance().getAdt();
      Constant zero = adt.getConstant("0");
      assertTrue(new OperationSignature("succ", true, adt.getSort(), adt.getSort()).instantiates(zero).size() == 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testVariableWithSameNameButDifferentTypesAreIllegals() {
        Sort dummySort = new Sort("dummy");
        Sort stupidSort = new Sort("stupid");
        Variable dummyX = new Variable("x", dummySort);
        Variable stupidX = new Variable("x", stupidSort);

        OperationSignature opSignature = new OperationSignature("op", false, dummySort, dummySort, stupidSort);
        opSignature.instantiates(dummyX, stupidX);
    }

    @Test
    public void testGetVariables() {
        Adt adt = IntegerAdt.instance().getAdt();
        Constant zero = adt.getConstant("0");
        Variable x = new Variable("x", adt.getSort());
        Variable y = new Variable("y", adt.getSort());
        OperationSignature succ = adt.getOperationSignature("succ");
        OperationSignature add = adt.getOperationSignature("add");
        
        Term term = add.instantiates(x, add.instantiates(zero, succ.instantiates(y)));
        ImmutableSet<Variable> expectedResult1 = ImmutableSet.of(x, y);
        assertEquals(term.getVariables(), expectedResult1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRenameVariablesNullBag() {
        Adt adt = IntegerAdt.instance().getAdt();
        Constant zero = adt.getConstant("0");
        Variable x = new Variable("x", adt.getSort());
        Variable y = new Variable("y", adt.getSort());
        OperationSignature succ = adt.getOperationSignature("succ");
        OperationSignature add = adt.getOperationSignature("add");

        Term term = add.instantiates(x, add.instantiates(zero, succ.instantiates(y)));
        term.renameVariables(null);
    }

    @Test
    public void testRenameVariables() {
        Adt adt = IntegerAdt.instance().getAdt();
        Constant zero = adt.getConstant("0");
        Variable x = new Variable("x", adt.getSort());
        Variable y = new Variable("y", adt.getSort());
        OperationSignature succ = adt.getOperationSignature("succ");
        OperationSignature add = adt.getOperationSignature("add");

        Operation operation = add.instantiates(x, add.instantiates(zero, succ.instantiates(y)));
        SubstitutionBag bag = new SubstitutionBag(new Substitution(y, zero));
        Term result = operation.renameVariables(bag);
        assertTrue(result instanceof Operation);
        assertEquals(operation.getOperationSignature(), ((Operation) result).getOperationSignature());
        assertTrue(bag.hasSubstitution(x));
        assertTrue(bag.getValue(x) instanceof Variable);
        assertEquals(bag.getValue(y), zero);
    }
}