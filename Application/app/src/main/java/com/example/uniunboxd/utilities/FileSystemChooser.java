package com.example.uniunboxd.utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * The FileSystemChooser class provides methods for choosing a PDF or an image file from the file system.
 * It also provides a method to read text from a given Uri.
 */
public class FileSystemChooser {

    /**
     * Opens a file chooser to select a PDF file.
     * @param f The fragment from which the file chooser is opened.
     */
    public static void ChoosePDF(Fragment f) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/pdf");
        f.startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                1
        );
    }

    /**
     * Opens a file chooser to select a PDF file with a specific request code.
     * @param f The fragment from which the file chooser is opened.
     * @param code The request code for the startActivityForResult call.
     */
    public static void ChoosePDF(Fragment f, int code){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/pdf");
        f.startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                code
        );
    }

    /**
     * Opens a file chooser to select an image file.
     * @param f The fragment from which the file chooser is opened.
     */
    public static void ChooseImage(Fragment f){
        Intent chooseFile = new Intent(Intent.ACTION_PICK);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("image/*");
        f.startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                1
        );
    }

    /**
     * Opens a file chooser to select an image file with a specific request code.
     * @param f The fragment from which the file chooser is opened.
     * @param code The request code for the startActivityForResult call.
     */
    public static void ChooseImage(Fragment f, int code) {
        Intent chooseFile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        f.startActivityForResult(chooseFile, code);
    }

    /**
     * Reads text from a given Uri.
     * @param uri The Uri to read from.
     * @param activity The activity from which the content resolver is obtained.
     * @return A byte array containing the read data.
     * @throws IOException If an error occurs while reading.
     */
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
