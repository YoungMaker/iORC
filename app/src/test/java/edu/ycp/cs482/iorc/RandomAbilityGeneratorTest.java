package edu.ycp.cs482.iorc;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs482.iorc.dummy.RandAbilityGenerator;

import static org.junit.Assert.assertTrue;

/**
 * Created by Hunter on 3/3/2018.
 */

public class RandomAbilityGeneratorTest {
    //create test objects
    RandAbilityGenerator abilGen;
    long str, con, dex, _int, wis, cha;
    //the minimum and maximum values a valid score can be
    int minVal = 1;
    int maxVal = 20;
    //change number of test run
    int numTests = 10000;

    @Before
    public void setupRNG(){
        //create ability score generator
        abilGen = new RandAbilityGenerator();
    }

    @Test
    public void generatedAbilTest() throws Exception {
        //run test multiple times
        for(int i = 0; i <= numTests; i++) {

            //generate new ability scores
            abilGen.generateAbilitiesScores();

            //generate ability scores
            str = abilGen.getStr();
            con = abilGen.getCon();
            dex = abilGen.getDex();
            _int = abilGen.get_int();
            wis = abilGen.getWis();
            cha = abilGen.getCha();

            //observation if wanted
            System.out.println(str);
            System.out.println(con);
            System.out.println(dex);
            System.out.println(_int);
            System.out.println(wis);
            System.out.println(cha);

            //make sure the ability scores generated are not too high or too low
            assertTrue(minVal <= str && str <= maxVal);
            assertTrue(minVal <= con && con <= maxVal);
            assertTrue(minVal <= dex && dex <= maxVal);
            assertTrue(minVal <= _int && _int <= maxVal);
            assertTrue(minVal <= wis && wis <= maxVal);
            assertTrue(minVal <= cha && cha <= maxVal);
        }
    }
}
