package com.github.gaud0101.nasa;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.net.ssl.HttpsURLConnection;

public class DownloadTask extends AsyncTask<DownloadTask.Params, Float, DownloadTask.Result> {
    public static class Params {
        public final GregorianCalendar date;
        public final String apiKey;
        public final ProgressBar progressBar;
        public final FragmentManager manager;
        public final int container;

        public Params(ProgressBar progressBar, FragmentManager manager, int container, GregorianCalendar date, String apiKey) {
            this.date = date;
            this.apiKey = apiKey;
            this.progressBar = progressBar;
            this.manager = manager;
            this.container = container;
        }
    }

    public static class Result implements Serializable {
        public final byte[] image;
        public final String date;
        public final String explanation;
        public final URL hdurl;
        public final String title;

        private Result(JSONObject obj, byte[] image) throws JSONException, MalformedURLException {
            try {
                this.image = image;
                this.date = obj.getString("date");
                this.explanation = obj.getString("explanation");
                this.hdurl = new URL(obj.getString("hdurl"));
                this.title = obj.getString("title");
            } catch (Exception e) {
                System.err.println(obj);
                throw e;
            }
        }
    }

    private WeakReference<ProgressBar> progressBar;
    private WeakReference<FragmentManager> manager;
    private int container;

    @Override
    protected Result doInBackground(Params... params) {
        Result retval = null;
        Params param = params[0];
        this.progressBar = new WeakReference<>(param.progressBar);
        this.manager = new WeakReference<>(param.manager);
        this.container = param.container;

        String urlTxt = "https://api.nasa.gov/planetary/apod?api_key="
                + param.apiKey
                + "&date="
                + param.date.get(Calendar.YEAR)
                + "-"
                + param.date.get(Calendar.MONTH)
                + "-"
                + param.date.get(Calendar.DAY_OF_MONTH);

        try {
            byte[] jsonBytes = fetch(urlTxt);
            JSONObject json = new JSONObject(new String(jsonBytes));

            urlTxt = json.getString("url");

            byte[] imageBytes = fetch(urlTxt);

            retval = new Result(json, imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retval;
    }

    private byte[] fetch(String urlTxt) throws IOException {
        byte[] retval = null;

        URL url = new URL(urlTxt);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.connect();

        int length = connection.getContentLength();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try (InputStream in = connection.getInputStream()) {
            int sz;
            while (!isCancelled() && 0 < (sz = in.read(buffer))) {
                out.write(buffer, 0, sz);

                if (length > 0) {
                    publishProgress(((float) out.size()) / ((float) length));
                }
            }
        }

        retval = out.toByteArray();

        return retval;
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);
        System.out.println(values[0]);

        ProgressBar bar = progressBar.get();

        if (bar == null) {
            cancel(true);
        } else {
            bar.setMax(1000);
            bar.setProgress((int) (values[0] * 1000));
        }
    }

    @Override
    protected void onPostExecute(Result bitmap) {
        super.onPostExecute(bitmap);

        FragmentManager manager = this.manager.get();
        System.out.println("Done " + manager);

        if (manager != null) {
            ImageFragment fragment = ImageFragment.newInstance(bitmap);
            manager.beginTransaction().add(container, fragment).commit();
        }
    }
}
