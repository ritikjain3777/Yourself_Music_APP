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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserTrendingPage extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fs;
    FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trending_page);
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
        adapter = new FirestoreRecyclerAdapter<Trending, TrendingsViewHolder>(options) {
            @NonNull
            @Override
            public TrendingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_trending_layout,parent,false);
                return new TrendingsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TrendingsViewHolder holder, int position, @NonNull Trending model) {
                holder.name.setText(model.getStatus_name());
                holder.artist.setText(model.getStatus_artist());
                Glide.with(UserTrendingPage.this).load(model.getStatus_thumbnail()).into(holder.imageView);
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeActivity(model.getStatus_name(),model.getStatus_artist(),model.getStatus_download_link(),model.getStatus_timestamp(),model.getStatus_video_url());
                    }
                });
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserTrendingPage.this));
        recyclerView.setAdapter(adapter);
    }

    private void changeActivity(String status_name, String status_artist, String status_download_link, String status_timestamp, String status_video_url) {
        Intent intent = new Intent(UserTrendingPage.this,Second.class);
        intent.putExtra("name",status_name);
        intent.putExtra("artist",status_artist);
        intent.putExtra("download",status_download_link);
        intent.putExtra("timestamp",status_timestamp);
        intent.putExtra("video",status_video_url);
        startActivity(intent);
    }

    class TrendingsViewHolder extends RecyclerView.ViewHolder {
        TextView name,artist;
        ImageView imageView;
        CardView card;
        public TrendingsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.offerName);
            artist = itemView.findViewById(R.id.offerLink);
            imageView = itemView.findViewById(R.id.offerImage);
            card = itemView.findViewById(R.id.card);
        }
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
    public void allStatusUser(View view){
        startActivity(new Intent(getApplicationContext(),UserMainView.class));
    }
    public void TrendingUser(View view){
        recreate();
    }
    public void OfferUser(View view){
        startActivity(new Intent(getApplicationContext(),UserOfferView.class));
    }
}
