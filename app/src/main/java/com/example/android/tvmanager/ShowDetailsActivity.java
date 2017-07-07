package com.example.android.tvmanager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView showName;
    private TextView summary;
    private TextView premiered;
    private TextView status;
    private TextView rating;
    private TextView genres;
    private ImageView imageView;
    private ImageView favoriteButton;
    private Bitmap bitmap;
    private String search;
    private String url = "https://developer.android.com/_static/images/android/touchicon-180.png";
    public static final String SEARCH_TEXT = "search";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("shows");
    DatabaseReference userRef = database.getReference("users");
    DatabaseReference currentUserRef = userRef.child(User.getCurrentUser().getUid());
    final DatabaseReference favoriteShow = currentUserRef.child("favorite");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setTitleTextColor(Color.WHITE);


//        search = getIntent().getStringExtra(SEARCH_TEXT);
//        Log.e("SAERCH TEXT : ", search);

//        new GetSeriesInformation(search, ShowDetailsActivity.this).execute();
//        showName = (TextView) findViewById(R.id.showName);
        summary = (TextView) findViewById(R.id.summary);
        premiered = (TextView) findViewById(R.id.premiered);
        status = (TextView) findViewById(R.id.status);
        genres = (TextView) findViewById(R.id.genres);
        rating = (TextView) findViewById(R.id.rating);
        imageView=(ImageView) findViewById(R.id.image);
        favoriteButton = (ImageView) findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(this);
        setViews();


    }
    public void setViews(){
        setTitle(ShowDetails.getInstance().getName());
//        showName.setText(ShowDetails.getInstance().getName());
        summary.setText(ShowDetails.getInstance().getSummary());
        premiered.setText(ShowDetails.getInstance().getPremiered());
        status.setText(ShowDetails.getInstance().getStatus());
        genres.setText(ShowDetails.getInstance().getGenres());
        rating.setText(ShowDetails.getInstance().getRating());
        new ImageLoadTask(ShowDetails.getInstance().getImage(), imageView).execute();
        DatabaseReference showRef = myRef.child(ShowDetails.getInstance().getName());
        showRef.setValue(ShowDetails.getInstance());
        setFavoriteButton();
    }

//    @Override
//    public void onTaskCompleted() {
//        if(ShowDetails.getInstance().Filled())
//            setViews();
//        else {
//            finish();
//        }
//    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.favoriteButton){
//            DatabaseReference currentUserRef = userRef.child(User.getCurrentUser().getUid());
//            final DatabaseReference favoriteShow = currentUserRef.child("favorite");


            favoriteShow.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child(ShowDetails.getInstance().getName()).exists()){
                        Log.e("result", " NOT EXIST ");
                        DatabaseReference thisShow = favoriteShow.child(ShowDetails.getInstance().getName());
                        thisShow.setValue(ShowDetails.getInstance());
                        Toast.makeText(ShowDetailsActivity.this, "Added to favorite",
                                Toast.LENGTH_SHORT).show();
                        favoriteShow.removeEventListener(this);
                        return;

                    }else if(dataSnapshot.child(ShowDetails.getInstance().getName()).exists()){
                        Log.e("result", " EXIST ");
                        DatabaseReference thisShow = favoriteShow.child(ShowDetails.getInstance().getName());
                        thisShow.removeValue();
                        Toast.makeText(ShowDetailsActivity.this, "Deleted from favorite",
                                Toast.LENGTH_SHORT).show();
                        favoriteShow.removeEventListener(this);
                        return;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }
    }
     public void setFavoriteButton(){
         favoriteShow.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 if(!dataSnapshot.child(ShowDetails.getInstance().getName()).exists()){
                     favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
//                     favoriteShow.removeEventListener(this);
//                     return;

                 }else if(dataSnapshot.child(ShowDetails.getInstance().getName()).exists()){
                     favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
//                     favoriteShow.removeEventListener(this);
//                     return;
                 }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
     }
}
