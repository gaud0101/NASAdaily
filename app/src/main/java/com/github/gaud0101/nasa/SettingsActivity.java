package com.github.gaud0101.nasa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.GregorianCalendar;

/**
 * Activity for configuring the application.
 */
public class SettingsActivity extends AppCompatActivity implements HelpText {
    private final NavHelper<SettingsActivity> nav = new NavHelper<>(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        nav.onCreate();

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
        String key = prefs.getString(MainActivity.PREF_API, "");

        Button saveButton = findViewById(R.id.save_key);
        EditText edit = findViewById(R.id.api_key);
        edit.setText(key);
        saveButton.setOnClickListener((v) -> saveKey(edit.getText().toString()));
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
        return R.string.help_settings;
    }

    private void saveKey(String key) {
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        if (key == null || key.isEmpty()) {
            edit.remove(MainActivity.PREF_API);
        } else {
            edit.putString(MainActivity.PREF_API, key);
        }

        edit.apply();
        this.finish();
    }
}