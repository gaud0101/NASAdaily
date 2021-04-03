package com.github.gaud0101.nasa;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavHelper<T extends AppCompatActivity & HelpText> {
    private final T activity;

    public NavHelper(T activity) {
        this.activity = activity;
    }

    public void onCreate() {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        toggle.syncState();

        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((item) -> this.onNavigate(item));
    }

    public boolean onNavigate(MenuItem item) {
        Class<?> klass;

        switch (item.getItemId()) {
            case R.id.menu_home:
                klass = MainActivity.class;
                break;

            case R.id.menu_date:
                klass = DateActivity.class;
                break;

            case R.id.menu_favorites:
                klass = FavoritesActivity.class;
                break;

            case R.id.menu_help:
                new AlertDialog.Builder(activity, R.style.Theme_AppCompat_DayNight_Dialog)
                        .setMessage(activity.getHelpText())
                        .show();
                return true;

            default:
                return false;
        }

        Intent intent = new Intent(activity,klass);
        activity.startActivity(intent);
        return true;
    }
}
