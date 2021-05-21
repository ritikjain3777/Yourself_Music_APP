package com.example.yourselfmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddOffer extends AppCompatActivity {
    ImageView imageView;
    FirebaseFirestore fs;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    Uri imageuri;
    Boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        imageView = findViewById(R.id.selectImage);
        fs = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        Button b = findViewById(R.id.submit);
        EditText name = findViewById(R.id.oName);
        EditText price = findViewById(R.id.oPrice);
        EditText link = findViewById(R.id.oLink);

        TextView home = findViewById(R.id.homebtn);
        ImageView hi = findViewById(R.id.homeIcon);
        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkfield(name);
                checkfield(price);
                checkfield(link);
                if (valid){
                    DocumentReference doc = fs.collection("Offer").document(name.getText().toString());
                    Map<String,String> info = new HashMap<>();
                    info.put("offer_name",name.getText().toString());
                    info.put("offer_price",price.getText().toString());
                    info.put("offer_link",link.getText().toString());
                    info.put("offer_image","https://firebasestorage.googleapis.com/v0/b/yourself-ec711.appspot.com/o/offerImages%2Fa017a319-4e5c-47d8-81cf-d63d1af8a9c1?alt=media&token=66208fe3-0bbf-4b66-9398-2917eb271f62");
                    doc.set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddOffer.this, "Offer Added Successfully", Toast.LENGTH_SHORT).show();
                            name.setText("");
                            price.setText("");
                            link.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddOffer.this, "Failed To Add", Toast.LENGTH_SHORT).show();
                            name.setText("");
                            price.setText("");
                            link.setText("");

                        }
                    });

                }
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode==RESULT_OK && data != null && data.getData() != null){
            imageuri = data.getData();
            imageView.setImageURI(imageuri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final String Random = UUID.randomUUID().toString();
        final ProgressDialog pd = new ProgressDialog(AddOffer.this);
        pd.setTitle("Uploading");
        pd.show();
        StorageReference ref = storageReference.child("offerImages/"+Random);
        ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                savingData(Random);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddOffer.this, "Failed to upload a file", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double percent = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                pd.setMessage("Percentage "+(int)percent+" %");
            }
        });
    }

    private void savingData(String random) {
        EditText name = findViewById(R.id.oName);
        EditText price = findViewById(R.id.oPrice);
        EditText link = findViewById(R.id.oLink);
        Button b = findViewById(R.id.submit);
        StorageReference ref = storageReference.child("offerImages/"+random);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final String url = uri.toString();
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkfield(name);
                        checkfield(price);
                        checkfield(link);
                        if(valid){
                            DocumentReference doc = fs.collection("Offer").document(name.getText().toString());
                            Map<String,String> info = new HashMap<>();
                            info.put("offer_name",name.getText().toString());
                            info.put("offer_price",price.getText().toString());
                            info.put("offer_link",link.getText().toString());
                            info.put("offer_image",url);
                            doc.set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddOffer.this, "Offer Added Successfully", Toast.LENGTH_SHORT).show();
                                    name.setText("");
                                    price.setText("");
                                    link.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddOffer.this, "Failed To Add", Toast.LENGTH_SHORT).show();
                                    name.setText("");
                                    price.setText("");
                                    link.setText("");
                                }
                            });
                        }

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddOffer.this, "Failed To Store Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean checkfield(EditText textfield){
        if(textfield.getText().toString().isEmpty()){
            textfield.setError("error");
            Toast.makeText(this, "fill all the entries", Toast.LENGTH_SHORT).show();
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    }

    public void allStatus(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    public void allOffers(View view){
        startActivity(new Intent(getApplicationContext(),Offer.class));
    }
    public void AddNewStatus(View view){
        startActivity(new Intent(getApplicationContext(),AddStatus.class));
    }
    public void addOffer(View view){
        recreate();
    }
    public void checkAllOffer(View view){
        startActivity(new Intent(getApplicationContext(),Third.class));
    }
}
