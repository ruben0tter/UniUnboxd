package com.example.uniunboxd.utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

public class FileSystemChooser {
    static final int PICKFILE_RESULT_CODE = 42069;
    public static void ChoosePDF(Fragment f){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/pdf");
        f.startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                PICKFILE_RESULT_CODE
        );
    }

    public static void ChooseImage(Fragment f){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        f.startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                PICKFILE_RESULT_CODE
        );
    }

    public static void ChooseImage(Fragment f, int code) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        f.startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                code
        );
    }

    public static byte[] readTextFromUri(Uri uri, Activity activity) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream is = activity.getContentResolver().openInputStream(uri)) {
            int nRead;
            byte[] data = new byte[10000];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
        }
        return buffer.toByteArray();
    }
}
