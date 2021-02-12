package com.openclassrooms.realestatemanager.injections;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.EstateDatabase;
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static EstateDataRepository provideEstateDataSource(Context context) {
        EstateDatabase estateDatabase = EstateDatabase.Companion.getInstance(context);
        return new EstateDataRepository(estateDatabase.estateDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        EstateDataRepository dataSourceEstate = provideEstateDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceEstate, executor);
    }
}