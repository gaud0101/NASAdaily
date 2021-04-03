package com.github.gaud0101.nasa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements HelpText {
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

        // TODO: Fetch image from NASA
        System.out.println("TODO: " + cal.getTimeInMillis());
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