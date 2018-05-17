package com.NoteApp.notebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {
    public static boolean createFileWithContent(Context context, String directory, String fileName, String data) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), directory + "/" + fileName);
                FileWriter writer = new FileWriter(file);
                writer.write(data);
                writer.close();
            } catch (IOException e) {
                Toast.makeText(context, R.string.unable_create_file, Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(context, R.string.storage_unavailable, Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(context, R.string.file_create_success, Toast.LENGTH_SHORT).show();
        return true;
    }

    public static boolean sendEmailWithFile(Context context, String directory, String data) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.getDefault() );
        String date = df.format(c.getTime() );

        File filePath = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), directory + "/" + data);
        Uri path = Uri.fromFile(filePath);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_title + date);
        // Attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        context.startActivity(emailIntent.createChooser(emailIntent, "Send email"));
        return true;
    }
}
