package com.example.rentacar.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rentacar.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class AdminAddCar extends AppCompatActivity {

    private String categoryName, descriptionString, priceString, titleString, modelType, marca, carTip, downloadImageUrl;
    EditText carModel, title, description, price, carType, carMarca;
    ImageView carPhoto;
    Button addCar;
    private static final int CODE = 1;
    private Uri imageUri;
    private StorageReference carImageRef;
    private DatabaseReference carRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_car);

        categoryName = getIntent().getExtras().get("categorie").toString();
        carImageRef = FirebaseStorage.getInstance().getReference().child("Car images");
        carRef = FirebaseDatabase.getInstance().getReference().child("CARS");
        carMarca = (EditText) findViewById(R.id.carMarcaEditText);
        carType = (EditText) findViewById(R.id.carTipEditText);
        carModel = (EditText) findViewById(R.id.carModelEditText);
        title = (EditText) findViewById(R.id.titleEditText);
        description = (EditText) findViewById(R.id.descriptionEditText);
        price = (EditText) findViewById(R.id.priceEditText);
        carPhoto = (ImageView) findViewById(R.id.car_add_image);
        addCar = (Button) findViewById(R.id.addCarButton);
        loadingBar = new ProgressDialog(this);

        carPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateCarData();
            }
        });
    }


    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE && resultCode == RESULT_OK && data != null)
        {
            imageUri = data.getData();
            carPhoto.setImageURI(imageUri);
        }
    }

    private void ValidateCarData()
    {
        titleString = title.getText().toString();
        descriptionString = description.getText().toString();
        priceString = price.getText().toString();
        modelType = carModel.getText().toString();
        carTip = carType.getText().toString();
        marca = carMarca.getText().toString();

        if (imageUri == null){
            Toast.makeText(this,"Selecteaza o fotografie", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(titleString))
        {
            Toast.makeText(this,"Completeaza toate campurile", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(descriptionString))
        {
            Toast.makeText(this,"Completeaza toate campurile", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(priceString))
        {
            Toast.makeText(this,"Completeaza toate campurile", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(modelType))
        {
            Toast.makeText(this,"Completeaza toate campurile", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadCarInfos();
        }
    }

    public void uploadCarInfos()
    {

        loadingBar.setTitle("Adaugare autovehicul...");
        loadingBar.setMessage("Te rugam asteapta ca autovehiculul sa fie adaugat");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final StorageReference photoPath = carImageRef.child(imageUri.getLastPathSegment() + marca+modelType+carTip + ".jpg");

        final UploadTask uploadTask = photoPath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errorMessage = e.toString();
               Toast.makeText(AdminAddCar.this,"Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddCar.this,"Imagine salvata cu succes", Toast.LENGTH_SHORT).show();

                Task <Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            loadingBar.dismiss();
                            throw task.getException();
                        }
                        downloadImageUrl = photoPath.toString();
                        return photoPath.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddCar.this,"Url-ul imaginii obtinut cu succes",Toast.LENGTH_SHORT);
                            SaveCarToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveCarToDatabase(){

        /*carRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!((dataSnapshot.child("Users").child(username).exists()) && (dataSnapshot.child("Users").child(email).exists())))
                {
                    HashMap<String,Object> usersMap = new HashMap<>();
                    String  uniqueID = UUID.randomUUID().toString();
                    final String code = UUID.randomUUID().toString().substring(0,6);
                    Boolean banned = false;
                    Boolean isVerified = false;*/
        carRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("CARS").child(titleString).exists()))
                {
                    HashMap<String,Object> carMap = new HashMap<>();
                    carMap.put("marca",marca);
                    carMap.put("model",modelType);
                    carMap.put("tip",carTip);
                    carMap.put("title", titleString);
                    carMap.put("description",descriptionString);
                    carMap.put("price",priceString);
                    carMap.put("category",categoryName);
                    carMap.put("image",downloadImageUrl);

                    carRef.child(titleString).updateChildren(carMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(AdminAddCar.this,"Autovehicul adaugat cu succes!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String errorMessage = e.toString();
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddCar.this, "Error: " + errorMessage,Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    Toast.makeText(AdminAddCar.this, "Masina aceasta exista deja", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
