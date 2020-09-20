package com.adeasy.advertise.ui.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.helper.ViewHolderPostCats;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Category;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class CategoryPicker extends AppCompatActivity {

    Toolbar toolbar;
    TextView all_cat;
    RecyclerView recyclerView;
    CategoryManager categoryManager;
    FirestoreRecyclerOptions<Category> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_category_picker);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pick a category");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        all_cat = findViewById(R.id.all_cat);
        recyclerView = findViewById(R.id.categories_row);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        categoryManager = new CategoryManager();
        loadData();
    }

    private void loadData() {
        options = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(categoryManager.viewCategoryAll(), Category.class).build();

        FirestoreRecyclerAdapter<Category, ViewHolderPostCats> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<Category, ViewHolderPostCats>(
                        options
                ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderPostCats holder, final int position, @NonNull Category category) {

                        holder.titleView.setText(category.getName());
                        Picasso.get().load(category.getImageUrl()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ViewHolderPostCats onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catergory_row, parent, false);
                        return new ViewHolderPostCats(view);
                    }

                };

        firestoreRecyclerAdapter.startListening();
        recyclerView.setAdapter(firestoreRecyclerAdapter);
    }

}