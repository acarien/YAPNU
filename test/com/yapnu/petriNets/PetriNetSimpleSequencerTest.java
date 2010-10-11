/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.petriNets;

import com.yapnu.petriNet.PetriNetSequencer;
import com.yapnu.petriNet.PetriNet;
import com.yapnu.petriNet.PetriNetSimpleSequencer;
import java.util.Random;



/**
 *
 * @author adrien
 */

public class PetriNetSimpleSequencerTest extends AbstractTestPetriNetSequencer {

    @Override
    protected PetriNetSequencer GetPetriNetSequencer(Random random, PetriNet petriNet) {
        return new PetriNetSimpleSequencer(random, petriNet);
    }

}
