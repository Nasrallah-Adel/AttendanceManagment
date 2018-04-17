package com.example.mostafaaboelnasr.attendancemanagment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels.StudentModel;
import com.example.mostafaaboelnasr.attendancemanagment.utils.VolleySingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecognizeActivity extends AppCompatActivity {
    ProgressBar loading;
    FirebaseStorage storage;
    ImageView im;
    Button bt1, bt2;
    private StorageReference mStorageRef;
    private StorageReference imgRef;
    FirebaseDatabase mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize);
        bt1 = findViewById(R.id.tak);
        bt2 = findViewById(R.id.chec);
        im = findViewById(R.id.imm);
        loading = findViewById(R.id.loading);
        storage = FirebaseStorage.getInstance("gs://attendance-managment-c079f.appspot.com");
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance("https://attendance-managment-c079f.firebaseio.com/");
        mStorageRef = storage.getReference();

        imgRef = mStorageRef.child("temp");
    }

    public void take(View v) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 100);


    }

    Bitmap ib;
    Uri tempUri;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            ib = (Bitmap) data.getExtras().get("data");
            im.setImageBitmap(ib);
        }
        tempUri = getImageUri(getApplicationContext(), ib);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void check(View view) {

        loading.setVisibility(View.VISIBLE);

        StorageReference tripsRef = imgRef.child(26 + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ib.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = tripsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(RecognizeActivity.this, "حدث خطأ", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Toast.makeText(RecognizeActivity.this, "ok", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
                String imageUrl = taskSnapshot.getDownloadUrl().toString();
                try {
                    RecognizeMethod(imageUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void RecognizeMethod(String im_url) throws JSONException {


        loading.setVisibility(View.VISIBLE);

        String url = "https://api.kairos.com/recognize";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ib.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //body to send in the request
        JSONObject content_json_object = new JSONObject();
        try {
            content_json_object.put("image", im_url);
            content_json_object.put("gallery_name", "students");
            content_json_object.put("threshold", "70%");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                content_json_object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("vooooo2222", response.toString());
                try {
                    ExtractJson(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
//                VolleyLog.d(tag, "Error: " + error.getMessage());
                Log.e("vo err", "Site Info Error: " + error.getMessage());
                Toast.makeText(RecognizeActivity.this, "حدث خطأ", Toast.LENGTH_LONG).show();
                finish();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("app_id", "623be28d");
                headers.put("app_key", "11f606af354ab90750553b704ad86caa");
                return headers;
            }
        };


        jsObjRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(this).addRequestQue(jsObjRequest);

    }


//    {
//        "images": [
//        {
//            "candidates": [
//            {
//                "confidence": 0.9950643,
//                    "enrollment_timestamp": "1518011313361",
//                    "face_id": "f8dd7530cdb848ba8f2",
//                    "subject_id": "Elizabeth"
//            }
//      ],

    /**
     * here to get the data from the api and get it from the firebase
     */
    private void ExtractJson(JSONObject response) throws JSONException {

        JSONArray images = response.getJSONArray("images");

        JSONObject objectImages = images.getJSONObject(0);

        JSONArray candidates = null;
        if (objectImages.has("candidates")) {
            candidates = objectImages.getJSONArray("candidates");


            Log.d("cand", "------------->>>>>>>>>" + candidates.toString());
        }

        if (candidates == null) {

            Toast.makeText(RecognizeActivity.this, "لا يوجد تطابق ", Toast.LENGTH_LONG).show();
            loading.setVisibility(View.GONE);
            return;
        }

        //the ids of the criminals
        ArrayList<String> criminalsId = new ArrayList<>();

        for (int i = 0; i < candidates.length(); i++) {

            JSONObject objectCandidates = candidates.getJSONObject(i);

            criminalsId.add(objectCandidates.getString("subject_id"));

            Toast.makeText(RecognizeActivity.this, objectCandidates.getString("subject_id"), Toast.LENGTH_LONG).show();

        }


        if (criminalsId.size() != 0) {
        }
        //     getCriminals(criminalsId);
        else
            Toast.makeText(RecognizeActivity.this, "لا يوجد تطابق ", Toast.LENGTH_LONG).show();


    }

    ArrayList<StudentModel> crimeData = new ArrayList<>();

    private void getCriminals(final ArrayList<String> criminalsId) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Query query = myRef.child("face");

        crimeData.clear();

        Log.d("zlzl", "-------------------------------");

        for (int i = 0; i < criminalsId.size(); i++) {
            Log.d("loop id :", criminalsId.get(i));
        }

        Log.d("after loop", "-------------------------------");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                loading.setVisibility(View.GONE);
                StudentModel report = dataSnapshot.getValue(StudentModel.class);

                for (int i = 0; i < criminalsId.size(); i++) {
                    if (criminalsId.get(i).equals(report.getId())) {
                        crimeData.add(0, report);
                        //             adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
            }

        });
    }
}
