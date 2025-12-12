package com.example.weatherviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Weather> weatherList = new ArrayList<>();
    private WeatherArrayAdapter weatherArrayAdapter;
    private ListView weatherListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherListView = findViewById(R.id.weatherListView);
        weatherArrayAdapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText locationEditText = findViewById(R.id.locationEditText);
                URL url = createURL(locationEditText.getText().toString());

                if (url != null) {
                    dismissKeyboard(locationEditText);
                    new GetWeatherTask().execute(url);
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            R.string.invalid_url, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void dismissKeyboard(View view) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private URL createURL(String city) {
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.web_service_url);
        try {
            String urlString = baseUrl
                    + URLEncoder.encode(city, "UTF-8")
                    + "&days=7"
                    + "&APPID=" + URLEncoder.encode(apiKey, "UTF-8");

            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }
                    Log.d("WEATHER", "Resposta da API: " + builder.toString());
                    return new JSONObject(builder.toString());
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.coordinatorLayout),
                        R.string.connect_error, Snackbar.LENGTH_LONG).show();

                Log.e("WEATHER_ERROR", "Erro na requisição", e);
            } finally {
                if (connection != null)
                    connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weatherJson) {
            Log.e("WEATHER", weatherJson.toString());

            // RECEBE a lista convertida
            ArrayList<Weather> result = convertJSONtoArrayList(weatherJson);

            // LIMPA a lista atual do adapter
            weatherList.clear();

            // ADICIONA os novos itens
            weatherList.addAll(result);

            // AGORA sim atualiza a tela
            weatherArrayAdapter.notifyDataSetChanged();
            weatherListView.smoothScrollToPosition(0);
        }

    }

    private ArrayList<Weather> convertJSONtoArrayList(JSONObject json) {

        ArrayList<Weather> list = new ArrayList<>();

        try {
            JSONArray days = json.getJSONArray("days");

            for (int i = 0; i < days.length(); i++) {

                JSONObject day = days.getJSONObject(i);

                // 1. Converter "2025-11-26" para timestamp (segundos)
                long timestamp = convertDateToTimestamp(day.getString("date"));

                double minF = day.getDouble("minTempC");
                double maxF = day.getDouble("maxTempC");

                // 3. Umidade (já em decimal: 0.75) -> sua classe divide por 100, então multiplica por 100!
                double humidityPercent = day.getDouble("humidity") * 100;

                // 4. Descrição
                String description = day.getString("description");

                // 5. Converter ícone unicode para nome (⛅ -> "02d")
                String icon = convertIcon(day.getString("icon"));

                // 6. Criar Weather antigo
                Weather w = new Weather(
                        timestamp,
                        minF,
                        maxF,
                        humidityPercent,
                        description,
                        icon
                );

                list.add(w);
            }

        } catch (Exception e) {
            Log.e("WEATHER", "Erro ao converter JSON", e);
        }
        Log.e("WEATHER", list.toString());
        return list;
    }

    private long convertDateToTimestamp(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(date).getTime() / 1000;
    }

    private double cToF(double c) {
        return c * 9/5 + 32;
    }

    private String convertIcon(String icon) {
        switch (icon) {
            case "⛅": return "02d";
            case "☀": return "01d";
            case "🌧": return "09d";
            case "⛈": return "11d";
        }
        return "02d"; // fallback
    }




}