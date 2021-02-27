package com.openclassrooms.realestatemanager.ui.mainPage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding;
import com.openclassrooms.realestatemanager.ui.listPage.ListAdapter;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.ui.BaseActivity;
import com.openclassrooms.realestatemanager.ui.EstateViewModel;
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity;
import com.openclassrooms.realestatemanager.ui.detailDescription.DetailActivity;
import com.openclassrooms.realestatemanager.ui.detailDescription.DetailFragment;
import com.openclassrooms.realestatemanager.ui.listPage.ListFragment;
import com.openclassrooms.realestatemanager.ui.mapPage.MapActivity;
import com.openclassrooms.realestatemanager.ui.searchPage.SearchActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    //private TextView textViewMain;
    //private TextView textViewQuantity;

    /**First error : id call error*/
    //onCreate :
    //setContentView(R.layout.activity_main);
    //this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
    //this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);
    //this.configureTextViewMain();
    //this.configureTextViewQuantity();
    // }

    //private void configureTextViewMain(){
    //   this.textViewMain.setTextSize(15);
    //    this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    //}

    /**Second Error : call integer in setText*/
    //private void configureTextViewQuantity(){
    //   int quantity = Utils.convertDollarToEuro(100);
    //    this.textViewQuantity.setTextSize(20);
    //    this.textViewQuantity.setText(quantity);
    //}

    private ActivityMainBinding binding;
    private ListFragment listFragment;
    private DetailFragment detailFragment;
    private DetailActivity detailActivity;
    private Estate estate;
    private EstateViewModel estateViewModel;
    private long mandateNumberID;
    private long idEstate;
    private ListAdapter mAdapter;
    private List<Estate> estateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.onClickFab();
        this.configureAndShowListFragment();
        this.configureAndShowDetailFragment();
        this.configureViewModel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.configureToolbar();
        }
    }


    /**
     * Configure ViewModel
     */
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel.class);

    }

    /**
     * For menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    /**
     * For click on fab for create new estate
     */
    public void onClickFab() {
        binding.fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fabIntent = new Intent(getApplicationContext(), AddEditActivity.class);
                startActivity(fabIntent);
            }
        });
    }

    /**
     * For click on button on toolbar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = Objects.requireNonNull(getIntent());
        Estate estateDetail = (Estate) intent.getSerializableExtra("estate");
        idEstate = estateDetail != null ? estateDetail.getMandateNumberID() : 0;

        //Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.edit_btn:

                if (idEstate > 0) {
                    Intent editIntent = new Intent(this, AddEditActivity.class);
                    editIntent.putExtra("iDEstate", idEstate);
                    startActivity(editIntent);
                    finish();
                } else {
                    Snackbar.make(binding.getRoot(), "No estate selected", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            case R.id.search_btn:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.map_btn:
                Intent mapIntent = new Intent(this, MapActivity.class);
                startActivity(mapIntent);
                return true;
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }

    private static final String TAG = "Convert Price";

    /**
     * For connecting fragment list
     */
    private void configureAndShowListFragment() {
//        Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment_frameLayout);

        if (listFragment == null) {
            //Create new main fragment
            listFragment = new ListFragment();
            //Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_frameLayout, listFragment)
                    .commit();
        }
    }

    /**
     * For connecting fragment detail
     */
    private void configureAndShowDetailFragment() {
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_frameLayout);

        if (detailFragment == null && findViewById(R.id.detail_fragment_frameLayout) != null) {
            //Create new main fragment
            detailFragment = new DetailFragment();
            //Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_frameLayout, detailFragment)
                    .commit();
        }
    }
}
