package com.example.myapplication.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.api.ApiInterface
import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.localdatabase.ShortenLinkDatabase
import com.example.myapplication.model.ApiCallResult
import com.example.myapplication.model.ShortenUrl
import com.example.myapplication.utils.Constants
import retrofit2.Response

class ShortenLinkRepository(private val db: ShortenLinkDatabase) {

    suspend fun getMyLinkSorted(link : String) : Response<ApiCallResult>{
        return RetrofitInstance.getInstance().create(ApiInterface::class.java).getResponce(Constants.myAPIKey, link)
    }

    suspend fun insert(givenSortedLink : ShortenUrl) = db.getShortenLinkDao().insertShortenLink(givenSortedLink)

    fun getAllShortenLink() = db.getShortenLinkDao().getAllLinks()
}