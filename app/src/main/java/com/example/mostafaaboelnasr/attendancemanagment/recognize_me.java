package com.example.mostafaaboelnasr.attendancemanagment;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mostafaaboelnasr.attendancemanagment.utils.VolleySingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 4/18/18.
 */

public class recognize_me {
    private StorageReference mStorageRef;
    private StorageReference imgRef;
    FirebaseDatabase mFirebaseDatabaseReference;
    FirebaseStorage storage;
Bitmap ib;
    public recognize_me(Bitmap ibb) {
ib=ibb;
        storage = FirebaseStorage.getInstance("gs://attendance-managment-c079f.appspot.com");
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance("https://attendance-managment-c079f.firebaseio.com/");
        mStorageRef = storage.getReference();

        imgRef = mStorageRef.child("temp");
    }

    public void check(View view) {

        StorageReference tripsRef = imgRef.child(29 + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ib.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = tripsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

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

        //body to send in the request
        JSONObject content_json_object = new JSONObject();
        try {

            content_json_object.put("image", im_url);
            content_json_object.put("gallery_name", "student");

            System.out.println("jason content" + content_json_object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                content_json_object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("ressss " + response.toString());

                try {
                    ExtractJson(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                VolleyLog.d(tag, "Error: " + error.getMessage());
                Log.e("vo err", "Site Info Error: " + error.getMessage());

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
        System.out.println("jason arary " + response);
        JSONObject objectImages = images.getJSONObject(0);

        JSONObject candidates = null;
        if (objectImages.has("transaction")) {
            candidates = objectImages.getJSONObject("transaction");


            Log.d("cand", "------------->>>>>>>>>" + candidates.toString());
        }

        if (candidates == null) {

            Toast.makeText(RecognizeActivity.this, "لا يوجد تطابق ", Toast.LENGTH_LONG).show();
            loading.setVisibility(View.GONE);
            return;
        }

        //the ids of the criminals
        String stuId = "";
        double max_conf = 0.0;
        try {
            stuId = candidates.getString("subject_id");
            System.out.println(" idddddd : " + stuId);

            Toast.makeText(RecognizeActivity.this, "your id is : " + stuId, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            loading.setVisibility(View.GONE);

            Toast.makeText(RecognizeActivity.this, "not found" + stuId, Toast.LENGTH_LONG).show();

        }

        loading.setVisibility(View.GONE);


        if (!stuId.equals("")) {
            //   Toast.makeText(RecognizeActivity.this, "no id: "+stuId, Toast.LENGTH_LONG).show();

            System.out.println("Retrive ?????");
        }
        //     getstu(criminalsId);
        else
            Toast.makeText(RecognizeActivity.this, "لا يوجد تطابق ", Toast.LENGTH_LONG).show();


    }

}
