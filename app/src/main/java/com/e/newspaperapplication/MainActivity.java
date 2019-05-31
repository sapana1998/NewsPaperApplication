package com.e.newspaperapplication;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NavigableMap;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView; //connect to xml
    private RecyclerView.LayoutManager mLayoutManager;  //layout manager connect view component
    private RecyclerView.Adapter mAdapter;  //transmit the data source to view
    ArrayList<HashMap<String,String>> arrayListNews;
    static String description1; //String is class

    @Override   //an notation
    protected void onCreate(Bundle savedInstanceState) //bundle is collection of any type of data. its parsable.bundle method having getExtra ,putExtra
    {
        super.onCreate(savedInstanceState); //super is used to call parent class and Context is parent class
        setContentView(R.layout.activity_main); //it is method of Activity Class // it is Compiler used dex

        initComponents();
    }

    private void initComponents() {
        mRecyclerView=(RecyclerView) findViewById(R.id.mRecycleView);
        mLayoutManager=new LinearLayoutManager(MainActivity.this);  //it is pass context than there parent class is context
        mRecyclerView.setLayoutManager(mLayoutManager);
        callAPI();
    }

    private void callAPI() {

// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.myjson.com/bins/wzain";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() //antonymous listener
                {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(MainActivity.this, "Response is: "+ response.substring(0,500), Toast.LENGTH_SHORT).show();
                        parseAPIResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseAPIResponse(String response) {

        try {
            JSONObject objResponse = new JSONObject(response);
            JSONArray arrayHeadlines = objResponse.getJSONArray("headlines");
            arrayListNews = new ArrayList<>();

            for (int i = 0; i < arrayHeadlines.length(); i++) {
                JSONObject objItem = arrayHeadlines.getJSONObject(i);
                String title = objItem.getString("title");
                String imgUrl = objItem.getString("imgUrl");
                String description = objItem.getString("description");

                HashMap<String, String> map = new HashMap<>();  //mapping the things
                map.put("title", title);
                map.put("url", imgUrl);
                map.put("detail", description);
                arrayListNews.add(map);
            }

            //set adapter
            mAdapter = new HomeListAdapter(MainActivity.this, arrayListNews);
            mRecyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}