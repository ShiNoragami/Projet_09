package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.EstateDatabase;
import com.openclassrooms.realestatemanager.provider.EstateContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class EstateContentProviderTest {

    //for data
    private ContentResolver mContentResolver;

    //Data Set for test
    private static long mandateNumberID = 1;

    @Before
    public void setUp() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("Estate.db");
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), EstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getEstateWhenNoEstateInserted() {
        final Cursor cursor =
                mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.URI_ESTATE, mandateNumberID),
                        null, null, null, null);
        assertThat(cursor,notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetEstate() {
        //Before : Adding demo estate
        final Uri estateUri = mContentResolver.insert(EstateContentProvider.URI_ESTATE, generateEstate());
        //test
        final Cursor cursor =
                mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.URI_ESTATE, mandateNumberID),
                        null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("estateType")), is ("House"));
        mContentResolver.delete(Objects.requireNonNull(estateUri), null, null);
    }

    private ContentValues generateEstate() {
        final ContentValues values = new ContentValues();

        values.put("estateType", "House");
        values.put("surface", 100);
        values.put("rooms", 3);
        values.put("bedrooms", 2);
        values.put("bathrooms",1);
        values.put("ground",700);
        values.put("price", 250000);
        values.put("description", "Very nice house in center");
        values.put("address", "2 rue du bois du bray");
        values.put("postalCode", 77127);
        values.put("city", "lieusaint");
        values.put("schools", false);
        values.put("stores", true);
        values.put("park", false);
        values.put("restaurants", true);
        values.put("upOfSaleDate", "27/01/2021");
        values.put("soldDate", "");
        values.put("agentName", "Boyer Donavan");

        return values;
    }
}