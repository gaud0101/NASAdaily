package com.github.gaud0101.nasa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements HelpText {
    private static final String DEFAULT_API_KEY = "DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d";
    public static final String PREFERENCES = "NASA";
    public static final String PREF_DATE = "selected-date";

    private final NavHelper<MainActivity> nav = new NavHelper<>(this);

    private long lastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
        long selected = prefs.getLong(PREF_DATE,0);

        if (lastDate == selected && selected != 0) {
            return;
        }

        GregorianCalendar cal = new GregorianCalendar();

        if (selected != 0) {
            cal.setTimeInMillis(selected);
        }

        fetchImage(cal);
    }

    private void fetchImage(GregorianCalendar cal) {
        lastDate = cal.getTimeInMillis();

        ProgressBar bar = findViewById(R.id.progress);
        new DownloadTask().execute(new DownloadTask.Params(bar, getSupportFragmentManager(), R.id.fragment, cal, DEFAULT_API_KEY));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return nav.onNavigate(item);
    }

    @Override
    public int getHelpText() {
        return R.string.help_main;
    }
}