package com.code.airpollutionmonitoring;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarGraphActivity extends AppCompatActivity {

    private static final int MAX_X_VALUE = 31;
    private static final int MAX_Y_VALUE = 500;
    private static final int MIN_Y_VALUE= 0;
    private static final int MIN_X_VALUE= 1;
    private static final int GROUPS = 4;
    private static final String SET_LABEL = "Air Quality Index of pollutants";
    private double aqi1,aqi2,aqi3,aqi4;
    private static final float BAR_SPACE = 0.05f;
    private static final float BAR_WIDTH = 0.2f;
    private BarChart barGraph;
    private final String url="https://api.thingspeak.com/channels/955795/feeds.json?api_key=J10B3J6FCU0JQ0CC&results=";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_graph);

        barGraph = findViewById(R.id.barGraph);

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);

    }

    private void configureChartAppearance() {
        Log.d("TAG","AAAAAAAAAAAAAAAAAAA");
        barGraph.setPinchZoom(true);
        barGraph.setDrawBarShadow(false);
        barGraph.setDrawGridBackground(false);

        barGraph.getDescription().setEnabled(false);

        XAxis xAxis = barGraph.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);

        YAxis leftAxis = barGraph.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        barGraph.getAxisRight().setEnabled(false);

        barGraph.getXAxis().setAxisMinimum(0);
        barGraph.getXAxis().setAxisMaximum(MAX_X_VALUE);
    }

    private BarData createChartData() {
        Log.d("TAG","BBBBBBBBBBBBBBBBBBBBBBBBB");
        final ArrayList<BarEntry> values1 = new ArrayList<>();
        final ArrayList<BarEntry> values2 = new ArrayList<>();
        final ArrayList<BarEntry> values3 = new ArrayList<>();
        final ArrayList<BarEntry> values4 = new ArrayList<>();

  //      Bundle extra= getIntent().getExtras();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("feeds");
                    int length=array.length()-1;

                    for(int i=(length-31);i<length+31;i++)
                    {
                        aqi1 = Double.parseDouble(array.getJSONObject(length).getString("field1")) * 0.47;
                        aqi2 = Double.parseDouble(array.getJSONObject(length).getString("field2")) * 51;
                        aqi3 = Double.parseDouble(array.getJSONObject(length).getString("field3")) * 54;
                        aqi4 = Double.parseDouble(array.getJSONObject(length).getString("field4")) * 0.3;

                        values1.add(new BarEntry(i, (float) aqi1));
                        values2.add(new BarEntry(i, (float) aqi2));
                        values3.add(new BarEntry(i, (float) aqi3));
                        values4.add(new BarEntry(i, (float) aqi4));
                    }

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








        /*for(int i=0;i<31;i++)
        {
            aqi1=extra.getDouble("data1");
            aqi1=extra.getDouble("data2");
            aqi1=extra.getDouble("data3");
            aqi1=extra.getDouble("data4");

            values1.add(new BarEntry(i, (float) aqi1));
            values2.add(new BarEntry(i, (float) aqi2));
            values3.add(new BarEntry(i, (float) aqi3));
            values4.add(new BarEntry(i, (float) aqi4));

        }
*/
       /* Random random=new Random();
        for(int i=0;i<31;i++) {
            values1.add(new BarEntry(i,random.nextInt(500)));
            values2.add(new BarEntry(i,random.nextInt(500)));
            values3.add(new BarEntry(i,random.nextInt(500)));
            values4.add(new BarEntry(i,random.nextInt(500)));
        }*/
        Log.d("TAG","FOR lOOP FOR VALES COMPLETE");

        BarDataSet set1 = new BarDataSet(values1,"Particulate Matter 2.5um");
        BarDataSet set2 = new BarDataSet(values2,"Particulate Matter 10um");
        BarDataSet set3 = new BarDataSet(values3,"Carbon Monoxide");
        BarDataSet set4 = new BarDataSet(values4,"Sulphur Dioxide");


        set1.setColor(ColorTemplate.MATERIAL_COLORS[0]);
        set2.setColor(ColorTemplate.MATERIAL_COLORS[1]);
        set3.setColor(ColorTemplate.MATERIAL_COLORS[2]);
        set4.setColor(ColorTemplate.MATERIAL_COLORS[3]);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);

        BarData data = new BarData(dataSets);
        return data;
    }

    private void prepareChartData(BarData data) {
        barGraph.setData(data);
        barGraph.getBarData().setBarWidth(BAR_WIDTH);
        float groupSpace = 1f - ((BAR_SPACE + BAR_WIDTH) * GROUPS);
        barGraph.groupBars(0, groupSpace, BAR_SPACE);

        barGraph.invalidate();
    }
}
