package com.example.weatherviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherArrayAdapter extends ArrayAdapter<Weather> {

    private static class ViewHolder {
        ImageView conditionImageView;
        TextView dayTextView;
        TextView lowTextView;
        TextView hiTextView;
        TextView humidityTextView;
    }

    private final Map<String, Bitmap> bitmaps = new HashMap<>();

    public WeatherArrayAdapter(Context context, List<Weather> forecast) {
        super(context, -1, forecast);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Weather day = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            viewHolder.conditionImageView =
                    convertView.findViewById(R.id.conditionImageView);
            viewHolder.dayTextView =
                    convertView.findViewById(R.id.dayTextView);
            viewHolder.lowTextView =
                    convertView.findViewById(R.id.lowTextView);
            viewHolder.hiTextView =
                    convertView.findViewById(R.id.hiTextView);
            viewHolder.humidityTextView =
                    convertView.findViewById(R.id.humidityTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (bitmaps.containsKey(day.iconURL)) {
            viewHolder.conditionImageView.setImageBitmap(
                    bitmaps.get(day.iconURL));
        } else {
            new LoadImageTask(viewHolder.conditionImageView)
                    .execute(day.iconURL);
        }

        Context context = getContext();
        viewHolder.dayTextView.setText(
                context.getString(R.string.day_description,
                        day.dayOfWeek, day.description));

        viewHolder.lowTextView.setText(
                context.getString(R.string.low_temp, day.minTemp));

        viewHolder.hiTextView.setText(
                context.getString(R.string.high_temp, day.maxTemp));

        viewHolder.humidityTextView.setText(
                context.getString(R.string.humidity, day.humidity));

        return convertView;
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private final ImageView imageView;
        private String url;

        LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];

            try {
                HttpURLConnection connection =
                        (HttpURLConnection) new URL(url).openConnection();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                bitmaps.put(url, bitmap);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}