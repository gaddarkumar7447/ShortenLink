package com.example.myapplication.localdatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.ShortenUrl

@Dao
interface ShortenLinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortenLink(shortenUrl: ShortenUrl) : Long

    @Query("SELECT * FROM ShortenUrlTable")
    fun getAllLinks() : LiveData<List<ShortenUrl>>
}