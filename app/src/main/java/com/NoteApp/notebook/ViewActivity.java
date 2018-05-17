package com.NoteApp.notebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class ViewActivity extends AppCompatActivity {
    public final static String ID_EXTRA = "com.NoteApp.notebook._ID";
    private SQLiteDatabase db;
    private Cursor cursor;
    ViewCursorAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.log_title);
        setSupportActionBar(toolbar);


        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
            ListView listView = (ListView) findViewById(R.id.Listview_records);

            try {
                db = databaseHelper.getReadableDatabase();
                cursor = db.query("RECORD", new String[]{"_id", "CREATOR", "CONTENT", "DATE"},
                    null, null, null, null, null);

            listAdapter = new ViewCursorAdapter(this, cursor, 0);
            listView.setAdapter(listAdapter);
        } catch(SQLiteException e) {
            Toast.makeText(this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
        }
        listView.setOnItemClickListener(onListClick);
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ViewActivity.this, EditActivity.class);
            intent.putExtra(ID_EXTRA, String.valueOf(id));
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
        switch ( item.getItemId() ) {

            case R.id.new_item:
                    Intent intent = new Intent(ViewActivity.this, AddActivity.class);
                    startActivity(intent);
                return true;

            case R.id.send_data:
                try {
                    SQLiteDatabase db = databaseHelper.getReadableDatabase();
                    String data = DatabaseHelper.getDatabaseContentsAsString(db);

                    Helper.createFileWithContent(ViewActivity.this, "data", "data.csv", data);
                    Helper.sendEmailWithFile(ViewActivity.this, "data", "data.csv");
                } catch (SQLException e) {
                    Toast.makeText(this, R.string.unable_send_data, Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.delete_all:
                try {
                    showDeleteAllDataDialog(databaseHelper);
                } catch (SQLException e) {
                    Toast.makeText(this, R.string.unable_delete_data, Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteAllDataDialog(final SQLiteOpenHelper databaseHelper) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.warning_message);
        alertDialog.setMessage(R.string.delete_all_confirm);
        alertDialog.setCancelable(true);

        alertDialog.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllData(databaseHelper);
            }
        });
        alertDialog.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void deleteAllData(SQLiteOpenHelper databaseHelper) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        DatabaseHelper.deleteAllRecords(db);
        Toast.makeText(this, R.string.all_data_deleted, Toast.LENGTH_SHORT).show();
        Intent updateIntent = new Intent(ViewActivity.this, ViewActivity.class);
        startActivity(updateIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null) cursor.close();
        if(db != null) db.close();
    }
}
