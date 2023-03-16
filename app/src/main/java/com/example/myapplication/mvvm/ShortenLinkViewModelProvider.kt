package com.example.myapplication.mvvm

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShortenLinkViewModelProvider(private val shortenLinkRepository: ShortenLinkRepository, private val app : Application, private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShortenLinkModel(shortenLinkRepository, app, context) as T
    }
}