package edu.ucsb.cs.cs190i.aviato.seefood;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import okhttp3.OkHttpClient;

/*
 * Source: https://github.com/Clarifai/clarifai-android-starter/blob/b28ce678ce1a099e7c836a81cbc70760339478a5/app/src/main/java/com/clarifai/android/starter/api/v2/App.java
 */

public class App extends Application {
    private static App INSTANCE;

    @NonNull
    public static App get() {
        final App instance = INSTANCE;
        if (instance == null) {
            throw new IllegalStateException("App has not been created yet!");
        }
        return instance;
    }

    @Nullable
    private ClarifaiClient client;

    @Override
    public void onCreate() {
        INSTANCE = this;
        client = new ClarifaiBuilder("API_KEY_HERE","").buildSync();
        super.onCreate();
    }

    @NonNull
    public ClarifaiClient clarifaiClient() {
        final ClarifaiClient client = this.client;
        if (client == null) {
            throw new IllegalStateException("Cannot use Clarifai client before initialized");
        }
        return client;
    }
}