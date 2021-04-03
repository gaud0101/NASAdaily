package com.github.gaud0101.nasa;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

/**
 * Activity for managing favorite images.
 */
public class FavoritesActivity extends AppCompatActivity implements HelpText {
    private static class FavoritesCursorAdapter extends CursorAdapter {
        public FavoritesCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
            view.findViewById(R.id.btn_delete).setFocusable(false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView title = view.findViewById(R.id.favorite_title);
            TextView date = view.findViewById(R.id.favorite_date);

            int idIdx = cursor.getColumnIndex(BaseColumns._ID);
            int titleIdx = cursor.getColumnIndexOrThrow(Database.COLUMN_TITLE);
            int dateIdx = cursor.getColumnIndexOrThrow(Database.COLUMN_DATE);

            String titleTxt = cursor.getString(titleIdx);
            String dateTxt = cursor.getString(dateIdx);
            long id = cursor.getLong(idIdx);

            title.setText(titleTxt);
            date.setText(dateTxt);

            ImageButton btn = view.findViewById(R.id.btn_delete);
            btn.setOnClickListener((v) -> {
                Database db = new Database(context);
                db.unfavorite(id);

                Snackbar.make(view, R.string.snack_unfaved, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, (u) -> {
                            db.favorite(titleTxt, dateTxt);
                            this.swapCursor(db.listFavorites());
                        })
                        .show();

                this.swapCursor(db.listFavorites());
            });
        }
    }

    private final NavHelper<FavoritesActivity> nav = new NavHelper<>(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        nav.onCreate();

        ListView list = findViewById(R.id.favorites_list);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Database db = new Database(getApplicationContext());
            saveDate(db.getMillis(id));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Context ctx = getApplicationContext();
        Database db = new Database(ctx);

        ListView list = findViewById(R.id.favorites_list);
        list.setAdapter(new FavoritesCursorAdapter(ctx, db.listFavorites()));
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
        return R.string.help_favorites;
    }

    private void saveDate(long millis) {
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(MainActivity.PREF_DATE, millis);
        edit.apply();
        this.finish();
    }
}