package com.bitcoinpricetracker.hg.bitcoinpricetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";


        TextView bPriceTextView;
        Spinner spinner;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            bPriceTextView = (TextView) findViewById(R.id.price);
            spinner = (Spinner) findViewById(R.id.currency_spinner);


            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.currency_list, android.R.layout.simple_spinner_item);


            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            spinner.setAdapter(adapter);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    Log.d("Bitcoin", "" + adapterView.getItemAtPosition(pos));
                    String chosenCurrency = adapterView.getItemAtPosition(pos).toString();
                    Networking(BASE_URL + chosenCurrency);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.d("Bitcoin", "No Selection");
                }
            });

        }


        private void Networking(String url) {

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // called when response is "200 OK"
                    Log.d("Bitcoin", "JSON: " + response.toString());

                    try {
                        String price = response.getString("ask");
                        bPriceTextView.setText(price);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                    Log.d("Bitcoin", "Fail response: " + response);
                    Log.e("ERROR", e.toString());
                    Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
