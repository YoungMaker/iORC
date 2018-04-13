package edu.ycp.cs482.iorc.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Random;

import edu.ycp.cs482.iorc.R;

public class DiceWidgetActivity extends AppCompatActivity {

    ImageButton androidImageButton_d20, androidImageButton_d12, androidImageButton_d10,
                androidImageButton_d8, androidImageButton_d6, androidImageButton_d4;

    Button rollButton;

    Random rand = new Random();
    private String diceOutput;
    private TextView textOut;
    private int diceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_widget);


        /*Rolling a d20*/
        androidImageButton_d20 = findViewById(R.id.image_button_d20);
        androidImageButton_d20.setBackgroundColor(0);
        androidImageButton_d20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                diceType = 20;
                setBackground(diceType);

            }
        });

        /*Rolling a d12*/
        androidImageButton_d12 = findViewById(R.id.image_button_d12);
        androidImageButton_d12.setBackgroundColor(0);
        androidImageButton_d12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                diceType = 12;
                setBackground(diceType);
            }
        });

        /*Rolling a d10*/
        androidImageButton_d10 = findViewById(R.id.image_button_d10);
        androidImageButton_d10.setBackgroundColor(0);
        androidImageButton_d10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                diceType = 10;
                setBackground(diceType);
            }
        });

        /*Rolling a d8*/
        androidImageButton_d8 = findViewById(R.id.image_button_d8);
        androidImageButton_d8.setBackgroundColor(0);
        androidImageButton_d8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                diceType = 8;
                setBackground(diceType);
            }
        });

        /*Rolling a d6*/
        androidImageButton_d6 = findViewById(R.id.image_button_d6);
        androidImageButton_d6.setBackgroundColor(0);
        androidImageButton_d6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                diceType = 6;
                setBackground(diceType);
            }
        });

        /*Rolling a d4*/
        androidImageButton_d4 = findViewById(R.id.image_button_d4);
        androidImageButton_d4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                diceType = 4;
                setBackground(diceType);
            }
        });

        rollButton = findViewById(R.id.rollButton);
        androidImageButton_d4.setBackgroundColor(0);
        rollButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                rand = new Random();
                int die = rand.nextInt(diceType) + 1;
                String result = String.valueOf(die);
                diceOutput = " d" + diceType + ":  " + result;
                textOut = findViewById(R.id.txtOutput);
                textOut.setText(diceOutput);
            }
        });
    }

    private void setBackground(int n) {
        if(n == 20){
            androidImageButton_d20.setBackgroundColor(Color.GRAY);
            androidImageButton_d12.setBackgroundColor(0);
            androidImageButton_d10.setBackgroundColor(0);
            androidImageButton_d8.setBackgroundColor(0);
            androidImageButton_d6.setBackgroundColor(0);
            androidImageButton_d4.setBackgroundColor(0);
        }
        else if(n == 12){
            androidImageButton_d20.setBackgroundColor(0);
            androidImageButton_d12.setBackgroundColor(Color.GRAY);
            androidImageButton_d10.setBackgroundColor(0);
            androidImageButton_d8.setBackgroundColor(0);
            androidImageButton_d6.setBackgroundColor(0);
            androidImageButton_d4.setBackgroundColor(0);
        }
        else if(n == 10){
            androidImageButton_d20.setBackgroundColor(0);
            androidImageButton_d12.setBackgroundColor(0);
            androidImageButton_d10.setBackgroundColor(Color.GRAY);
            androidImageButton_d8.setBackgroundColor(0);
            androidImageButton_d6.setBackgroundColor(0);
            androidImageButton_d4.setBackgroundColor(0);
        }
        else if(n == 8){
            androidImageButton_d20.setBackgroundColor(0);
            androidImageButton_d12.setBackgroundColor(0);
            androidImageButton_d10.setBackgroundColor(0);
            androidImageButton_d8.setBackgroundColor(Color.GRAY);
            androidImageButton_d6.setBackgroundColor(0);
            androidImageButton_d4.setBackgroundColor(0);
        }
        else if(n == 6){
            androidImageButton_d20.setBackgroundColor(0);
            androidImageButton_d12.setBackgroundColor(0);
            androidImageButton_d10.setBackgroundColor(0);
            androidImageButton_d8.setBackgroundColor(0);
            androidImageButton_d6.setBackgroundColor(Color.GRAY);
            androidImageButton_d4.setBackgroundColor(0);
        }
        else if(n == 4){
            androidImageButton_d20.setBackgroundColor(0);
            androidImageButton_d12.setBackgroundColor(0);
            androidImageButton_d10.setBackgroundColor(0);
            androidImageButton_d8.setBackgroundColor(0);
            androidImageButton_d6.setBackgroundColor(0);
            androidImageButton_d4.setBackgroundColor(Color.GRAY);
        }

    }

}
