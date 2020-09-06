package com.adeasy.advertise.ui.newPost;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.NewPostViewModel;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.helper.ViewHolderPostCats;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Category;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class AllCategoriesNewPost extends Fragment implements CategoryCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView title;
    ImageView imageView;
    RecyclerView recyclerView;
    CategoryManager categoryManager;
    NewPostViewModel newPostViewModel;
    FirestoreRecyclerOptions<Category> options;

    public AllCategoriesNewPost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewPost.
     */
    // TODO: Rename and change types and number of parameters
    public static AllCategoriesNewPost newInstance(String param1, String param2) {
        AllCategoriesNewPost fragment = new AllCategoriesNewPost();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.manuka_fragment_categories_new_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("New Post");

        newPostViewModel = ViewModelProviders.of(getActivity()).get(NewPostViewModel.class);

        recyclerView = view.findViewById(R.id.CategoryListPost);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        categoryManager = new CategoryManager(this);

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
                                newPostViewModel.setCategorySelected(getItem(position));
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
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoryManager.destroy();
    }

}