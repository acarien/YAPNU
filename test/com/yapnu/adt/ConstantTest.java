/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.adt;

import junit.framework.Assert;
import org.junit.Test;


//import org.junit.Test;

/**
 *
 * @author adrien
 */

public class ConstantTest {
    
    private final Sort sort = new Sort("sort");

    @Test
    public void TestIsValueObject() {
        Constant term1 = new Constant("term", sort);
        Constant term1Cloned = new Constant("term", sort);
        Constant term2 = new Constant("term2", sort);
        Constant term3 = new Constant("term", new Sort("anotherSort"));

        Assert.assertTrue("same objects", term1.equals(term1Cloned));
        Assert.assertTrue("symmetry", term1Cloned.equals(term1));
        Assert.assertFalse("difference on names", term1.equals(term2));
        Assert.assertFalse("difference on sorts", term1.equals(term3));
    }
}
