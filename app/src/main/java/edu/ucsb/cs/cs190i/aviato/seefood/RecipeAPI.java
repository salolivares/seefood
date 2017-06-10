package edu.ucsb.cs.cs190i.aviato.seefood;

/*
 * Created by sal on 5/22/17.
 */


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import edu.ucsb.cs.cs190i.aviato.seefood.json.RecipeResponse;


public class RecipeAPI {
    private static String BASE_URL = "https://api.edamam.com/search";
    private static String API_KEY = "fbf8880153e010a1ce016562d04ee681";
    private static String APP_ID = "cd3b4591";
    

    public static void getRecipeResponse(String s, final JsonResponseListener listener){
        RecipeTask recipeTask = new RecipeTask(listener);
        recipeTask.execute(s);
    }

    public interface JsonResponseListener {
        void onRecipeResponse(RecipeResponse recipeResponse);
    }

    private static class RecipeTask extends AsyncTask<String, Void, RecipeResponse> {
        private JsonResponseListener listener;

        public RecipeTask(JsonResponseListener listener) {
            super();
            this.listener = listener;
        }

        @Override
        protected RecipeResponse doInBackground(String... params) {
            RecipeResponse RecipeResponse = null;

            try {
                ///search?q=strawberry&app_id=cd3b4591&app_key=fbf8880153e010a1ce016562d04ee681&from=0&to=9
                String endpoint = BASE_URL + "?q=" + params[0] + "&app_id=" + APP_ID + "&app_key=" + API_KEY + "&from=0&to=30";
                URL recipeAPIendpoint = new URL(endpoint);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) recipeAPIendpoint.openConnection();

                if (httpsURLConnection. getResponseCode() == 200) {
                    InputStream responseBody = httpsURLConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    Gson gson = new Gson();
                    RecipeResponse = gson.fromJson(jsonReader, RecipeResponse.class);

                    responseBodyReader.close();
                } else {
                    Log.d("API", "Error: Response code - " + httpsURLConnection.getResponseCode());
                }

                httpsURLConnection.disconnect();

            } catch (IOException e) {
                Log.d("hi", "catch");
                e.printStackTrace();
            }

            return RecipeResponse;


        }

        @Override
        protected void onPostExecute(RecipeResponse RecipeResponse) {
            if(listener != null) {

                listener.onRecipeResponse(RecipeResponse);
            }
        }
    }



}