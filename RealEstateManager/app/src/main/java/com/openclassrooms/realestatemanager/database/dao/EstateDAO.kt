package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.models.Estate

@Dao
interface EstateDAO {

    @Query("SELECT * FROM Estate")
    fun getEstates(): LiveData<List<Estate>>

    @Query("SELECT * FROM Estate WHERE mandateNumberID = :mandateNumberID")
    fun getEstate(mandateNumberID: Long): LiveData<Estate>

    @Insert
    fun insertEstate(estate: Estate): Long

    @Update
    fun updateEstate(estate: Estate): Int

    @Query("DELETE FROM Estate WHERE mandateNumberID = :mandateNumberID")
    fun deleteItem(mandateNumberID: Long): Int

    @RawQuery(observedEntities = [Estate::class])
    fun getSearchEstate(query: SupportSQLiteQuery): LiveData<List<Estate>>

    //For ContentProvider
    @Query("SELECT * FROM Estate WHERE mandateNumberID = :mandateNumberID")
    fun getEstateWithCursor(mandateNumberID: Long): Cursor

}