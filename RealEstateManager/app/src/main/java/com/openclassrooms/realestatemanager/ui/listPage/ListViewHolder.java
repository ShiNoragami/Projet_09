package com.openclassrooms.realestatemanager.ui.listPage;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListItemBinding;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class ListViewHolder extends RecyclerView.ViewHolder {

    FragmentListItemBinding fragmentListItemBinding;
    Locale locale = Locale.getDefault();

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
            if (locale == Locale.US) {
                Objects.requireNonNull(fragmentListItemBinding.price).setText("$" + NumberFormat.getInstance(Locale.US).format(estate.getPrice()));
            } else {
                Objects.requireNonNull(fragmentListItemBinding.price).setText("€" + NumberFormat.getInstance(Locale.FRANCE).format(Utils.convertDollarToEuro(estate.getPrice())));
            }
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
