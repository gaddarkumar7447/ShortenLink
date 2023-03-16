package com.example.myapplication.mvvm

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ApiCallResult
import com.example.myapplication.model.ShortenUrl
import com.example.myapplication.utils.ApiResponceState
import kotlinx.coroutines.launch
import retrofit2.Response

class ShortenLinkModel(private val myRepository: ShortenLinkRepository, myApplication: Application, private val context: Context) : AndroidViewModel(myApplication) {

    val apiResultList: MutableLiveData<ApiResponceState<ShortenUrl>> = MutableLiveData()

    private fun handleApiResponce(responceCollected: Response<ApiCallResult>): ApiResponceState<ShortenUrl> {
        if (responceCollected.isSuccessful) {
            responceCollected.body().let { apiCallResult ->
                when (apiCallResult?.url?.status) {
                    7 -> return ApiResponceState.SuccessState(apiCallResult.url)
                    1 -> return ApiResponceState.ErrorState(errorMessage = "Link already shorten")
                    2, 5 -> return ApiResponceState.ErrorState(errorMessage = "Invalid Link")
                    else -> return ApiResponceState.ErrorState(errorMessage = "Error in shorting")
                }
            }
        }
        return ApiResponceState.ErrorState(errorMessage = "Error is shorting")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getLinkShorten(link: String) = viewModelScope.launch {
        apiResultList.postValue(ApiResponceState.InProcessState())
        if (isInternetConnected(context)) {
            val responceCollect = myRepository.getMyLinkSorted(link)
            apiResultList.postValue(handleApiResponce(responceCollect))
        } else {
            apiResultList.postValue(ApiResponceState.ErrorState(errorMessage = "No Internet connection"))
        }
    }


    fun insertShortenList(shortenUrl: ShortenUrl) = viewModelScope.launch {
        myRepository.insert(shortenUrl)
    }

    fun getAllLink() = myRepository.getAllShortenLink()


    @RequiresApi(Build.VERSION_CODES.M)
    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}