package com.openclassrooms.realestatemanager.ui.searchPage;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivitySearchResultBinding;
import com.openclassrooms.realestatemanager.ui.BaseActivity;

import java.util.Objects;

public class SearchResultActivity extends BaseActivity {

    private ActivitySearchResultBinding activitySearchResultBinding;
    private SearchResultFragment searchResultFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchResultBinding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        View view = activitySearchResultBinding.getRoot();

        setContentView(view);

        this.configureToolbar();
        this.configureUpButton();
        this.configureAndShowSearchResultFragment();

        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setTitle("Search Results");
    }

    /**
     * For declaration fragment
     */
    private void configureAndShowSearchResultFragment() {
//        Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        searchResultFragment = (SearchResultFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_result);

        if (searchResultFragment == null) {
            //Create new main fragment
            searchResultFragment = new SearchResultFragment();
            //Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_search_result, searchResultFragment)
                    .commit();
        }
    }


}