package edu.ycp.cs482.iorc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class DiceWidgetActivity extends AppCompatActivity {

    ImageButton androidImageButton_d20;
    ImageButton androidImageButton_d12;
    ImageButton androidImageButton_d10;
    ImageButton androidImageButton_d8;
    ImageButton androidImageButton_d6;
    ImageButton androidImageButton_d4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_widget);

        /*Rolling a d20*/
        androidImageButton_d20 = (ImageButton) findViewById(R.id.image_button_d20);
        androidImageButton_d20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(DiceWidgetActivity.this, "18", Toast.LENGTH_LONG).show();
            }
        });

        /*Rolling a d12*/
        androidImageButton_d12 = (ImageButton) findViewById(R.id.image_button_d12);
        androidImageButton_d12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(DiceWidgetActivity.this, "11", Toast.LENGTH_LONG).show();
            }
        });

        /*Rolling a d10*/
        androidImageButton_d10 = (ImageButton) findViewById(R.id.image_button_d10);
        androidImageButton_d10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(DiceWidgetActivity.this, "9", Toast.LENGTH_LONG).show();
            }
        });

        /*Rolling a d8*/
        androidImageButton_d8 = (ImageButton) findViewById(R.id.image_button_d8);
        androidImageButton_d8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(DiceWidgetActivity.this, "7", Toast.LENGTH_LONG).show();
            }
        });

        /*Rolling a d6*/
        androidImageButton_d6 = (ImageButton) findViewById(R.id.image_button_d6);
        androidImageButton_d6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(DiceWidgetActivity.this, "5", Toast.LENGTH_LONG).show();
            }
        });

        /*Rolling a d4*/
        androidImageButton_d4 = (ImageButton) findViewById(R.id.image_button_d4);
        androidImageButton_d4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(DiceWidgetActivity.this, "3", Toast.LENGTH_LONG).show();
            }
        });
    }
}
