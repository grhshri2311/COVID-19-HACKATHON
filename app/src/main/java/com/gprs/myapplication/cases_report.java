package com.gprs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class cases_report extends AppCompatActivity {

    Spinner spinner,spinner2;
    private RequestQueue queue;
    ArrayList<String> arrayList,district;
    ArrayList<String> arrayList1,district1;
    ArrayList<String> active;
    ArrayList<String> confirm;
    ArrayList<String> death;
    ArrayList<String> recover;
    ArrayList<String> active1;
    ArrayList<String> confirm1;
    ArrayList<String> death1;
    ArrayList<String> recover1;
    ArrayAdapter<String> dataAdapter,dataAdapter2;
    String state=null;
    ArrayList<JSONObject> object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_report2);

        queue = Volley.newRequestQueue(this);
        arrayList = new ArrayList();
        district=new ArrayList<>();
        arrayList1 = new ArrayList();
        active = new ArrayList();
        confirm = new ArrayList();
        recover = new ArrayList();
        death = new ArrayList();
        active1 = new ArrayList();
        confirm1 = new ArrayList();
        recover1 = new ArrayList();
        district1=new ArrayList<>();
        death1 = new ArrayList();
        object=new ArrayList<>();

        spinner = findViewById(R.id.state_select);
        spinner2 = findViewById(R.id.state_district);


        dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, R.id.txt_bundle, arrayList);
             dataAdapter2 = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, R.id.txt_bundle, district);


        spinner.setAdapter(dataAdapter);
        spinner.setGravity(11);

        int initialposition=spinner.getSelectedItemPosition();
        spinner.setSelection(initialposition, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    for (int i = 0; i < arrayList1.size(); i++) {
                        if (arrayList1.get(i).equals(parent.getItemAtPosition(position))) {
                            state = arrayList1.get(i);
                            district.clear();
                            district1.clear();
                            active1.clear();
                            recover1.clear();
                            death1.clear();
                            confirm1.clear();
                            dataAdapter2.notifyDataSetChanged();
                            new cases_report.RetrieveFeedTask1().execute();
                            set(i);
                            break;
                        }
                    }
                }
                else {
                    state=null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setAdapter(dataAdapter2);
        spinner2.setGravity(11);

        spinner2.setSelection(0, false);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    for (int i = 0; i < district1.size(); i++) {
                        if (district1.get(i).equals(parent.getItemAtPosition(position))) {
                            set1(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new cases_report.RetrieveFeedTask().execute();

    }

    private void set1(int i) {
        TextView head = findViewById(R.id.heading);
        TextView textView = findViewById(R.id.textView5);
        TextView textView1 = findViewById(R.id.textView6);
        TextView textView2 = findViewById(R.id.textView7);
        TextView textView3 = findViewById(R.id.textview8);

        head.setText(district1.get(i));
        textView.setText(active1.get(i));
        textView1.setText(confirm1.get(i));
        textView2.setText(death1.get(i));
        textView3.setText(recover1.get(i));
    }

    private void set(int i) {
        TextView head = findViewById(R.id.heading);
        TextView textView = findViewById(R.id.textView5);
        TextView textView1 = findViewById(R.id.textView6);
        TextView textView2 = findViewById(R.id.textView7);
        TextView textView3 = findViewById(R.id.textview8);

        head.setText(arrayList1.get(i));
        textView.setText(active.get(i));
        textView1.setText(confirm.get(i));
        textView2.setText(death.get(i));
        textView3.setText(recover.get(i));


    }

    public void visualize(View view) {
        startActivity(new Intent(cases_report.this,visualize.class));
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {


        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            try {
                URL url = new URL("https://api.covid19india.org/data.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);

            }
            return null;
        }

        protected void onPostExecute(String response) {


            Log.i("INFO", response);

            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray jsonArray = object.getJSONArray("statewise");

                for (int i = 1; i < jsonArray.length(); i++) {
                    arrayList.add(jsonArray.getJSONObject(i).optString("state").toString());
                    arrayList1.add(jsonArray.getJSONObject(i).optString("state").toString());
                    active.add(jsonArray.getJSONObject(i).optString("active").toString());
                    confirm.add(jsonArray.getJSONObject(i).optString("confirmed").toString());
                    death.add(jsonArray.getJSONObject(i).optString("deaths").toString());
                    recover.add(jsonArray.getJSONObject(i).optString("recovered").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();


            }
            Collections.sort(arrayList);
            Collections.reverse(arrayList);
            arrayList.add("Select State");
            Collections.reverse(arrayList);
            dataAdapter.notifyDataSetChanged();

        }
    }

    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {


        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            try {
                URL url = new URL("https://api.covid19india.org/state_district_wise.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);

            }
            return null;
        }

        protected void onPostExecute(String response) {


            Log.i("INFO", response);



            try {
                if(state!=null) {
                    JSONObject object1 = (JSONObject) new JSONTokener(response).nextValue();
                    object1=object1.getJSONObject(state).getJSONObject("districtData");

                    Iterator<String> keys = object1.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        district.add(key);
                        district1.add(key);
                        active1.add(object1.getJSONObject(key).optString("active"));
                        confirm1.add(object1.getJSONObject(key).optString("confirmed"));
                        death1.add(object1.getJSONObject(key).optString("deceased"));
                        recover1.add(object1.getJSONObject(key).optString("recovered"));
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();


            }
            Collections.sort(district);
            Collections.reverse(district);
            district.add("Select District");
            Collections.reverse(district);
            dataAdapter2.notifyDataSetChanged();

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

