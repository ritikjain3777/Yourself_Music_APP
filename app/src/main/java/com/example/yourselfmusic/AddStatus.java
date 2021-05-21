package com.example.yourselfmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddStatus extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    EditText name,artist,link;
    Button b1;
    public Uri imageuri;
    ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    Boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_status);
        firebaseFirestore = FirebaseFirestore.getInstance();
        name = findViewById(R.id.sName);
        artist =findViewById(R.id.sArtist);
        link = findViewById(R.id.vUrl);
        b1 = findViewById(R.id.submit);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        imageView = findViewById(R.id.selectImage);

        TextView home = findViewById(R.id.homebtn);
        ImageView hi = findViewById(R.id.homeIcon);
        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkfield(name);
                checkfield(artist);
                checkfield(link);
                if(valid){
                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                    String dateToStr = format.format(today);
                    System.out.println(dateToStr);
                    String video_url = "https://drive.google.com/uc?id="+link.getText().toString();
                    String download_link = "https://drive.google.com/uc?export=download&id="+link.getText().toString();
                    DocumentReference doc = firebaseFirestore.collection("all_status_info").document(name.getText().toString());
                    Map<String,String> info = new HashMap<>();
                    info.put("status_name",name.getText().toString());
                    info.put("status_artist",artist.getText().toString());
                    info.put("status_thumbnail","http://yourselfmusic.pythonanywhere.com//media/2.jpg");
                    info.put("status_timestamp",dateToStr);
                    info.put("status_video_url",video_url);
                    info.put("status_download_link",download_link);
                    doc.set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddStatus.this, "Successful Add A Status", Toast.LENGTH_SHORT).show();
                            name.setText("");
                            artist.setText("");
                            link.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddStatus.this, "Failed To Add", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
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
        if (requestCode==1 && resultCode==RESULT_OK && data != null && data.getData() != null){
            imageuri = data.getData();
            imageView.setImageURI(imageuri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final String Random = UUID.randomUUID().toString();
        final ProgressDialog pd = new ProgressDialog(AddStatus.this);
        pd.setTitle("uploading...");
        pd.show();
        System.out.println("good");
        StorageReference ref = storageReference.child("images/"+Random);
        ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                System.out.println("ok");
                forSaveData(Random);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("bad");
                pd.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double percent = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                System.out.println("kk");
                pd.setMessage("Percentage "+(int)percent+"%");

            }
        });
    }

    private void forSaveData(String random) {
        System.out.println(random);
        EditText name1 = findViewById(R.id.sName);
        EditText artist2 =findViewById(R.id.sArtist);
        EditText link3 = findViewById(R.id.vUrl);
        StorageReference reference = storageReference.child("images/"+random);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final String thumbnail = uri.toString();
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkfield(name1);
                        checkfield(artist2);
                        checkfield(link3);
                        if(valid){
                            Date today = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                            String dateToStr = format.format(today);
                            System.out.println(dateToStr);
                            String video_url = "https://drive.google.com/uc?id="+link.getText().toString();
                            String download_link = "https://drive.google.com/uc?export=download&id="+link.getText().toString();
                            DocumentReference doc = firebaseFirestore.collection("all_status_info").document(name.getText().toString());
                            Map<String,String> info = new HashMap<>();
                            info.put("status_name",name.getText().toString());
                            info.put("status_artist",artist.getText().toString());
                            info.put("status_thumbnail",thumbnail);
                            info.put("status_timestamp",dateToStr);
                            info.put("status_video_url",video_url);
                            info.put("status_download_link",download_link);
                            doc.set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddStatus.this, "Successful Add A Status", Toast.LENGTH_SHORT).show();
                                    name.setText("");
                                    artist.setText("");
                                    link.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddStatus.this, "Failed To Add", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });
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
        recreate();
    }
    public void addOffer(View view){
        startActivity(new Intent(getApplicationContext(),AddOffer.class));
    }
    public void checkAllOffer(View view){
        startActivity(new Intent(getApplicationContext(),Third.class));
    }
}
