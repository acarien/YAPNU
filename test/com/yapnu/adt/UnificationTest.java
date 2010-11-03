/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.yapnu.adt.model.IntegerAdt;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class UnificationTest {

    private final Adt intAdt = IntegerAdt.instance().getAdt();
    private final Constant one = new Constant("1", intAdt.getSort());
    private final Variable variableX = intAdt.getVariable("x");
    private final Variable variableY = intAdt.getVariable("y");
    private final Constant zero = intAdt.getConstant("0");
    private ArrayList<Unification> bags;
    private Unification expectedResult;


    @Before
    public void setUp() {
        bags = new ArrayList<Unification>();
        expectedResult = new Unification();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDistributeNullList() {
        Unification.Distribute(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDistributeSomeBagsAreNull() {
        bags.add(new Unification());
        bags.add(null);
        Unification.Distribute(bags);
    }

    @Test
    public void testDistributeEmptyList() {        
        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralEmptyBags() {
        Unification tmp = new Unification();
        tmp.add(new SubstitutionBag());
        tmp.add(new SubstitutionBag());

        bags.add(new Unification());
        bags.add(tmp);
        bags.add(new Unification());

        expectedResult.add(new SubstitutionBag());        
        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithOneBag() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(variableX, zero);

        Unification tmp = new Unification();
        tmp.add(content);
        bags.add(tmp);

        expectedResult.add(content);
        
        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithOneBagWithContentOtherBagsAreEmpty() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(variableX, zero);

        Unification tmp = new Unification();
        tmp.add(content);
        bags.add(new Unification());
        bags.add(tmp);
        bags.add(new Unification());

        expectedResult.add(content);
        
        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralBagsWithSameContent() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(variableX, zero);

        Unification tmp = new Unification();
        tmp.add(content);
        bags.add(new Unification());
        bags.add(tmp);
        bags.add(tmp);

        expectedResult.add(content);
        
        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralBagsWithDifferentContents() {
        SubstitutionBag content1 = new SubstitutionBag();
        content1.tryAddSubstitution(variableX, zero);

        Unification tmp1 = new Unification();
        tmp1.add(content1);

        SubstitutionBag content2 = new SubstitutionBag();
        content2.tryAddSubstitution(variableY, zero);

        Unification tmp2 = new Unification();
        tmp2.add(content2);

        bags.add(new Unification());
        bags.add(tmp1);
        bags.add(tmp2);

        SubstitutionBag res = new SubstitutionBag();
        expectedResult.add(res);
        res.tryAddSubstitutions(content1);
        res.tryAddSubstitutions(content2);
        
        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralSetsWithDifferentMultiplicity() {
        SubstitutionBag content1a = new SubstitutionBag();
        content1a.tryAddSubstitution(variableX, zero);

        Unification content = new Unification();
        content.add(content1a);

        SubstitutionBag content2a = new SubstitutionBag();
        content2a.tryAddSubstitution(variableY, zero);

        SubstitutionBag content2b = new SubstitutionBag();
        content2b.tryAddSubstitution(variableY, one);

        Unification content2 = new Unification();
        content2.add(content2a);
        content2.add(content2b);

        bags.add(new Unification());
        bags.add(content);
        bags.add(content2);

        SubstitutionBag res1 = new SubstitutionBag();
        expectedResult.add(res1);
        res1.tryAddSubstitutions(content1a);
        res1.tryAddSubstitution(variableY, zero);

        SubstitutionBag res2 = new SubstitutionBag();
        expectedResult.add(res2);
        res2.tryAddSubstitutions(content1a);
        res2.tryAddSubstitution(variableY, one);
        
        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralSetsWithDifferentMultiplicity2() {
        SubstitutionBag content1a = new SubstitutionBag();
        content1a.tryAddSubstitution(variableX, zero);
        SubstitutionBag content1b = new SubstitutionBag();
        content1b.tryAddSubstitution(variableX, one);

        Unification content1 = new Unification();
        content1.add(content1a);
        content1.add(content1b);

        SubstitutionBag content2a = new SubstitutionBag();
        content2a.tryAddSubstitution(variableY, zero);
        SubstitutionBag content2b = new SubstitutionBag();
        content2b.tryAddSubstitution(variableY, one);

        Unification content2 = new Unification();
        content2.add(content2a);
        content2.add(content2b);

        Unification tmp = new Unification();
        tmp.add(new SubstitutionBag());
        tmp.add(new SubstitutionBag());
        tmp.add(new SubstitutionBag());
        
        bags.add(new Unification());
        bags.add(new Unification());
        bags.add(content1);
        bags.add(tmp);
        bags.add(content2);

        SubstitutionBag res1 = new SubstitutionBag();
        expectedResult.add(res1);
        res1.tryAddSubstitution(variableX, zero);
        res1.tryAddSubstitution(variableY, zero);

        SubstitutionBag res2 = new SubstitutionBag();
        expectedResult.add(res2);
        res2.tryAddSubstitution(variableX, zero);
        res2.tryAddSubstitution(variableY, one);

        SubstitutionBag res3 = new SubstitutionBag();
        expectedResult.add(res3);
        res3.tryAddSubstitution(variableX, one);
        res3.tryAddSubstitution(variableY, zero);

        SubstitutionBag res4 = new SubstitutionBag();
        expectedResult.add(res4);
        res4.tryAddSubstitution(variableX, one);
        res4.tryAddSubstitution(variableY, one);

        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithExclusiveSets() {
        SubstitutionBag content1 = new SubstitutionBag();
        content1.tryAddSubstitution(variableX, zero);

        Unification tmp1 = new Unification();
        tmp1.add(content1);

        SubstitutionBag content2 = new SubstitutionBag();
        content2.tryAddSubstitution(variableX, one);

        Unification tmp2 = new Unification();
        tmp2.add(content2);

        bags.add(new Unification());
        bags.add(tmp1);
        bags.add(tmp2);
        
        Unification result = Unification.Distribute(bags);
        assertFalse(result.isSuccess());
    }

    @Test
    public void testDistributeListWithExclustiveBags() {
        SubstitutionBag content1a = new SubstitutionBag();
        content1a.tryAddSubstitution(variableX, zero);

        SubstitutionBag content1b = new SubstitutionBag();
        content1b.tryAddSubstitution(variableX, one);

        Unification tmp1 = new Unification();
        tmp1.add(content1a);
        tmp1.add(content1b);

        SubstitutionBag content2a = new SubstitutionBag();
        content2a.tryAddSubstitution(variableX, one);

        SubstitutionBag content2b = new SubstitutionBag();
        content2b.tryAddSubstitution(variableY, zero);

        Unification tmp2 = new Unification();
        tmp2.add(content2a);
        tmp2.add(content2b);
        
        bags.add(tmp1);
        bags.add(tmp2);

        SubstitutionBag res1 = new SubstitutionBag();
        expectedResult.add(res1);
        res1.tryAddSubstitution(variableX, zero);
        res1.tryAddSubstitution(variableY, zero);

        SubstitutionBag res2 = new SubstitutionBag();
        expectedResult.add(res2);
        res2.tryAddSubstitution(variableX, one);

        SubstitutionBag res3 = new SubstitutionBag();
        expectedResult.add(res3);
        res3.tryAddSubstitution(variableX, one);
        res3.tryAddSubstitution(variableY, zero);

        Unification result = Unification.Distribute(bags);
        assertTrue(result.isSuccess());
        assertEquals(result, expectedResult);
    }
}
