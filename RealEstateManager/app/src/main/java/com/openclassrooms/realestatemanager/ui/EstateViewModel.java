package com.openclassrooms.realestatemanager.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class EstateViewModel extends ViewModel {

    //Repository
    private final EstateDataRepository estateDataSource;
    private final Executor executor;

    /**
     * Constructor
     *
     * @param estateDataSource
     * @param executor
     */
    public EstateViewModel(EstateDataRepository estateDataSource, Executor executor) {
        this.estateDataSource = estateDataSource;
        this.executor = executor;
    }

    /**
     * Get all estates
     *
     * @return
     */
    public LiveData<List<Estate>> getEstates() {
        return estateDataSource.getEstates();
    }

    /**
     * GET estate with id
     *
     * @param mandateNumberID
     * @return
     */
    public LiveData<Estate> getEstate(long mandateNumberID) {
        return estateDataSource.getEstate(mandateNumberID);
    }

    /**
     * Create Estate
     *
     * @param estate
     */
    public void createEstate(Estate estate) {
        executor.execute(() -> {
            estateDataSource.createEstate(estate);
        });
    }

    /**
     * Delete estate
     *
     * @param mandateNumberID
     */
    public void deleteEstate(long mandateNumberID) {
        executor.execute(() -> {
            estateDataSource.deleteEstate(mandateNumberID);
        });
    }

    /**
     * Update estate
     *
     * @param estate
     */
    public void updateEstate(Estate estate) {
        executor.execute(() -> {
            estateDataSource.updateEstate(estate);
        });
    }
}