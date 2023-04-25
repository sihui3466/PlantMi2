package com.example.plantmi;

import static com.example.plantmi.LocalDataAdd.historyDataSourceMoisture;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlantProfilePage extends AppCompatActivity {
    private View plantStatus;
    private FirebaseAdd firebaseAdd;
    private LocalDataAdd localDataAdd;
    
    Button openCamera, waterBtn, logoutBtn, galleryBtn;
    ImageView imageView;
    TextView username, userEmail;
    ImageButton editUser;
    FirebaseAuth auth;
    DatabaseReference rootDatabaseReference, nameRootDatabaseReference;
    SensorSoil sensorSoil;
    SensorLight sensorLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantprofile);
       
        waterBtn = findViewById(R.id.waterButton);
        galleryBtn = findViewById(R.id.GalleryButton);
        logoutBtn = findViewById(R.id.logout);
        imageView = findViewById(R.id.capturedImage);
        plantStatus = findViewById(R.id.plantstatus);
        editUser = findViewById(R.id.edituser);
        auth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        userEmail = findViewById(R.id.user_email);
        
        //Firebase 
        FirebaseUser currentUser = auth.getCurrentUser();
        String userUID = currentUser.getUid();
        nameRootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("username");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        userEmail.setText(currentUser.getEmail().toString());

        // Check if user is logged in
        if (currentUser != null) { 
            // User signed in successfully, check if they have a picture stored in Firebase Storage
            String uid = currentUser.getUid();
            StorageReference photoRef = storageRef.child("images/" + currentUser.getEmail() + "/" + uid);

            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Check if the image belongs to the current user
                    String filename = uri.getLastPathSegment();
                    if (filename != null && filename.contains(uid)) {
                        // Picture found, load it into the ImageView
                        Picasso.get().load(uri).into(imageView);
                    } else {
                        // Image does not belong to current user, do nothing
                        Toast.makeText(PlantProfilePage.this, "Image Unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Profile picture not found, do nothing
                    Toast.makeText(PlantProfilePage.this, "Image retrieve unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
        
        nameRootDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String data = snapshot.getValue().toString();
                    username.setText(data);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        // to go from PlantProfilePage to EditUser to edit user details
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantProfilePage.this, EditUser.class);
                startActivity(intent);
                finish();
            }
        });

        // to go from PlantProfilePage to Watering Plant activity
        waterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantProfilePage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // to get image from gallery
        final ActivityResultLauncher<Intent> launcherGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri photoUri = result.getData().getData();
                            StorageReference storageRef=FirebaseStorage.getInstance().getReference();
                            //Create reference of img file in firebase storage
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            StorageReference photoRef = storageRef.child("images/"+currentUser.getEmail()+"/"+ uid);

                            //Upload image to firebase storage
                            //convert image data into byte array
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(photoUri);
                                byte[] yourPhotoToByteArray = getBytes(inputStream);

                                UploadTask uploadTask = photoRef.putBytes(yourPhotoToByteArray);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Image uploaded successfully, load it into the ImageView
                                        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Picasso.get().load(uri).into(imageView);
                                                Toast.makeText(PlantProfilePage.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PlantProfilePage.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    public byte[] getBytes(InputStream inputStream) throws IOException {
                        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                        int bufferSize = 1024;
                        byte[] buffer = new byte[bufferSize];

                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            byteBuffer.write(buffer, 0, len);
                        }

                        return byteBuffer.toByteArray();
                    }
                    }
        );
        
        //open gallery action
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcherGallery.launch(intent);
            }
        });

        //logout button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PlantProfilePage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

        // swipe up to go from PlantProfilePage to PlantStatus
        plantStatus.setOnTouchListener(new OnSwipeTouchListener(PlantProfilePage.this) {
            public void onSwipeTop() {
                Intent i = new Intent(PlantProfilePage.this, PlantStatus.class);
                startActivity(i);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });

        rootDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // display toast if plant moisture level is <= 20% to remind user to water plant
        rootDatabaseReference.child("sensor_soil").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase", "Error in getting Moisture Level data", task.getException());
                }
                else {
                    sensorSoil = task.getResult().getValue(SensorSoil.class);
                    Log.d("Firebase", sensorSoil.getValue().toString());
                    double d = Double.parseDouble(sensorSoil.getValue().toString());
                    double value = Math.round( (100 - ((d/4095)*100)) );
                    if (value <= 20) {
                        Toast.makeText(PlantProfilePage.this, "Remember to water mi!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        rootDatabaseReference.child("sensor_light").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase", "Error in getting Moisture Level data", task.getException());
                }
                else {
                    sensorLight = task.getResult().getValue(SensorLight.class);
                    Log.d("Firebase", sensorLight.getValue().toString());
                    String value = sensorLight.getValue().toString();
                }
            }
        });

        // to store in firebase the current plant status upon opening of app
        firebaseAdd = new FirebaseAdd();

        // to get history of plant status data and store in local arraylist to be displayed using recyclerview in history activities
        localDataAdd = new LocalDataAdd();

    }

    // to remove history stored in local arraylist when app is closed (to refresh data displayed in recyclerview)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        historyDataSourceMoisture.clearHistory();
    }
}


