package edu.ycp.cs482.iorc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Random;

public class DiceWidgetActivity extends AppCompatActivity {

    ImageButton androidImageButton_d20;
    ImageButton androidImageButton_d12;
    ImageButton androidImageButton_d10;
    ImageButton androidImageButton_d8;
    ImageButton androidImageButton_d6;
    ImageButton androidImageButton_d4;

    Random rand = new Random();

    int die20 = rand.nextInt(20) + 1;
    int die12 = rand.nextInt(12) + 1;
    int die10 = rand.nextInt(10) + 1;
    int die8 = rand.nextInt(8) + 1;
    int die6 = rand.nextInt(6) + 1;
    int die4 = rand.nextInt(4) + 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_widget);



        /*Rolling a d20*/
        androidImageButton_d20 = (ImageButton) findViewById(R.id.image_button_d20);
        androidImageButton_d20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                die20 = rand.nextInt(20) + 1;
                String d20 = String.valueOf(die20);
                Toast.makeText(DiceWidgetActivity.this, d20, Toast.LENGTH_SHORT).show();
            }
        });

        /*Rolling a d12*/
        androidImageButton_d12 = (ImageButton) findViewById(R.id.image_button_d12);
        androidImageButton_d12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                die12 = rand.nextInt(12) + 1;
                String d12 = String.valueOf(die12);
                Toast.makeText(DiceWidgetActivity.this, d12, Toast.LENGTH_SHORT).show();
            }
        });

        /*Rolling a d10*/
        androidImageButton_d10 = (ImageButton) findViewById(R.id.image_button_d10);
        androidImageButton_d10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                die10 = rand.nextInt(10) + 1;
                String d10 = String.valueOf(die10);
                Toast.makeText(DiceWidgetActivity.this, d10, Toast.LENGTH_SHORT).show();
            }
        });

        /*Rolling a d8*/
        androidImageButton_d8 = (ImageButton) findViewById(R.id.image_button_d8);
        androidImageButton_d8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                die8 = rand.nextInt(8) + 1;
                String d8 = String.valueOf(die8);
                Toast.makeText(DiceWidgetActivity.this, d8, Toast.LENGTH_SHORT).show();
            }
        });

        /*Rolling a d6*/
        androidImageButton_d6 = (ImageButton) findViewById(R.id.image_button_d6);
        androidImageButton_d6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                die6 = rand.nextInt(6) + 1;
                String d6 = String.valueOf(die6);
                Toast.makeText(DiceWidgetActivity.this, d6, Toast.LENGTH_SHORT).show();
            }
        });

        /*Rolling a d4*/
        androidImageButton_d4 = (ImageButton) findViewById(R.id.image_button_d4);
        androidImageButton_d4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                die4 = rand.nextInt(4) + 1;
                String d4 = String.valueOf(die4);
                Toast.makeText(DiceWidgetActivity.this, d4, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
