package com.openclassrooms.realestatemanager.ui.listPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.models.UriList;
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository;
import com.openclassrooms.realestatemanager.ui.EstateViewModel;

import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.ui.detailDescription.DetailActivity;
import com.openclassrooms.realestatemanager.ui.detailDescription.DetailFragment;
import com.openclassrooms.realestatemanager.ui.mainPage.MainActivity;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ListFragment extends Fragment {

    private FragmentListBinding fragmentListBinding;

    private List<Estate> estateList;
    private UriList photoLists = new UriList();
    private ListAdapter mAdapter;
    private EstateViewModel estateViewModel;
    private DetailFragment detailFragment;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // For viewBinding
        fragmentListBinding = FragmentListBinding.inflate(inflater, container,false);
        View view = fragmentListBinding.getRoot();

        this.configureViewModel();
        this.configureRecyclerView();
        this.configureOnClickRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstateState) {
        super.onActivityCreated(saveInstateState);
    }

    /**
     * Configure ViewModel
     */
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel.class);

        this.estateViewModel.getEstates().observe(this, this::updateEstateList);
    }
    /**
     * Configuration RecyclerView
     * Configure RecyclerView, Adapter, LayoutManager & glue it
     */
    private void configureRecyclerView() {

        this.estateList = new ArrayList<>();
        //Create adapter
        this.mAdapter = new ListAdapter(this.estateList, Glide.with(this), this.photoLists);
        //Set Layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentListBinding.fragmentListRV.setLayoutManager(layoutManager);
        fragmentListBinding.fragmentListRV.setAdapter(mAdapter);
    }

    /**
     * Configure item click on RecyclerView
     */
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(fragmentListBinding.fragmentListRV, R.layout.fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        detailFragment = (DetailFragment) Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.detail_fragment_frameLayout);
                        //for tablet format
                        if (detailFragment != null && detailFragment.isVisible()) {
                            Estate estate = mAdapter.getEstates(position);
                            Log.d("bundleListFragment", "bundleFragment" + estate);
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("estate", estate);
                            startActivity(intent);
                        }else{
                            //for phone format
                            Estate estate = mAdapter.getEstates(position);
                            Intent intent = new Intent(getContext(), DetailActivity.class);
                            intent.putExtra("estate", estate);

                            Log.d("bundleRV", "estate" + estate);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentListBinding = null;
    }

    /**
     * for update estate list
     * @param estates
     */
    private void updateEstateList(List<Estate> estates) {
        if(estates != null)
            mAdapter.updateData(estates);

    }
}