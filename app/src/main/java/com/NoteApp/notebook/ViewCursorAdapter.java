package com.NoteApp.notebook;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Define data displayed in layout_custom_list_row.xml
 */

public class ViewCursorAdapter extends CursorAdapter{
    private LayoutInflater cursorInflater;

    public ViewCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView;

        textView = (TextView) view.findViewById(R.id.textView_creator);
        textView.setText( cursor.getString( cursor.getColumnIndex("CREATOR") ));

        textView = (TextView) view.findViewById(R.id.textView_content);
        textView.setText( cursor.getString( cursor.getColumnIndex("CONTENT") ));

        textView = (TextView) view.findViewById(R.id.textView_date);
        textView.setText( cursor.getString( cursor.getColumnIndex("DATE") ));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.layout_custom_list_row, parent, false);
    }
}
