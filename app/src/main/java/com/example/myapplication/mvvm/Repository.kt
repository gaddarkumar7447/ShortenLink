package com.example.myapplication.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.api.ApiInterface
import com.example.myapplication.model.ShortenLink
import com.example.myapplication.utils.Constants

class Repository(private val apiInterface: ApiInterface) {

    private val mutableLiveData = MutableLiveData<ShortenLink>()

    val data : LiveData<ShortenLink>
    get() = mutableLiveData

    suspend fun getDataFromApi(link : String){
        val result = apiInterface.getResponce(Constants.myAPIKey, link).body()
        if (result != null){
            mutableLiveData.postValue(result)
        }
    }
}