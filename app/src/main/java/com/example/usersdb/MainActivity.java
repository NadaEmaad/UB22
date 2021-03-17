package com.example.usersdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edUserName;
    EditText edUserNumber;
    EditText edUserAddress;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1;
        final ProgressDialog[] progressDialog = new ProgressDialog[1];

        edUserName = findViewById(R.id.userName);
        edUserNumber = findViewById(R.id.userNumber);
        edUserAddress = findViewById(R.id.userAddress);
        b1 = (Button) findViewById(R.id.saveToFirebase);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog[0] = new ProgressDialog(MainActivity.this);
                progressDialog[0].setMessage("Loading..."); // Setting Message
                progressDialog[0].setTitle("ProgressDialog"); // Setting Title
                progressDialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog[0].show(); // Display Progress Dialog
                progressDialog[0].setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog[0].dismiss();
                    }
                }).start();
            }
        });

    }





    public void saveToFirebase(View v) {
        String userName = edUserName.getText().toString();
        String userNumber = edUserNumber.getText().toString();
        String userAddress = edUserAddress.getText().toString();


        Map<String, Object> user = new HashMap<>();
        user.put("user_Name", userName);
        user.put("user_Number", userNumber);
        user.put("user_Address", userAddress);

// Add a new document with a generated ID
        db.collection("users").add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: "
                                + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });


        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }
}