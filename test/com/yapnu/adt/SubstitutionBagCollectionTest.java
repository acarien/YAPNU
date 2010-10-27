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

    private Adt intAdt = IntegerAdt.instance().getAdt();
    private ArrayList<Set<SubstitutionBag>> bags;
    private HashSet<SubstitutionBag> expectedResult;

    @Before
    public void setUp() {
        bags = new ArrayList<Set<SubstitutionBag>>();
        expectedResult = new HashSet<SubstitutionBag>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDistributeNullList() {
        SubstitutionBagCollection.Distribute(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDistributeSomeBagsAreNull() {
        bags.add(new HashSet<SubstitutionBag>());
        bags.add(null);
        SubstitutionBagCollection.Distribute(bags);
    }

    @Test
    public void testDistributeEmptyList() {
        Set<SubstitutionBag> result = SubstitutionBagCollection.Distribute(bags);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeEmptyBag() {
        Set<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(new SubstitutionBag());
        tmp.add(new SubstitutionBag());

        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp);
        bags.add(new HashSet<SubstitutionBag>());

        expectedResult.add(new SubstitutionBag());
        Set<SubstitutionBag> result = SubstitutionBagCollection.Distribute(bags);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeOneBag() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(intAdt.getVariable("x"), intAdt.getConstant("0"));

        HashSet<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(content);
        bags.add(tmp);

        expectedResult.add(content);

        Set<SubstitutionBag> result = SubstitutionBagCollection.Distribute(bags);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeOneBagWithContentOtherEmpty() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(intAdt.getVariable("x"), intAdt.getConstant("0"));

        HashSet<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(content);
        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp);
        bags.add(new HashSet<SubstitutionBag>());

        expectedResult.add(content);

        Set<SubstitutionBag> result = SubstitutionBagCollection.Distribute(bags);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeSeveralBagsWithSameContent() {
        SubstitutionBag content = new SubstitutionBag();
        content.tryAddSubstitution(intAdt.getVariable("x"), intAdt.getConstant("0"));

        HashSet<SubstitutionBag> tmp = new HashSet<SubstitutionBag>();
        tmp.add(content);
        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp);
        bags.add(tmp);

        expectedResult.add(content);

        Set<SubstitutionBag> result = SubstitutionBagCollection.Distribute(bags);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDistributeSeveralBags() {
        SubstitutionBag content1 = new SubstitutionBag();
        content1.tryAddSubstitution(intAdt.getVariable("x"), intAdt.getConstant("0"));

        HashSet<SubstitutionBag> tmp1 = new HashSet<SubstitutionBag>();
        tmp1.add(content1);

        SubstitutionBag content2 = new SubstitutionBag();
        content2.tryAddSubstitution(intAdt.getVariable("y"), intAdt.getConstant("0"));

        HashSet<SubstitutionBag> tmp2 = new HashSet<SubstitutionBag>();
        tmp2.add(content2);

        bags.add(new HashSet<SubstitutionBag>());
        bags.add(tmp1);
        bags.add(tmp2);

        SubstitutionBag res = new SubstitutionBag();
        expectedResult.add(res);
        res.tryAddSubstitutions(content1);
        res.tryAddSubstitutions(content2);

        Set<SubstitutionBag> result = SubstitutionBagCollection.Distribute(bags);
        assertEquals(result, expectedResult);
    }

    @Test
    public void awd() {
        Constant one = new Constant("1", intAdt.getSort());
        SubstitutionBag content1a = new SubstitutionBag();
        content1a.tryAddSubstitution(intAdt.getVariable("x"), intAdt.getConstant("0"));

        HashSet<SubstitutionBag> content = new HashSet<SubstitutionBag>();
        content.add(content1a);

        SubstitutionBag content2a = new SubstitutionBag();
        content2a.tryAddSubstitution(intAdt.getVariable("y"), intAdt.getConstant("0"));

        SubstitutionBag content2b = new SubstitutionBag();
        content2b.tryAddSubstitution(intAdt.getVariable("y"), one);

        HashSet<SubstitutionBag> content2 = new HashSet<SubstitutionBag>();
        content2.add(content2a);
        content2.add(content2b);

        bags.add(new HashSet<SubstitutionBag>());
        bags.add(content);
        bags.add(content2);

        SubstitutionBag res1 = new SubstitutionBag();
        expectedResult.add(res1);
        res1.tryAddSubstitutions(content1a);
        res1.tryAddSubstitution(intAdt.getVariable("y"), intAdt.getConstant("0"));

        SubstitutionBag res2 = new SubstitutionBag();
        expectedResult.add(res2);
        res2.tryAddSubstitutions(content1a);
        res2.tryAddSubstitution(intAdt.getVariable("y"), one);

        Set<SubstitutionBag> result = SubstitutionBagCollection.Distribute(bags);
        assertEquals(result, expectedResult);
    }

    @Test
    public void awd2() {
        Constant one = new Constant("1", intAdt.getSort());
        SubstitutionBag content1a = new SubstitutionBag();
        content1a.tryAddSubstitution(intAdt.getVariable("x"), intAdt.getConstant("0"));
        SubstitutionBag content1b = new SubstitutionBag();
        content1b.tryAddSubstitution(intAdt.getVariable("x"), one);

        HashSet<SubstitutionBag> content1 = new HashSet<SubstitutionBag>();
        content1.add(content1a);
        content1.add(content1b);

        SubstitutionBag content2a = new SubstitutionBag();
        content2a.tryAddSubstitution(intAdt.getVariable("y"), intAdt.getConstant("0"));
        SubstitutionBag content2b = new SubstitutionBag();
        content2b.tryAddSubstitution(intAdt.getVariable("y"), one);

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
        res1.tryAddSubstitution(intAdt.getVariable("x"), intAdt.getConstant("0"));
        res1.tryAddSubstitution(intAdt.getVariable("y"), intAdt.getConstant("0"));

        SubstitutionBag res2 = new SubstitutionBag();
        expectedResult.add(res2);
        res2.tryAddSubstitution(intAdt.getVariable("x"), intAdt.getConstant("0"));
        res2.tryAddSubstitution(intAdt.getVariable("y"), one);

        SubstitutionBag res3 = new SubstitutionBag();
        expectedResult.add(res3);
        res3.tryAddSubstitution(intAdt.getVariable("x"), one);
        res3.tryAddSubstitution(intAdt.getVariable("y"), intAdt.getConstant("0"));

        SubstitutionBag res4 = new SubstitutionBag();
        expectedResult.add(res4);
        res4.tryAddSubstitution(intAdt.getVariable("x"), one);
        res4.tryAddSubstitution(intAdt.getVariable("y"), one);

        Set<SubstitutionBag> result = SubstitutionBagCollection.Distribute(bags);
        assertEquals(result, expectedResult);
    }
}
