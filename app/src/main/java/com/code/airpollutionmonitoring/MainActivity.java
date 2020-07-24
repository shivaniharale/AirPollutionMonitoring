package com.code.airpollutionmonitoring;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button button1,button2,button3,button4,button5;
    private double aqi1,aqi2,aqi3,aqi4,AQI;
    TextView textView;
    private final String url="https://api.thingspeak.com/channels/955795/feeds.json?api_key=J10B3J6FCU0JQ0CC&results=";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textview);

        Typeface MyFont = Typeface.createFromAsset(getAssets(), "fonts/showg.ttf");

        button1 = findViewById(R.id.button1);
        button1.setTypeface(MyFont);

        button2 = findViewById(R.id.button2);
        button2.setTypeface(MyFont);

        button3 = findViewById(R.id.button3);
        button3.setTypeface(MyFont);

        button4 = findViewById(R.id.button4);
        button4.setTypeface(MyFont);

        button5 = findViewById(R.id.button5);
        button5.setTypeface(MyFont);

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

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBarGraph();
            }
        });

        Log.d("TAG","ZZZZZZZZZZZZZZ");

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("feeds");
                    int length=array.length()-1;

                    aqi1=Double.parseDouble(array.getJSONObject(length).getString("field1"))*0.47;
                    Log.d("TAG","HIIIII"+aqi1);
                    aqi2=Double.parseDouble(array.getJSONObject(length).getString("field2"))*51;
                    aqi3=Double.parseDouble(array.getJSONObject(length).getString("field3"))*54;
                    aqi4=Double.parseDouble(array.getJSONObject(length).getString("field4"))*0.3;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR","Error loading entries");
            }
        }
        );
        requestQueue.add(jsonObjectRequest);

        double[] array ={aqi1, aqi2, aqi3, aqi4};
        Arrays.sort(array);
        int length = array.length - 1;
        AQI = array[length];
        textView.append(""+AQI);
        Log.d("T","HELLO"+aqi1+":::"+aqi2+":::"+aqi3+":::"+aqi4);

        Intent intent = new Intent(MainActivity.this, BarGraphActivity.class);
        intent.putExtra("data1", aqi1);
        intent.putExtra("data2", aqi2);
        intent.putExtra("data3", aqi3);
        intent.putExtra("data4", aqi4);

        Log.d("TAG","Z");

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
    public void openBarGraph(){
        Intent intent5=new Intent(this, BarGraphActivity.class);
        startActivity(intent5);
    }

}
