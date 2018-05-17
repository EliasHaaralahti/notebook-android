package com.NoteApp.notebook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_item);
        toolbar.setTitle(R.string.add_title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        // Null check required
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault() );
        ( (EditText) findViewById( R.id.field_date) ).setText( df.format(c.getTime() ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick_imageButton_setDate(View view) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                EditText editText_date = (EditText)findViewById(R.id.field_date);
                editText_date.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.save_item:
                String creator = ((TextView) findViewById(R.id.field_creator)).getText().toString();
                String content = ((TextView) findViewById(R.id.field_content)).getText().toString();
                String date = ((TextView) findViewById(R.id.field_date)).getText().toString();

                if ( creator.equals("") || content.equals("") ) {
                    Toast.makeText(this, R.string.request_fill_fields, Toast.LENGTH_LONG).show();
                    return false;
                }

                SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
                try {
                    SQLiteDatabase db = databaseHelper.getReadableDatabase();
                    DatabaseHelper.insertRecord(db, creator, content, date);
                    Intent intent = new Intent(AddActivity.this, ViewActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    Toast.makeText(this, R.string.unable_add_item, Toast.LENGTH_LONG).show();
                    break;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
