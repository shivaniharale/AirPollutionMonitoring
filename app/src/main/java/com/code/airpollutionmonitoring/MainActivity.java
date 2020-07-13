package com.code.airpollutionmonitoring;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button button1,button2,button3,button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface MyFont=Typeface.createFromAsset(getAssets(),"fonts/showg.ttf");

        button1= findViewById(R.id.button1);
        button1.setTypeface(MyFont);

        button2= findViewById(R.id.button2);
        button2.setTypeface(MyFont);

        button3= findViewById(R.id.button3);
        button3.setTypeface(MyFont);

        button4= findViewById(R.id.button4);
        button4.setTypeface(MyFont);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSub1Activity();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSub2Activity();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSub3Activity();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSub4Activity();
            }
        });
    }

    public void openSub1Activity(){
        Intent intent1=new Intent(this,Sub1Activity.class);
        startActivity(intent1);
    }
    public void openSub2Activity(){
        Intent intent2=new Intent(this,Sub2Activity.class);
        startActivity(intent2);
    }
    public void openSub3Activity(){
        Intent intent3=new Intent(this,Sub3Activity.class);
        startActivity(intent3);
    }
    public void  openSub4Activity(){
        Intent intent4=new Intent(this,Sub4Activity.class);
        startActivity(intent4);
    }
}
