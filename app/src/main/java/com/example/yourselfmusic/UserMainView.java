package com.example.yourselfmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserMainView extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter1;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_view);
        recyclerView = findViewById(R.id.recylelist);
        TextView home = findViewById(R.id.homebtn);
        ImageView hi = findViewById(R.id.homeIcon);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView login = findViewById(R.id.forLogin);
        fauth = FirebaseAuth.getInstance();
        //System.out.println(fauth.getCurrentUser().getEmail());
        if (fauth.getCurrentUser() != null){
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    recreate();
                }
            });
        }else{
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(UserMainView.this);
                    ad.setTitle("Login");
                    LinearLayout linearLayout = new LinearLayout(UserMainView.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final EditText input = new EditText(UserMainView.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Username");
                    linearLayout.addView(input);
                    final EditText pass = new EditText(UserMainView.this);
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass.setHint("password");
                    linearLayout.addView(pass);
                    linearLayout.setPadding(30,0,30,0);
                    ad.setView(linearLayout);
                    ad.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            fauth.signInWithEmailAndPassword(input.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    System.out.println(authResult.getUser().getEmail());
                                    if (authResult.getUser().getEmail().equals("sankalpjangid667@gmail.com") || authResult.getUser().getEmail().equals("ritikjain3777@gmail.com")){
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        Toast.makeText(UserMainView.this, "done", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(UserMainView.this, "not done", Toast.LENGTH_SHORT).show();
                                        recreate();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserMainView.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog a = ad.create();
                    a.show();


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
        adapter1 = new FirestoreRecyclerAdapter<MainInfo, MainsInfoViewHolder>(options) {
            @NonNull
            @Override
            public MainsInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_main_design,parent,false);
                return new MainsInfoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainsInfoViewHolder holder, int position, @NonNull MainInfo model) {
                holder.name.setText(model.getStatus_name());
                holder.artist.setText(model.getStatus_artist());
                Glide.with(UserMainView.this).load(model.getStatus_thumbnail()).into(holder.thumbnail);
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeActivity(model.getStatus_name(),model.getStatus_artist(),model.getStatus_download_link(),model.getStatus_timestamp(),model.getStatus_video_url());
                    }
                });
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserMainView.this));
        recyclerView.setAdapter(adapter1);

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

    private void changeActivity(String status_name, String status_artist, String status_download_link, String status_timestamp, String status_video_url) {
        Intent intent = new Intent(UserMainView.this,Second.class);
        intent.putExtra("name",status_name);
        intent.putExtra("artist",status_artist);
        intent.putExtra("download",status_download_link);
        intent.putExtra("timestamp",status_timestamp);
        intent.putExtra("video",status_video_url);
        startActivity(intent);
    }

    class MainsInfoViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView name,artist;
        CardView card;

        public MainsInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.status_name);
            artist = itemView.findViewById(R.id.status_artist);
            card = itemView.findViewById(R.id.card);
        }
    }

    public void allStatusUser(View view){
        recreate();
    }
    public void TrendingUser(View view){
        startActivity(new Intent(getApplicationContext(),UserTrendingPage.class));
    }
    public void OfferUser(View view){
        startActivity(new Intent(getApplicationContext(),UserOfferView.class));
    }
}
