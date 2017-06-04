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

    @Nullable
    private FirebaseStorage firebaseStorage;

    @Override
    public void onCreate() {
        System.out.println("HERE");
        INSTANCE = this;
        client = new ClarifaiBuilder("gGdtlfx3ALK7wKl-nRTV_9qNQgSzS8AgX-xwk6Nf","-lTxAVv1JnNg7PGE6ZJw8r3q42Us68fzH0L6I_O8").buildSync();
        firebaseStorage = FirebaseStorage.getInstance();
        super.onCreate();
    }

    @NonNull
    public FirebaseStorage firebaseStorage(){
        final FirebaseStorage firebaseStorage  = this.firebaseStorage;
        if(firebaseStorage == null) {
            throw new IllegalStateException("Cannot do this");
        }
        return firebaseStorage;
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
