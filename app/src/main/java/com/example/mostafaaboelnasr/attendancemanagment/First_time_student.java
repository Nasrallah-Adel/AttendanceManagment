package com.example.mostafaaboelnasr.attendancemanagment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class First_time_student extends AppCompatActivity {
    DatabaseReference reference;
    private StorageReference mStorageRef;
    private StorageReference imgRef;
    ProgressBar loading;
    FirebaseStorage storage;
    ImageView im;
    Button bt1, bt2;

    FirebaseDatabase mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_student);
        bt1 = findViewById(R.id.take);
        bt2 = findViewById(R.id.save_identity);
        im = findViewById(R.id.imageView2);
        loading = findViewById(R.id.loading);
        storage = FirebaseStorage.getInstance("gs://attendance-managment-c079f.appspot.com");
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance("https://attendance-managment-c079f.firebaseio.com/");
        mStorageRef = storage.getReference();

        imgRef = mStorageRef.child("students");


    }

    /**
     * here to init the views from the xml
     */

    public void take(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 100);


    }

    Bitmap ib;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            ib = (Bitmap) data.getExtras().get("data");
            im.setImageBitmap(ib);
        }
    }

    public void add_stu(View view) {

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
                Toast.makeText(First_time_student.this, "حدث خطأ", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                String imageUrl = taskSnapshot.getDownloadUrl().toString();
                try {
                    sendReportToDatabase(imageUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * here to add the data of the report to the database after uploaded the image to the storage
     *
     * @param image
     */
    private void sendReportToDatabase(String image) throws JSONException {


        //set the criminal data;
        StudentModel criminal = new StudentModel();

        criminal.setId("26");

        criminal.setName("Nasrallah");


        criminal.setMobDNA(image);

        //added to the api to enroll
        enrollMethod(criminal);

    }


    /**
     * to get ids for the firebase
     *
     * @return random string
     */
    protected String random() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    /**
     * hena 3ashan e3mel request
     *
     * @throws JSONException
     */
    private void enrollMethod(final StudentModel criminal) throws JSONException {


        //the return type
        //url

        String url = "https://api.kairos.com/enroll";

        //body to send in the request
        JSONObject content_json_object = new JSONObject();
        try {
            content_json_object.put("image", criminal.getMobDNA());
            content_json_object.put("subject_id", criminal.getId());
            content_json_object.put("gallery_name", "Students");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                content_json_object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("responce voooo "+response.toString());
                Log.d("volleey", response.toString());
                if (response.has("face_id")) {
                    reference = FirebaseDatabase.getInstance().getReference().child("University")
                            .child("Faculty").child("face").push();
                    reference.setValue(criminal);
                    loading.setVisibility(View.GONE);
                    Toast.makeText(First_time_student.this, "شكرا تمت الإضافة بنجاح", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(First_time_student.this, "حدث خطأ برجاء المحاوله مره اخري", Toast.LENGTH_LONG).show();

                }
                loading.setVisibility(View.GONE);
//                try {
//                    activity.onRequestServed(response, code);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.setVisibility(View.GONE);
//                VolleyLog.d(tag, "Error: " + error.getMessage());
                Log.e("ONerrorResponse", "Site Info Error: " + error.getMessage());
                Toast.makeText(First_time_student.this, "حدث خطأ برجاء المحاوله مره اخري", Toast.LENGTH_LONG).show();

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
}

