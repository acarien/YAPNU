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

        assertTrue("same objects", this.term.equals(termCloned));
        assertTrue("symmetry", termCloned.equals(this.term));
        assertFalse("difference on names", this.term.equals(term2));
        assertFalse("difference on sorts", this.term.equals(term3));
    }

    @Test
    public void testIsGenerator() {
        assertTrue(this.term.isGenerator());
    }

    @Test
    public void testIsNormalForm() {
        assertTrue(this.term.isNormalForm());
    }

    @Test
    public void testSizeIsOne() {
        assertEquals(this.term.size(), 1);
    }

    @Test
    public void testIsValidSubstitution() {
        assertTrue("'substitued' with the same term", this.term.tryGetMatchingSubstitutions(new Constant("term", sort), null));
        assertFalse("'substitued' with another term", this.term.tryGetMatchingSubstitutions(new Constant("anotherTerm", sort), null));
        assertFalse("'substitued' with nullterm", this.term.tryGetMatchingSubstitutions(null, null));
    }

    @Test
    public void testSubstitutes() {
        assertEquals("'substitution' with no substitutions", this.term.substitutes(null), this.term);
        assertEquals("'substitution' works with any substitution", this.term.substitutes(new SubstitutionBag()), this.term);
    }
}
