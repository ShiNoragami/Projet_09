package com.openclassrooms.realestatemanager.ui.listPage;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListItemBinding;
import com.openclassrooms.realestatemanager.models.Estate;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class ListViewHolder extends RecyclerView.ViewHolder {

    FragmentListItemBinding fragmentListItemBinding;

    public ListViewHolder(FragmentListItemBinding fragmentListItemBinding) {
        super(fragmentListItemBinding.getRoot());
        this.fragmentListItemBinding = fragmentListItemBinding;
    }

    /**
     *
     * @param estate
     * @param glide
     */
    @SuppressLint("SetTextI18n")
    public void updateWithEstate(Estate estate, RequestManager glide) {
        //for estate type
        Objects.requireNonNull(fragmentListItemBinding.estateType).setText(estate.getEstateType());
        //for city
        Objects.requireNonNull(fragmentListItemBinding.City).setText(estate.getCity());
        //for Price
        if (estate.getPrice() != null) {
            Objects.requireNonNull(fragmentListItemBinding.price).setText("$" + NumberFormat.getInstance(Locale.US).format(estate.getPrice()));
        }
        //for estate sold
        if (estate.getSold()) {
            fragmentListItemBinding.listPhotoSold.setImageResource(R.drawable.sold4);
        }
        //for photo
        if(!estate.getPhotoList().getPhotoList().isEmpty()) {
            glide.load(estate.getPhotoList().getPhotoList().get(0)).into(fragmentListItemBinding.listPhoto);
        }else {
            fragmentListItemBinding.listPhoto.setImageResource(R.drawable.no_image);
        }
    }
}