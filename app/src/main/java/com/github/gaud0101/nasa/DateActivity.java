package com.github.gaud0101.nasa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.GregorianCalendar;

/**
 * Activity for selecting the currently visible image.
 */
public class DateActivity extends AppCompatActivity implements HelpText {
    private final NavHelper<DateActivity> nav = new NavHelper<>(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        nav.onCreate();

        final DatePicker picker = findViewById(R.id.date_picker);

        Button saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener((v) -> {
            GregorianCalendar cal = new GregorianCalendar();
            cal.clear();
            cal.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth(), 0, 0, 1);
            saveDate(cal.getTimeInMillis());
        });

        Button todayBtn = findViewById(R.id.today);
        todayBtn.setOnClickListener((v) -> saveDate(0));
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
        return R.string.help_date;
    }

    private void saveDate(long millis) {
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(MainActivity.PREF_DATE, millis);
        edit.apply();
        this.finish();
    }
}