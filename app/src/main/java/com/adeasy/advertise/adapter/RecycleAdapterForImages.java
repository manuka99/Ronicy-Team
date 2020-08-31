package com.adeasy.advertise.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderAdImage;
import com.adeasy.advertise.helper.ViewHolderPhoneNumbers;
import com.adeasy.advertise.model.VerifiedNumber;

import java.io.InputStream;
import java.util.List;

public class RecycleAdapterForImages extends RecyclerView.Adapter<ViewHolderAdImage> {

    private List<Uri> uriImages;
    private RecycleAdapterInterface callback;
    private Context context;

    public interface RecycleAdapterInterface{
        public void itemRemoved();
    }

    public RecycleAdapterForImages(List<Uri> uriImages, RecycleAdapterInterface callback, Context context){
        this.uriImages = uriImages;
        this.callback = callback;
        this.context = context;
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
            InputStream imageStream = context.getContentResolver().openInputStream(uriImages.get(position));
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            bitmap = Bitmap.createScaledBitmap(bitmap,150, 150, true);
            holder.adImage.setImageBitmap(bitmap);
            //holder.adImage.setImageURI(uriImages.get(position));

            holder.imageRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uriImages.remove(position);
                    callback.itemRemoved();
                    notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return uriImages.size();
    }

    public List<Uri> getSelectedImages(){
        return uriImages;
    }

}
