package com.adeasy.advertise.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderAdImage;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterForImages extends RecyclerView.Adapter<ViewHolderAdImage> {

    private List<String> uriImages;
    private List<String> deletedFirebaseUriImages;
    private RecycleAdapterInterface callback;
    private Context context;
    private static final String TAG = "RecycleAdapterForImages";
    private static final String FIREBASE_HOST = "firebasestorage.googleapis.com";

    public interface RecycleAdapterInterface {
        public void itemRemoved();
    }

    public RecycleAdapterForImages(List<String> uriImages, RecycleAdapterInterface callback, Context context) {
        this.uriImages = uriImages;
        this.callback = callback;
        this.context = context;
        deletedFirebaseUriImages = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderAdImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ad_images, parent, false);
        return new ViewHolderAdImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderAdImage holder, final int position) {
        try {

            Uri imageUri = Uri.parse(uriImages.get(position));

            if (imageUri.getHost().equals(FIREBASE_HOST) == false) {
                InputStream imageStream = context.getContentResolver().openInputStream(Uri.parse(uriImages.get(position)));
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                holder.adImage.setImageBitmap(bitmap);

                //Picasso.get().load(uriImages.get(position)).into(holder.adImage);
            }

            //holder.adImage.setImageURI(uriImages.get(position));
            else
                Picasso.get().load(uriImages.get(position)).fit().into(holder.adImage);

            holder.imageRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Uri.parse(uriImages.get(position)).getHost().equals(FIREBASE_HOST))
                        deletedFirebaseUriImages.add(uriImages.get(position));
                    uriImages.remove(position);
                    callback.itemRemoved();
                    notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return uriImages.size();
    }

    public List<String> getSelectedImages() {
        return uriImages;
    }

    public List<String> getDeletedFirebaseUriImages() {
        return deletedFirebaseUriImages;
    }

}
