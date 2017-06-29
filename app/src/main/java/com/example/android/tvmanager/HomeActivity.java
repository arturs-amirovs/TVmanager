package com.example.android.tvmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.android.tvmanager.ShowDetailsActivity.SEARCH_TEXT;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("shows");
    DatabaseReference userRef = database.getReference("users");
    DatabaseReference currentUserRef = userRef.child(User.getCurrentUser().getUid());
    final DatabaseReference favoriteShow = currentUserRef.child("favorite");
    ArrayList<ShowDetailsClass> showList = new ArrayList<>();

    private ViewAdapter listAdapter;
    private GridView categoryLVList;

    private EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        searchText = (EditText) findViewById(R.id.searchText);
        findViewById(R.id.searchButton).setOnClickListener(this);
        findViewById(R.id.signoutButton).setOnClickListener(this);
        categoryLVList = (GridView) findViewById(R.id.categoryLVList);
        favoriteShow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showList = new ArrayList<>();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    ShowDetailsClass details = new ShowDetailsClass();
                    details.setName(messageSnapshot.child("name").getValue().toString());
                    details.setPremiered(messageSnapshot.child("premiered").getValue().toString());
                    details.setStatus(messageSnapshot.child("status").getValue().toString());
                    details.setSummary(messageSnapshot.child("summary").getValue().toString());
                    details.setGenres(messageSnapshot.child("genres").getValue().toString());
                    details.setRating(messageSnapshot.child("rating").getValue().toString());
                    details.setImage(messageSnapshot.child("image").getValue().toString());
                    Log.e("data", details.getName());
                    showList.add(details);
                }

                listAdapter = new ViewAdapter(HomeActivity.this, showList);
                categoryLVList.setAdapter(listAdapter);
                categoryLVList.setOnItemClickListener(HomeActivity.this);
//                LinearLayoutManager layoutManager
//                        = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
//
//                RecyclerView myList = (RecyclerView) findViewById(R.id.categoryLVList);
//                myList.setLayoutManager(layoutManager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.searchButton){
            searchShow(searchText.getText().toString());
        } else if(i == R.id.signoutButton) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginScreen.class));
            finish();
        }
    }

    public void searchShow(String search){
        newActivity(search);
    }

    public void newActivity(String search) {
        Intent intent = new Intent(this, ShowDetailsActivity.class);
        intent.putExtra("search", search);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView text = (TextView)view.findViewById(R.id.nameList);
        startActivity(new Intent(this, ShowDetailsActivity.class).putExtra(SEARCH_TEXT, text.getText().toString()));
    }
}
