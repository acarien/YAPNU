/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import static junit.framework.Assert.*;
import org.junit.Test;



//import org.junit.Test;

/**
 *
 * @author adrien
 */

public class ConstantTest {
    
    private final Sort sort = new Sort("sort");
    private Constant term = new Constant("term", sort);

    @Test
    public void testIsValueObject() {        
        Constant termCloned = new Constant("term", this.sort);
        Constant term2 = new Constant("term2", this.sort);
        Constant term3 = new Constant("term", new Sort("anotherSort"));
        Constant term4 = new Constant("term", this.sort, false);

        assertTrue("same objects", this.term.equals(termCloned));
        assertTrue("symmetry", termCloned.equals(this.term));
        assertFalse("difference on names", this.term.equals(term2));        
        assertFalse("difference on sorts", this.term.equals(term3));
        assertTrue("same event with difference in isGenerator", this.term.equals(term4));
    }

    @Test
    public void testIsNormalForm() {
        assertTrue(term.isNormalForm());
        assertFalse(new Constant("anotherConstant", sort, false).isNormalForm());
    }

    @Test
    public void testSizeIsOne() {
        assertEquals(this.term.size(), 1);
    }

    @Test
    public void testIsValidSubstitution() {
        SubstitutionBag bag = new SubstitutionBag();
        assertTrue("a constant can be substittued by himeself", this.term.tryGetMatchingSubstitutions(new Constant("term", sort), bag));
        assertTrue("a constant can be substittued by himeself - it does not create a substitution", bag.size() == 0);
        bag.clear();
        assertFalse("a constant cannot be substitued with another term", this.term.tryGetMatchingSubstitutions(new Constant("anotherTerm", sort), bag));
        assertFalse("a constant cannot be substitued with a nullterm", this.term.tryGetMatchingSubstitutions(null, bag));
        assertTrue("a constant can be substitued with a variable", this.term.tryGetMatchingSubstitutions(new Variable("x", sort), bag));
    }

    @Test
    public void testSubstitutes() {
        assertEquals("'substitution' with no substitutions", this.term.substitutes(null), this.term);
        assertEquals("'substitution' works with any substitution", this.term.substitutes(new SubstitutionBag()), this.term);
    }

    @Test
    public void testGetVariables()  {
      assertTrue(this.term.getVariables().isEmpty());
    }

    @Test
    public void testRenameVariables()  {
      SubstitutionBag bag = new SubstitutionBag();
      assertEquals(this.term.renameVariables(null), term);
      assertEquals(this.term.renameVariables(bag), term);
    }
}
