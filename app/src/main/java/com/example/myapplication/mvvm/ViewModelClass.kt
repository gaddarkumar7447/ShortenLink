package com.example.myapplication.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ShortenLink
import kotlinx.coroutines.launch

class ViewModelClass(private val repository: Repository, link : String) : ViewModel(){
    init {
        viewModelScope.launch {
            repository.getDataFromApi(link)
        }
    }

    val data : LiveData<ShortenLink>
    get() = repository.data
}