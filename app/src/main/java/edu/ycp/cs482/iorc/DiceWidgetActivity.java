package edu.ycp.cs482.iorc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Random;

public class DiceWidgetActivity extends AppCompatActivity {

    ImageButton androidImageButton_d20, androidImageButton_d12, androidImageButton_d10,
                androidImageButton_d8, androidImageButton_d6, androidImageButton_d4;

    Random rand = new Random();
    private String diceOutput;
    private TextView textOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_widget);



        /*Rolling a d20*/
        androidImageButton_d20 = (ImageButton) findViewById(R.id.image_button_d20);
        androidImageButton_d20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                int die20 = rand.nextInt(20) + 1;
                String d20 = String.valueOf(die20);
                diceOutput = "d20:  " + d20;
                textOut = (TextView) findViewById(R.id.txtOutput);
                textOut.setText(diceOutput);
            }
        });

        /*Rolling a d12*/
        androidImageButton_d12 = (ImageButton) findViewById(R.id.image_button_d12);
        androidImageButton_d12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                int die12 = rand.nextInt(12) + 1;
                String d12 = String.valueOf(die12);
                diceOutput = "d12:  " + d12;
                textOut = (TextView) findViewById(R.id.txtOutput);
                textOut.setText(diceOutput);
            }
        });

        /*Rolling a d10*/
        androidImageButton_d10 = (ImageButton) findViewById(R.id.image_button_d10);
        androidImageButton_d10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                int die10 = rand.nextInt(10) + 1;
                String d10 = String.valueOf(die10);
                diceOutput = "d10:  " + d10;
                textOut = (TextView) findViewById(R.id.txtOutput);
                textOut.setText(diceOutput);
            }
        });

        /*Rolling a d8*/
        androidImageButton_d8 = (ImageButton) findViewById(R.id.image_button_d8);
        androidImageButton_d8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                int die8 = rand.nextInt(8) + 1;
                String d8 = String.valueOf(die8);
                diceOutput = " d8:  " + d8;
                textOut = (TextView) findViewById(R.id.txtOutput);
                textOut.setText(diceOutput);
            }
        });

        /*Rolling a d6*/
        androidImageButton_d6 = (ImageButton) findViewById(R.id.image_button_d6);
        androidImageButton_d6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                int die6 = rand.nextInt(6) + 1;
                String d6 = String.valueOf(die6);
                diceOutput = " d6:  " + d6;
                textOut = (TextView) findViewById(R.id.txtOutput);
                textOut.setText(diceOutput);
            }
        });

        /*Rolling a d4*/
        androidImageButton_d4 = (ImageButton) findViewById(R.id.image_button_d4);
        androidImageButton_d4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                rand = new Random();
                int die4 = rand.nextInt(4) + 1;
                String d4 = String.valueOf(die4);
                diceOutput = " d4:  " + d4;
                textOut = (TextView) findViewById(R.id.txtOutput);
                textOut.setText(diceOutput);
            }
        });
    }
}
