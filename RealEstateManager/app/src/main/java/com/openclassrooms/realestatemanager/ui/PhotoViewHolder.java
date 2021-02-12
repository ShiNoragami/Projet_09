package com.openclassrooms.realestatemanager.ui;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPhotoItemBinding;

import java.util.Objects;

;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private ActivityAddPhotoItemBinding activityAddPhotoItemBinding;
    private long estateEdit;


    public PhotoViewHolder(ActivityAddPhotoItemBinding activityAddPhotoItemBinding) {
        //for viewBinding
        super(activityAddPhotoItemBinding.getRoot());
        this.activityAddPhotoItemBinding = activityAddPhotoItemBinding;

    }

    /**
     * For update with data
     *
     * @param photoList
     * @param glide
     * @param photoDescription
     * @param estateEdit
     */
    public void updateWithDetails(Uri photoList, RequestManager glide, String photoDescription, long estateEdit) {

        activityAddPhotoItemBinding.photoDescription.setText(photoDescription);

        glide.load(photoList).apply(RequestOptions.centerCropTransform()).into(activityAddPhotoItemBinding.photoImage);
        //for delete image display in Edit and Not in create
        if (estateEdit == 0) {
            Objects.requireNonNull(activityAddPhotoItemBinding.deleteImage).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(activityAddPhotoItemBinding.deleteImage).setVisibility(View.VISIBLE);
        }
    }
}