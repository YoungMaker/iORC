package edu.ycp.cs482.iorc.dummy;

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
    private Random rand = new Random(System.currentTimeMillis());

    public void generateAbilitiesScores(){
        str = generateAbilityScore();
        con = generateAbilityScore();
        dex = generateAbilityScore();
        _int = generateAbilityScore();
        wis = generateAbilityScore();
        cha = generateAbilityScore();
    }

    public int generateAbilityScore(){
        return rand.nextInt(max-min+1) + min;
    }

    public long getStr(){
        return  str;
    }

    public long getCon() {
        return con;
    }

    public long getDex() {
        return dex;
    }

    public long get_int() {
        return _int;
    }

    public long getWis() {
        return wis;
    }

    public long getCha() {
        return cha;
    }
}
