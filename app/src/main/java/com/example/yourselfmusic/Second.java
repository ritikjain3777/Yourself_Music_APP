package com.example.yourselfmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Second extends AppCompatActivity {
    ProgressBar progressBar = null;
    VideoView v;
    FirebaseFirestore firebaseFirestore;
    TextView name,artist,timestamp;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        name = findViewById(R.id.name);
        artist = findViewById(R.id.artist);
        timestamp = findViewById(R.id.timestamp);
        v =findViewById(R.id.videoView);
        TextView home = findViewById(R.id.homebtn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ImageView hi = findViewById(R.id.homeIcon);
        progressBar = findViewById(R.id.progressbar);
        fauth = FirebaseAuth.getInstance();

        if(fauth.getCurrentUser() != null){
            if (fauth.getCurrentUser().getEmail().equals("sankalpjangid667@gmail.com") || fauth.getCurrentUser().getEmail().equals("ritikjain3777@gmail.com")){
                findViewById(R.id.bottomAdmin).setVisibility(View.VISIBLE);
                findViewById(R.id.bottomUser).setVisibility(View.GONE);
            }
            else{
                findViewById(R.id.bottomUser).setVisibility(View.VISIBLE);
                findViewById(R.id.bottomAdmin).setVisibility(View.GONE);
            }
        }else{
            findViewById(R.id.bottomUser).setVisibility(View.VISIBLE);
            findViewById(R.id.bottomAdmin).setVisibility(View.GONE);
        }

        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fauth.getCurrentUser() != null) {
                    if (fauth.getCurrentUser().getEmail().equals("sankalpjangid667@gmail.com") || fauth.getCurrentUser().getEmail().equals("ritikjain3777@gmail.com")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), UserMainView.class));
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), UserMainView.class));
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fauth.getCurrentUser() != null) {
                    if (fauth.getCurrentUser().getEmail().equals("sankalpjangid667@gmail.com") || fauth.getCurrentUser().getEmail().equals("ritikjain3777@gmail.com")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), UserMainView.class));
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), UserMainView.class));
                }
            }
        });

        String videourl = getIntent().getStringExtra("video");
        String sname = getIntent().getStringExtra("name");
        String sartist = getIntent().getStringExtra("artist");
        String stimestamp = getIntent().getStringExtra("timestamp");
        name.setText(sname);
        artist.setText(sartist);
        timestamp.setText(stimestamp);

        v.setVideoPath(videourl);
        MediaController mediaController = new MediaController(Second.this);
        v.setMediaController(mediaController);
        mediaController.setAnchorView(v);
        v.start();
        progressBar.setVisibility(View.VISIBLE);
        v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        progressBar.setVisibility(View.GONE);
                        mediaPlayer.start();
                    }
                });
            }
        });

    }

    public void download(View view){
        String download_link = getIntent().getStringExtra("download");
        String filename = getIntent().getStringExtra("name")+".mp4";
        System.out.println(filename);
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(download_link));
        //startActivity(browserIntent);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(Second.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                downloadFile(filename,download_link);
            }else{
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }

        }else{
            downloadFile(filename,download_link);

        }
    }

    private void downloadFile(String filename, String download_link) {
        Uri downloaduri = Uri.parse(download_link);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        try {
            if (downloadManager != null){
                DownloadManager.Request request = new DownloadManager.Request(downloaduri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI).setTitle(filename).setDescription("Downloading "+filename)
                        .setAllowedOverMetered(true).setAllowedOverRoaming(true).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename).setMimeType(getMimeType(downloaduri));
                request.allowScanningByMediaScanner();
                downloadManager.enqueue(request);
                Toast.makeText(Second.this, "Download Started", Toast.LENGTH_SHORT).show();

            }else{
                Intent intent = new Intent(Intent.ACTION_VIEW,downloaduri);
                startActivity(intent);
            }

        }catch (Exception e){
            Toast.makeText(Second.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String download_link = getIntent().getStringExtra("download");
                String filename = getIntent().getStringExtra("name");
                downloadFile(filename,download_link);
            }
            else{
                Toast.makeText(Second.this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getMimeType(Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
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
        startActivity(new Intent(getApplicationContext(),Third.class));
    }
    public void allStatusUser(View view){
        startActivity(new Intent(getApplicationContext(),UserMainView.class));
    }
    public void TrendingUser(View view){
        startActivity(new Intent(getApplicationContext(),UserTrendingPage.class));
    }
    public void OfferUser(View view){
        startActivity(new Intent(getApplicationContext(),UserOfferView.class));
    }
}
