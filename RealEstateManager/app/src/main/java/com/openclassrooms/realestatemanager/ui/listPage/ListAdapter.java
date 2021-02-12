package com.openclassrooms.realestatemanager.ui.listPage;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.databinding.FragmentListItemBinding;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.UriList;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {


    //For data
    private List<Estate> estateList;
    private RequestManager glide;
    private List<UriList> photoLists;

    /**
     * Constructor
     *
     * @param estateList
     * @param glide
     * @param photoLists
     */
    public ListAdapter(List<Estate> estateList, RequestManager glide, UriList photoLists) {

        this.estateList = new ArrayList<>();
        this.glide = glide;
        this.photoLists = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ListViewHolder(FragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    /**
     * Update viewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.updateWithEstate(this.estateList.get(position), this.glide);

    }

    /**
     * For return estates
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return this.estateList.size();
    }

    public Estate getEstates(int position) {
        return this.estateList.get(position);
    }

    /**
     * For update estate list
     *
     * @param estateList
     */
    public void updateData(List<Estate> estateList) {
        this.estateList = estateList;
        this.notifyDataSetChanged();
    }
}