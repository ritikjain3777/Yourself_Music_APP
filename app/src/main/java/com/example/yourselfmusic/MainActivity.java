package com.example.yourselfmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter1;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recylelist);
        TextView home = findViewById(R.id.homebtn);
        ImageView hi = findViewById(R.id.homeIcon);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView login = findViewById(R.id.forLogin);
        fauth = FirebaseAuth.getInstance();

        if (fauth.getCurrentUser() != null){
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),UserMainView.class));
                }
            });
        }

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
        Query query = firebaseFirestore.collection("all_status_info");
        FirestoreRecyclerOptions<MainInfo> options = new FirestoreRecyclerOptions.Builder<MainInfo>().setQuery(query,MainInfo.class).build();
        adapter1 = new FirestoreRecyclerAdapter<MainInfo, MainInfoViewHolder>(options){

            @NonNull
            @Override
            public MainInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design,parent,false);
                return new MainInfoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainInfoViewHolder holder, int position, @NonNull MainInfo model) {
                holder.name.setText(model.getStatus_name());
                holder.artist.setText(model.getStatus_artist());
                Glide.with(MainActivity.this).load(model.getStatus_thumbnail()).into(holder.thumbnail);
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeActivity(model.getStatus_name(),model.getStatus_artist(),model.getStatus_download_link(),model.getStatus_timestamp(),model.getStatus_video_url());
                    }
                });
                holder.b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addToTrendingPage(model.getStatus_name(),model.getStatus_artist(),model.getStatus_download_link(),model.getStatus_timestamp(),model.getStatus_video_url(),model.getStatus_thumbnail());
                    }
                });
                holder.b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeStatus(model.getStatus_name());
                    }
                });
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter1);

    }

    private void removeStatus(String status_name) {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("Remove Status");
        ad.setMessage("Are you sure you want to remove status");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DocumentReference doc = firebaseFirestore.collection("all_status_info").document(status_name);
                doc.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Remove Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed To Remove", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogInterface.dismiss();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog a = ad.create();
        a.show();

    }

    private void addToTrendingPage(String status_name, String status_artist, String status_download_link, String status_timestamp, String status_video_url, String status_thumbnail) {
        DocumentReference doc = firebaseFirestore.collection("TrendingPage").document(status_name);
        Map<String,String> info = new HashMap<>();
        info.put("status_name",status_name);
        info.put("status_artist",status_artist);
        info.put("status_download_link",status_download_link);
        info.put("status_timestamp",status_timestamp);
        info.put("status_thumbnail",status_thumbnail);
        info.put("status_video_url",status_video_url);
        doc.set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Successfully added to ternding section", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeActivity(String status_name, String status_artist, String status_download_link, String status_timestamp, String status_video_url) {
        Intent intent = new Intent(MainActivity.this,Second.class);
        intent.putExtra("name",status_name);
        intent.putExtra("artist",status_artist);
        intent.putExtra("download",status_download_link);
        intent.putExtra("timestamp",status_timestamp);
        intent.putExtra("video",status_video_url);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter1.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter1.stopListening();
    }

    class MainInfoViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView name,artist;
        CardView card;
        Button b1,b2;

        public MainInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.status_name);
            artist = itemView.findViewById(R.id.status_artist);
            card = itemView.findViewById(R.id.card);
            b1 = itemView.findViewById(R.id.trending);
            b2 = itemView.findViewById(R.id.remove);
        }
    }

    public void allStatus(View view){
        recreate();
    }
    public void allOffers(View view){
        startActivity(new Intent(getApplicationContext(),Offer.class));
    }
    public void AddNewStatus(View view){
        startActivity(new Intent(getApplicationContext(),AddStatus.class));
    }
    public void addOffer(View view){
        startActivity(new Intent(getApplicationContext(),AddOffer.class));
    }
    public void checkAllOffer(View view){
        startActivity(new Intent(getApplicationContext(),Third.class));
    }

}
