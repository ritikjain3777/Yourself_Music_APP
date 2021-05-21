package com.example.yourselfmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class Third extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
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
        adapter = new FirestoreRecyclerAdapter<AllOffer, AllOfferViewHolder>(options) {
            @NonNull
            @Override
            public AllOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allofferstyle,parent,false);
                return new AllOfferViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllOfferViewHolder holder, int position, @NonNull AllOffer model) {
                holder.t1.setText(model.getOffer_name());
                holder.t2.setText(model.getOffer_price());
                Glide.with(Third.this).load(model.getOffer_image()).into(holder.i1);
                holder.t3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startNewActivity(model.getOffer_link());
                    }
                });
                holder.b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeOffer(model.getOffer_name());
                    }
                });

            }

        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Third.this));
        recyclerView.setAdapter(adapter);
    }

    private void removeOffer(String offer_name) {
        AlertDialog.Builder ad = new AlertDialog.Builder(Third.this);
        ad.setTitle("Remove Offer");
        ad.setMessage("Are you sure you want to Remove Offer");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DocumentReference doc = firebaseFirestore.collection("Offer").document(offer_name);
                doc.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Third.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Third.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });
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

    private void startNewActivity(String offer_link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(offer_link));
        startActivity(browserIntent);
    }

    class AllOfferViewHolder extends RecyclerView.ViewHolder {
        ImageView i1;
        TextView t1,t2,t3;
        Button b;
        public AllOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            i1 = itemView.findViewById(R.id.offerImage);
            t1 = itemView.findViewById(R.id.offerName);
            t2 = itemView.findViewById(R.id.offerPrice);
            t3 = itemView.findViewById(R.id.offerLink);
            b = itemView.findViewById(R.id.removeOffer);
        }
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
        startActivity(new Intent(getApplicationContext(),AddOffer.class));

    }
    public void checkAllOffer(View view){
        recreate();
    }
}
