package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.Bug;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//import com.example.MyViewHolder;

public class BugsActivity2 extends AppCompatActivity {

    private FirebaseRecyclerOptions<Bug> options;
    private FirebaseRecyclerAdapter<Bug, MyViewHolder> adapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private RecyclerView recyclerView;

    DatabaseReference ref ,recylcerRef;
    String user;
    Bug bug = new Bug();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_activity_bugs2);

        Log.i("mylog","Bugs activity 2 oncreate");

        ref = FirebaseDatabase.getInstance().getReference().child("Bug");

        recyclerView = findViewById(R.id.bugListRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getEmail();


        Query mQuery = ref.orderByChild("user").equalTo(user);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot bugsnapshot:snapshot.getChildren())
                {
                    Log.i("mylog","bug snapshots");
                    Log.i("mylog",bugsnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Bug>().setQuery(mQuery,Bug.class).build();  //preveously ref in place of mQuery, but this draws  items by user!
        adapter = new FirebaseRecyclerAdapter<Bug, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Bug bug) {

                final String id = bug.getBugId();
                final String description = bug.getDescription();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ViewBugActivity.class);
                        intent.putExtra("BUGID_EXTRA",id);
                        intent.putExtra("BUG_DESCRIPTION_EXTRA",description);
                        startActivity(intent);

                    }
                });
                holder.TVbugId.setText(bug.getBugId());
                holder.TVdescription.setText(bug.getDescription());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ravindu_single_view_layout,parent,false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}