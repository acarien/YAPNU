/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class VariableTest {
    
    private final Sort sort = new Sort("sort");
    private final Variable x = new Variable("x", this.sort);

    /**
     * Test of getSort method, of class Variable.
     */
    @Test
    public void testGetSort() {        
        assertEquals(x.getSort(), this.sort);
    }

    /**
     * Test of equals method, of class Variable.
     */
    @Test
    public void testEquals() {        
        Variable xCloned = new Variable("x", this.sort);
        Variable v2 = new Variable("y", this.sort);
        Variable v3 = new Variable("x", new Sort("anotherSort"));

        assertTrue("same objects", x.equals(xCloned));
        assertTrue("symmetry", xCloned.equals(x));
        assertFalse("difference on names", x.equals(v2));
        assertFalse("difference on sorts", x.equals(v3));
    }

    @Test
    public void testIsNotNormalForm() {
        assertFalse(x.isNormalForm());
    }

    @Test
    public void testSizeIsOne() {
        assertEquals(x.size(), 1);
    }

    @Test
    public void testIsValidSubstitution() {
        SubstitutionBag bag = new SubstitutionBag();

        assertFalse("variable substituted by null", x.tryGetMatchingSubstitutions(null, bag));
        assertTrue(bag.size() == 0);
        bag.clear();

        assertTrue("variable substituted by cte", x.tryGetMatchingSubstitutions(new Constant("cte", sort), bag));
        assertTrue(bag.size() == 1);
        bag.clear();

        assertTrue("variable substituted by another variable", x.tryGetMatchingSubstitutions(new Variable("y", sort), bag));
        assertTrue(bag.size() == 1);
        bag.clear();

        assertFalse("variable substituted by term of another sort", x.tryGetMatchingSubstitutions(new Constant("cte", new Sort("another sort")), bag));
        assertTrue(bag.size() == 0);
        bag.clear();

        OperationSignature signature = new OperationSignature("succ", true, sort, sort);
        Operation operation = signature.instantiates(new Constant("cte", sort));        
        assertTrue("variable substituted by operation", x.tryGetMatchingSubstitutions(operation, bag));
        assertTrue(bag.size() == 1);
        bag.clear();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCannotIsValidSubstitute() {
        SubstitutionBag bag = new SubstitutionBag();
        x.tryGetMatchingSubstitutions(new Variable("y", sort), bag);

        x.tryGetMatchingSubstitutions(new Constant("y", sort), null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCannotSubstitute() {
        x.substitutes(null);
    }

    @Test
    public void testSubstitues() {
        SubstitutionBag bag = new SubstitutionBag();

        bag.tryAddSubstitution(x, new Constant("abc", sort));
        assertEquals("A substitution for the right term is provided", x.substitutes(bag), new Constant("abc", sort));
        bag.clear();

        bag.tryAddSubstitution(new Variable("y", sort), new Constant("abc", sort));
        assertEquals("No substitution for the right term is provided", x.substitutes(bag), x);
        bag.clear();

        assertEquals("No substitution provided", x.substitutes(bag), x);
    }

    @Test
    public void testGetVariables() {
        ImmutableSet<Variable> expectedResult = ImmutableSet.of(x);
        assertEquals(x.getVariables(), expectedResult);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRenameVariablesNullBag() {
        x.renameVariables(null);
    }

    @Test
    public void testRenameVariablesUseExistingSubstitution() {
        SubstitutionBag bag = new SubstitutionBag();
        Term previousRenaming = x.renameVariables(bag);
        Term result = x.renameVariables(bag);
        assertEquals(previousRenaming, result);
    }

    @Test
    public void testRenameVariablesProduceNewSubstitution() {
        SubstitutionBag bag = new SubstitutionBag();
        Term result = x.renameVariables(bag);
        assertTrue(result instanceof Variable);
        assertTrue(((Variable) result).isGenerated());
        assertFalse("Generated and original variables have not the same name.", ((Variable) result).getName().equals(x.getName()));
        assertTrue("Generated and original variables have the same sort.", ((Variable) result).getSort().equals(x.getSort()));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateVariableWithNullName() {
        new Variable(null, sort);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateVariableWithEmptyName() {
        new Variable("", sort);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateVariableWithNullSort() {
        new Variable("x", null);
    }
}
