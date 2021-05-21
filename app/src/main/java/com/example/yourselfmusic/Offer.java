package com.example.yourselfmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Offer extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fs;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        recyclerView = findViewById(R.id.recylelist);
        fs = FirebaseFirestore.getInstance();
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

        Query query = fs.collection("TrendingPage");
        FirestoreRecyclerOptions<Trending> options = new FirestoreRecyclerOptions.Builder<Trending>().setQuery(query,Trending.class).build();
        adapter = new FirestoreRecyclerAdapter<Trending, TrendingViewHolder>(options) {
            @NonNull
            @Override
            public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offerstyle,parent,false);
                return new TrendingViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TrendingViewHolder holder, int position, @NonNull Trending model) {
                holder.name.setText(model.getStatus_name());
                holder.artist.setText(model.getStatus_artist());
                Glide.with(Offer.this).load(model.getStatus_thumbnail()).into(holder.imageView);
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeActivity(model.getStatus_name(),model.getStatus_artist(),model.getStatus_download_link(),model.getStatus_timestamp(),model.getStatus_video_url());
                    }
                });
                holder.b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeTrendingPage(model.getStatus_name());
                    }
                });

            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Offer.this));
        recyclerView.setAdapter(adapter);
    }

    private void removeTrendingPage(String status_name) {
        DocumentReference doc = fs.collection("TrendingPage").document(status_name);
        doc.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Offer.this, "successfully remove from trending section", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Offer.this, "Failed to remove", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void changeActivity(String status_name, String status_artist, String status_download_link, String status_timestamp, String status_video_url) {
        Intent intent = new Intent(Offer.this,Second.class);
        intent.putExtra("name",status_name);
        intent.putExtra("artist",status_artist);
        intent.putExtra("download",status_download_link);
        intent.putExtra("timestamp",status_timestamp);
        intent.putExtra("video",status_video_url);
        startActivity(intent);
    }

    class TrendingViewHolder extends RecyclerView.ViewHolder {
        TextView name,artist;
        ImageView imageView;
        CardView card;
        Button b1;
        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.offerName);
            artist = itemView.findViewById(R.id.offerLink);
            imageView = itemView.findViewById(R.id.offerImage);
            card = itemView.findViewById(R.id.card);
            b1 = itemView.findViewById(R.id.removeTrending);
        }
    }

    public void allStatus(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    public void allOffers(View view){
        recreate();
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
