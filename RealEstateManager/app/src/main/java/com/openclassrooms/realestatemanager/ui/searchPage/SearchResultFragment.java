package com.openclassrooms.realestatemanager.ui.searchPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchResultBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.SearchEstate;
import com.openclassrooms.realestatemanager.models.UriList;
import com.openclassrooms.realestatemanager.ui.SearchViewModel;
import com.openclassrooms.realestatemanager.ui.detailDescription.DetailActivity;
import com.openclassrooms.realestatemanager.ui.detailDescription.DetailFragment;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;




public class SearchResultFragment extends Fragment {

    private FragmentSearchResultBinding fragmentSearchResultBinding;

    private List<Estate> estateList;
    private UriList photoLists = new UriList();
    private SearchResultAdapter mAdapter;
    private SearchViewModel searchViewModel;
    private SearchEstate estateSearch;
    private DetailFragment detailFragment;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //For viewBinding
        fragmentSearchResultBinding = FragmentSearchResultBinding.inflate(inflater, container, false);
        View view = fragmentSearchResultBinding.getRoot();

        configureViewModel();
        configureRecyclerView();
        configureOnClickRecyclerView();

        return view;
    }

    /**
     * For configure ViewModel
     */
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
        //for retrieve estate from search

        Intent intent = new Intent(Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent()));
        estateSearch = (SearchEstate) intent.getSerializableExtra("estateSearch");
        Log.d("estateSearch", String.valueOf(estateSearch));

        //for observe data
        this.searchViewModel.searchEstate(Objects.requireNonNull(estateSearch.getEstateType()), estateSearch.getCity(), estateSearch.getMinRooms(), estateSearch.getMaxRooms(),
                estateSearch.getMinSurface(), estateSearch.getMaxSurface(), estateSearch.getMinPrice(), estateSearch.getMaxPrice(),
                estateSearch.getMinUpOfSaleDate(), estateSearch.getMaxOfSaleDate(), estateSearch.getPhotos(), estateSearch.getSchools(), estateSearch.getStores(),
                estateSearch.getPark(), estateSearch.getRestaurants(), estateSearch.getSold()).observe(this, this::updateEstateList);
    }


    /**
     * Configuration RecyclerView
     * Configure RecyclerView, Adapter, LayoutManager & glue it
     */
    private void configureRecyclerView() {

        this.estateList = new ArrayList<>();
        //Create adapter
        this.mAdapter = new SearchResultAdapter(this.estateList, Glide.with(this), this.photoLists);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentSearchResultBinding.searchResultListRV.setLayoutManager(layoutManager);
        fragmentSearchResultBinding.searchResultListRV.setAdapter(mAdapter);
    }

    /**
     * For click on item RecyclerView
     */
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(fragmentSearchResultBinding.searchResultListRV, R.layout.fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        detailFragment = (DetailFragment) Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.detail_fragment_frameLayout);
                        //for tablet format
                        if (detailFragment != null && detailFragment.isVisible()) {
                            Estate estate = mAdapter.getEstates(position);
                            detailFragment.updateUiForTablet(estate);
                            Log.d("bundleListFragment", "bundleFragment" + estate);

                        } else {
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
        fragmentSearchResultBinding = null;
    }

    /**
     * for update list of estates in adapter
     *
     * @param estates
     */
    private void updateEstateList(List<Estate> estates) {
        if (estates != null) {
            mAdapter.updateData(estates);
            Log.d("updateListSearch", "updateListSearch" + estates);
        }
        if (Objects.requireNonNull(estates).isEmpty()) {
            Snackbar.make(fragmentSearchResultBinding.getRoot(), "No result found, please retry with another search", Snackbar.LENGTH_LONG)
                    .setAction("Return", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Objects.requireNonNull(getActivity()).finish();
                        }
                    })
                    .show();
        }
    }
}