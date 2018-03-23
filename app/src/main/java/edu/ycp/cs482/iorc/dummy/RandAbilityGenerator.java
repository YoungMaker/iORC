package edu.ycp.cs482.iorc.dummy;

import android.content.Intent;
import android.util.Log;

import java.util.Random;

/**
 * Created by Hunter on 3/3/2018.
 */

public class RandAbilityGenerator {
    //TODO change to allow for a dynamic range of ability scores names

    //ability score types
    private long str, con, dex, _int, wis, cha;
    //create and seed RNG
    private Random rand;
    //large value to initialize the lowest value for a single die roll
    private int maxDieRollVal = 9999999;
    //regex for defining what the parameters for a dice roll are
    private String paramRegex = "[0-9]*d[0-9]*";
    //splitter regex for dice roll
    //aka what to look for when splitting the number of dice and range of each die
    private String splitRegex = "d";

    public RandAbilityGenerator(){
        rand = new Random(System.currentTimeMillis());
    }

    //role param should be something like 1d6 or 3d4
    //the first number being the amount of dice rolled and
    //the second number being the range of the dice (ex. 2d4 would be 2 4 sided dice)
    public int generateRoll(String rollParam){
        int[] params = rollParamParser(rollParam);
        int total = 0;
        int min = 1;
        int max = determineMax(params[1]);
        total = standardRoll(min, max, params[0]);
        return total;
    }

    //special is for things like drop lowest or reroll lowest
    //if the special does not match anything then the standard roll is performed
    public int generateRoll(String rollParam, String special){
        int[] params = rollParamParser(rollParam);
        int total = 0;
        int max = determineMax(params[1]);
        int min = 1;

        switch (special){
            case "drop_lowest":;
            case "reroll_lowest":;
            default: total = standardRoll(min, max, params[0]);
        }

        return total;
    }

    public int[] rollParamParser(String rollParam){
        //first parameter will be the number of dice rolled
        //second will be the range of the dice
        int [] parameters = new int[2];

        if(rollParam.matches(paramRegex)){
            //split the string at the d point
            String[] splitHolder = rollParam.split(splitRegex);

            //convert the split values into integers and add to the parameters array
            for(int i = 0; i < parameters.length; i++){
                parameters[i] = Integer.parseInt(splitHolder[i]);
            }
        } else {
            Log.e("INVALID_PARAM_SYNTAX", "rollParameter" + rollParam + "is not in a valid format, expected match to " + paramRegex);
            return null;
        }

        return parameters;
    }

    //max value of a given roll
    public int determineMax(int maxDieVal){
        int maxVal = 0;
        maxVal = maxDieVal;
        return maxVal;
    }

    //min value of given roll
    public int determineMin(int numDice){
        int minVal = numDice;
        return minVal;
    }

    //roll with no special cases
    public int standardRoll(int min, int max, int numRolls){
        int total = 0;
        for(int i = 0; i < numRolls; i++){
            total += rand.nextInt(max-min+1) + min;
        }

        return total;
    }

    //drop lowest roll
    public int dropLowest(int min, int max, int numRolls){
        int total = 0;
        int lowestVal = maxDieRollVal, lowestInd = 0, rollVal = 0;
        int[] rolls = new int[numRolls];

        //roll the dice
        for(int i = 0; i < numRolls; i++){
            rollVal = rand.nextInt(max-min+1) + min;

            if(rollVal < lowestVal){
                lowestVal = rollVal;
                lowestInd = i;
            }
            rolls[i] = rollVal;
        }

        //total all of the dice rolled except the lowest
        for(int i = 0; i < numRolls; i++){
            if(i != lowestInd){
                total += rolls[i];
            }
        }

        return total;
    }

    //reroll lowest roll
    public int rerollLowest(int min, int max, int numRolls){
        int total = 0;
        int lowestVal = maxDieRollVal, lowestInd = 0, rollVal = 0;
        int[] rolls = new int[numRolls];

        //roll the dice
        for(int i = 0; i < numRolls; i++){
            rollVal = rand.nextInt(max-min+1) + min;

            if(rollVal < lowestVal){
                lowestVal = rollVal;
                lowestInd = i;
            }
            rolls[i] = rollVal;
        }

        //reroll lowest die value
        rollVal = rand.nextInt(max-min+1) + min;
        rolls[lowestInd] = rollVal;

        //total all of the dice rolled
        for(int i = 0; i < numRolls; i++){
            total += rolls[i];
        }

        return total;
    }
}
