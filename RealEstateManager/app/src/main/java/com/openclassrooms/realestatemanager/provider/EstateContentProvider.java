package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.EstateDatabase;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.EstateKt;

import java.util.Objects;

public class EstateContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    public static final String TABLE_NAME = Estate.class.getSimpleName();
    public static final Uri URI_ESTATE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    /**
     * Entry point of contentProvider
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        return true;
    }

    /**
     * For retrieve data from URI
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (getContext() != null) {
            long mandateNumberID = ContentUris.parseId(uri);
            final Cursor cursor =
                    EstateDatabase.Companion.getInstance(getContext()).estateDao().getEstateWithCursor(mandateNumberID);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

            return cursor;
        }
        throw new IllegalArgumentException("Failed to query row from uri" + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.estate/" + AUTHORITY + "." + TABLE_NAME;
    }

    /**
     * Return MIME type for retrieve data type precisely
     *
     * @param uri
     * @param contentValues
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (getContext() != null) {
            final long id = EstateDatabase.Companion.getInstance(getContext()).estateDao()
                    .insertEstate(EstateKt.fromContentValues(Objects.requireNonNull(contentValues)));
            if (id != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }
        throw new IllegalArgumentException("Failed to insert row into" + uri);
    }

    /**
     * For delete data at ContentValues Format
     *
     * @param uri
     * @param s
     * @param strings
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null) {
            final int count =
                    EstateDatabase.Companion.getInstance(getContext()).estateDao().deleteItem(ContentUris.parseId(uri));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to delete row into" + uri);
    }

    /**
     * for update data at ContentValues format
     *
     * @param uri
     * @param contentValues
     * @param s
     * @param strings
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null) {
            final int count =
                    EstateDatabase.Companion.getInstance(getContext()).estateDao().updateEstate(EstateKt.fromContentValues(contentValues));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into" + uri);
    }
}