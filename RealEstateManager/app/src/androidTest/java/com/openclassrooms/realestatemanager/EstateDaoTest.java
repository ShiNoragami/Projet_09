package com.openclassrooms.realestatemanager;

import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.EstateDatabase;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.PhotoDescription;
import com.openclassrooms.realestatemanager.models.UriList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EstateDaoTest {

    //For Data
    private EstateDatabase estateDatabase;
    //DATA SET for test
    private static long MANDATE_NUMBER_ID = 1;

    private static UriList uriListTest = new UriList();
    private static PhotoDescription descriptionTest = new PhotoDescription();



    private static Estate ESTATE_HOUSE = new Estate(1, "house", 200, 4, 2, 1, 200, 100000.00, "Très belle maison", "2 rue du bois du bray", 77127, "Lieusaint", true, false,
            false, true, true, 1601510400000L,"", "Boyer Donavan",uriListTest,descriptionTest,uriListTest);

    private static Estate ESTATE_FLAT = new Estate(2, "flat", 80, 2, 1, 1, 0, 50000.00, "Very nice flat", "5 rue longue", 66000, "Perpignan", false, true,
            true, true, true,1601510400000L,"","Plaza Sylvain", uriListTest,descriptionTest,uriListTest);
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        try {
            this.estateDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                    EstateDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @After
    public void closeDb() throws Exception {
        estateDatabase.close();
    }

    @Test
    public void insertAndGetEstate() throws InterruptedException {
        //adding demo
        this.estateDatabase.estateDao().insertEstate(ESTATE_HOUSE);
        this.estateDatabase.estateDao().insertEstate(ESTATE_FLAT);
        //test
        List<Estate> estateList = LiveDataTestUtil.getValue(this.estateDatabase.estateDao().getEstates());
        assertEquals(2, estateList.size());
    }

    @Test
    public void getEstateWhenNoItemInserted() throws InterruptedException {
        //test
        List<Estate> estatesList = LiveDataTestUtil.getValue(this.estateDatabase.estateDao().getEstates());
        assertTrue(estatesList.isEmpty());
    }
}