package com.adeasy.advertise.ui.getintouch;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewAllBugsAdmin extends AppCompatActivity {

    private FirebaseRecyclerOptions<Bug> options;
    private FirebaseRecyclerAdapter<Bug, MyViewHolder> adapter;
    private RecyclerView recyclerView;
    DatabaseReference ref ,recylcerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_view_all_bugs_admin);

        ref = FirebaseDatabase.getInstance().getReference().child("Bug");

        recyclerView = findViewById(R.id.bugListRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<Bug>().setQuery(ref,Bug.class).build();  //preveously ref in place of mQuery, but this draws  items by user!
        adapter = new FirebaseRecyclerAdapter<Bug, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Bug bug) {

                final String id = bug.getBugId();
                final String description = bug.getDescription();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),ViewBugAdmin.class);
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