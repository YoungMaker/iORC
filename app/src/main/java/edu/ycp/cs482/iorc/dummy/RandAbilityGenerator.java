package edu.ycp.cs482.iorc.dummy;

import android.content.Intent;
import android.util.Log;

import java.util.Random;

/**
 * Created by Hunter on 3/3/2018.
 */

public class RandAbilityGenerator {
    //TODO change to allow for a dynamic range of ability scores names

    //minimum and maximum ability scores
    private int max = 20;
    private int min = 1;
    //ability score types
    private long str, con, dex, _int, wis, cha;
    //create and seed RNG
    private Random rand;
    //regex for defining what the parameters for a dice roll are
    private String paramRegex = "[0-9]*d[0-9]*";
    //splitter regex for dice roll
    //aka what to look for when splitting the number of dice and range of each die
    private String splitRegex = "d";

    public void generateAbilitiesScores(){
        rand = new Random(System.currentTimeMillis());
    }

    //role param should be something like 1d6 or 3d4
    //the first number being the amount of dice rolled and
    //the second number being the range of the dice (ex. 2d4 would be 2 4 sided dice)
    public int generateAbilityScore(String rollParam){

        return rand.nextInt(max-min+1) + min;
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
}
