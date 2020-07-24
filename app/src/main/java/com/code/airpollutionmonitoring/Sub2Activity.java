package com.code.airpollutionmonitoring;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Response.Listener;

public class Sub2Activity<name> extends AppCompatActivity {
    private static final String TAG = "Sub2Activity";
    private static final String CHANNEL_ID ="0" ;
    private String url;
    private TextView text;
    private TextView currentData;
    private LinearLayout layout;
    private RequestQueue requestQueue;
    private String message;
    private final int limit=100,threshold=20;
    private int PERMISSION_REQUEST_CODE = 1000;
    private double sum,average;


    //20ug
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);
        createNotificationChannel();

        text =findViewById(R.id.textdata);
        currentData=findViewById(R.id.textView2);
        layout=findViewById(R.id.layout);

        url = "https://api.thingspeak.com/channels/955795/feeds.json?api_key=J10B3J6FCU0JQ0CC&results=";
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("feeds");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject temp = array.getJSONObject(i);
                        String totaltime = temp.getString("created_at");
                        String date =totaltime.substring(0,10);
                        String time=totaltime.substring(11,19);
                        String entry = temp.getString("entry_id");
                        String value = temp.getString("field2");
                        double valueDouble=Double.parseDouble(value);
                        sum+=valueDouble;
                        average=sum/i;

                        text.append("Sr n-->"+i+"\0\0\0Date-->"+date+"\0\0\0 Time-->"+time+"\0\0\0 Entry id-->"
                                + entry + "\0\0\0Data-->" + value + "\n");
                        Log.d(TAG,"REACHED HERE TOO");

                    }
                    int length=array.length()-1;
                    currentData.append(array.getJSONObject(length).getString("field2"));
                    double data=Double.parseDouble(array.getJSONObject(length).getString("field2"));


                    data=120;
                    if( data >= threshold)
                    {
                        message="Increased PM 10 level:Perform precautionary measures";
                        addNotification(message);
                    }
                    else if (data>=limit)
                    {
                        message="Hazardous 10 level:Evacuate immediately";
                        addNotification(message);
                        createSMS(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(layout,
                        "Error loading data entries.",
                        Snackbar.LENGTH_LONG
                ).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void addNotification(String message) {
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.airpollutionimage)
                .setContentTitle("WARNING")
                .setContentText(message)
                .setAutoCancel(true);

        Intent notificationIntent = new Intent(this, Sub1Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, Integer.parseInt(CHANNEL_ID), notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d(TAG,"NOTIFICATION SUCCESSFUL");
        manager.notify(Integer.parseInt(CHANNEL_ID), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"c1", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createSMS(String message){
//        Intent intent=new Intent(getApplicationContext(),Sub1Activity.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                SmsManager smsManager = SmsManager.getDefault();
                //smsManager.sendTextMessage("9890726906",null,message,pendingIntent,null);
                //smsManager.sendTextMessage("9011619459",null,message,pendingIntent,null);
                smsManager.sendTextMessage("9762204449", null, message, null, null);
                Log.d(TAG, "DID I REACH HERE?");
            }
        }
    }
}