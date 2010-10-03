/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.acarien.petriNets;

import org.junit.Test;

/**
 *
 * @author adrien
 */
public class AlgebraicPetriNetSequencerTest {
    @Test()
    public void TestSequence() {

       /* HashMap<String, Sort> sorts = new HashMap<String, Sort>();
        
        Sort boolSort = new Sort("bool", new BoolAlgebra()); //definit ce qu'est un sort et associe une algebre avec
        sorts.put(boolSort.getName(), boolSort);
        
        GenerativeTerm tTerm = new GenerativeTerm("true", boolSort);
        GenerativeTerm fTerm = new GenerativeTerm("false", boolSort);
        
        boolSort.AddTerm(tTerm);
        boolSort.AddTerm(fTerm);

        //le codomain est implicite
        Variable vX = new Variable("x", boolSort);
        Variable vY = new Variable("y", sorts.get("bool"));
        boolSort.AddTerm(vX);
        boolSort.AddTerm(vY);

        ArrayList<Sort> andDomain = new ArrayList<Sort>();
        andDomain.add(boolSort);
        andDomain.add(boolSort);
        OperationSignatureTerm andTerm = new OperationSignatureTerm("and", andDomain, boolSort);
        boolSort.AddTerm(andTerm);        

        Term[] terms = {(Term) vX, (Term) fTerm};

        Term instantiatedTerm1 = andTerm.substitues(terms);
        Term[] parameters = {(Term) tTerm, (Term) andTerm.substitues(terms)};
        Term instantiatedTerm2 = andTerm.substitues(parameters);

        boolean algebraValue = instantiatedTerm1.Evaluate();

        ArrayList<ClosedTerm> marking;
        ArrayList<Term>[][] wPlus;


        Algebra boolAlgebra = new BoolAlgebra();*/

        //Term term = new Term(sorts.getSort("bool"), Term.Generator,)

       /* String[][] sorts  = {{"bool"}};
        Sort sort = new Sort("bool");
        GenerativeTerms terms = new GenerativeTemrs("false", "true");
        


        int[][] input = {{1}, {0}};
        int[][] output = {{0}, {1}};
        int[] marking = {2, 0};

       PetriNet petriNet = new PetriNet(input, output, marking);       
       sequencer.deploy();
       LinkedList<Integer> firedTransitions = sequencer.getFiredTransitions();

       LinkedList<Integer> reference = new LinkedList<Integer>();
       reference.addLast(-1);
       reference.addLast(0);
       reference.addLast(0);
       Assert.assertEquals(firedTransitions, reference);

       int[] finalMarking = petriNet.getMarking();
       int[] referenceMarking = {0, 2};
       Assert.assertArrayEquals(finalMarking, referenceMarking);*/
    }
}
