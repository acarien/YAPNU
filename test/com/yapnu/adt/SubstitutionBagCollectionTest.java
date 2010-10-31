/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yapnu.adt;

import com.yapnu.adt.model.IntegerAdt;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adrien
 */
public class SubstitutionBagCollectionTest {

    private final Adt intAdt = IntegerAdt.instance().getAdt();
    private final Constant one = new Constant("1", intAdt.getSort());
    private final Variable variableX = intAdt.getVariable("x");
    private final Variable variableY = intAdt.getVariable("y");
    private final Constant zero = intAdt.getConstant("0");
    private ArrayList<Set<SubstitutionBag>> bags;
    private HashSet<SubstitutionBag> expectedResult;


    @Before
    public void setUp() {
        bags = new ArrayList<Set<SubstitutionBag>>();
        expectedResult = new HashSet<SubstitutionBag>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDistributeNullList() {
        SubstitutionBagCollection.Distribute(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDistributeNullSet() {
        SubstitutionBagCollection.Distribute(bags, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDistributeSomeBagsAreNull() {
        bags.add(new HashSet<SubstitutionBag>());
        bags.add(null);
        SubstitutionBagCollection.Distribute(bags, new HashSet<SubstitutionBag>());
    }

    @Test
    public void testDistributeEmptyList() {
        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralEmptyBags() {
        Set<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(new SubstitutionBag());
        tmp.add(new SubstitutionBag());

        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp);
        bags.add(new HashSet<SubstitutionBag>());

        expectedResult.add(new SubstitutionBag());
        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithOneBag() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(variableX, zero);

        HashSet<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(content);
        bags.add(tmp);

        expectedResult.add(content);

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithOneBagWithContentOtherBagsAreEmpty() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(variableX, zero);

        HashSet<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(content);
        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp);
        bags.add(new HashSet<SubstitutionBag>());

        expectedResult.add(content);

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralBagsWithSameContent() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(variableX, zero);

        HashSet<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(content);
        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp);
        bags.add(tmp);

        expectedResult.add(content);

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralBagsWithDifferentContents() {
        SubstitutionBag content1 = new SubstitutionBag();
        content1.tryAddSubstitution(variableX, zero);

        HashSet<SubstitutionBag> tmp1 = new HashSet<SubstitutionBag>();
        tmp1.add(content1);

        SubstitutionBag content2 = new SubstitutionBag();
        content2.tryAddSubstitution(variableY, zero);

        HashSet<SubstitutionBag> tmp2 = new HashSet<SubstitutionBag>();
        tmp2.add(content2);

        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp1);
        bags.add(tmp2);

        SubstitutionBag res = new SubstitutionBag();
        expectedResult.add(res);
        res.tryAddSubstitutions(content1);
        res.tryAddSubstitutions(content2);

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralSetsWithDifferentMultiplicity() {
        SubstitutionBag content1a = new SubstitutionBag();
        content1a.tryAddSubstitution(variableX, zero);

        HashSet<SubstitutionBag> content = new HashSet<SubstitutionBag>();
        content.add(content1a);

        SubstitutionBag content2a = new SubstitutionBag();
        content2a.tryAddSubstitution(variableY, zero);

        SubstitutionBag content2b = new SubstitutionBag();
        content2b.tryAddSubstitution(variableY, one);

        HashSet<SubstitutionBag> content2 = new HashSet<SubstitutionBag>();
        content2.add(content2a);
        content2.add(content2b);

        bags.add(new HashSet<SubstitutionBag>());
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

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithSeveralSetsWithDifferentMultiplicity2() {
        SubstitutionBag content1a = new SubstitutionBag();
        content1a.tryAddSubstitution(variableX, zero);
        SubstitutionBag content1b = new SubstitutionBag();
        content1b.tryAddSubstitution(variableX, one);

        HashSet<SubstitutionBag> content1 = new HashSet<SubstitutionBag>();
        content1.add(content1a);
        content1.add(content1b);

        SubstitutionBag content2a = new SubstitutionBag();
        content2a.tryAddSubstitution(variableY, zero);
        SubstitutionBag content2b = new SubstitutionBag();
        content2b.tryAddSubstitution(variableY, one);

        HashSet<SubstitutionBag> content2 = new HashSet<SubstitutionBag>();
        content2.add(content2a);
        content2.add(content2b);

        Set<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(new SubstitutionBag());
        tmp.add(new SubstitutionBag());
        tmp.add(new SubstitutionBag());
        
        bags.add(new HashSet<SubstitutionBag>());
        bags.add(new HashSet<SubstitutionBag>());
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

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeListWithExclusiveSets() {
        SubstitutionBag content1 = new SubstitutionBag();
        content1.tryAddSubstitution(variableX, zero);

        HashSet<SubstitutionBag> tmp1 = new HashSet<SubstitutionBag>();
        tmp1.add(content1);

        SubstitutionBag content2 = new SubstitutionBag();
        content2.tryAddSubstitution(variableX, one);

        HashSet<SubstitutionBag> tmp2 = new HashSet<SubstitutionBag>();
        tmp2.add(content2);

        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp1);
        bags.add(tmp2);
        
        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertFalse(SubstitutionBagCollection.Distribute(bags, result));        
    }

    @Test
    public void testDistributeListWithExclustiveBags() {
        SubstitutionBag content1a = new SubstitutionBag();
        content1a.tryAddSubstitution(variableX, zero);

        SubstitutionBag content1b = new SubstitutionBag();
        content1b.tryAddSubstitution(variableX, one);

        HashSet<SubstitutionBag> tmp1 = new HashSet<SubstitutionBag>();
        tmp1.add(content1a);
        tmp1.add(content1b);

        SubstitutionBag content2a = new SubstitutionBag();
        content2a.tryAddSubstitution(variableX, one);

        SubstitutionBag content2b = new SubstitutionBag();
        content2b.tryAddSubstitution(variableY, zero);

        HashSet<SubstitutionBag> tmp2 = new HashSet<SubstitutionBag>();
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

        Set<SubstitutionBag> result = new HashSet<SubstitutionBag>();
        assertTrue(SubstitutionBagCollection.Distribute(bags, result));
        assertEquals(result, expectedResult);
    }
}
