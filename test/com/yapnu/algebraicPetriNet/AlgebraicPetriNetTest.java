/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.algebraicPetriNet;

import com.yapnu.adt.Constant;
import com.yapnu.adt.model.BlackTokenAdt;
import org.junit.Test;

/**
 *
 * @author adrien
 */
public class AlgebraicPetriNetTest {

  
    @Test
    public void testSomeMethod() {
        Constant token = BlackTokenAdt.instance().getAdt().getConstant("token");
                
        TransitionMatrixBuilder inputBuilder = new TransitionMatrixBuilder(2, 1);
        inputBuilder.Add(0, 0, token, 2);
        inputBuilder.Add(1, 0, token, 1);

        TransitionMatrixBuilder outputBuilder = new TransitionMatrixBuilder(2, 1);
        inputBuilder.Add(0, 0, token, 3);
        inputBuilder.Add(1, 0, token, 2);

        InitialMarkingBuilder builder = new InitialMarkingBuilder(2);
        builder.Add(0, token, 4);
        builder.Add(1, token, 4);

        AlgebraicPetriNet algebraicPetriNet = new AlgebraicPetriNet(builder, inputBuilder.build(), outputBuilder.build());
        algebraicPetriNet.fireTransition(0);
    }

}