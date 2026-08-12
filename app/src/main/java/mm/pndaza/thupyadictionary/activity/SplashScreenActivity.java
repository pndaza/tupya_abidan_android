package mm.pndaza.thupyadictionary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mm.pndaza.thupyadictionary.R;
import mm.pndaza.thupyadictionary.utils.SharePref;


public class SplashScreenActivity extends AppCompatActivity {

    private static final String DATABASE_PATH = "databases";
    private static final String DATABASE_FILENAME = "words.db";
    private static final int ASSET_DB_VERSION = 1;
    private String SAVED_PATH;
    private Context context;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // load theme
        if (SharePref.getInstance(this).getPrefNightModeState()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        SAVED_PATH = getFilesDir().toString();
        context = this;

        // save default setting
        SharePref sharePref = SharePref.getInstance(this);
        if (sharePref.isFirstTime()) {
            sharePref.saveDefault();
        }




        if (isDatabaseExist() && sharePref.isDatabaseCopied()
                && sharePref.getDatabaseVersion() == ASSET_DB_VERSION) {
            startMainActivity();
        } else {
            setupDatabase();
        }

    }

    private boolean isDatabaseExist() {
        return new File(SAVED_PATH + "/" + DATABASE_PATH + "/" + DATABASE_FILENAME).exists();
    }

    private void setupDatabase() {
        executor.execute(this::copyDatabase);
    }

    private void copyDatabase() {
        boolean success = false;

        File path = new File(SAVED_PATH + "/" + DATABASE_PATH );
        // check database folder is exist and if not, make folder.
        if (!path.exists()) {
            path.mkdirs();
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = getAssets()
                    .open(DATABASE_PATH + "/" + DATABASE_FILENAME);
            outputStream = new FileOutputStream(
                    SAVED_PATH + "/" + DATABASE_PATH + "/" + DATABASE_FILENAME);

            byte[] buffer = new byte[1024];
            int length;
            while (( length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (success) {
            SharePref.getInstance(context).setDbCopyState(true);
            SharePref.getInstance(context).setDatabaseVersion(ASSET_DB_VERSION);
            mainHandler.post(this::startMainActivity);
        } else {
            // DB copy failed — don't proceed to MainActivity with a corrupt/missing DB.
            mainHandler.post(this::finish);
        }
    }

    private void startMainActivity() {

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            finish();
            SplashScreenActivity.this.startActivity(intent);
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

}
