package com.example.yourselfmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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

public class UserOfferView extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_offer_view);
        recyclerView = findViewById(R.id.recylelist);
        firebaseFirestore = FirebaseFirestore.getInstance();

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

        Query query = firebaseFirestore.collection("Offer");
        FirestoreRecyclerOptions<AllOffer> options = new FirestoreRecyclerOptions.Builder<AllOffer>().setQuery(query,AllOffer.class).build();
        adapter = new FirestoreRecyclerAdapter<AllOffer, AllOffersViewHolder>(options) {
            @NonNull
            @Override
            public AllOffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_offer_design,parent,false);
                return new AllOffersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllOffersViewHolder holder, int position, @NonNull AllOffer model) {
                holder.t1.setText(model.getOffer_name());
                holder.t2.setText(model.getOffer_price());
                Glide.with(UserOfferView.this).load(model.getOffer_image()).into(holder.i1);
                holder.t3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startNewActivity(model.getOffer_link());
                    }
                });
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserOfferView.this));
        recyclerView.setAdapter(adapter);
    }

    private void startNewActivity(String offer_link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(offer_link));
        startActivity(browserIntent);
    }

    class AllOffersViewHolder extends RecyclerView.ViewHolder {
        ImageView i1;
        TextView t1,t2,t3;
        public AllOffersViewHolder(@NonNull View itemView) {
            super(itemView);
            i1 = itemView.findViewById(R.id.offerImage);
            t1 = itemView.findViewById(R.id.offerName);
            t2 = itemView.findViewById(R.id.offerPrice);
            t3 = itemView.findViewById(R.id.offerLink);
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
        startActivity(new Intent(getApplicationContext(),UserTrendingPage.class));
    }
    public void OfferUser(View view){
        recreate();
    }
}
