package edu.ucsb.cs.cs190i.aviato.seefood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import edu.ucsb.cs.cs190i.aviato.seefood.auth.LoginActivity;
import edu.ucsb.cs.cs190i.aviato.seefood.json.*;
import edu.ucsb.cs.cs190i.aviato.seefood.recycleViewHelpers.SimpleItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener {
    /*
        Constants
     */
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int LOGIN_REQUEST_CODE = 1222;
    private static final String APP_TAG = "SeeFood";
    public static final String FIREBASE_DB_KEY = "food";
    private static final int PICK_PHOTO_CODE = 1233;

    /*
        Fields
     */
    private String photoFileName;
    private Query query;
    private FoodAdapter foodAdapter;
    private ArrayList<FoodItem> foodAdapterItems;
    private ArrayList<String> foodAdapterKeys;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        FloatingActionButton galleryFab = (FloatingActionButton) findViewById(R.id.gallery_fab);
        galleryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Login - check if already logged in.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            showLogin();
        } else {
            setupRecyclerView();
        }
    }

    private void showLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodAdapter.destroy();
    }

    private void setupRecyclerView() {
        query = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FIREBASE_DB_KEY);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.food_rv);

        foodAdapter = new FoodAdapter(this, query, foodAdapterItems, foodAdapterKeys, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(foodAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                onImagePicked(takenPhotoUri);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == LOGIN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                setupRecyclerView();
            }
        }

        if (requestCode == PICK_PHOTO_CODE){
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    Uri photoUri = data.getData();
                    onImagePicked(photoUri);
                } else { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void onImagePicked(final Uri takenPhotoUri) {
        // Now we will upload our image to the Clarifai API
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Upload to clarifai
        new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
            @Override protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
                // The default Clarifai model that identifies concepts in images
                final ConceptModel foodModel = App.get().clarifaiClient().getDefaultModels().foodModel();

                byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(MainActivity.this, takenPhotoUri, 75);

                // Use this model to predict, with the image that the user just selected as the input
                assert imageBytes != null;
                return foodModel.predict()
                        .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                        .executeSync();
            }

            @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
                if(response == null) {
                    Log.e(APP_TAG,"null concept");
                    progressDialog.dismiss();
                    return;
                }


                if (!response.isSuccessful()) {
                    Log.e(APP_TAG,"error while contacting api");
                    progressDialog.dismiss();
                    return;
                }
                final List<ClarifaiOutput<Concept>> predictions = response.get();
                if (predictions.isEmpty()) {
                    Log.e(APP_TAG,"no results from api");
                    progressDialog.dismiss();
                    return;
                }

                // Upload to google storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference photoRef = storageRef.child("images").child(photoFileName);

                byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(MainActivity.this, takenPhotoUri, 25);
                assert imageBytes != null;

                UploadTask uploadTask = photoRef.putBytes(imageBytes);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Log.e(APP_TAG, "could not upload to google storage");

                        // Delete photo from external storage
                        File file = new File(takenPhotoUri.toString());
                        boolean deleted = file.delete();
                        if(deleted){
                            Log.i(APP_TAG, "uploaded photo removed from device");
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        fetchRecipes(downloadUrl, WordUtils.capitalize(predictions.get(0).data().get(0).name()));

                        // Delete photo from external storage
                        File file = new File(takenPhotoUri.toString());
                        boolean deleted = file.delete();
                        if(deleted){
                            Log.i(APP_TAG, "uploaded photo removed from device");
                        }
                    }
                });


            }
        }.execute();
    }


    private void fetchRecipes(final Uri photoUri, final String foodName) {
        RecipeAPI.getRecipeResponse(foodName, new RecipeAPI.JsonResponseListener() {
            @Override
            public void onRecipeResponse(RecipeResponse recipeResponse) {
                System.out.println(recipeResponse.getHits().get(0).getRecipe().getLabel());

                List<RecipeItem> recipeItemList = new ArrayList<RecipeItem>();

                for (Hit hit: recipeResponse.getHits())
                {
                    String label = hit.getRecipe().getLabel();
                    String url = hit.getRecipe().getUrl();
                    String imageUrl = hit.getRecipe().getImage();
                    List<String> healthLabels = hit.getRecipe().getHealthLabels();

                    recipeItemList.add(new RecipeItem(label, url, imageUrl, healthLabels));
                }

                String foodItemKey = foodName + System.currentTimeMillis();
                FoodItem foodItem = new FoodItem(foodItemKey, foodName,photoUri.toString(), recipeItemList);
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                DatabaseReference foodRef = database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FIREBASE_DB_KEY).child(foodItemKey);
                foodRef.setValue(foodItem);
            }
        });
    }

    private void openCamera() {
        // New photo
        photoFileName = "photo" + System.currentTimeMillis() + ".jpg";

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void openGallery() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            return FileProvider.getUriForFile(MainActivity.this, "com.aviato.fileprovider", file);
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            showLogin();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void recyclerViewListClicked(View v, int position, int callingView) {
        System.out.println(foodAdapter.getItem(position).foodName);

        Intent intent = new Intent(this, RecipesActivity.class);
        intent.putExtra("food_object", foodAdapter.getItem(position));
        startActivity(intent);

    }
}
