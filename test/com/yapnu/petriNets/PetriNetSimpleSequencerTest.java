/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yapnu.petriNets;

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