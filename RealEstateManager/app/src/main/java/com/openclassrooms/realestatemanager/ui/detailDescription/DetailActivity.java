package com.openclassrooms.realestatemanager.ui.detailDescription;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityDetailBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.ui.BaseActivity;
import com.openclassrooms.realestatemanager.ui.EstateViewModel;
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity;

import java.util.Objects;

public class DetailActivity extends BaseActivity {

    private ActivityDetailBinding activityDetailBinding;
    private DetailFragment detailFragment;
    private Estate estate;
    private EstateViewModel estateViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = activityDetailBinding.getRoot();
        setContentView(view);

        this.configureToolbar();
        this.configureUpButton();
        this.configureAndShowDetailFragment();
        this.configureViewModel();
        //for title toolbar
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setTitle("Estate Description");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.description_menu, menu);
        return true;
    }

    /**
     * For edit button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.edit_btn:

                long idEstate = estate.getMandateNumberID();
                Intent editIntent = new Intent(this, AddEditActivity.class);
                editIntent.putExtra("iDEstate", idEstate);
                Log.d("editEstate", "editEstate" + idEstate);
                startActivity(editIntent);
                return true;

            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Call update method here because we are sure that DetailFragment is visible
        this.updateDetailUiForFragment();
    }

    /**
     * Configure ViewModel
     */
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel.class);
    }

    /**
     * For detail fragment declaration
     */
    private void configureAndShowDetailFragment() {
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_frameLayout);

        if (detailFragment == null) {
            //Create new main fragment
            detailFragment = new DetailFragment();
            //Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_frameLayout, detailFragment)
                    .commit();
        }
    }

    /**
     * For data for tablet
     */
    private void updateDetailUiForFragment() {

        Intent intentTablet = Objects.requireNonNull(this).getIntent();
        estate = (Estate) intentTablet.getSerializableExtra("estate");
        Log.d("estateDetail", "estateDetail" + estate);
    }
}