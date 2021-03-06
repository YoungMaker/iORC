package edu.ycp.cs482.iorc;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs482.iorc.Apollo.RandAbilityGenerator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
    public void rollParamParserTest(){
        //TODO expand
        int[] test1 = abilGen.rollParamParser("1d20");
        assertEquals(1, test1[0]);
        assertEquals(20, test1[1]);
        int[] test2 = abilGen.rollParamParser("50d2");
        assertEquals(50, test2[0]);
        assertEquals(2, test2[1]);
    }

    @Test
    public void determineMinValTest(){
        int test1 = abilGen.determineMax(4);
        assertEquals(4, test1);
        int test2 = abilGen.determineMax(20);
        assertEquals(20, test2);
    }

    @Test
    public void determineMaxValTest(){
        int test1 = abilGen.determineMin(4);
        assertEquals(4, test1);
        int test2 = abilGen.determineMin(20);
        assertEquals(20, test2);
    }

    @Test
    public void generatedAbilTest() throws Exception {
        int testNums = 100;
        for(int i = 0; i < testNums; i++){
            int test1 = abilGen.generateRoll("1d4");
            System.out.println("TEST" + test1);
            assertTrue(1 <= test1 && test1 <= 4);
            int test2 = abilGen.generateRoll("5d12");
            System.out.println("TEST" + test2);
            assertTrue(5 <= test2 && test2 <= 60);
        }

    }
}
