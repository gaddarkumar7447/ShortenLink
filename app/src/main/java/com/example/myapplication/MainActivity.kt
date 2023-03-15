package com.example.myapplication

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.api.ApiInterface
import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.mvvm.Repository
import com.example.myapplication.mvvm.ViewModelClass
import com.example.myapplication.mvvm.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelClass: ViewModelClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showTheInfo()
        /*MainScope().launch {
            apiCall()
        }*/
        val link = "https://leetcode.com/problems/rearrange-array-to-maximize-prefix-score/description/"
        callViaMVVM(link)
    }

    private fun callViaMVVM(link: String) {
        val apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
        val repository = Repository(apiInterface)
        viewModelClass = ViewModelProvider(this, ViewModelFactory(repository, link))[ViewModelClass::class.java]
        viewModelClass.data.observe(this, Observer {
            Log.d("TAG", it.url.shortLink)

           binding.link.text = it.url.shortLink
        })

    }

    /*private suspend fun apiCall() {
        val instance = RetrofitInstance.getInstance().create(ApiInterface::class.java)
        instance.getResponce(Constants.myAPIKey, "https://leetcode.com/problems/rearrange-array-to-maximize-prefix-score/description/").body().apply {
            val shortenLinkA = this?.url?.title
            Log.d("TAG", "$shortenLinkA")
        }
    }*/

    private fun showTheInfo() {
        binding.helpButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.info)
            dialog.setCancelable(true)
            dialog.show()
        }
    }
}