/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.petriNets;

import com.yapnu.petriNet.PetriNetSequencer;
import com.yapnu.petriNet.PetriNet;
import com.yapnu.petriNet.PetriNetFastSequencer;
import java.util.Random;

/**
 *
 * @author adrien
 */
public class PetriNetFastSequencerTest extends AbstractTestPetriNetSequencer {

    @Override
    protected PetriNetSequencer GetPetriNetSequencer(Random random, PetriNet petriNet) {
        return new PetriNetFastSequencer(random, petriNet);
    }

    
}