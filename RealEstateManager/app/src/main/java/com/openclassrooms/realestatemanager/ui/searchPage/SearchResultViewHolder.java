package com.openclassrooms.realestatemanager.ui.searchPage;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListItemBinding;
import com.openclassrooms.realestatemanager.models.Estate;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {

    FragmentListItemBinding fragmentListItemBinding;

    public SearchResultViewHolder(FragmentListItemBinding fragmentListItemBinding) {
        //for viewBinding
        super(fragmentListItemBinding.getRoot());
        this.fragmentListItemBinding = fragmentListItemBinding;
    }

    /**
     * @param estate
     * @param glide
     */
    @SuppressLint("SetTextI18n")
    public void updateWithEstate(Estate estate, RequestManager glide) {
        if(estate != null) {
            // for Estate type
            Objects.requireNonNull(fragmentListItemBinding.estateType).setText(estate.getEstateType());
            // For city
            Objects.requireNonNull(fragmentListItemBinding.City).setText(estate.getCity());
            //for price
            if (estate.getPrice() != null) {
                Objects.requireNonNull(fragmentListItemBinding.price).setText("$" + NumberFormat.getInstance(Locale.US).format(estate.getPrice()));
            }
            //for sold estate
            if (estate.getSold()) {
                fragmentListItemBinding.listPhotoSold.setImageResource(R.drawable.sold4);
            }
            //for photo
            if (!estate.getPhotoList().getPhotoList().isEmpty()) {
                glide.load(estate.getPhotoList().getPhotoList().get(0)).into(fragmentListItemBinding.listPhoto);
            } else {
                fragmentListItemBinding.listPhoto.setImageResource(R.drawable.no_image);
            }
        }

    }
}