package com.adeasy.advertise.ui.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.helper.ViewHolderPostCats;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.ui.Order.OrderPhoneVerify;
import com.adeasy.advertise.ui.Order.Step2;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class NewAdCategories extends AppCompatActivity implements CategoryCallback {

    TextView title;
    ImageView imageView;
    RecyclerView recyclerView;
    CategoryManager categoryManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_new_ad_categories);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.CategoryListPost);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        categoryManager = new CategoryManager(this);
        loadData();
    }

    private void loadData() {
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(categoryManager.viewCategoryAll(), Category.class).build();

        FirestoreRecyclerAdapter<Category, ViewHolderPostCats> FirestoreRecyclerAdapter =
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
                                Intent intent = new Intent(getApplication(), NewAdvertisement.class);
                                intent.putExtra("key", getItem(position).getId());
                                startActivity(intent);
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

        FirestoreRecyclerAdapter.startListening();
        recyclerView.setAdapter(FirestoreRecyclerAdapter);
    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        categoryManager.destroy();
    }

}