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
import com.adeasy.advertise.model.Suggestion;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Creator-A.M.W.W.R.L.Wataketiya
//IT19014128
//ravinduwata@gmail.com

public class ViewAllSuggestionsAdmin extends AppCompatActivity {

    private FirebaseRecyclerOptions<Suggestion> options;
    private FirebaseRecyclerAdapter<Suggestion, MyViewHolder> adapter;
    private RecyclerView recyclerView;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ravindu_view_all_suggestions_admin);

        ref = FirebaseDatabase.getInstance().getReference().child("Suggestion");

        recyclerView = findViewById(R.id.bugListRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<Suggestion>().setQuery(ref,Suggestion.class).build();  //preveously ref in place of mQuery, but this draws  items by user!
        adapter = new FirebaseRecyclerAdapter<Suggestion, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Suggestion suggestion) {

                final String id = suggestion.getSuggestionID();
                final String description = suggestion.getSuggestionDescription();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ViewSuggestionAdmin.class);
                        intent.putExtra("SUGGESTION_ID_EXTRA",id);
                        intent.putExtra("SUGGESTION_DESCRIPTION_EXTRA",description);
                        startActivity(intent);

                    }
                });
                holder.TVbugId.setText(suggestion.getSuggestionID());
                holder.TVdescription.setText(suggestion.getSuggestionDescription());
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