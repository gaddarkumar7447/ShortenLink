package com.example.myapplication

import android.app.Dialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.HistoryRVAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.localdatabase.ShortenLinkDao
import com.example.myapplication.localdatabase.ShortenLinkDatabase
import com.example.myapplication.mvvm.ShortenLinkModel
import com.example.myapplication.mvvm.ShortenLinkRepository
import com.example.myapplication.mvvm.ShortenLinkViewModelProvider
import com.example.myapplication.utils.ApiResponceState
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelClass: ShortenLinkModel
    private lateinit var myBottomSheetDialog: BottomSheetDialog
    private lateinit var myRVAdapter : HistoryRVAdapter
    private lateinit var myDialogRootView : View
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showTheInfo()

        // starting the animation of shorten button
        // as soon as the application gets its onCreate called
        startShortenButtonAnimation()

        // setting up view Model
        setUpMyViewModel()

        // set up my History Recycler ViewAdapter and initializing the history RV adapter object
        setUpRVAdapter()

        setUpMyBottomSheetDialog()

        // set up shorten button Listener
        setUpShortenButton()

        // observe live data change
        observeLiveDataChange(myDialogRootView)

        setUpPasteButtonListener()
    }

    private fun setUpPasteButtonListener() {

        // setting up the Paste Button Listener
        // we first get an instance of CLIPBOARD System Service as our ClipBoard Manager
        // When an user clicks on the paste button the first primary clip
        // is fetched and stored in a variable which is further set
        // as the text of our URL editText

        binding.pasteButton.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val textToPaste = clipboard.primaryClip?.getItemAt(0)?.text
            binding.enteredURL.setText(textToPaste)
        }
    }

    private fun observeLiveDataChange(bottomDialogView: View) {
        val myDialogContentView : ConstraintLayout = bottomDialogView.findViewById(R.id.bottom_dialog_content_view)
        val progressBar : ProgressBar = bottomDialogView.findViewById(R.id.bottom_dialog_progress)
        val TitleTV : TextView = bottomDialogView.findViewById(R.id.long_url_title)
        val ShortLinkTV : TextView = bottomDialogView.findViewById(R.id.shortened_link)
        val SendButton : TextView = bottomDialogView.findViewById(R.id.share_short_link_button)

        // observing ApiResultState live data change
        viewModelClass.apiResultList.observe(this, Observer {
            when(it){
                is ApiResponceState.SuccessState -> {
                    viewModelClass.insertShortenList(it.myData!!)
                    progressBar.visibility = View.GONE
                    myDialogContentView.visibility = View.VISIBLE
                    TitleTV.text = it.myData.title
                    ShortLinkTV.text = it.myData.shortLink
                    SendButton.text = getString(R.string.share_link_text)

                    // Creating an intent to share to shortened link
                    // and giving user a chooser to select apps from
                    SendButton.setOnClickListener {
                        val myIntent = Intent(Intent.ACTION_SEND);
                        myIntent.type = "text/plain"
                        myIntent.putExtra(Intent.EXTRA_SUBJECT, "Short Link")
                        myIntent.putExtra(Intent.EXTRA_TEXT, ShortLinkTV.text)
                        startActivity(Intent.createChooser(myIntent, "Choose App to Share Shorten Link"))
                    }
                }
                 is ApiResponceState.ErrorState ->{
                     progressBar.visibility = View.GONE
                     myDialogContentView.visibility = View.VISIBLE
                     TitleTV.text = getString(R.string.error_text)
                     ShortLinkTV.text = it.message
                     SendButton.text = getString(R.string.exit_text)
                     SendButton.setOnClickListener {
                         myBottomSheetDialog.dismiss()
                     }
                 }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    myDialogContentView.visibility = View.GONE
                }
            }
        })
        viewModelClass.getAllLink().observe(this, Observer {
            myRVAdapter.myDifferList.submitList(it)
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpShortenButton() {
        binding.shortenButton.setOnClickListener{
            viewModelClass.getLinkShorten(binding.enteredURL.text.toString())
            myBottomSheetDialog.show()
        }
    }

    private fun setUpMyBottomSheetDialog() {
        // setting up my bottom sheet dialog
        // and its view
        myBottomSheetDialog = BottomSheetDialog(this)
        myDialogRootView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_view, binding.bottomSheet, false)
        myBottomSheetDialog.setContentView(myDialogRootView)
    }

    private fun setUpRVAdapter() {
        myRVAdapter = HistoryRVAdapter(this)
        binding.bottomSheet.findViewById<RecyclerView>(R.id.history_RV).apply {
            this.adapter = myRVAdapter
            this.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setUpMyViewModel() {
        // initializing our view model
        val repository = ShortenLinkRepository(ShortenLinkDatabase.createDataBase(this))
        val myViewModelProvider = ShortenLinkViewModelProvider(repository, this.application, this)
        viewModelClass = ViewModelProvider(this, myViewModelProvider)[ShortenLinkModel::class.java]
    }

    private fun startShortenButtonAnimation() {
        // Loading the button animation to the Home page shorten button
        binding.shortenButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_anim))
    }


    /*private fun callViaMVVM(link: String) {
        val apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
        val shortenLinkRepository = ShortenLinkRepository(apiInterface)
        viewModelClass = ViewModelProvider(this, ViewModelFactory(shortenLinkRepository, link))[ShortenLinkModel::class.java]
        viewModelClass.data.observe(this, Observer {
            Log.d("TAG", it.url.shortLink)
           binding.link.text = it.url.shortLink
        })
    }*/

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
