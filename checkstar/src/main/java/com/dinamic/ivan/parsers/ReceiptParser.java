package com.dinamic.ivan.parsers;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.dinamic.ivan.BuildConfig;
import com.dinamic.ivan.entities.Expense;
import com.dinamic.ivan.entities.Receipt;
import com.dinamic.ivan.exc.GoogleAuthError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.koushikdutta.async.future.FutureCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ReceiptParser extends BaseParser {
    private static class Options {
        FutureCallback<Receipt> onRecognized;
    }

    public static class Builder {
        Options opts = new Options();

        public Builder onRecognized(FutureCallback<Receipt> callback) { this.opts.onRecognized = callback; return this; }

        public ReceiptParser build(Context context) {
            return new ReceiptParser(context, opts);
        }
    }

    Options opts = new Options();

    private final static int REUQEST_CODE_CAMERA = 1;
    private final static String LOG_TAG = "RECEIPTS_CAPTURE";

    public ReceiptParser(Context context, Options opts) {
        super(context);

        this.opts = opts;
    }

    public void capture() {
        Intent signInIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(signInIntent, REUQEST_CODE_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if (requestCode == REUQEST_CODE_CAMERA) {
            if (resultCode != RESULT_OK) {
                return;
            }// TODO: Error handle

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = (Bitmap) intent.getExtras().get("data");

                    File photoFile = new File(getPhotoSaveFilePath());

                    FileOutputStream outputStream = null;

                    try {
                        outputStream = new FileOutputStream(photoFile);
                    } catch (FileNotFoundException e) { // TODO
                    }

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    analyser.recognizeReceipt(photoFile.getAbsolutePath(), opts.onRecognized);
                }
            }).start();
        }
    }

    private String getPhotoSaveFilePath() {
        return Environment.getExternalStorageDirectory() + String.format("/photo_%d.jpg", System.currentTimeMillis());
    }
}
