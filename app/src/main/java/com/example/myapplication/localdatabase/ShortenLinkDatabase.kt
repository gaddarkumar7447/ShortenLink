package com.example.myapplication.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.ShortenUrl

@Database(entities = [ShortenUrl::class], version = 1, exportSchema = false)
abstract class ShortenLinkDatabase : RoomDatabase() {

    abstract fun getShortenLinkDao() : ShortenLinkDao

    companion object{
        private var INSTANCE : ShortenLinkDatabase?= null

        fun createDataBase(context: Context) : ShortenLinkDatabase{
            synchronized(this){
                if (INSTANCE == null){
                    val instance = Room.databaseBuilder(context, ShortenLinkDatabase::class.java, "shortLinks_db.db").build()
                    INSTANCE = instance
                }
            }
            return INSTANCE!!
        }
    }
}