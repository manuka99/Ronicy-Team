package com.adeasy.advertise.ui.administration.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderPostCats;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.ui.administration.home.DashboardHome;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class SelectCategoryAndFilter extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    CategoryManager categoryManager;
    FirestoreRecyclerOptions<Category> options;
    String filterType = "All";
    TextView allCatSeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_select_category_and_filter);

        try{
            filterType = getIntent().getStringExtra("filterType");
        }catch (Exception e){
            filterType = "All";
            e.printStackTrace();
        }

        allCatSeleted = findViewById(R.id.allCatSeleted);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Filter ads - " + filterType);
        getSupportActionBar().setSubtitle("Select Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.CategoryListPost);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        categoryManager = new CategoryManager();

        allCatSeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityOnCategorySelected("All", "All Categories");
            }
        });

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
                                startActivityOnCategorySelected(getItem(position).getId(), getItem(position).getName());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        categoryManager.destroy();
    }

    private void startActivityOnCategorySelected(String category, String catName){
        Intent intent = new Intent(this, AdsFilter.class);
        intent.putExtra("category", category);
        intent.putExtra("categoryName", catName);
        intent.putExtra("filterType", filterType);
        startActivity(intent);
    }

}